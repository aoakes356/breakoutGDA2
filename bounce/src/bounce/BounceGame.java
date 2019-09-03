package bounce;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import bounce.GameObject;
import jig.Entity;
import jig.ResourceManager;

import jig.Vector;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A Simple Game of Bounce.
 * 
 * The game has three states: StartUp, Playing, and GameOver, the game
 * progresses through these states based on the user's input and the events that
 * occur. Each state is modestly different in terms of what is displayed and
 * what input is accepted.
 * 
 * In the playing state, our game displays a moving rectangular "ball" that
 * bounces off the sides of the game container. The ball can be controlled by
 * input from the user.
 * 
 * When the ball bounces, it appears broken for a short time afterwards and an
 * explosion animation is played at the impact site to add a bit of eye-candy
 * additionally, we play a short explosion sound effect when the game is
 * actively being played.
 * 
 * Our game also tracks the number of bounces and syncs the game update loop
 * with the monitor's refresh rate.
 * 
 * Graphics resources courtesy of qubodup:
 * http://opengameart.org/content/bomb-explosion-animation
 * 
 * Sound resources courtesy of DJ Chronos:
 * http://www.freesound.org/people/DJ%20Chronos/sounds/123236/
 * 
 * 
 * @author wallaces
 * 
 */
public class BounceGame extends StateBasedGame {
	
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	public static final String BALL_BALLIMG_RSC = "bounce/resource/redBall.png";
	public static final String BALL_BROKENIMG_RSC = "bounce/resource/redBall.png";
	public static final String BALL_SMALLBALL_RSC = "bounce/resource/smallBall.png";
	public static final String GAMEOVER_BANNER_RSC = "bounce/resource/GameOver.png";
	public static final String STARTUP_BANNER_RSC = "bounce/resource/PressSpace.png";
	public static final String BANG_EXPLOSIONIMG_RSC = "bounce/resource/explosion.png";
	public static final String BANG_EXPLOSIONSND_RSC = "bounce/resource/explosion.wav";
  public static final String BRICK_BASIC_RSC = "bounce/resource/basicBrick.png";
  public static final String PADDLE_BASIC_RSC = "bounce/resource/basic_paddle.png";
  public static final String BRICK_SMALL_RSC = "bounce/resource/smallBrick.png";
  public static final String BRICK_SMALL2_RSC = "bounce/resource/smallBrick2.png";
  public static final String BRICK_SMALL3_RSC = "bounce/resource/smallBrick3.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
  public ArrayList<GameObject> gameObjects;
  public ArrayList<BrickStack> levels;
	public Ball ball;
	public BrickStack currentLevel;
	public Paddle paddle;
	public ArrayList<Bang> explosions;
	public Iterator<BrickStack> levelSelector;

	/**
	 * Create the BounceGame frame, saving the width and height for later use.
	 * 
	 * @param title
	 *            the window's title
	 * @param width
	 *            the window's width
	 * @param height
	 *            the window's height
	 */
	public BounceGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);
		explosions = new ArrayList<Bang>(10);
		gameObjects = new ArrayList<GameObject>(50);
    currentLevel = null;
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());
		
		// the sound resource takes a particularly long time to load,
		// we preload it here to (1) reduce latency when we first play it
		// and (2) because loading it will load the audio libraries and
		// unless that is done now, we can't *disable* sound as we
		// attempt to do in the startUp() method.
		ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);

		// preload all the resources to avoid warnings & minimize latency...
		ResourceManager.loadImage(BALL_BALLIMG_RSC);
		//ResourceManager.loadImage(BALL_BROKENIMG_RSC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(STARTUP_BANNER_RSC);
		ResourceManager.loadImage(BANG_EXPLOSIONIMG_RSC);
		ResourceManager.loadImage(BRICK_BASIC_RSC);
		ResourceManager.loadImage(BRICK_SMALL_RSC);
		ResourceManager.loadImage(BALL_SMALLBALL_RSC);
    ResourceManager.loadImage(PADDLE_BASIC_RSC);
    ResourceManager.loadImage(BRICK_SMALL2_RSC);
    ResourceManager.loadImage(BRICK_SMALL3_RSC);
    levels = new ArrayList<>();
    BrickStack b1 = new BrickStack();
    BrickStack b2 = new BrickStack();
    BrickStack b3 = new BrickStack();
    levels.add(b1);
    levels.add(b2);
    levels.add(b3);
    for(int i = 0; i < 19; i++) {
      b1.addBrick(new Vector((ScreenWidth / 20.0f) * i + ScreenWidth / 20.0f, 150.0f), 1);
      b2.addBrick(new Vector((ScreenWidth / 20.0f) * i + ScreenWidth / 20.0f, (ScreenHeight/30)*i), 2);
      b3.addBrick(new Vector(i*40.0f+40.0f, 150.0f+(float)(Math.sin(i*(Math.PI/10.0f))*100.0f)), 2);

    }
    levelSelector = levels.iterator();
		ball = new Ball(ScreenWidth, ScreenHeight, 0.0f, 0.0f);
		paddle = new Paddle(ScreenWidth/2.0f,ScreenHeight,0.0f,0.0f);
		gameObjects.add(ball);
		gameObjects.add(paddle);

	}

	public void nextLevel(){
    if(currentLevel == null){
      currentLevel = levelSelector.next();
      gameObjects.addAll(currentLevel.bricks);
    }else{
      if(currentLevel.isWon()){
        currentLevel = levelSelector.next();
        gameObjects.addAll(currentLevel.bricks);
      }else{
        Brick temp = null;
        for(Iterator<Brick> it = currentLevel.bricks.iterator(); it.hasNext(); temp = it.next()){
          if(!gameObjects.contains(temp)){
            gameObjects.add(temp);
          }
        }
        currentLevel.reset();

      }
    }
  }
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new BounceGame("Bounce!", 800, 600));
			app.setDisplayMode(800, 600, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	
}

package bounce;

import java.util.Iterator;

import bounce.GameObject;
import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the bounce counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(false);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		
		bg.ball.render(g);
		g.drawString("Bounces: ?", 10, 30);
		for (Bang b : bg.explosions)
			b.render(g);
		g.drawImage(ResourceManager.getImage(BounceGame.STARTUP_BANNER_RSC),
				225, 270);		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;

		if (input.isKeyDown(Input.KEY_SPACE))
			bg.enterState(BounceGame.PLAYINGSTATE);	
		
		// bounce the ball...
		GameObject obj;
		boolean bounced;
    for(Iterator<GameObject> e = bg.gameObjects.iterator(); e.hasNext();) {
      obj = e.next();
      bounced = false;
      if (obj.getCoarseGrainedMaxX() > bg.ScreenWidth){
        obj.translate( -obj.getCoarseGrainedMaxX()+bg.ScreenWidth-.001f,0.0f);
        obj.collide(90.0f);
        bounced = true;
      }else if(obj.getCoarseGrainedMinX() < 0) {
        obj.translate(-obj.getCoarseGrainedMinX()+.001f, 0.0f);
        obj.collide(90.0f);
        bounced = true;
      } else if (obj.getCoarseGrainedMaxY() > bg.ScreenHeight){
        obj.translate( 0.0f,-obj.getCoarseGrainedMaxY()+bg.ScreenHeight-.001f);
        obj.collide(0);
        bounced = true;
      }else if(obj.getCoarseGrainedMinY() < 0) {
        obj.translate(0.0f, -obj.getCoarseGrainedMinY()+.001f);
        obj.collide(0);
        bounced = true;
      }
      if (bounced) {
        bg.explosions.add(new Bang(obj.getX(), obj.getY()));
      }
      obj.update(delta);
    }


		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}

	}

	@Override
	public int getID() {
		return BounceGame.STARTUPSTATE;
	}
	
}
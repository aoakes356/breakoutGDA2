package bounce;

import java.util.Iterator;

import bounce.GameObject;
import jig.Collision;
import jig.Entity;
import jig.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import org.newdawn.slick.util.FastTrig;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	int bounces;
	int scoreDelta;
	Vector dir;
	private boolean isChanged;
	public int lives = 3;
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		scoreDelta = 0;
		lives = 3;
		container.setSoundOn(true);
    BounceGame bg = ((BounceGame)game);
    bg.paddle.giveBall(bg.ball);
    bg.nextLevel();
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		GameObject currentObj;
	  for(Iterator<GameObject> it = bg.gameObjects.iterator(); it.hasNext();){
	    currentObj = it.next();
	    if(currentObj.active) {
        currentObj.render(g);
      }else{
	      System.out.println("Deleting le brick.");
	      isChanged = true;
	      if(currentObj.type == GameObject.GAMEOBJ_STAT) {
	        System.out.println("Adding to the score!");
          bg.score += ((Brick) currentObj).getStartingLives();
          scoreDelta += ((Brick) currentObj).getStartingLives();
        }
	      it.remove();
      }

    }
		//bg.ball.render(g);
		g.drawString("LIVES " + lives, 10, 30);
    g.drawString("SCORE " + bg.score, ((BounceGame) game).ScreenWidth-100,30 );
		for (Bang b : bg.explosions)
			b.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
		
		/*if (input.isKeyDown(Input.KEY_W)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, -.010f)));
		}
		if (input.isKeyDown(Input.KEY_S)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, +.010f)));
		}*/
		if(input.isKeyPressed(Input.KEY_F)){
		  /** Press F to pay respects. **/
      bg.currentLevel.nukeBricks();
    }
		if (input.isKeyDown(Input.KEY_A)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(-.040f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(+.040f, 0f)));
		}
    if(input.isKeyPressed(Input.MOUSE_LEFT_BUTTON) || input.isKeyPressed(Input.KEY_ENTER)){
      if(bg.paddle.hasBall) {
        dir = new Vector(-(bg.ball.getX()-input.getMouseX()),-bg.ball.getY()+input.getMouseY()).unit();
        bg.ball.setVelocity(dir.scale(.5f));
        bg.paddle.takeBall();
        bg.paddle.stick = false;
        bg.explosions.add(new Bang(bg.paddle.getX(),bg.paddle.getY()));
        System.out.println("Paddle WILL NOT grab ball now.");
      }else{
        System.out.println("Paddle should grab ball now.");
		    bg.paddle.stick = true;
      }

    }
    if(input.isKeyPressed(Input.KEY_TAB)){
      Brick temp;
      for(Iterator<Brick> it = bg.currentLevel.bricks.iterator();it.hasNext();){
        temp = it.next();
        if(temp != null){
          temp.active = false;
        }
      }
      GameObject obj;
      for(Iterator<GameObject> e = bg.gameObjects.iterator(); e.hasNext();) {
        obj = e.next();
        if (obj == null || !obj.active) {
          e.remove();
        }else {
          obj.update(delta);
        }
    }

      bg.nextLevel();
    }
		// bounce the ball...
    GameObject obj,obj2;
		boolean bounced;
    Collision col;
    for(Iterator<GameObject> e = bg.gameObjects.iterator(); e.hasNext();) {
      obj = e.next();
      if(obj == null) {continue;}
      bounced = false;
      if (obj.getCoarseGrainedMaxX() > bg.ScreenWidth){
        obj.translate( -obj.getCoarseGrainedMaxX()+bg.ScreenWidth-.001f,0.0f);
        if(obj.type != GameObject.GAMEOBJ_STAT) {
          obj.collide(90.0f);
        }
        bounced = true;
      }else if(obj.getCoarseGrainedMinX() < 0) {
        obj.translate(-obj.getCoarseGrainedMinX() + .001f, 0.0f);
        obj.collide(90.0f);
        bounced = true;
      }else if(obj.type == GameObject.GAMEOBJ_NONSTAT && obj.getCoarseGrainedMaxY() > bg.ScreenHeight){
        if(lives-- <= 0) {
          bg.score -= scoreDelta;
          ((GameOverState) game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
          game.enterState(BounceGame.GAMEOVERSTATE);
        }
        bg.paddle.giveBall((Ball)obj);
        bounced = true;
      }else if(obj.getCoarseGrainedMinY() < 0) {
        obj.translate(0.0f, -obj.getCoarseGrainedMinY()+.001f);
        if(obj.type != GameObject.GAMEOBJ_STAT) {
          obj.collide(0);
        }
        bounced = true;
      }
      /*if (bounced) {
        //bg.explosions.add(new Bang(obj.getX(), obj.getY()));
        bounces++;
      }*/
      float surfaceAngle;
      Vector velocity;
      Paddle p;
      /**
       * Check for collisions between game objects.
       **/
      for(Iterator<GameObject> it = bg.gameObjects.iterator(); it.hasNext();) {
        obj2 = it.next();
        if(obj2 == null) {continue;}
        if(!obj2.equals(obj)) {
          col = obj2.collides(obj);
          if (col != null){
            surfaceAngle = (float) Math.toDegrees(Math.atan2(col.getMinPenetration().getY(), col.getMinPenetration().getX()) + Math.PI / 2.0f);
            if(obj.type == GameObject.GAMEOBJ_NONSTAT) {
              // Translate the object away from the collision more if it's moving quickly
              Ball.clipEnforce(obj,obj2);
              //obj.translate(-col.getMinPenetration().getX() * (1+Math.abs(((Ball)obj).getVelocity().getX()))*4.0f, -col.getMinPenetration().getY() * (1+Math.abs(((Ball)obj).getVelocity().getY()))*4.0f);
            }else if(obj.type == GameObject.GAMEOBJ_MOMENT){
              p = (Paddle)obj;
              if(!p.stick) {
                bounces++;
                velocity = ((Ball) obj2).getVelocity().scale(1.01f);
                ((Ball) obj2).setVelocity(velocity);
              }else{
                System.out.println("Giving the ball to the paddle!");
                p.giveBall((Ball)obj2);
              }

            }
            if(obj2.type == GameObject.GAMEOBJ_NONSTAT) {
              // Translate the object away from the collision more if it's moving quickly
              Ball.clipEnforce(obj2,obj);
              //obj2.translate(col.getMinPenetration().getX() * (1+Math.abs(((Ball)obj2).getVelocity().getX()))*4.0f, col.getMinPenetration().getY() * (1+Math.abs(((Ball)obj2).getVelocity().getY()))*4.0f);
            }else if(obj2.type == GameObject.GAMEOBJ_MOMENT){
              p = (Paddle)obj2;
              bounces++;
              velocity = ((Ball) obj).getVelocity().scale(1.01f);
              ((Ball) obj).setVelocity(velocity);
              if(bounces%10 == 0) {
                p.giveBall((Ball) obj);
              }
            }
            if(obj.type != GameObject.GAMEOBJ_STAT || obj2.type != GameObject.GAMEOBJ_STAT) {
              obj2.collide(-surfaceAngle);
              obj.collide(surfaceAngle);
            }
          }
        }
      }

    }
		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}
		// Remove innactive objects.
    for(Iterator<GameObject> e = bg.gameObjects.iterator(); e.hasNext();) {
      obj = e.next();
      if (obj == null || !obj.active) {
        System.out.println("Adding to the score!");
        scoreDelta += ((Brick)obj).getStartingLives();
        bg.score += ((Brick)obj).getStartingLives();
        e.remove();
        isChanged = true;
      }else {
        obj.update(delta);
      }
    }
    if(isChanged){
      if(bg.currentLevel.isWon()) {
        if(bg.levelSelector.hasNext()) {
          game.enterState(BounceGame.ROUNDWONSTATE, new EmptyTransition(), new HorizontalSplitTransition());
        }else{
          game.enterState(BounceGame.GAMEWONSTATE, new EmptyTransition(), new HorizontalSplitTransition());
        }
      }
    }

		if (bounces >= 10000) {
		}
	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}
	
}
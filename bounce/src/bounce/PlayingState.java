package bounce;

import java.util.Iterator;

import bounce.GameObject;
import jig.Collision;
import jig.Entity;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
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
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		container.setSoundOn(true);
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
	      it.remove();
      }

    }
		//bg.ball.render(g);
		g.drawString("Bounces: " + bounces, 10, 30);
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
		if (input.isKeyDown(Input.KEY_A)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(-.030f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(+.030f, 0f)));
		}
		// bounce the ball...
    GameObject obj,obj2;
		boolean bounced;
    Collision col;
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
        //bg.explosions.add(new Bang(obj.getX(), obj.getY()));
        bounces++;
      }
      float surfaceAngle;
      float ballVel; // Store the magnitude of the velocity of the current object.
      for(Iterator<GameObject> it = bg.gameObjects.iterator(); it.hasNext();) {
        obj2 = it.next();
        if(!obj2.equals(obj)) {
          col = obj2.collides(obj);
          if (col != null){
            surfaceAngle = (float) Math.toDegrees(Math.atan2(col.getMinPenetration().getY(), col.getMinPenetration().getX()) + Math.PI / 2.0f);
            if(obj.type == GameObject.GAMEOBJ_NONSTAT) {
              ballVel = ((Ball)obj).getVelocity().length();
              // Translate the object away from the collision more if its move quickly
              obj.translate(-col.getMinPenetration().getX() * (1+ballVel)*8.0f, -col.getMinPenetration().getY() * (1+ballVel)*8.0f);
              bg.explosions.add(new Bang(obj.getX(),obj.getY()));
              bounces++;
            }
            if(obj2.type == GameObject.GAMEOBJ_NONSTAT) {
              ballVel = ((Ball)obj2).getVelocity().length();
              // Translate the object away from the collision more if its move quickly
              obj2.translate(col.getMinPenetration().getX() * (1+ballVel)*2.0f, col.getMinPenetration().getY() * (1+ballVel)*2.0f);
            }
            obj2.collide(-surfaceAngle);
            obj.collide(surfaceAngle);
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
      obj.update(delta);
      if (!obj.active) {
        e.remove();
      }
    }

		if (bounces >= 10000) {
			((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
			game.enterState(BounceGame.GAMEOVERSTATE);
		}
	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}
	
}
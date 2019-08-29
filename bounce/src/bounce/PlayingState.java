package bounce;

import java.util.Iterator;

import bounce.resource.GameObject;
import jig.Entity;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


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
		
		bg.ball.render(g);
		g.drawString("Bounces: " + bounces, 10, 30);
		for (Bang b : bg.explosions)
			b.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
		
		if (input.isKeyDown(Input.KEY_W)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, -.10f)));
		}
		if (input.isKeyDown(Input.KEY_S)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, +.10f)));
		}
		if (input.isKeyDown(Input.KEY_A)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(-.10f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(+.10f, 0f)));
		}
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
        bounces++;
      }
      obj.update(delta);
    }
		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
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
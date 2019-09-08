package bounce;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

import java.util.Iterator;

public class SplashState extends BasicGameState {
  private int timer;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timer = 4000;
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		g.drawImage(ResourceManager.getImage(BounceGame.SPLASH_BANNER), 0,
				0);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {


		timer -= delta;
		if (timer <= 0)
			game.enterState(BounceGame.STARTUPSTATE, new FadeOutTransition(), new FadeInTransition() );

	}

	@Override
	public int getID() {
		return BounceGame.GAMEOVERSTATE;
	}
}

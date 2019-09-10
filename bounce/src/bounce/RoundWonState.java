package bounce;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class RoundWonState extends BasicGameState {
  private int timer;
  @Override
  public int getID() {
    return BounceGame.ROUNDWONSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {

  }
  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    timer = 4000;
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

    BounceGame bg = (BounceGame)game;
    g.drawImage(ResourceManager.getImage(BounceGame.ROUND_OVER_BANNER), 225,
        270);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    timer -= delta;
    if(timer <= 0){
      game.enterState(BounceGame.PLAYINGSTATE);
    }

  }
}

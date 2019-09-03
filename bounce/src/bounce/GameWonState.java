package bounce;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameWonState extends BasicGameState {
  private int timer;
  @Override
  public int getID() {
    return BounceGame.GAMEWONSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    timer = 4000;
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    BounceGame bg = (BounceGame)game;
    for (Bang b : bg.explosions)
      b.render(g);
    g.drawImage(ResourceManager.getImage(BounceGame.GAME_WON_BANNER), 225,
        270);

  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
  }
}

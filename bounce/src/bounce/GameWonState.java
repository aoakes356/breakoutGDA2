package bounce;

import jig.ResourceManager;
import org.newdawn.slick.*;
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
    g.drawString("SCORE " + bg.score, ((BounceGame) game).ScreenWidth/2.0f-40,bg.ScreenHeight/2.0f+75 );

  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    Input input = container.getInput();
    if(input.isKeyDown(Input.KEY_ENTER) || input.isKeyDown(Input.KEY_SPACE)){
      game.enterState(((BounceGame)game).SPLASH_STATE);
    }
  }
}

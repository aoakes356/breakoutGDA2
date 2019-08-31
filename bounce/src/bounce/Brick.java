package bounce;

import bounce.GameObject;
import jig.ResourceManager;
import jig.Vector;

public class Brick extends GameObject {
  private int lives = 1;
  public boolean collided = false;
  public Brick(final float x, final float y) {
    super(x, y);
    setType(GameObject.GAMEOBJ_STAT);
    addImageWithBoundingBox(ResourceManager
        .getImage(BounceGame.BRICK_BASIC_RSC));
  }

  @Override
  public void collide(float tangent) {
    collided = true;
    System.out.println("Collided with le box.");
  }

  public void update(final int delta) {
    if(collided){
      lives--;
      collided = false;
    }
    if(lives <= 0){
      active = false;
    }
  }

  public void setLives(int lives){
    this.lives = lives;
  }
}

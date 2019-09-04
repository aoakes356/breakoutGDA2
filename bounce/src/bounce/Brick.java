package bounce;

import bounce.GameObject;
import jig.ResourceManager;
import jig.Vector;

public class Brick extends GameObject {
  private int lives;
  private int startingLives;
  public boolean collided = false;
  String currentImage;
  public Brick(final float x, final float y) {
    super(x, y);
    setType(GameObject.GAMEOBJ_STAT);
    lives = 1;
    addImageWithBoundingBox(ResourceManager
        .getImage(BounceGame.BRICK_SMALL_RSC));
    currentImage = BounceGame.BRICK_SMALL_RSC;
    startingLives = 1;
    active = true;
  }
  public Brick(Vector pos){
    this(pos.getX(),pos.getY());
  }
  public Brick(Vector pos, int lives){
    super(pos.getX(),pos.getY());
    if(lives == 1) {
      addImageWithBoundingBox(ResourceManager
          .getImage(BounceGame.BRICK_SMALL_RSC));
      currentImage = BounceGame.BRICK_SMALL_RSC;
    }else if(lives == 2){
      addImageWithBoundingBox(ResourceManager
          .getImage(BounceGame.BRICK_SMALL2_RSC));
      currentImage = BounceGame.BRICK_SMALL2_RSC;

    }else if(lives == 3){
      addImageWithBoundingBox((ResourceManager
          .getImage(BounceGame.BRICK_SMALL3_RSC)));
      currentImage = BounceGame.BRICK_SMALL3_RSC;

    }
    this.lives = lives;
    this.startingLives = lives;
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
      setImage();
    }
    if(lives <= 0){
      active = false;
    }
  }

  public void setImage(){
    if(lives == 0){
      return;
    }
    destroyImage();
    if(lives == 1) {
      addImage(ResourceManager
          .getImage(BounceGame.BRICK_SMALL_RSC));
      currentImage = BounceGame.BRICK_SMALL_RSC;
    }else if(lives == 2){
      addImage(ResourceManager
          .getImage(BounceGame.BRICK_SMALL2_RSC));
      currentImage = BounceGame.BRICK_SMALL2_RSC;

    }else if(lives == 3){
      addImage(ResourceManager
          .getImage(BounceGame.BRICK_SMALL3_RSC));
      currentImage = BounceGame.BRICK_SMALL3_RSC;

    }
  }

  public void destroyImage(){
    if(currentImage != null) {
      removeImage(ResourceManager.getImage(currentImage));
    }
  }

  public void resetLives(){
    lives= startingLives;
    setImage();
    active = true;
  }

  public void setLives(int lives){
    this.lives = lives;
    this.startingLives = lives;
  }

  public int getLives(){
    return lives;
  }
  public int getStartingLives(){
    return startingLives;
  }
}

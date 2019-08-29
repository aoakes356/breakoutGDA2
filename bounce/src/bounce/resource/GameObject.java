package bounce.resource;


import jig.Entity;

public class GameObject extends Entity {
  // Allow for abstract collision checking between game objects.
  public GameObject(){
    super();
  }
  public GameObject(float x, float y){
    super(x,y);
  }
  public void collide(float tangent){
  }
  public void update(final int delta){

  }
}

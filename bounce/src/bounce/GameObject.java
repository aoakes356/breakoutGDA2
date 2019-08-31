
package bounce;


import jig.Entity;

public class GameObject extends Entity {
  public static final int GAMEOBJ_NONSTAT = 2;
  public static final int GAMEOBJ_STAT = 1;
  public static final int GAMEOBJ_UNDEF = 0;
  public int type;
  public boolean active = true;
  // Allow for abstract collision checking between game objects.
  public GameObject(){
    super();
    type = GAMEOBJ_UNDEF;
  }
  public GameObject(float x, float y){
    super(x,y);
    type = 0;
  }
  public void collide(float tangent){
  }
  public void update(final int delta){

  }
  public void setType(int newType){
    type = newType;
  }
}

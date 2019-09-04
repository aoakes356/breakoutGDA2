package bounce;

import jig.Vector;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BrickStack {
  public HashMap<Vector,Brick> brickMap;
  public ArrayList<Brick> bricks;
  public BrickStack(){
    // Make one brick in the top left by default
    brickMap = new HashMap<>();
    bricks = new ArrayList<>();
  }

  public BrickStack(ArrayList<Vector> coords){
    Vector temp;
    Brick newBrick;
    brickMap = new HashMap<>();
    for(Iterator<Vector> it = coords.iterator(); it.hasNext();){
      temp = it.next();
      newBrick = new Brick(temp);
      brickMap.put(temp,newBrick);
      bricks.add(newBrick);
    }
  }

  public void reset(){
    Brick temp;
    for(Iterator<Brick> it = bricks.iterator(); it.hasNext();){
      temp = it.next();
      temp.resetLives();
      temp.active = true;
    }
  }

  public boolean isWon(){
    Brick temp;
    for(Iterator<Brick> it = bricks.iterator(); it.hasNext();) {
      temp = it.next();
      if(temp.active){
        return false;
      }
    }
    return true;

  }

  public void update(final int delta){
    Brick temp = null;
    for(Iterator<Brick> it = bricks.iterator(); it.hasNext(); temp = it.next()){
      temp.update(delta);
    }

  }
  public void addBrick(Vector pos, int lives){
    Brick newBrick = new Brick(pos, lives);
    brickMap.put(newBrick.getPosition(),newBrick);
    bricks.add(newBrick);

  }
  public void addBrick(Brick newBrick){
    brickMap.put(newBrick.getPosition(),newBrick);
    bricks.add(newBrick);
  }
  public ArrayList<Brick> getBricks(){
    return bricks;
  }


  /** Made this so you can't get a good score if used, useful for testing though. **/
  public void nukeBricks(){
    Brick temp;
    for(Iterator<Brick> it = bricks.iterator(); it.hasNext(); ) {
      temp = it.next();
      temp.active = false;
    }
  }

}


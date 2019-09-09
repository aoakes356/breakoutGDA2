package bounce;

import jig.ConvexPolygon;
import jig.ResourceManager;
import jig.Vector;

public class Paddle extends GameObject{
  private Vector velocity;
  private Vector startPos;
	private int countdown;
	public boolean hasBall = false;
	public boolean stick = false;
	public int hits = 0;
	private int oldHits = 0;
	private Ball ball;
	private String currentImage;

	public Paddle(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		//addShape(new ConvexPolygon(50,8));
		setType(GameObject.GAMEOBJ_MOMENT);
		addImageWithBoundingBox(ResourceManager
        .getImage(BounceGame.PADDLE_BASIC_RSC));
		currentImage = BounceGame.PADDLE_BASIC_RSC;
		velocity = new Vector(vx, 0.0f);
		startPos = new Vector(getX(),getY());
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * Bounce the ball off a surface. This simple implementation, combined
	 * with the test used when calling this method can cause "issues" in
	 * some situations. Can you see where/when? If so, it should be easy to
	 * fix!
	 *
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
		//removeImage(ResourceManager.getImage(BounceGame.BALL_BALLIMG_RSC));
		//addImageWithBoundingBox(ResourceManager
		//		.getImage(BounceGame.BALL_BROKENIMG_RSC));
    velocity = velocity.bounce(surfaceTangent);
		velocity.setY(0);
	}

	@Override
  public void collide(float tangent){
	  bounce(tangent);
  }

  public void giveBall(Ball b){
	  hasBall = true;
	  ball = b;
	  b.setY(getY()-(b.getCoarseGrainedHeight()));
	  b.setX(getX());
	  b.setVelocity(new Vector(0.0f,0.0f));
  }
  public void takeBall(){
	  hasBall = false;
  }

  public void updateImage(){
	  if(hits > 10) {
	    hits = 0;
    }
    removeImage(ResourceManager.getImage(currentImage));
	  if(hits > 0) {
      currentImage = "bounce/resource/basic_paddle" + hits + ".png";
    }else{
	    currentImage = BounceGame.PADDLE_BASIC_RSC;
    }
    addImage(ResourceManager.getImage(currentImage));
  }

	/**
	 * Update the Ball based on how much time has passed...
	 *
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	@Override
	public void update(final int delta) {
	  velocity = velocity.scale(.95f);
		translate(velocity.scale(delta).getX(),0.0f);
		if(hits != oldHits){
		  oldHits = hits;
		  updateImage();
    }
		if(hasBall){
		  ball.translate(velocity.scale(delta).getX(),0.0f);
    }
    setY(startPos.getY());
	}

}

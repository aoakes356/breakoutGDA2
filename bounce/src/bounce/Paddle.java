package bounce;

import jig.ConvexPolygon;
import jig.ResourceManager;
import jig.Vector;

public class Paddle extends GameObject{
  private Vector velocity;
	private int countdown;
	public boolean hasBall = false;
	public boolean stick = false;
	private Ball ball;

	public Paddle(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		//addShape(new ConvexPolygon(50,8));
		setType(GameObject.GAMEOBJ_MOMENT);
		addImageWithBoundingBox(ResourceManager
        .getImage(BounceGame.PADDLE_BASIC_RSC));
		velocity = new Vector(vx, 0.0f);
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
		velocity.setX(0);
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

	/**
	 * Update the Ball based on how much time has passed...
	 *
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	@Override
	public void update(final int delta) {
	  velocity = velocity.scale(.95f);
	  velocity.setY(0);
		translate(velocity.scale(delta));
		if(hasBall){
		  ball.translate(velocity.scale(delta));
    }
	}

}

package bounce;

import bounce.GameObject;
import jig.*;

/**
 * The Ball class is an Entity that has a velocity (since it's moving). When
 * the Ball bounces off a surface, it temporarily displays a image with
 * cracks for a nice visual effect.
 * 
 */
 class Ball extends GameObject {

	private Vector velocity;
	private int countdown;

	public Ball(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		setType(GameObject.GAMEOBJ_NONSTAT);
		addShape(new ConvexPolygon(20,20));
		addImage(ResourceManager
				.getImage(BounceGame.BALL_SMALLBALL_RSC));
		velocity = new Vector(vx, vy);
		countdown = 0;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	// Fixes the collision logic.
  public static Vector clipEnforce(GameObject nonStatic, GameObject isStatic){
    Vector vel = ((Ball)nonStatic).getVelocity();
    while(nonStatic.collides(isStatic) != null){
      nonStatic.translate(vel.scale(-2));
    }
    //nonStatic.translate(unitVel.scale(-nonStatic.collides(isStatic).getMinPenetration().length()*10.0f));
    return new Vector(0.0f,0.0f);

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
		countdown = 500;
		velocity = velocity.bounce(surfaceTangent);
	}

	@Override
  public void collide(float tangent){
    bounce(tangent);
  }

	/**
	 * Update the Ball based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	@Override
	public void update(final int delta) {
	  float speed = velocity.length();
	  if(speed > 2){
	    velocity = velocity.scale(2.0f/speed);
    }
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
			if (countdown <= 0) {
				addImage(ResourceManager
						.getImage(BounceGame.BALL_BALLIMG_RSC));
				removeImage(ResourceManager
						.getImage(BounceGame.BALL_BROKENIMG_RSC));
			}
		}
	}
}

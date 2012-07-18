import java.awt.Graphics;

public abstract class GameObject {
	int x; // x and y coordinates upper left
	int y;

	int width;
	int height;

	int velocityX; // Pixels to move each time move() is called.
	int velocityY;

	int rightBound; // Maximum permissible x, y values.
	int bottomBound;
	
	PongCourt court;

	public GameObject(int x, int y, int velocityX, int velocityY, int width,
			int height, PongCourt court) {
		this.x = x;
		this.y = y;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.width = width;
		this.height = height;
		this.court = court;
	}

	public void setBounds(int width, int height) {
		rightBound = width - this.width;
		bottomBound = height - this.height;
	}

	public void setVelocity(int velocityX, int velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	// Move the object at the given velocity.
	public void move() {
		x += velocityX;
		y += velocityY;

		accelerate();
		clip();
	}

	// Keep the object in the bounds of the court
	public void clip() {
		if (x < 0)
			x = 0;
		else if (x > rightBound)
			x = rightBound;

		if (y < 0)
			y = 0;
		else if (y > bottomBound)
			y = bottomBound;
	}

	/**
	 * Compute whether two GameObjects intersect.
	 * 
	 * @param other
	 *            The other game object to test for intersection with.
	 * @return NONE if the objects do not intersect. Otherwise, a direction
	 *         (relative to <code>this</code>) which points towards the other
	 *         object.
	 */
	public Intersection intersects(GameObject other) {
		if (       other.x > x + width
				|| other.y > y + height
				|| other.x + other.width  < x
				|| other.y + other.height < y)
			return Intersection.NONE;

		// compute the vector from the center of this object to the center of
		// the other
		double dx = other.x + other.width /2 - (x + width /2);
		double dy = other.y + other.height/2 - (y + height/2);

		double theta = Math.atan2(dy, dx);
		double diagTheta = Math.atan2(height, width);

		if ( -diagTheta <= theta && theta <= diagTheta )
			return Intersection.RIGHT;
		if ( diagTheta <= theta && theta <= Math.PI - diagTheta )
			return Intersection.DOWN;
		if ( Math.PI - diagTheta <= theta || theta <= diagTheta - Math.PI )
			return Intersection.LEFT;
		// if ( diagTheta - Math.PI <= theta && theta <= diagTheta)
		return Intersection.UP;
	}

	public abstract void accelerate();

	public abstract void draw(Graphics g);
}

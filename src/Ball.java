import java.awt.*;

public class Ball extends GameObject {
	final static int DIAMETER = 15;

	public Ball(int x, int y, int velocityX, int velocityY, PongCourt court) {
		super(x, y, velocityX, velocityY, DIAMETER, DIAMETER, court);
	}

	public void accelerate() {
		if (x < 0)
			velocityX =  Math.abs(velocityX);
		else if (x > rightBound)
			velocityX = -Math.abs(velocityX);
		if (y < 0)
			velocityY =  Math.abs(velocityY);
		else if (y > bottomBound){
			court.loseLife();
		    court.resetE();
		}
	}

	// Bounce the ball, if necessary
	public void bounce(Intersection i) {
		switch (i) {
		case NONE: break;
		case UP: velocityY = -Math.abs(velocityY); break;
		case DOWN: velocityY = Math.abs(velocityY); break;
		case LEFT: velocityX = -Math.abs(velocityX); break;
		case RIGHT: velocityX = Math.abs(velocityX); break;
		}
	}

	public void draw(Graphics g) {
		g.setColor(Color.magenta);
		g.fillOval(x, y, DIAMETER, DIAMETER);
	}
}

import java.awt.*;

public class Paddle extends GameObject {
	final static int HEIGHT = 10;
	final static int WIDTH = 50;

	public Paddle(int courtwidth, int courtheight, PongCourt court) {
		super((courtwidth - WIDTH) / 2, courtheight - HEIGHT - 20, 0, 0, WIDTH, HEIGHT, court);
	}

	public void accelerate() {
		if (x < 0 || x > rightBound)
			velocityX = 0;
		if (y < 0 || y > bottomBound)
			velocityY = 0;
	}

	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(x, y, WIDTH, HEIGHT);
	}
	
	public void setX(int x){
		this.x = x; 
	}
}

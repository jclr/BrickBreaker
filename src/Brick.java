import java.awt.*;

public class Brick extends GameObject {
	
	final static int WIDTH = 20;
	final static int HEIGHT = 10;
	
	public Brick(int x, int y, int velocityX, int velocityY, PongCourt court){
		super(x, y, velocityX, velocityY, WIDTH, HEIGHT, court);
	}
	
	
	public void draw(Graphics g) {
		g.fillRect(x, y, WIDTH, HEIGHT);
	}
	
	public void accelerate(){
		velocityX = 0;
		velocityY = 0;
	}
	
}

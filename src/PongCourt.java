import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class PongCourt extends JPanel {
	private Ball ball;
	private Paddle paddle;
	
	private int lives = 3; // Initial number of lives 
	
	final int BRICKS_HEIGHT = 6; // Brick
	final int BRICKS_WIDTH = 27; // Size
	
	private Brick[][] bricks = new Brick[BRICKS_HEIGHT][BRICKS_WIDTH]; // Array of bricks
	private int numBricks = BRICKS_HEIGHT*BRICKS_WIDTH; // Initial number of bricks

	private int interval = 35; // Milliseconds between updates.
	private Timer timer;       // Each time timer fires we animate one step.

	final int COURTWIDTH = 600;  // Court
	final int COURTHEIGHT = 400; // Size

	final int PADDLE_VEL = 8; // Paddle Velocity
	
	private boolean help = false; // Has "help" been pressed?
	private boolean gameOver = false; // Is the game over?
	private boolean won = false; // Has the game been won?
	private int scoreNum = 0; // The score
	
	private JLabel life;
	private JLabel blocks;
	private JLabel score;
	
	private MouseMotionListener mouse; // Mouse listener for paddle motion

	public PongCourt(JLabel life, JLabel blocks, JLabel score) {
		setPreferredSize(new Dimension(COURTWIDTH, COURTHEIGHT));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFocusable(true);
		mouse = new Mouse();
		addMouseMotionListener(mouse);
		
		this.life = life;
		this.blocks = blocks;
		this.score = score;
		
		for (int i = 0; i < bricks.length; i++){
			for (int j = 0; j < bricks[i].length; j++){
				bricks[i][j] = new Brick(j*22 + 4, i*12 + 4, 0, 0, this);
			}
		}

		timer = new Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent e) { tick(); }});
		timer.start(); 

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					paddle.setVelocity(-PADDLE_VEL, 0);
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					paddle.setVelocity(PADDLE_VEL, 0);
				else if (e.getKeyCode() == KeyEvent.VK_R)
					reset();
			}

			public void keyReleased(KeyEvent e) {
				paddle.setVelocity(0, 0);
			}
		});
		// After a PongCourt object is built and installed in a container
		// hierarchy, somebody should invoke reset() to get things started... 
	}

	public void reset() {
		ball = new Ball(0, 100, 2, 8, this);
		paddle = new Paddle(COURTWIDTH, COURTHEIGHT, this);
		for (int i = 0; i < bricks.length; i++){
			for (int j = 0; j < bricks[i].length; j++){
				bricks[i][j] = new Brick(j*22 + 4, i*12 + 4, 0, 0, this);
			}
		}
		lives = 3;
		gameOver = false;
		won = false;
		help = false;
		life.setText("Lives: " + lives);
		blocks.setText("Bricks Left: " + numBricks);
		score.setText("Score: " + scoreNum);
		timer.start();
		grabFocus();
	}
	
	// Resets ball, paddle, and updates the number of lives
	public void resetE(){
		ball = new Ball(0, 100, 2, 8, this);
		paddle = new Paddle(COURTWIDTH, COURTHEIGHT, this);
		grabFocus();
		life.setText("Lives: " + lives);
	}

	void tick() {
		// Initialization
		ball.setBounds(getWidth(), getHeight());
		ball.move();
		paddle.setBounds(getWidth(), getHeight());
		paddle.move();
		Intersection inter = paddle.intersects(ball);
		ball.bounce(inter);
		
		// Ball control
		if (inter != Intersection.NONE){
		int diff = ball.x - paddle.x;
		if (diff > 12 && diff < 22){
			ball.setVelocity(0, ball.velocityY);
		}
		else if (diff < 12 && diff > 2){
			ball.setVelocity(-2, ball.velocityY);
		}
		else if (diff < 2 && diff > -8){
			ball.setVelocity(-4, ball.velocityY);
		}
		else if (diff < -8 && diff > -15){
			ball.setVelocity(-6, ball.velocityY);
		}
		else if (diff < 32 && diff > 22){
			ball.setVelocity(2, ball.velocityY);
		}
		else if (diff < 42 && diff > 32){
			ball.setVelocity(4, ball.velocityY);
		}
		else if (diff < 50 && diff > 42){
			ball.setVelocity(6, ball.velocityY);
		}
		}
		
		// Game Processing
		boolean wonHelp = true;
		for (int i = 0; i < bricks.length; i++){
			for (int j = 0; j < bricks[i].length; j++){
				if (bricks[i][j] != null){
				Intersection in = bricks[i][j].intersects(ball);
				if (in != Intersection.NONE){
					scoreNum += (Math.abs((i-5)*10) + 10);
					ball.bounce(in);
					bricks[i][j] = null;
					int count = 0;
					for (int x = 0; x < bricks.length; x++){
						for(int y = 0; y < bricks[x].length; y++){
							if (bricks[x][y] != null){
							count++;
							}
						}
					}
					numBricks = count;
					blocks.setText("Bricks Left: " + numBricks);
					score.setText("Score: " + scoreNum);
				}
				if (bricks[i][j] != null){
					wonHelp = false;
				}
				}
			}
		}
		if (wonHelp){
			won = true;
		}
		repaint(); // Repaint indirectly calls paintComponent.
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Paint background, border
		
		if (help){ // Displays the help screen
			timer.stop();
			g.drawString("The goal of this game is to eliminate all the bricks by bouncing the ball off of them.", 10, 15);
			g.drawString("You begin with three lives. If the ball hits the bottom of the window, a life will be taken away.", 10, 30);
			g.drawString("The game is over when all of your lives are gone.", 10, 45);
			g.drawString("Your score is based on how many of each color brick you've eliminated.", 10, 60);
			g.drawString("Yellow bricks are worth 10 points, orange are worth 20, red are worth 30, green are worth 40", 10, 75);
			g.drawString("blue are worth 50, and black are worth 60.", 10, 90);
			g.drawString("The paddle can be controlled either with the arrow keys or with the mouse.", 10, 105);
			g.drawString("The angle of the ball's path depends on where the ball hits the paddle.", 10, 120);
			g.drawString("Have fun!!!", 10, 135);
		}
		
		else if (!gameOver && !won){ // Displays the main playing screen
		timer.start();
		ball.draw(g);
		paddle.draw(g);
		for (int i = 0; i < bricks.length; i++){
			for (int j = 0; j < bricks[i].length; j++){
				if (bricks[i][j] != null){
					switch(i){ // Sets colors
					case 0: 
						g.setColor(Color.BLACK);
						bricks[i][j].draw(g);
						break;
					case 1: 
						g.setColor(Color.BLUE);
						bricks[i][j].draw(g);
						break;
					case 2: 
						g.setColor(Color.GREEN);
						bricks[i][j].draw(g);
						break;
					case 3: 
						g.setColor(Color.RED);
						bricks[i][j].draw(g);
						break;
					case 4: 
						g.setColor(Color.ORANGE);
						bricks[i][j].draw(g);
						break;
					case 5: 
						g.setColor(Color.YELLOW);
						bricks[i][j].draw(g);
						break;
					/*default: g.setColor(Color.BLACK);
					         break;*/
				    
				}
			}
		}
		}
		}
		
		else if (gameOver && !won){ // Displays the losing screen
			g.drawString("GAME OVER", 265, 200);
			timer.stop();
		}
		
		else if (!gameOver && won){ // Displays the winning screen
			g.drawString("YOU WON!", 265, 200);
			timer.stop();
		}
	}
	
	public void loseLife(){ // Decrements the number of lives
		lives--;
		if (lives == 0){
			gameOver = true;
		}
	}
	
	public void setHelp(boolean b){ // Sets the help boolean
		help = b;
	}
	
	public boolean getHelp(){ // Gets the help boolean
		return help;
	}
	
	public void startTimer(){ 
		timer.start();
	}
	
	// Inner class for the mouse listener
	class Mouse extends MouseAdapter implements MouseMotionListener {
		public void mouseMoved(MouseEvent e){
			Point p = e.getPoint();
			int xPoint = (int) p.getX();
			paddle.setX(xPoint);
		}
	}
	
	
}

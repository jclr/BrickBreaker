import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Game {

	private Game() {
		// Top-level frame
		final JFrame frame = new JFrame("Pong");
		frame.setLocation(300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Labels
		JLabel lives = new JLabel("Lives: ");
		JLabel blocks = new JLabel("Blocks Left: ");
		JLabel score = new JLabel("Score: ");

		// Main playing area
		final PongCourt court = new PongCourt(lives, blocks, score);
		frame.add(court, BorderLayout.CENTER);
		

		// Reset button
		final JPanel panel = new JPanel();
		frame.add(panel, BorderLayout.NORTH);
		final JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() { // Add action listener to the button
			public void actionPerformed(ActionEvent e) {
				court.reset();
			}
		});
		
		// Help Button
		final JButton help = new JButton("Help");
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // Add action listener to the button
				if (court.getHelp()){
					court.setHelp(false);
					court.startTimer();
					court.grabFocus();
				}
				else{
					court.setHelp(true);
				}
			}
		});
		
		//Add Borders
		blocks.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		score.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lives.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// Add buttons and labels to the toolbar
		panel.add(blocks);
		panel.add(score);
		panel.add(lives);
		panel.add(reset);
        panel.add(help);

		// Put the frame on the screen
		frame.pack();
        frame.setVisible(true);
		// Start the game running
        court.reset();
		}

	/*
	 * Rather than directly building the top level frame object in the main
	 * method, we use the invokeLater utility method to ask the Swing framework
	 * to invoke the method 'run' of the Runnable object we pass it, at some
	 * later time that is convenient for it. (The key technical difference is
	 * that this will cause the new object to be created by a different
	 * "thread".)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Game();
			}
		});
	}

}

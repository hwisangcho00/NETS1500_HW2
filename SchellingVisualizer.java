package schelling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Graphical visualizer for Schelling simulator
 * 
 * @author zives, modified by swapneel
 * 
 */
public class SchellingVisualizer {

	// frame for drawing
	private JFrame frame;

	private SchellingSimulator simulator;

	/**
	 * Used to determine how large to make each cell on the grid
	 */
	public final int SCALE = 12;

	/**
	 * A component representing the grid
	 * 
	 * @author zives
	 */
	private class SchellingMap extends Component {

		private static final long serialVersionUID = 1L;

		public Dimension getPreferredSize() {
			return new Dimension((simulator.getWidth() + 1) * SCALE + 1,
					(simulator.getHeight() + 1) * SCALE + 1);
		}

		public Dimension getMinimumSize() {
			return getPreferredSize();
		}

		/**
		 * Draw the grid
		 */
		public void paint(Graphics g) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, (simulator.getWidth()) * SCALE - 1,
					(simulator.getHeight()) * SCALE - 1);

			g.setColor(Color.BLACK);
			g.drawRect(0, 0, (simulator.getWidth()) * SCALE - 1,
					(simulator.getHeight()) * SCALE - 1);

			for (int x = 0; x < simulator.getWidth(); x++) {
				for (int y = 0; y < simulator.getHeight(); y++) {
					int label = simulator.getGrid(x, y);

					if (label == 1) {
						g.setColor(Color.BLUE);
						g.fillRect(x * SCALE, y * SCALE, SCALE - 1, SCALE - 1);
						g.setColor(Color.LIGHT_GRAY);
						g.drawRect(x * SCALE, y * SCALE, SCALE - 1, SCALE - 1);
					} else if (label == 2) {
						g.setColor(Color.YELLOW);
						g.fillRect(x * SCALE, y * SCALE, SCALE - 1, SCALE - 1);
						g.setColor(Color.LIGHT_GRAY);
						g.drawRect(x * SCALE, y * SCALE, SCALE - 1, SCALE - 1);
					} else {
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(x * SCALE, y * SCALE, SCALE - 1, SCALE - 1);
						g.setColor(Color.DARK_GRAY);
						g.drawRect(x * SCALE, y * SCALE, SCALE - 1, SCALE - 1);
					}
				}
			}
		}
	}

	/**
	 * The constructor
	 * Will create an object of the simulator and create the graphics
	 */
	public SchellingVisualizer() {
		//create new simulator
		simulator = new SchellingSimulator(1000, 2, (int) (75 * 75 * 0.60), 2, 75, 75);
//		simulator = new SchellingSimulator(100, 3, 20000, 4, 150, 150);
//		simulator = new SchellingSimulator(100, 2, 100, 1, 10, 10);

		
		//do all the graphics
		frame = new JFrame("Schelling Visualizer");

		frame.setLayout(new BorderLayout());

		JPanel p = new JPanel();

		p.add(new SchellingMap());

		frame.add(BorderLayout.CENTER, p);
		frame.setSize(1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Run the simulation
	 * 
	 * @param K the number of trials
	 */
	public void simulate(int K) {
		ArrayList<Double> homophily = new ArrayList<Double>();
		homophily = simulator.simulate(K);

		// Compute mean + stddev
		double avgHom = 0;
		double stDev = 0;
		for (int i = 0; i < K; i++) {
			avgHom += homophily.get(i);
		}
		avgHom /= K;
		for (int i = 0; i < K; i++) {
			stDev += (avgHom - homophily.get(i)) * (avgHom - homophily.get(i));
		}
		stDev = Math.sqrt(stDev / K);

		System.out.println("Average homophily ratio across trials: " + avgHom);
		System.out.println("Standard deviation across trials: " + stDev);
		
		updateMap();
	}

	/**
	 * This method will update the schelling map, based on the simulation just completed
	 */
	public void updateMap() {
		//redraw the frame
		frame.validate();
		frame.repaint();
		//make the frame visible now - since we are done with the simulation
		frame.setVisible(true);
	}

	/**
	 * Main program
	 */
	public static void main(String[] args) {

		SchellingVisualizer vis = new SchellingVisualizer();
		vis.simulate(100);
	}
}

package socialMediaFakenNews.topology;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class TopologyCreator {

	protected Context <Object> context;
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected Random random;
	@SuppressWarnings("rawtypes")
	protected Class agentClass;
	protected String convertToBotMethod;
	protected int totalNodes;
	protected int numberOfBots;
	
	@SuppressWarnings("rawtypes")
	public TopologyCreator(
			Context <Object> context, 
			ContinuousSpace<Object> space, 
			Grid<Object> grid, 
			Class agentClass,
			String convertToBotMethod,
			int totalNodes,
			int numberOfBots) {
		this.context = context;
		this.space = space;
		this.grid = grid;
		this.agentClass = agentClass;
		this.convertToBotMethod = convertToBotMethod;
		this.totalNodes = totalNodes;
		this.numberOfBots = numberOfBots;
		
		// Initialize the random object to work with.	
		random = new Random();
	}
	
	protected void addAgentsInCircleToSpace() {
		double degreesCount;
		double teta;
		double iteration = 0;
		double x;
		double y;
		// Move the objects to the space
		for (Object obj : context) {
			degreesCount = 0 + (iteration * (360.0 / totalNodes));
			// System.out.println("Degrees count "+ degreesCount);
			teta = Math.toRadians(degreesCount);
			x = 24 * Math.sin(teta) + 25;
			y = 24 * Math.cos(teta) + 25;
			// System.out.println("Moving agent to " + x + ", " + y);
			// System.out.println("Space dimentions: "+ space.getDimensions());
			space.moveTo(obj, x, y);
			iteration++;
		}
	}
	
	protected double randomNumberBetweenMargins(int low, int high) {
		Integer intNumber = random.nextInt(high - low) + low;
		return random.nextDouble() + intNumber.doubleValue();
	}
	
	protected void addAgentsInRandomSpace() {
		double x;
		double y;
		for (Object obj : context) {
			x = randomNumberBetweenMargins(0, 50);
			y = randomNumberBetweenMargins(0, 50);
			// System.out.println("Adding element to " + x+", "+y);
			space.moveTo(obj, x, y);
		}
	}
	
	public void createTopology() {
		// System.out.println("Creating topology " + this.getClass().getSimpleName());
	}
}

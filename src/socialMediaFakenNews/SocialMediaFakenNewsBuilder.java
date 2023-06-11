package socialMediaFakenNews;

import java.util.HashMap;
import java.util.Iterator;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import socialMediaFakenNews.agent.Agent;
import socialMediaFakenNews.agent.AgentState;
import socialMediaFakenNews.topology.TopologyGenerator;
import socialMediaFakenNews.util.ModelUtils;

public class SocialMediaFakenNewsBuilder implements ContextBuilder<Object> {	
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Context build(Context <Object> context) { 
		context.setId(ModelUtils.modelContextName);
		
		// Create the space in which our network is going to be contained
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(ModelUtils.modelSpaceName, 
				context, 
				new SimpleCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.StickyBorders(), 
				ModelUtils.spaceXSize, 
				ModelUtils.spaceYSize);
		
		// Create the grid in which our agents are going to be moved
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(new HashMap<String, Object>());
		Grid<Object> grid = gridFactory.createGrid(ModelUtils.modelGridName, context, // GridBuilderParameters.singleOccupancy2DTorus(new SimpleGridAdder<Object>(), 50, 50)); 
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(),
						true, 50, 50));
		
		// Get the parameters for our simulation
		Parameters params = RunEnvironment.getInstance().getParameters();
		ModelUtils.getParameters(params);
		TopologyGenerator generator = new TopologyGenerator(context, space, grid);
		generator.configure(
				Agent.class, 
				"convertToBot",
				ModelUtils.totalAgents, 
				ModelUtils.nodesInBarabasi, 
				ModelUtils.initialNodesInBarabasi, 
				0, 
				ModelUtils.createInterest, 
				ModelUtils.nOfInterests, 
				ModelUtils.nFollowersTobeInfluencer, 
				0);
		generator.generateSelectedTopology(ModelUtils.selectedTopology);
		addBelievers(context);
		
		addDeniers(context);
		
		addInfluencers(context, ModelUtils.nInfluencersBelievers, AgentState.BELIEVER);
		addInfluencers(context, ModelUtils.nInfluencersDeniers, AgentState.DENIER);
		addInfluencers(context, ModelUtils.nInfluencersSusceptibles, AgentState.SUSCEPTIBLE);
		
		RunEnvironment.getInstance().endAt(ModelUtils.numberOfTicks);
		System.out.println("Created Model " + ModelUtils.pDeny);
		return context;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addBelievers(Context context) {
		
		//Agent believer;
		Iterator<Object> agents = context.getRandomObjects(Agent.class, ModelUtils.believersCount).iterator();
		Agent believer;
		while (agents.hasNext()) {
			// System.out.println("Adding believer");
			believer = (Agent) agents.next();
			believer.convertToBeliever();
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDeniers(Context context) {
		
		Iterator<Object> agents = context.getRandomObjects(Agent.class, ModelUtils.denyCount).iterator();
		Agent denier;
		while (agents.hasNext()) {
			// System.out.println("Adding denier");
			denier = (Agent) agents.next();
			denier.convertToDenier();
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void addInfluencers(Context context, int nInfluencers, AgentState influencersState) {
		Iterator<Object> agents = context.getRandomObjects(Agent.class, nInfluencers).iterator();
		Agent influencer;
		while (agents.hasNext()) {
			// System.out.println("Adding influencer");
			influencer = (Agent) agents.next();
			influencer.setState(influencersState);
			incrementAgentConnectionsTo(ModelUtils.nFollowersTobeInfluencer, influencer, context);
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void incrementAgentConnectionsTo(int totalConnections, Agent influencer, Context context) {
		Network<Object> net = (Network<Object>) context.getProjection(ModelUtils.modelNetworkName);
		int degree = net.getDegree(influencer);
		// System.out.println("Degree of the influencer is " + degree);
		Iterator<Object> agents;
		Agent tmp;
		while (degree < totalConnections) {
			agents = context.getRandomObjects(Agent.class, totalConnections - degree).iterator();
			while (agents.hasNext()) {
				tmp = (Agent) agents.next();
				if (!net.isAdjacent(influencer, tmp)) {
					net.addEdge(influencer, tmp);
				}
			}
			degree = net.getDegree(influencer);
			// System.out.println("Degree of the influencer is " + degree);
		}
	}

}

package socialMediaFakenNews.util;

import repast.simphony.parameter.Parameters;

public class ModelUtils {

	public static int believersCount; 
	public static int denyCount; 
	public static int susceptibleCount; 
	public static int averageFollowers;
	public static int spaceXSize = 50;
	public static int spaceYSize = 50;
	public static String selectedTopology;
	public static int numberOfTicks;
	public static int totalAgents;
	public static int nodesInBarabasi;
	public static int initialNodesInBarabasi;
	public static int nBots;
	public static boolean createInterest;
	public static int nOfInterests;
	public static int nInfluencersBelievers;
	public static int nInfluencersDeniers;
	public static int nInfluencersSusceptibles;
	public static int nFollowersTobeInfluencer;
	public static int nBotsConnections;
	
	public static double pInfl;
	public static double pbelieve;
	public static double pDeny;
	public static double pVacc;
	public static double pShareFake;
	public static double pShareD;
	
	public static String modelContextName = "SocialMediaFakenNews_Context";
	public static String modelNetworkName = "SocialMediaFakenNews_Network";
	public static String modelSpaceName = "SocialMediaFakenNews_Space";
	public static String modelGridName = "SocialMediaFakenNews_Grid";
	
	public static int timeToStartLosingInterest = 75;
	public static double timeReduceFactor = 1.0;
	
	
	public static void getParameters(Parameters params) {
		believersCount = params.getInteger("believer_count");
		denyCount = params.getInteger("denier_count");
		susceptibleCount = params.getInteger("user_count");
		averageFollowers = params.getInteger("avg_followers");
		selectedTopology = params.getString("selected_topology");
		numberOfTicks = params.getInteger("number_of_ticks");
		totalAgents = params.getInteger("user_count");
		nodesInBarabasi = params.getInteger("nodes_in_barabasi");
		initialNodesInBarabasi = params.getInteger("initial_nodes_in_barabasi");
		nBots = params.getInteger("bots_count");
		createInterest = params.getBoolean("create_interests");
		nOfInterests = params.getInteger("number_of_interests");
		nInfluencersBelievers = params.getInteger("influencer_count_believers");
		nInfluencersDeniers = params.getInteger("influencer_count_deniers");
		nInfluencersSusceptibles = params.getInteger("influencer_count_susceptibles");
		nFollowersTobeInfluencer = params.getInteger("n_followers_to_be_influencer");
		nBotsConnections = params.getInteger("n_bots_connections");
		pInfl = params.getDouble("influencer_prob");
		pbelieve = params.getDouble("believe_prob");
		pDeny = params.getDouble("deny_prob");
		pVacc = params.getDouble("vacc_prob");
		pShareFake = params.getDouble("pShareFake");
		pShareD = params.getDouble("pShareD");
	}
}

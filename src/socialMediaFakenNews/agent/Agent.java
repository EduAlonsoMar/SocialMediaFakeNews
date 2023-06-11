package socialMediaFakenNews.agent;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.util.ContextUtils;
import socialMediaFakenNews.util.ModelUtils;

public class Agent {
	
	private Random r;
	private AgentState state;
	private Queue<SocialMediaMessage> feed;
	private double pDeny = ModelUtils.pDeny;
	private double pBelieve = ModelUtils.pbelieve;
	private double pInfl = ModelUtils.pInfl;
	private double pVacc = ModelUtils.pVacc;
	private double pShareF = ModelUtils.pShareFake;
	private double pShareD = ModelUtils.pShareD;
	private int ticksFromTheBeggining = 0;
	private boolean isInfluencer = false;
	private boolean isSharingFakeNews = false;
	
	public Agent() {
		this.state = AgentState.SUSCEPTIBLE;
		this.r = new Random();
		this.feed = new PriorityQueue<SocialMediaMessage>();
	}
	
	public AgentState getState() {
		return this.state;
	}
	
	public void setState(AgentState state) {
		this.state = state;
	}
	
	public boolean isInfluencer() {
		return this.isInfluencer;
	}
		
	public boolean isBeleiver() {
		return this.state == AgentState.BELIEVER;
	}
	
	public boolean isFactChecker() {
		return this.state == AgentState.DENIER;
	}
	
	public void convertToBeliever() {
		this.state = AgentState.BELIEVER;
	}
	
	public void convertToDenier() {
		this.state = AgentState.DENIER;
	}
	
	public boolean isSharingFakeNews() {
		return this.isSharingFakeNews;
	}
	
	private SocialMediaMessage readFeed() {
		SocialMediaMessage message = null;
		while (!this.feed.isEmpty()) {
			message = this.feed.poll();
		}
		return message;
	}
	
	private void calculateIsInfluencer() {
		@SuppressWarnings("unchecked")
		Context<Object> context = ContextUtils.getContext(this);
		@SuppressWarnings("unchecked")
		Network<Object> net = (Network<Object>) context.getProjection(ModelUtils.modelNetworkName);
		Iterator<RepastEdge<Object>> targets = net.getOutEdges(this).iterator();
		int i = 0;
		while (targets.hasNext()) {
			i++;
			targets.next();
		}
		this.isInfluencer = i > ModelUtils.nFollowersTobeInfluencer;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		this.ticksFromTheBeggining++;
		this.isSharingFakeNews = false;
		
		if (this.ticksFromTheBeggining == 1) {
			calculateIsInfluencer();
		}
		
		if (this.state == AgentState.SUSCEPTIBLE) {
			SocialMediaMessage message = readFeed();
			if (message != null) {
				// System.out.println("reading " + message.getType().toString() + " message");
				if (message.getType() == FeedType.FAKE_NEWS) {
					receiveInformation(message.getCreator().isInfluencer());
				} else {
				  	receiveVaccine(message.getCreator().isInfluencer());
				}	
			}
			
		} else if (this.state == AgentState.BELIEVER) {
			SocialMediaMessage message = readFeed();
			if (message != null) {
				if (message.getType() == FeedType.DEBUNKING) {
					//receiveVaccine(message.getCreator().isInfluencer);
					
					if (stopBelieving()) {
						this.state = AgentState.CURED;
					}
				}	
			}
			
			if (this.state == AgentState.BELIEVER) {
				shareMessage(new SocialMediaMessage(this, FeedType.FAKE_NEWS));
			}
		} else if (this.state == AgentState.DENIER) {
			shareMessage(new SocialMediaMessage(this, FeedType.DEBUNKING));
		}
	}
	
	/**
	 * Calculates if this particular user believes something.
	 * It uses the probability of believe.
	 * Generates a random number between 0 and 100. 
	 * If the generated number plus the probability is 100 or more,
	 * then the susceptible user believes the information. 
	 * @return true if user believes,
	 * 			false otherwise.
	 */
	public void receiveInformation(boolean byInfluencer) {
		Random random = new Random();
		double randomForBeleive = random.nextDouble();
		double randomForDeny = random.nextDouble();
		double timeAdjustment = (ticksFromTheBeggining > ModelUtils.timeToStartLosingInterest) ? (1.0/(ticksFromTheBeggining/ModelUtils.timeReduceFactor)) : 1;
		double Pbelieve2use = (byInfluencer ? pInfl : pBelieve) * (timeAdjustment);
		double PDeny2Use = (byInfluencer ? pInfl : pDeny) * timeAdjustment;
	
		if (randomForBeleive < Pbelieve2use) {
			this.state = AgentState.BELIEVER;
			
		} else if (randomForDeny < PDeny2Use) {
			System.out.println("receiving Fake news:\n randomForBelieve: " + randomForBeleive + "\nrandomForDeny: " + randomForDeny);
			this.state = AgentState.DENIER;
			
		}
	}
	
	public void receiveVaccine(boolean byInfluencer) {
		Random random = new Random();
		double randomNumber = random.nextDouble();
		double timeAdjustment = (ticksFromTheBeggining > ModelUtils.timeToStartLosingInterest) ? (1.0/(ticksFromTheBeggining/ModelUtils.timeReduceFactor)) : 1;
		double Pvacc2use = (byInfluencer ? pInfl : pDeny) * timeAdjustment;
		// System.out.println("receiving Vaccine: \nrandomNumber: " + randomNumber + "\nPvacc2use: " + Pvacc2use);
		if (randomNumber < Pvacc2use) {
			System.out.println("User vaccinated: " + randomNumber + " pVacc: " + Pvacc2use);
			this.state = AgentState.DENIER;
		}
	}
	
	private boolean stopBelieving() {
		double timeAdjustment = (ticksFromTheBeggining > ModelUtils.timeToStartLosingInterest) ? (1.0/(ticksFromTheBeggining/ModelUtils.timeReduceFactor)) : 0.1;
		double p2use = (this.pVacc * timeAdjustment);
		double random = this.r.nextDouble();
		if (random < p2use) {
			System.out.println("Believer to check if stopBelieving: " + random + " Prob: " + p2use);
		}
		
		return (random < p2use);
	}
	
	private void shareMessage(SocialMediaMessage message) {
		
		double number = this.r.nextDouble();
		boolean share = false;
		
		if (message.getType() == FeedType.FAKE_NEWS) {
			if (number < this.pShareF) {
				share = true;
				this.isSharingFakeNews = true;
			}
		} else {
			if (number < this.pShareD) {
				share = true;
			}
		}


		if (share) {
			@SuppressWarnings("unchecked")
			Context<Object> context = ContextUtils.getContext(this);
			@SuppressWarnings("unchecked")
			Network<Object> net = (Network<Object>) context.getProjection(ModelUtils.modelNetworkName);
			Iterator<RepastEdge<Object>> followers = net.getOutEdges(this).iterator();
			Agent tmp;
			RepastEdge<Object> edge;
			while(followers.hasNext()) {
				edge = followers.next();
				tmp = (Agent) edge.getTarget();
				tmp.receiveMessage(message);
			}
		}
	}
	
	private void receiveMessage(SocialMediaMessage message) {
		this.feed.add(message);
	}

}

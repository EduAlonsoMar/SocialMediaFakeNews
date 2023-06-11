package socialMediaFakenNews.agent;


public class SocialMediaMessage implements Comparable<SocialMediaMessage> {
	
	private FeedType type;
	private Agent creator;
	
	public SocialMediaMessage(Agent creator, FeedType type) {
		this.type = type;
		this.creator = creator;
	}
	
	public Agent getCreator() {
		return this.creator;
	}
	
	public FeedType getType() {
		return this.type;
	}
	
	@Override
	public int compareTo(SocialMediaMessage other) {
	    return (Integer)(this.type).compareTo(other.type);
	}

}

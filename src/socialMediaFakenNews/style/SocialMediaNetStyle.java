package socialMediaFakenNews.style;

import java.awt.Color;

import repast.simphony.space.graph.RepastEdge;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;
import socialMediaFakenNews.agent.Agent;
import socialMediaFakenNews.agent.AgentState;

public class SocialMediaNetStyle implements EdgeStyleOGL2D {

	@Override
	public int getLineWidth(RepastEdge<?> edge) {
		return (int) (Math.abs(edge.getWeight()));
	}

	@Override
	public Color getColor(RepastEdge<?> edge) {
		Agent a = (Agent) edge.getSource();
		if (a.getState() == AgentState.SUSCEPTIBLE) {
			return Color.GRAY;
		} else if (a.getState() == AgentState.BELIEVER) {
			return Color.RED;
		} else if (a.getState() == AgentState.CURED) {
			return Color.CYAN;
		} else {
			return Color.GRAY;
		}
	}

}

package socialMediaFakenNews.style;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import socialMediaFakenNews.agent.Agent;
import socialMediaFakenNews.agent.AgentState;

public class SocialMediaAgentStyle extends DefaultStyleOGL2D {

	@Override
	public Color getColor(Object object) {
		Agent a;
		if (object instanceof Agent) {
			a = (Agent) object;
		} else {
			return Color.BLACK;
		}

		if (a.getState() == AgentState.BELIEVER) {
			return Color.PINK;
		} else if (a.getState() == AgentState.BOT) {
			return Color.RED;
		} else if (a.isInfluencer()) {
			return Color.YELLOW;
		} else if (a.getState() == AgentState.SUSCEPTIBLE) {
			return Color.BLUE;
		} else if (a.getState() == AgentState.CURED) {
			return Color.GREEN;
		} else {
			return Color.DARK_GRAY;
		}
	}
}

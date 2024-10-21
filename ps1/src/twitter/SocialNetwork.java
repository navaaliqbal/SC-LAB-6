package twitter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
public class SocialNetwork {


	public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
	    Map<String, Set<String>> followsGraph = new HashMap<>();

	    for (Tweet tweet : tweets) {
	        String user = tweet.getAuthor().toLowerCase();
	        String text = tweet.getText();

	        // Initialize a set to hold the mentioned users
	        Set<String> mentions = new HashSet<>();

	        // Regex to find mentions in the form of @username
	        String regex = "@(\\w+)";
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(text);

	        // Collect all mentions from the tweet text
	        while (matcher.find()) {
	            String mention = matcher.group(1).toLowerCase();  // Get the username without '@'
	            mentions.add(mention);
	        }

	        // If there are mentions, add them to the follows graph
	        if (!mentions.isEmpty()) {
	            followsGraph.putIfAbsent(user, new HashSet<>());
	            followsGraph.get(user).addAll(mentions);
	        }
	    }

	    return followsGraph;
	}


    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     *
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();

        // Build the follower count map
        for (String user : followsGraph.keySet()) {
            Set<String> follows = followsGraph.get(user);
            for (String followedUser : follows) {
                followerCount.put(followedUser, followerCount.getOrDefault(followedUser, 0) + 1);
            }
        }

        // Sort users by follower count in descending order
        List<String> influencers = new ArrayList<>(followerCount.keySet());
        influencers.sort((user1, user2) -> followerCount.get(user2) - followerCount.get(user1));

        return influencers;
    }
}

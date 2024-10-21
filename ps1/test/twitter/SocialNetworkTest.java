package twitter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.Instant;
import java.util.*;

/**
 * Tests for SocialNetwork.java
 */
public class SocialNetworkTest {

    // Test 1: Empty List of Tweets
    @Test
    public void testGuessFollowsGraphEmptyTweets() {
        List<Tweet> emptyTweets = new ArrayList<>();
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(emptyTweets);
        assertTrue(graph.isEmpty());
    }

    // Test 2: Tweets Without Mentions
    @Test
    public void testGuessFollowsGraphNoMentions() {
        Tweet tweet1 = new Tweet(1, "navaal", "Just having a great day!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.isEmpty());
    }

    // Test 3: Single Mention
    @Test
    public void testGuessFollowsGraphSingleMention() {
        Tweet tweet1 = new Tweet(1, "navaal", "Hello @ayesha!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        
        assertTrue(graph.containsKey("navaal"));
        assertTrue(graph.get("navaal").contains("ayesha"));
    }

    // Test 4: Multiple Mentions in a Single Tweet
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        Tweet tweet1 = new Tweet(1, "navaal", "Hey @ayesha and @amna, what's up?", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        
        assertTrue(graph.containsKey("navaal"));
        assertTrue(graph.get("navaal").contains("ayesha"));
        assertTrue(graph.get("navaal").contains("amna"));
    }

    // Test 5: Multiple Tweets from One User
    @Test
    public void testGuessFollowsGraphMultipleTweetsFromOneUser() {
        Tweet tweet1 = new Tweet(1, "navaal", "Hey @ayesha!", Instant.now());
        Tweet tweet2 = new Tweet(2, "navaal", "Hi @amna!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);

        assertTrue(graph.containsKey("navaal"));
        assertTrue(graph.get("navaal").contains("ayesha"));
        assertTrue(graph.get("navaal").contains("amna"));
    }

    // Test 6: Empty Graph for influencers()
    @Test
    public void testInfluencersEmptyGraph() {
        Map<String, Set<String>> emptyGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(emptyGraph);
        assertTrue(influencers.isEmpty());
    }

    // Test 7: Single User Without Followers
    @Test
    public void testInfluencersSingleUserWithoutFollowers() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("navaal", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(graph);
        assertTrue(influencers.isEmpty());
    }

    // Test 8: Single Influencer
    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("navaal", new HashSet<>(Arrays.asList("ayesha")));
        graph.put("ayesha", new HashSet<>());  // ayesha has no followers, but navaal follows ayesha
        List<String> influencers = SocialNetwork.influencers(graph);

        assertEquals(Arrays.asList("ayesha"), influencers);
    }

    // Test 9: Multiple Influencers
    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("navaal", new HashSet<>(Arrays.asList("ayesha", "amna")));
        graph.put("ayesha", new HashSet<>(Arrays.asList("amna")));
        graph.put("amna", new HashSet<>());

        List<String> influencers = SocialNetwork.influencers(graph);

        assertEquals(Arrays.asList("amna", "ayesha"), influencers);
    }

    // Test 10: Tied Influence
    @Test
    public void testInfluencersTiedInfluence() {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("navaal", new HashSet<>(Arrays.asList("ayesha", "amna")));
        graph.put("ayesha", new HashSet<>(Arrays.asList("amna")));
        graph.put("amna", new HashSet<>(Arrays.asList("ayesha")));

        List<String> influencers = SocialNetwork.influencers(graph);

        // amna and ayesha both have the same number of followers (1)
        assertTrue(influencers.contains("amna"));
        assertTrue(influencers.contains("ayesha"));
    }

}

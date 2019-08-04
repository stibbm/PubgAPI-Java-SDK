package PlayerUnknownsBattlegroundsSDK.PlayerUnknownsBattlegroundsSDK;

import java.util.ArrayList;

import org.json.JSONObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	// Note: player names are caps sensitive in order to work
	// properly
	String examplePlayer = "Kaymind";
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }
    
    

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
    
    /**
     * Tests grabbing a players account Id using their player name
     */
    public void testGrabbingAccountId() {
    	PubgAPI api = new PubgAPI();
    	String accountId = api.getPlayerAccountId(examplePlayer);
    	System.out.println(accountId);
    }
    
    /**
     * Tests grabbing a match from the api using a matchId
     */
    public void testGetMatchAPICall() {
    	PubgAPI api = new PubgAPI();
    	ArrayList<String> recentMatches = api.getRecentMatches(examplePlayer);
    	for(String matchId : recentMatches) {
    		JSONObject match = api.getMatchAPICall(matchId);
    		System.out.println(match);
    		break;
    	}
    }
    
    /**
     * Tests the get telemetry API call
     */
    public void testGetTelemetryCall() {
    	PubgAPI api = new PubgAPI();
    	ArrayList<String> recentMatches = api.getRecentMatches(examplePlayer);
    	for(String matchId : recentMatches) {
    		api.getTelemetryJSON(matchId);
    		//System.out.println(match);
    		break;
    	}
    }
    
    /**
     * Tests the getMatches api call
     */
    public void testGetMatchesAPICall() {
    	PubgAPI pubgAPI = new PubgAPI();
    	JSONObject shroud = pubgAPI.getPlayerJSON(examplePlayer);
    	ArrayList<String> recentMatches = pubgAPI.getRecentMatches(examplePlayer);
    	System.out.println(shroud);
    	System.out.println(recentMatches);
    	for(String matchId : recentMatches) {
    		System.out.println(matchId);
    	}
    	assertTrue(true);
    }
    
    
    /**
     * Tests grabbing a player api call
     */
    public void testGetPlayerAPICall() {
    	PubgAPI api = new PubgAPI();
    	JSONObject player = api.getPlayerJSON(examplePlayer);
    	System.out.println(player);
    	assertTrue(true);
    }
    
    /**
     * Tests grabbing a players lifetime stats
     */
    public void testGetPlayerLifetimeStats() {
    	PubgAPI api = new PubgAPI();
    	JSONObject playerLifetimeStats = api.getPlayerLifetimeStats(api.getPlayerAccountId(examplePlayer));
    	System.out.println(playerLifetimeStats);
    	assertTrue(playerLifetimeStats!=null);
    }

}

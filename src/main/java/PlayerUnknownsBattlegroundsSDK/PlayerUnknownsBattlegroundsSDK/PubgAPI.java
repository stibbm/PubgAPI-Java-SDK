/**
 * Matthew Stibbins
 * 7/28/2019
 * Player Unknown's Battlegrounds Java SDK for accessing web API
 */


package PlayerUnknownsBattlegroundsSDK.PlayerUnknownsBattlegroundsSDK;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class PubgAPI {
	private static final String defaultAPIKEYfilename = "credentials.txt";
	private String API_KEY = "";
	
	
	/**
	 * Default constructor which attempts to load the users APIKEY
	 * from a file named credentials.txt in the programs working directory
	 */
	public PubgAPI() 
	{
		loadAPIKeyFromFile();
	}
	
	
	/**
	 * Return list of recent match ids for the given player
	 * @param playerName
	 * @return
	 */
	public ArrayList<String> getRecentMatches(String playerName){
		try {
			Thread.sleep(6000);
		}catch(Exception e) {
			
		}
		
		
		ArrayList<String> matchIds = new ArrayList<>();
		try {
			JSONObject playerJSON = getPlayerJSON(playerName);
			JSONArray data = playerJSON.getJSONArray("data");
			JSONObject relationshipsWrapper = data.getJSONObject(0);
			JSONObject relationships = relationshipsWrapper.getJSONObject("relationships");
			JSONObject matchesWrapper = relationships.getJSONObject("matches");
			JSONArray matchesArray = matchesWrapper.getJSONArray("data");
			
			for(int i=0;i<matchesArray.length();i++) {
				JSONObject match = matchesArray.getJSONObject(i);
				String matchId = match.getString("id");
				matchIds.add(matchId);
			}
			
		}
		catch(Exception e) {
			
		}
		return matchIds;
	}
	
	/**
	 * Retuns the accountId corresponding to the given
	 * player
	 * @param playerName
	 * @return
	 */
	public String getPlayerAccountId(String playerName) {
		
		try {
			Thread.sleep(6000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		String accountId = "";
		JSONObject playerJSON = getPlayerJSON(playerName);
		
		for(String key : playerJSON.keySet()) {
			System.out.println(key);
		}
		//System.exit(0);
		JSONArray dataWrapper = playerJSON.getJSONArray("data");
		JSONObject data = dataWrapper.getJSONObject(0);
		accountId = data.getString("id");
		System.out.println(accountId);
		//System.exit(0);
		return accountId;
	}
	
	/**
	 * Returns the match JSON object corresponding to the given matchId
	 * @param matchId
	 * @return
	 */
	public JSONObject getMatchAPICall(String matchId) {
		String response = "";
		try {
			String urlString = "https://api.pubg.com/shards/steam/matches/" + matchId;
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/vnd.api+json");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
        
        while((line=br.readLine())!=null) {
        	response+=line;
        	System.out.println(line);
        }
        br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}	
    	JSONObject obj = new JSONObject(response);
    	return obj;
	}
	
	/**
	 * Returns the lifetime stats returned by the api for the given player
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	public JSONObject getPlayerLifetimeStats(String accountId) {
		try {
			Thread.sleep(6000);
		}catch(Exception e) {
				
		}
		
		try {
		String urlString = "https://api.pubg.com/shards/steam/players/" + accountId + "/seasons/lifetime";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Accept", "application/vnd.api+json");
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        String response = "";
        while((line=br.readLine())!=null) {
        	response+=line;
        	System.out.println(line);
        }
        br.close();
        JSONObject lifetimeStats = null;
        lifetimeStats = new JSONObject(response);
        
        return lifetimeStats;
    	}catch(Exception e) {
    		return null;
    	}
    }
	
	/**
	 * Returns the JSONObject corresponding to the given player
	 * @param playerName
	 * @return
	 */
	public JSONObject getPlayerJSON(String playerName) {
		try {
			//Thread.sleep(6000);
			}catch(Exception e) {
				
			}
		
		try {
			// open connection and set headers
			String urlString = "https://api.pubg.com/shards/steam/players?filter[playerNames]=" + playerName;
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
			conn.setRequestProperty("Accept", "application/vnd.api+json");
			
			// read in the api's response
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = null;
		    String playerJSONString = "";
		    while((line=br.readLine())!=null) {
		    	playerJSONString+=line;
		    	System.out.println(line);
		    }
		    br.close();
		    
		    return new JSONObject(playerJSONString);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the telemetry JSON for a given match id
	 * @param matchId
	 * @return
	 */
	public JSONObject getTelemetryJSON(String matchId) {
		try {
				Thread.sleep(6000);
			}catch(Exception e) {
				
			}
		
		JSONObject telemetryJSON = null;
		try {
			// open connection and set headers
			String urlString = "https://api.pubg.com/shards/steam/players?filter[playerNames]=" + matchId;
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
			conn.setRequestProperty("Accept", "application/vnd.api+json");
			conn.setRequestProperty("Accept-Encoding", "gzip");
			
			
			Reader reader = null;
			if("gzip".equals(conn.getContentEncoding())) {
				reader = new InputStreamReader(new GZIPInputStream(conn.getInputStream()));
			}else {
				reader = new InputStreamReader(conn.getInputStream());
			}
			
			String telemetryText = "";
			
			while(true) {
				int ch = reader.read();
				if(ch==1) {
					break;
				}
				telemetryText += ch;
				//System.out.print((char)ch);
			}
			telemetryJSON = new JSONObject(telemetryText);
			
		}catch(Exception e) {
			
		}
		return telemetryJSON;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Loads in the users API_KEY from then credentials.txt file
	 */
	public void loadAPIKeyFromFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(defaultAPIKEYfilename)));
			String line = null;
			
			while((line = br.readLine())!=null) {
				if(line.length() > 20) {
					API_KEY = line;
					break;
				}
			}
			br.close();
		}
		catch(Exception e) {
			System.out.println("Failed to load api key from the credentials.txt file");
		}
	}
	
	
	
	
}

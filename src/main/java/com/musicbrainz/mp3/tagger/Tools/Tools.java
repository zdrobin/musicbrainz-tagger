package com.musicbrainz.mp3.tagger.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class Tools {

	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	
	public static JsonNode jsonToNode(String json) {

		try {
			JsonNode root = MAPPER.readTree(json);
			return root;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final String httpGet(String url) {
		String res = "";
		try {
			URL externalURL = new URL(url);

			URLConnection yc = externalURL.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							yc.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) 
				res+="\n" + inputLine;
			in.close();

			return res;
		} catch(IOException e) {}
		return res;
	}
	
	public static String replaceWhiteSpaceWithPercent(String s) {
		return s.replaceAll("\\s+", "%");
	}
	
	public static String nodeToJson(ObjectNode a) {
		try {
			return Tools.MAPPER.writeValueAsString(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String nodeToJsonPretty(JsonNode a) {
		try {
			return Tools.MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
}

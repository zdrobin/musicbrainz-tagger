package com.musicbrainz.mp3.tagger.Tools;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public class Tools {

	static final Logger log = LoggerFactory.getLogger(Tools.class);

	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static final String USER_AGENT = "musicbrainz-tagger/1.0.3 (https://github.com/tchoulihan/musicbrainz-tagger)";
	
//	public static final File SAMPLE_SONG = new File(System.getProperty("user.dir") + "/src/main/resources/entertainer.mp3");
//	public static final File SAMPLE_SONG = new File(System.getProperty("user.dir") + "/src/main/resources/06 A Short Reprise for Mary Todd, Wh.mp3");
	public static final File SAMPLE_SONG = new File("/home/tyler/Downloads/Nine Inch Nails - The Downward Spiral/05 Closer.mp3");
//	public static final File SAMPLE_SONG = new File("/home/tyler/Downloads/Feist/Let It Die/05 Leisure Suite.mp3");
//	public static final File SAMPLE_SONG = new File(System.getProperty("user.dir") + "/src/main/resources/05. Blueprint.mp3");
//	public static final File SAMPLE_SONG = new File(System.getProperty("user.dir") + "/src/main/resources/20 You're Not an Airplane.mp3");
//	public static final File SAMPLE_SONG = new File(System.getProperty("user.dir") + "/src/main/resources/devil.mp3");

	
	public static final String httpGet(String url) {
		String res = "";
		try {
			URL externalURL = new URL(url);

			URLConnection yc = externalURL.openConnection();
			yc.setRequestProperty("User-Agent", USER_AGENT);
			
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
	
	public static String encodeURL(String s) {

//		try {
//			return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%");
			try {
//				String removedWhites = s.replaceAll("\\s+","%20").replaceAll("\"", "%22");
//				return new URI(removedWhites).toASCIIString();
				return URLEncoder.encode(s, "UTF-8");
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String replaceWhiteSpace(String s) {
		return s.replaceAll("\\s+","%20");
	}
	
	public static String surroundWithQuotes(String s) {
		return Tools.encodeURL("\"" + s + "\"");
	}
	
	public static JsonNode jsonToNode(String json) {
	
		try {
			JsonNode root = MAPPER.readTree(json);
			return root;
		} catch (Exception e) {
			log.error("json: " + json);
			e.printStackTrace();
		}
		return null;
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

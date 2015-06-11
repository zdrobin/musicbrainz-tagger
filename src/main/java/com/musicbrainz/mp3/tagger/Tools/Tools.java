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
import java.util.NoSuchElementException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

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
	
	public static Mp3File getMp3File(File f) {
		Mp3File mp3File = null;
		try {
			mp3File = new Mp3File(f);
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			e.printStackTrace();
		}
		
		return mp3File;
	}




	public static void printId3v1Tags(Mp3File mp3file) {
		if (mp3file.hasId3v1Tag()) {
			ID3v1 id3v1Tag = mp3file.getId3v1Tag();
			System.out.println("Track: " + id3v1Tag.getTrack());
			System.out.println("Artist: " + id3v1Tag.getArtist());
			System.out.println("Title: " + id3v1Tag.getTitle());
			System.out.println("Album: " + id3v1Tag.getAlbum());
			System.out.println("Year: " + id3v1Tag.getYear());
			System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
			System.out.println("Comment: " + id3v1Tag.getComment());
		}
	}

	public static void printId3v2Tags(Mp3File mp3file) {
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			System.out.println("Track: " + id3v2Tag.getTrack());
			System.out.println("Artist: " + id3v2Tag.getArtist());
			System.out.println("Title: " + id3v2Tag.getTitle());
			System.out.println("Album: " + id3v2Tag.getAlbum());
			System.out.println("Year: " + id3v2Tag.getYear());
			System.out.println("Date: " + id3v2Tag.getDate());
			System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
			System.out.println("Comment: " + id3v2Tag.getComment());
			System.out.println("Composer: " + id3v2Tag.getComposer());
			System.out.println("Publisher: " + id3v2Tag.getPublisher());
			System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
			System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
			System.out.println("Copyright: " + id3v2Tag.getCopyright());
			System.out.println("URL: " + id3v2Tag.getUrl());
			System.out.println("Encoder: " + id3v2Tag.getEncoder());
			byte[] albumImageData = id3v2Tag.getAlbumImage();
			if (albumImageData != null) {
				System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
				System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
			}

			System.out.println("Length(ms): " + mp3file.getLengthInMilliseconds());
		}
	}

	public static ID3v1 getId3v1Tag(Mp3File f) {
		ID3v1 id3Tag;

		if (f.hasId3v2Tag()) {
			id3Tag = f.getId3v2Tag();
		} else if (f.hasId3v1Tag()) {
			id3Tag = f.getId3v1Tag();
		} else {
			throw new NoSuchElementException("The track has no id3 tags");
		}

		return id3Tag;


	}
}

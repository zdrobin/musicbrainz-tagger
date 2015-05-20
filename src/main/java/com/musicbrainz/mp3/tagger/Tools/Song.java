package com.musicbrainz.mp3.tagger.Tools;

import java.io.File;
import java.util.NoSuchElementException;

import org.codehaus.jackson.JsonNode;

public class Song {

	private JsonNode json;

	public String getJson() {
		return Tools.nodeToJsonPretty(json);
	}
	
	public static Song fetchSong(File f) {
		return new Song(f);
	}

	private Song(File f) {
		json = Tagger.fetchMBRecordingJSONFromFile(f);
		
		if (getFirstRecording() == null) {
			throw new NoSuchElementException("Could not find recording in the MusicBrainz Database.");
		}
	}

	private JsonNode getFirstRecording() {
		return json.get("recordings").get(0);
	}

	public String getRecordingMBID() {
		return getFirstRecording().get("id").asText();		
	}
	
	public String getRecording() {
		return getFirstRecording().get("title").asText();		
	}

	public JsonNode getFirstRelease() {
		return getFirstRecording().get("releases").get(0);
	}

	public String getReleaseMBID() {
		return getFirstRelease().get("id").asText();
	}
	
	public String getRelease() {
		return getFirstRelease().get("title").asText();
	}

	private JsonNode getFirstArtistCredit() {
		return getFirstRecording().get("artist-credit").get(0).get("artist");
	}

	public String getArtistMBID() {
		return getFirstArtistCredit().get("id").asText();
	}
	
	public String getArtist() {
		return getFirstArtistCredit().get("name").asText();
	}
		

}

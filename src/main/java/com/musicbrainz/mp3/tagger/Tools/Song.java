package com.musicbrainz.mp3.tagger.Tools;

import java.io.File;
import java.util.NoSuchElementException;

import org.codehaus.jackson.JsonNode;

import com.mpatric.mp3agic.Mp3File;

public class Song {

	private JsonNode json;
	
	private String query;
	
	private Mp3File mp3File;

	/**
	 * Give the prettified json response from musicbrainz
	 * @return json
	 */
	public String getJson() {
		return Tools.nodeToJsonPretty(json);
	}
	
	/**
	 * Looks up the musicbrainz recording from a given music file, using 6 pieces of info:<br>
	 * track title<br>
	 * track #<br>
	 * artist name<br>
	 * album name<br>
	 * album year<br>
	 * song duration<br>

	 * @param audioFile
	 * @return Song
	 */
	public static Song fetchSong(File f) {
		return new Song(f);
	}

	private Song(File f) {
		mp3File = Tagger.getMp3File(f);
		query = Tagger.createQueryFromFile(mp3File);
		json = Tagger.fetchMBRecordingJSONFromQuery(query);
		
		if (getFirstRecording() == null) {
			throw new NoSuchElementException("Could not find recording in the MusicBrainz Database.\n"
					+ "query: " + query);
			
		}
	}
	
	/**
	 * Fetches the MusicBrainz query for the song
	 * @return query
	 */
	public String getQuery() {
		return query;
	}

	private JsonNode getFirstRecording() {
		return json.get("recordings").get(0);
	}

	/**
	 * Fetches the musicbrainz MBID for the recording.
	 * @return musicbrainz-MBID
	 */
	public String getRecordingMBID() {
		return getFirstRecording().get("id").asText().toLowerCase();		
	}
	
	/**
	 * Fetches the title for the recording.
	 * @return title
	 */
	public String getRecording() {
		return getFirstRecording().get("title").asText();		
	}

	private JsonNode getFirstRelease() {
		return getFirstRecording().get("releases").get(0);
	}

	/**
	 * Fetches the musicbrainz MBID for the release.
	 * @return musicbrainz-MBID
	 */
	public String getReleaseMBID() {
		return getFirstRelease().get("id").asText().toLowerCase();
	}
	
	/**
	 * Fetches the title for the release.
	 * @return title
	 */
	public String getRelease() {
		return getFirstRelease().get("title").asText();
	}

	private JsonNode getFirstArtistCredit() {
		return getFirstRecording().get("artist-credit").get(0).get("artist");
	}

	/**
	 * Fetches the musicbrainz MBID for the artist.
	 * @return musicbrainz-MBID
	 */
	public String getArtistMBID() {
		return getFirstArtistCredit().get("id").asText().toLowerCase();
	}
	
	/**
	 * Fetches the name of the artist.
	 * @return name
	 */
	public String getArtist() {
		return getFirstArtistCredit().get("name").asText();
	}
	
	public Long getDuration() {
		return mp3File.getLengthInMilliseconds();
	}
	
	public String getYear() {
		return Tagger.getId3v1Tag(mp3File).getYear();
	}
		

}

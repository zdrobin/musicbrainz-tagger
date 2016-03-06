package com.musicbrainz.mp3.tagger.Tools;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class SongFunctions {


	static final Logger log = LoggerFactory.getLogger(SongFunctions.class);

	protected JsonNode json;

	protected String query;


	public abstract String createQueryFromFile(Object o);

	public static JsonNode fetchMBRecordingJSONFromQuery(String query) {

		JsonNode jsonNode = null;


		try {


			String res = Tools.httpGet(query);
			//			log.info(res);
			jsonNode = Tools.jsonToNode(res);
			return jsonNode;


		} catch(NoSuchElementException e) {
			log.info("query failed: " + query);
			// Wait some time before retrying
			try {
				Thread.sleep(1200); // curent ratelimit is 22reqs /20 seconds
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			return fetchMBRecordingJSONFromQuery(query);
		}





	}

	/**
	 * Fetches the MusicBrainz query for the song
	 * @return query
	 */
	public String getQuery() {
		return query;
	}

	protected JsonNode getFirstRecording() {
		JsonNode recording = null;

		try {
			recording = json.get("recordings").get(0);

		} catch(NullPointerException e) {
			recording = json;
		}

		return recording;
	}

	/**
	 * Fetches the musicbrainz MBID for the recording.
	 * @return musicbrainz-MBID
	 */
	public String getRecordingMBID() {
		try {
			return getFirstRecording().get("id").asText().toLowerCase();	
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	/**
	 * Fetches the title for the recording.
	 * @return title
	 */
	public String getRecording() {
		try {
			return getFirstRecording().get("title").asText();
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}


	public Long getDuration() {
		try {
			return getFirstRecording().get("length").asLong();	
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	protected JsonNode getFirstRelease() {
		try {
			return getFirstRecording().get("releases").get(0);
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	/**
	 * Fetches the musicbrainz MBID for the release.
	 * @return musicbrainz-MBID
	 */
	public String getReleaseMBID() {
		try {
			return getFirstRelease().get("id").asText().toLowerCase();
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}


	/**
	 * Fetches the title for the release.
	 * @return title
	 */
	public String getRelease() {
		try {
			return getFirstRelease().get("title").asText();
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	protected JsonNode getFirstReleaseGroup() {
		try {
			return getFirstRecording().get("releases").get(0).get("release-group");
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	/**
	 * Fetches the musicbrainz MBID for the release group.
	 * @return musicbrainz-MBID
	 */
	public String getReleaseGroupMBID() {
		try {
			return getFirstReleaseGroup().get("id").asText().toLowerCase();
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	/**
	 * Fetches all the release groups associated with this song
	 * @return
	 */
	public abstract Set<ReleaseGroupInfo> getReleaseGroupInfos();


	private JsonNode getFirstArtistCredit() {
		try {
			return getFirstRecording().get("artist-credit").get(0).get("artist");
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	/**
	 * Fetches the musicbrainz MBID for the artist.
	 * @return musicbrainz-MBID
	 */
	public String getArtistMBID() {
		try {
			return getFirstArtistCredit().get("id").asText().toLowerCase();
		} catch(NullPointerException e) {
			throw new SongNotFoundException();
		}
	}

	/**
	 * Fetches the name of the artist.
	 * @return name
	 */
	public String getArtist() {
		return getFirstArtistCredit().get("name").asText();
	}

	/**
	 * Give the prettified json response from musicbrainz
	 * @return json
	 */
	public String toJson() {
		return Tools.nodeToJsonPretty(json);
	}


}

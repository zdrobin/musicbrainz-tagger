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


	public Long getDuration() {
		return getFirstRecording().get("length").asLong();	
	}

	protected JsonNode getFirstRelease() {
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

	protected JsonNode getFirstReleaseGroup() {
		return getFirstRecording().get("releases").get(0).get("release-group");
	}

	/**
	 * Fetches the musicbrainz MBID for the release group.
	 * @return musicbrainz-MBID
	 */
	public String getReleaseGroupMBID() {
		return getFirstReleaseGroup().get("id").asText().toLowerCase();
	}

	/**
	 * Fetches all the release groups associated with this song
	 * @return
	 */
	public Set<ReleaseGroupInfo> getReleaseGroupInfos() {

		Set<ReleaseGroupInfo> releaseGroupInfos = new LinkedHashSet<>();
		Set<String> releaseGroupMBIDs = new LinkedHashSet<>(); // for uniqueness
		JsonNode releases = getFirstRecording().get("releases");

		int i = 0;
		while (releases.has(i)) {
			String cReleaseGroupMBID = releases.get(i).get("release-group").get("id").asText();
			Integer discNo = releases.get(i).get("media").get(0).get("position").asInt();
			String trackNoStr = releases.get(i).get("media").get(0).get("track").get(0).get("number").asText();

			String primaryType = (releases.get(i).get("release-group").get("primary-type") != null) ?
					releases.get(i).get("release-group").get("primary-type").asText() : null;

					Set<String> secondaryTypes = null;

					JsonNode secondaryTypesJson = releases.get(i).get("release-group").get("secondary-types");
					if (secondaryTypesJson != null) {
						secondaryTypes = new LinkedHashSet<String>();
						int j = 0;
						while (secondaryTypesJson.has(j)) {
							secondaryTypes.add(secondaryTypesJson.get(j++).asText());
						}
					}


					// This was necessary because some track numbers had letters in them, IE A2
					Integer trackNo = 0;
					try {
						trackNo = Integer.valueOf(trackNoStr.replaceAll("[^\\d.]", ""));
					} catch(NumberFormatException e) {
						log.error("Track # was " + trackNoStr + " , so changed it to 0");
						e.printStackTrace();
					}

					// Only create and add if its a unique releaseGroupMBID
					if (!releaseGroupMBIDs.contains(cReleaseGroupMBID)) {
						ReleaseGroupInfo releaseGroupInfo = ReleaseGroupInfo.create(
								cReleaseGroupMBID, trackNo, discNo, primaryType, secondaryTypes);
						releaseGroupInfos.add(releaseGroupInfo);
						releaseGroupMBIDs.add(cReleaseGroupMBID);
					}

					i++;
		}

		return releaseGroupInfos;


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


}

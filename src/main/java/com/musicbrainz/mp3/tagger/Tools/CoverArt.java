package com.musicbrainz.mp3.tagger.Tools;

import java.util.NoSuchElementException;

import org.codehaus.jackson.JsonNode;

public class CoverArt {

	private JsonNode json;

	public String getJson() {
		return Tools.nodeToJsonPretty(json);
	}

	/**
	 * Fetches the coverart for a given song from coverartarchive.org
	 * @param song
	 * @return
	 */
	public static CoverArt fetchCoverArt(Song s) {
		return new CoverArt(s);
	}

	private CoverArt(Song s) {
		json = fetchCoverImagesFromMBID(s.getReleaseMBID());

		
		if (json == null) {
			throw new NoSuchElementException("No cover art found on coverartarchive.org");
		}
	}

	private JsonNode getFirstImageURL() {
		return json.get("images").get(0);
	}

	/**
	 * Fetches the coverartarchive.org image url for the release.
	 * @return image url
	 */
	public String getImageURL() {
		return getFirstImageURL().get("image").asText();
	}

	private JsonNode getThumbnails() {
		return getFirstImageURL().get("thumbnails");
	}

	/**
	 * Fetches the coverartarchive.org large thumbnail for the release.
	 * @return large thumbnail
	 */
	public String getLargeThumbnailURL() {
		return getThumbnails().get("large").asText();
	}

	/**
	 * Fetches the coverartarchive.org small thumbnail for the release.
	 * @return small thumbnail
	 */
	public String getSmallThumbnailURL() {
		return getThumbnails().get("small").asText();
	}

	
	private static JsonNode fetchCoverImagesFromMBID(String releaseMBID) {

		String query = "https://coverartarchive.org/release/" + releaseMBID;

		String res = Tools.httpGet(query);
		
		if (res.equals("")) {
			// Wait some time before retrying
			try {
				Thread.sleep(1100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return fetchCoverImagesFromMBID(query);
		}
		

		JsonNode jsonNode = Tools.jsonToNode(res);

		return jsonNode;
	}

}

package com.musicbrainz.mp3.tagger.Tools;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This just performs a lookup of the artist by mbid to get pictures and links of them
 * 
 * @author tyler
 *
 */
public class Artist {
	
	static final Logger log = LoggerFactory.getLogger(Artist.class);

	private String query;

	private JsonNode json;

	public String getJson() {
		return Tools.nodeToJsonPretty(json);
	}

	/**
	 * Fetches the coverart for a given song from coverartarchive.org
	 * @param song
	 * @return
	 */
	public static Artist fetchArtist(String mbid) {
		return new Artist(mbid);
	}

	private Artist(String mbid) {
		query = "https://musicbrainz.org/ws/2/artist/" + mbid+ "?inc=url-rels+tags&fmt=json";

		json = fetchArtistFromMBID(query);

		if (json == null) {
			throw new NoSuchElementException("No Artist found for mbid: " + mbid);
		}
	}

	/**
	 * Here's a sample query for pearl jam:
	 * https://musicbrainz.org/ws/2/artist/83b9cbe7-9857-49e2-ab8e-b57b01038103?inc=url-rels&fmt=json
	 * @param mbid
	 * @return
	 */
	private static JsonNode fetchArtistFromMBID(String query) {


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
			return fetchArtistFromMBID(query);
		}
	}

	/** For some reason I found many type=discography, so I'd just select the first found
	 * 
	 * @return
	 */
	private JsonNode findFirstInRelationsArray(String typeSearch) {
		JsonNode relationsArray = json.get("relations");

		for (int i = 0;;i++) {
			JsonNode cNode = relationsArray.get(i);
			if (cNode == null) {
				throw new NoSuchElementException("The type " + typeSearch + " doesn't exist");
			}
			String cType = cNode.get("type").asText();
			if (cType.equals(typeSearch)) {
				return cNode;
			}

		}

	}

	public String getLink(String typeSearch) {
		try {
			String link = findFirstInRelationsArray(typeSearch).get("url").get("resource").asText();

			return link;

		} catch(NoSuchElementException e) {
			e.printStackTrace();
			return null;
		}


	}
	
	public String getName() {
		return json.get("name").asText();
	}

	/**
	 * Fetches the coverartarchive.org image url for the release.
	 * @return image url
	 */
	public String getWikipedia() {
		return getLink("wikipedia");
	}

	public String getLyrics() {
		return getLink("lyrics");
	}

	public String getDiscography() {
		return getLink("discography");
	}

	public String getAllMusic() {
		return getLink("allmusic");
	}

	public String getIMDB() {
		return getLink("IMDb");
	}

	public String getLastFM() {
		return getLink("last.fm");
	}

	public String getOfficialHomepage() {
		return getLink("official homepage");
	}

	public String getFanPage() {
		return getLink("fanpage");
	}

	public String getWikiData() {
		return getLink("wikidata");
	}

	public String getYoutube() {
		return getLink("youtube");
	}

	public String getImage() {
		return getLink("image");
	}

	public String getSoundCloud() {
		return getLink("soundcloud");
	}

	public String getSocialNetwork() {
		return getLink("social network");
	}

	public static class Tag {
		private Integer count;
		private String name;

		public static Tag create(Integer count, String name) {
			return new Tag(count, name);
		}

		private Tag(Integer count, String name) {
			this.count = count;
			this.name = name;
		}

		public Integer getCount() {
			return count;
		}

		public String getName() {
			return name;
		}


	}

	public Set<Tag> getTags() {
		Set<Tag> tags = new HashSet<Tag>();

		JsonNode tagsNode = json.get("tags");


		int i = 0;
		while (tagsNode.has(i)) {
			Integer count = tagsNode.get(i).get("count").asInt();
			String name = tagsNode.get(i).get("name").asText();
			tags.add(Tag.create(count, name));
			i++;
		}
		
		return tags;


	}

}

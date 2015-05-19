package com.musicbrainz.mp3.tagger.Tools;

import org.codehaus.jackson.JsonNode;

public class CoverArt {
		
		private JsonNode json;
		
		public String getJson() {
			return Tools.nodeToJsonPretty(json);
		}
		
		public static CoverArt fetchCoverArt(Song s) {
			return new CoverArt(s);
		}

		private CoverArt(Song s) {
			json = Tagger.fetchCoverImagesFromMBID(s.getReleaseMBID());
		}
		
		private JsonNode getFirstImageURL() {
			return json.get("images").get(0);
		}
		
		public String getImageURL() {
			return getFirstImageURL().get("image").asText();
		}
		
		private JsonNode getThumbnails() {
			return getFirstImageURL().get("thumbnails");
		}
		
		public String getLargeThumbnailURL() {
			return getThumbnails().get("large").asText();
		}
		
		public String getSmallThumbnailURL() {
			return getThumbnails().get("small").asText();
		}
		
		
		
}

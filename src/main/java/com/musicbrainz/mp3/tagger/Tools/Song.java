package com.musicbrainz.mp3.tagger.Tools;

import java.io.File;
import java.util.NoSuchElementException;

import org.codehaus.jackson.JsonNode;

import com.mpatric.mp3agic.ID3v1;
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
		mp3File = Tools.getMp3File(f);
		query = createQueryFromFile(mp3File);
		json = fetchMBRecordingJSONFromQuery(query);
		
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
	
	private JsonNode getFirstReleaseGroup() {
		return getFirstRecording().get("releases").get(0).get("release-group");
	}
	
	/**
	 * Fetches the musicbrainz MBID for the release group.
	 * @return musicbrainz-MBID
	 */
	public String getReleaseGroupMBID() {
		return getFirstReleaseGroup().get("id").asText().toLowerCase();
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
		return Tools.getId3v1Tag(mp3File).getYear();
	}
	
	public Integer getTrackNumber() {
		String firstNumber = Tools.getId3v1Tag(mp3File).getTrack().split("(-|/)")[0];
		return Integer.valueOf(firstNumber);
	}
		
	
	/**
	 * Here's a sample musicbrainz query
	 * search terms for recording: 
	 * recording(title) 
	 * artist
	 * number(track#) 
	 * release(album) 
	 * date - yep, do the year 
	 * dur(duration in ms)
	 * fmt=json 
	 * limit=1
	 * http://musicbrainz.org/ws/2/recording/?query=recording:Closer+number:5+artist:Nine%Inch%Nails+dur:372666+release:The%Downward%Spiral&date:1994limit=10
	 */
	public static class MusicBrainzRecordingQuery {
		private String recording;
		private String artist;
		private Integer number;
		private String release;
		private String date;
		private Long duration;

		public static class Builder {
			private String recording;
			private String artist;
			private Integer number;
			private String release;
			private String date;
			private Long duration;


			public Builder(String recording, String artist, Long duration) {
				this.recording = recording;
				this.artist = artist;
				this.duration = duration;
			}

			public Builder number(Integer number) {
				this.number = number;
				return this;
			}
			public Builder release(String release) {
				this.release = release;
				return this;
			}
			public Builder date(String date) {
				this.date = date;
				return this;
			}

			public MusicBrainzRecordingQuery build() {
				return new MusicBrainzRecordingQuery(this);
			}

		}

		private MusicBrainzRecordingQuery(Builder b) {
			this.recording = b.recording;
			this.artist = b.artist;
			this.number = b.number;
			this.release = b.release;
			this.date = b.date;
			this.duration = b.duration;
		}

		public String createQuery() {

			StringBuilder s = new StringBuilder();
			s.append("http://musicbrainz.org/ws/2/recording/?query=");

			s.append("recording:" + Tools.surroundWithQuotes(recording));
			s.append(" AND artist:" + Tools.surroundWithQuotes(artist));
			s.append(" AND dur:[" + (duration - 1500) + " TO " + (duration + 1500) + "]");

			if (number != null) {
				s.append(" AND number:" + number);
			}

			if (release != null) {
				s.append(" AND release:" + Tools.surroundWithQuotes(release));
			}

			if (date != null) {
				s.append(" AND date:" + date + "*");
			}


			s.append("&limit=1");
			s.append("&fmt=json");


			return Tools.replaceWhiteSpace(s.toString());

		}


	}
	
	
	public static JsonNode fetchMBRecordingJSONFromQuery(String query) {

		String res = Tools.httpGet(query);

		if (res.equals("")) {
			// Wait some time before retrying
			try {
				Thread.sleep(1100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return fetchMBRecordingJSONFromQuery(query);
		}
		JsonNode jsonNode = Tools.jsonToNode(res);

		return jsonNode;

	}
	
	public static String createQueryFromFile(Mp3File f) {

		// Get the correct tag
		ID3v1 id3 = Tools.getId3v1Tag(f);

		//		Tagger.printId3v1Tags(f);

		// Construct the query
		MusicBrainzRecordingQuery.Builder b = new MusicBrainzRecordingQuery.Builder(id3.getTitle(), id3.getArtist(), f.getLengthInMilliseconds());

		String number = id3.getTrack();
		if (number != null) {
			
			// Check for 5/14 or a division sign, or a zero

			if (number.contains("/")) {
				number = number.split("/")[0];
			}
			
			
			b.number(Integer.parseInt(number));
		}

		String date = id3.getYear();
		if (date != null) {
			b.date(date);
		}

		String release = id3.getAlbum();
		if (release != null) {
			b.release(release);
		}

		MusicBrainzRecordingQuery mbq = b.build();

		String query = mbq.createQuery();

		return query;
	}


}

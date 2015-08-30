package com.musicbrainz.mp3.tagger.Tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;

public class Song extends SongFunctions {

	static final Logger log = LoggerFactory.getLogger(Song.class);

	private Mp3File mp3File;

	private static final Integer DURATION_WINDOW_MS = 25000;



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
		try {
			query = createQueryFromFile(mp3File);

			json = fetchMBRecordingJSONFromQuery(query);

			if (getFirstRecording() == null) {
				throw new NoSuchElementException("Could not find recording in the MusicBrainz Database.\n"
						+ "query: " + query);

			}
		} 
		// Happens if there's a null query
		catch(NoSuchElementException e) {
			e.printStackTrace();
		}

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

				if (recording == null || artist == null || duration == null) {
					throw new NoSuchElementException("The artist, recording, or duration is null");
				}
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
			s.append(" AND dur:[" + (duration - DURATION_WINDOW_MS/2) + " TO " + 
					(duration + DURATION_WINDOW_MS/2) + "]");

			if (number != null) {
				s.append(" AND number:" + number);
			}

			if (release != null) {

				s.append(" AND release:" + Tools.surroundWithQuotes(release));
			}

			// Date checking removed, it was very inadequate
			//			if (date != null) {
			//				s.append(" AND date:" + date + "*");
			//			}

			s.append("&limit=1");
			s.append("&fmt=json");




			String str = Tools.replaceWhiteSpace(s.toString());

			return str;

		}


	}




	public String createQueryFromFile(Object o) {
		Mp3File f = (Mp3File) o;

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

	@Override
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
	
	


}

package com.musicbrainz.mp3.tagger.Tools;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.codehaus.jackson.JsonNode;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.musicbrainz.mp3.tagger.Tools.Tagger.MusicBrainzQuery.Builder;

public class Tagger {

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
	public static class MusicBrainzQuery {
		private String recording;
		private String artist;
		private String number;
		private String release;
		private String date;
		private Long duration;

		public static class Builder {
			private String recording;
			private String artist;
			private String number;
			private String release;
			private String date;
			private Long duration;


			public Builder(String recording, String artist, Long duration) {
				this.recording = recording;
				this.artist = artist;
				this.duration = duration;
			}

			public Builder number(String number) {
				// Check for 5/14 or a division sign

				if (number.contains("/")) {
					this.number = number.split("/")[0];
					return this;
				}

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

			public MusicBrainzQuery build() {
				return new MusicBrainzQuery(this);
			}

		}

		private MusicBrainzQuery(Builder b) {
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



	public static JsonNode fetchMBRecordingJSONFromFile(File f) {

		String query = createQueryFromFile(f);		

		String res = Tools.httpGet(query);

		JsonNode jsonNode = Tools.jsonToNode(res);

		return jsonNode;

	}

	public static JsonNode fetchCoverImagesFromMBID(String releaseMBID) {

		String query = "https://coverartarchive.org/release/" + releaseMBID;

		String res = Tools.httpGet(query);

		JsonNode jsonNode = Tools.jsonToNode(res);

		return jsonNode;
	}

	public static String createQueryFromFile(File f) {
		Mp3File mp3File;
		try {
			mp3File = new Mp3File(f);
			return createQueryFromFile(mp3File);
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static String createQueryFromFile(Mp3File f) {
		
		// Get the correct tag
		ID3v1 id3 = getId3v1Tag(f);
		
//		Tagger.printId3v1Tags(f);

		// Construct the query
		Builder b = new MusicBrainzQuery.Builder(id3.getTitle(), id3.getArtist(), f.getLengthInMilliseconds());

		String number = id3.getTrack();
		if (number != null) {
			b.number(number);
		}

		String date = id3.getYear();
		if (date != null) {
			b.date(date);
		}

		String release = id3.getAlbum();
		if (release != null) {
			b.release(release);
		}

		MusicBrainzQuery mbq = b.build();

		String query = mbq.createQuery();

		return query;
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


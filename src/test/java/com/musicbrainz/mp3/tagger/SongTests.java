package com.musicbrainz.mp3.tagger;

import junit.framework.TestCase;

import org.junit.Before;

import com.mpatric.mp3agic.Mp3File;
import com.musicbrainz.mp3.tagger.Tools.Song;
import com.musicbrainz.mp3.tagger.Tools.Tools;

public class SongTests extends TestCase {

	Song song;

	@Before
	public void setUp() {
		song = Song.fetchSong(Tools.SAMPLE_SONG);
	}


	public void testCreateQuery() {

		Mp3File mp3File = Tools.getMp3File(Tools.SAMPLE_SONG);
		String query = Song.createQueryFromFile(mp3File);
		System.out.println(query);
		assertEquals("http://musicbrainz.org/ws/2/recording/?query=recording:%22Closer%22%20AND%20artist:%22Nine+Inch+Nails%22%20AND%20dur:[371166%20TO%20374166]%20AND%20number:5%20AND%20release:%22The+Downward+Spiral%22%20AND%20date:1994*&limit=1&fmt=json",
				query);

	}

	public void testRecordingMBID() {
		assertEquals("13dd61c7-ce73-4e97-9f0c-9f0e53144411", 
				song.getRecordingMBID());

	}


	public void testReleaseMBID() {
		assertEquals("602af258-e647-48e2-9660-bd0c5c8f92bf", 
				song.getReleaseMBID());

	}

	public void testReleaseGroupMBID() {
		assertEquals("7c4cab8d-dead-3870-b501-93c90fd0a580", 
				song.getReleaseGroupMBID());

	}

	public void testArtistMBID() {
		assertEquals("b7ffd2af-418f-4be2-bdd1-22f8b48613da", 
				song.getArtistMBID());

	}

	public void testRecording() {
		assertEquals("Closer", 
				song.getRecording());

	}

	public void testRelease() {
		assertEquals("The Downward Spiral", 
				song.getRelease());

	}

	public void testArtist() {
		assertEquals("Nine Inch Nails", 
				song.getArtist());

	}

	public void testJson() {
		System.out.println(song.getJson());
	}

	public void testYear() {
		System.out.println(song.getYear());
		assertEquals("1994", 
				song.getYear());
	}

	public void testTrack() {
		System.out.println(song.getTrackNumber());
		assertEquals(Integer.valueOf(5), song.getTrackNumber());

	}
	
	public void testNumber() {
		assertEquals(Integer.parseInt("08"), 8);
	}





}

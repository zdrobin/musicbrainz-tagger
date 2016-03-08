package com.musicbrainz.mp3.tagger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;

import com.mpatric.mp3agic.Mp3File;
import com.musicbrainz.mp3.tagger.Tools.ReleaseGroupInfo;
import com.musicbrainz.mp3.tagger.Tools.Song;
import com.musicbrainz.mp3.tagger.Tools.Tools;
import com.musicbrainz.mp3.tagger.Tools.SongNotFoundException;

public class SongTests extends TestCase {

	Song song;

	@Before
	public void setUp() {
		song = Song.fetchSong(Tools.SAMPLE_SONG);
	}


	public void testSongIsFound() {

		song = Song.fetchSong(Tools.SAMPLE_SONG_2);

		assertFalse(song.isFound());

	}


	public void testSongNotFoundException() {


		try {
			song = Song.fetchSong(Tools.SAMPLE_SONG_2);
			song.getArtist();

			fail("Exception wasn't thrown");
		} catch(SongNotFoundException e) {}

	}




	public void testCreateQuery() {

		Mp3File mp3File = Tools.getMp3File(Tools.SAMPLE_SONG);
		String query = song.createQueryFromFile(mp3File);
		System.out.println(query);
		assertEquals("http://musicbrainz.org/ws/2/recording/?query=recording:%22Closer%22%20AND%20artist:%22Nine+Inch+Nails%22%20AND%20dur:[360166%20TO%20385166]%20AND%20number:5%20AND%20release:%22The+Downward+Spiral%22&limit=1&fmt=json"
				,query);

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
		System.out.println(song.getRelease());
		assertEquals("The Downward Spiral", 
				song.getRelease());

	}


	public void testArtist() {
		assertEquals("Nine Inch Nails", 
				song.getArtist());

	}

	public void testJson() {
		System.out.println(song.toJson());
	}

	public void testDuration() {
		assertEquals(Long.valueOf(373133),
				song.getDuration());

	}

	public void testNumber() {
		assertEquals(Integer.parseInt("08"), 8);
	}

	public void weirdSong() {
		File dCabWeird = new File("/media/tyler/Tyhous_HD/Music/Death Cab for Cutie - Discography/4. 7 Inch Singles/1999 - Prove my Hypothesis 7 inch Single/01 Prove My Hypothesis.mp3");

		File dCabWeird2 = new File("/media/tyler/Tyhous_HD/Music/Death Cab for Cutie - Discography/1. Studio Albums/2008 - Narrow Stairs/04 Cath.mp3");

		File dCabWeird3 = new File("/media/tyler/Tyhous_HD/Music/Death Cab for Cutie - Discography/3. Live & Bootleg/2004 - MTV.com (Live)/01 The Dream is Over.mp3");

		File weird4 = new File("/media/tyler/Tyhous_HD/Music/Creedence Clearwater Revival/1970 - Pendulum/01 - Pagan Baby.mp3");

		File weird5 = new File("/media/tyler/Tyhous_HD/Music/Bob Dylan - Studio Discography [1962 - 2015]/[1975] - The Basement Tapes/CD 2/09 - Nothing Was Delivered.mp3");

		File weird6 = new File("/media/tyler/Tyhous_HD/Music/Death Cab for Cutie - Discography/1. Studio Albums/2002 - You Can Play These Songs With Chords/10 TV Trays.mp3");

		File weird7 = new File("/media/tyler/Tyhous_HD/Music/Death Cab for Cutie - Discography/3. Live & Bootleg/2000 - Crocodile Cafe (Live)/01 Title Track.mp3");

		try {
			Song s = Song.fetchSong(weird7);

			System.out.println(s.toJson());
			System.out.println(s.getReleaseGroupInfos());

		} catch(NoSuchElementException e) {}

	}

	public void testReleaseGroupMBIDs() {
		Set<ReleaseGroupInfo> albums = song.getReleaseGroupInfos();

		String albumsStr = Arrays.toString(albums.toArray());
		System.out.println(albumsStr);

		Boolean test = albumsStr.contains("7c4cab8d-dead-3870-b501-93c90fd0a580");
		assertTrue(test);
	}

	public void testMapper() throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println(Tools.MAPPER.writeValueAsString(song));

	}

	public void testReleaseGroupTrackNos() {
		//		File weirdBDSong = new File("/media/tyler/Tyhous_HD/Music/Bob Dylan - Studio Discography [1962 - 2015]/[1962] - Bob Dylan/02 - Talkin' New York.mp3");


		//		Song s = Song.fetchSong(weirdBDSong);
		//		System.out.println(s.toJson());
		for (ReleaseGroupInfo rg : song.getReleaseGroupInfos()) {
			System.out.println(rg.getMbid());
			System.out.println(rg.getTrackNo());
		}

	}

	public void testPrintAlbumTypes() {

		//		File weirdSong = new File("/media/tyler/Tyhous_HD/Music/Bob Dylan - Studio Discography [1962 - 2015]/[1963] - Freewheelin' Bob Dylan/13 - I Shall Be Free.mp3");
		//		Song s = Song.fetchSong(weirdSong);
		//		System.out.println(s.toJson());
		for (ReleaseGroupInfo rg : song.getReleaseGroupInfos()) {
			System.out.println("Primary type = " + rg.getPrimaryType());
			System.out.println("secondary type = " + rg.getSecondaryTypes());
		}
	}

	public void testAlbumTypes() {
		ReleaseGroupInfo rg = song.getReleaseGroupInfos().iterator().next();
		assertEquals(String.valueOf("Album"), rg.getPrimaryType());
		assertNull(rg.getSecondaryTypes());
	}







}

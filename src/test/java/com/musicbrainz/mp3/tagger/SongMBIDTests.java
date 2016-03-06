package com.musicbrainz.mp3.tagger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;

import com.musicbrainz.mp3.tagger.Tools.ReleaseGroupInfo;
import com.musicbrainz.mp3.tagger.Tools.SongMBID;
import com.musicbrainz.mp3.tagger.Tools.Tools;

public class SongMBIDTests extends TestCase {

	SongMBID song;

	@Before
	public void setUp() {
		song = SongMBID.fetchSong("13dd61c7-ce73-4e97-9f0c-9f0e53144411");
	}
	


	public void testCreateQuery() {

		String query = song.getQuery();
		System.out.println(song.getQuery());
		assertEquals("http://musicbrainz.org/ws/2/recording/13dd61c7-ce73-4e97-9f0c-9f0e53144411?inc=artist-credits+releases+release-groups+media+tags&fmt=json"
				,query);

	}

	public void testRecordingMBID() {
		assertEquals("13dd61c7-ce73-4e97-9f0c-9f0e53144411", 
				song.getRecordingMBID());

	}


	public void testReleaseMBID() {
		assertEquals("0503b0a0-2093-408b-ae73-665a13f772e0", 
				song.getReleaseMBID());

	}

	public void testReleaseGroupMBID() {
		assertEquals("1fec7952-ae4e-3f62-bdb8-019ec7f13b27", 
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
		assertEquals("The Art of Darkness", 
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

//		System.out.println(s.toJson());
		for (ReleaseGroupInfo rg : song.getReleaseGroupInfos()) {
			System.out.println(rg.getMbid());
			System.out.println(rg.getTrackNo());
		}
		
	}
	
	public void testPrintAlbumTypes() {
		
		for (ReleaseGroupInfo rg : song.getReleaseGroupInfos()) {
			System.out.println("Primary type = " + rg.getPrimaryType());
			System.out.println("secondary type = " + rg.getSecondaryTypes());
		}
	}
	
//	public void testAlbumTypes() {
//		ReleaseGroupInfo rg = song.getReleaseGroupInfos().iterator().next();
//		assertEquals(String.valueOf("Album"), rg.getPrimaryType());
//		assertNull(rg.getSecondaryTypes());
//	}
	


	
}

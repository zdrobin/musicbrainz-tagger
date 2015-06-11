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
		assertEquals("http://musicbrainz.org/ws/2/recording/?query=recording:%22You%27re+Not+an+Airplane%22%20AND%20artist:%22Guided+By+Voices%22%20AND%20dur:[31900%20TO%2034900]%20AND%20number:20%20AND%20release:%22Bee+Thousand%22%20AND%20date:1994*&limit=1&fmt=json",
				query);
		
	}
	
	public void testRecordingMBID() {
		assertEquals("7d293a68-e869-41f4-9323-46f5b2487a42", 
				song.getRecordingMBID());
		
	}
	
	public void testReleaseMBID() {
		assertEquals("c1295203-75fb-4d24-a3f7-ed1b8a564ee6", 
				song.getReleaseMBID());
		
	}
	
	public void testArtistMBID() {
		assertEquals("6c85f4c3-026c-4c16-9a7c-f546f42ed0fb", 
				song.getArtistMBID());
		
	}
	
	public void testRecording() {
		assertEquals("You’re Not an Airplane", 
				song.getRecording());
		
	}
	
	public void testRelease() {
		assertEquals("Bee Thousand", 
				song.getRelease());
		
	}
	
	public void testArtist() {
		assertEquals("Guided by Voices", 
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
	
	
	

	
}

package com.musicbrainz.mp3.tagger;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Before;

import com.musicbrainz.mp3.tagger.Tools.Song;
import com.musicbrainz.mp3.tagger.Tools.Tagger;

public class SongTests extends TestCase {
	
	File songFile;
	Song song;
	
	
	
	@Before
	public void setUp() {
		songFile = new File("/home/tyler/Downloads/Nine Inch Nails - The Downward Spiral/05 Closer.mp3");
		song = Song.fetchSong(songFile);
	}
	
	
	public void testCreateQuery() {
		
		String query = Tagger.createQueryFromFile(songFile);
		
		assertEquals("http://musicbrainz.org/ws/2/recording/?query=recording:Closer+artist:Nine%Inch%Nails+dur:372666+number:5+release:The%Downward%Spiral+date:1994&limit=1&fmt=json", 
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
	

	
}

package com.musicbrainz.mp3.tagger;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Before;

import com.musicbrainz.mp3.tagger.Tools.CoverArt;
import com.musicbrainz.mp3.tagger.Tools.Song;
import com.musicbrainz.mp3.tagger.Tools.Tools;

public class CoverArtTests extends TestCase {

	Song song;
	CoverArt coverArt;	
	
	@Before
	public void setUp() {
		song = Song.fetchSong(Tools.SAMPLE_SONG);
		coverArt = CoverArt.fetchCoverArt(song.getReleaseGroupMBID());
	}
	
	public void testJson() {
		System.out.println(coverArt.getJson());
	}
	
	public void testImageUrl() {
		assertEquals("http://coverartarchive.org/release/dceb6a01-3431-36af-b2e1-6462193bd67c/2196400361.jpg", 
				coverArt.getImageURL());
	}
	
	public void testsmallThumbnailUrl() {
		assertEquals("http://coverartarchive.org/release/dceb6a01-3431-36af-b2e1-6462193bd67c/2196400361-250.jpg", 
				coverArt.getSmallThumbnailURL());
	}
	
	public void testlargeThumbnailUrl() {
		assertEquals("http://coverartarchive.org/release/dceb6a01-3431-36af-b2e1-6462193bd67c/2196400361-500.jpg", 
				coverArt.getLargeThumbnailURL());
	}
}

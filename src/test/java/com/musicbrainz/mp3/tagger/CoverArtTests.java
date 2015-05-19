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
		coverArt = CoverArt.fetchCoverArt(song);
	}
	
	public void testJson() {
		System.out.println(coverArt.getJson());
	}
	
	public void testImageUrl() {
		assertEquals("http://coverartarchive.org/release/602af258-e647-48e2-9660-bd0c5c8f92bf/10149750572.jpg", 
				coverArt.getImageURL());
	}
	
	public void testsmallThumbnailUrl() {
		assertEquals("http://coverartarchive.org/release/602af258-e647-48e2-9660-bd0c5c8f92bf/10149750572-250.jpg", 
				coverArt.getSmallThumbnailURL());
	}
	
	public void testlargeThumbnailUrl() {
		assertEquals("http://coverartarchive.org/release/602af258-e647-48e2-9660-bd0c5c8f92bf/10149750572-500.jpg", 
				coverArt.getLargeThumbnailURL());
	}
}

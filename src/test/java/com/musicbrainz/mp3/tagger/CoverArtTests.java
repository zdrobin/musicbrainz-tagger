package com.musicbrainz.mp3.tagger;

import java.io.File;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
	
	
	public void testCoverArtNotFoundTest() {
		String mbid = "00fde5ab-541d-3c70-a8b5-6593a35b5bed";
		
		try {
			coverArt = CoverArt.fetchCoverArt(mbid);
		} catch(NoSuchElementException e) {}

	}
}

package com.musicbrainz.mp3.tagger;

import junit.framework.TestCase;

import org.junit.Before;

import com.musicbrainz.mp3.tagger.Tools.Artist;
import com.musicbrainz.mp3.tagger.Tools.ReleaseGroup;

public class ReleaseGroupTests extends TestCase {
	
	ReleaseGroup rg;
	String downwardSpiralRG = "7c4cab8d-dead-3870-b501-93c90fd0a580";
	
	@Before
	public void setUp() {		
		rg = ReleaseGroup.fetchReleaseGroup(downwardSpiralRG);
	}
	
	public void testJson() {
		System.out.println(rg.getJson());
	}
	
	public void testTitle() {
		ReleaseGroup rg2 = ReleaseGroup.fetchReleaseGroup("190299e7-9232-3719-baf0-c69f78892b4c");
		System.out.println(rg2.getTitle());
		
		
		assertEquals("The Downward Spiral",
				rg.getTitle());
	}
	
	public void testYear() {
		assertEquals("1994",
				rg.getYear());
	}
	
	public void testWikipedia() {
		assertEquals("http://en.wikipedia.org/wiki/The_Downward_Spiral", 
				rg.getWikipedia());
	}
	
	public void testLyrics() {
		assertEquals("http://lyrics.wikia.com/Nine_Inch_Nails:The_Downward_Spiral_(1994)", 
				rg.getLyrics());
	}
	
	
	public void testAllMusic() {
		assertEquals("http://www.allmusic.com/album/mw0000110711", 
				rg.getAllMusic());
	}
	
	public void testOfficialHomepage() {
		assertEquals("http://tds.nin.com/", 
				rg.getOfficialHomepage());
	}
	
	public void testWikiData() {
		assertEquals("http://www.wikidata.org/wiki/Q784933", 
				rg.getWikiData());
	}

	
	
}

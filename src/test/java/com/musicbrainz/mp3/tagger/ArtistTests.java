package com.musicbrainz.mp3.tagger;

import java.io.IOException;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;

import com.musicbrainz.mp3.tagger.Tools.Artist;
import com.musicbrainz.mp3.tagger.Tools.Tools;

public class ArtistTests extends TestCase {
	
	Artist artist;
	String pearlJamMbid = "83b9cbe7-9857-49e2-ab8e-b57b01038103";
	
	@Before
	public void setUp() {		
		artist = Artist.fetchArtist(pearlJamMbid);
	}
	
	public void testJson() {
		System.out.println(artist.getJson());
	}
	

	
	public void testWikipedia() {
		assertEquals("http://en.wikipedia.org/wiki/Pearl_Jam", 
				artist.getWikipedia());
	}
	
	public void testLyrics() {
		assertEquals("http://decoda.com/pearl-jam-lyrics", 
				artist.getLyrics());
	}
	
	public void testDiscography() {
		assertEquals("http://members.core.com/10/C7/ryan/pj/", 
				artist.getDiscography());
	}
	
	public void testAllMusic() {
		assertEquals("http://www.allmusic.com/artist/mn0000037730", 
				artist.getAllMusic());
	}
	
	public void testIMDB() {
		assertEquals("http://www.imdb.com/name/nm2149824/", 
				artist.getIMDB());
	}
	
	public void testLastFM() {
		assertEquals("http://www.last.fm/music/Pearl+Jam", 
				artist.getLastFM());
	}
	
	public void testOfficialHomepage() {
		assertEquals("http://www.pearljam.com/", 
				artist.getOfficialHomepage());
	}
	
	public void testFanPage() {
		assertEquals("http://www.fivehorizons.com/", 
				artist.getFanPage());
	}
	
	public void testWikiData() {
		assertEquals("http://www.wikidata.org/wiki/Q142701", 
				artist.getWikiData());
	}
	
	public void testYoutube() {
		assertEquals("http://www.youtube.com/pearljam", 
				artist.getYoutube());
	}
	
	public void testImage() {
		assertEquals("https://commons.wikimedia.org/wiki/File:PearlJamHamilton2011-01.jpg", 
				artist.getImage());
	}
	
	public void testSoundCloud() {
		assertEquals("https://soundcloud.com/pearljam", 
				artist.getSoundCloud());
	}
	
	public void testSocialNetwork() {
		assertEquals("https://plus.google.com/+PearlJam", 
				artist.getSocialNetwork());
	}
	
	public void testTags() throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println(Tools.MAPPER.writeValueAsString(artist.getTags()));
		
	}
	
	
	
	
	

}

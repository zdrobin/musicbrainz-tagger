package com.musicbrainz.mp3.tagger;

import java.io.File;

import junit.framework.TestCase;

import com.musicbrainz.mp3.tagger.Tools.Song;
import com.musicbrainz.mp3.tagger.Tools.Song.Tagger;

public class DerpTest extends TestCase {
	
	
	public void testQueryBuilder() {
		
//		Builder b = MusicBrainzQuery.Builder(recording, artist, duration);
		
		File closerFile = new File("/home/tyler/Downloads/Nine Inch Nails - The Downward Spiral/05 Closer.mp3");
		
		String query = Tagger.createQueryFromFile(closerFile);
		
	
		System.out.println(query);
		
		Song song = Song.fetchSong(closerFile);
		
		song.getMBID();
		
		
		
		
	}

}

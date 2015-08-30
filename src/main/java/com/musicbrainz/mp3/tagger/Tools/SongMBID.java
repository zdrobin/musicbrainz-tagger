package com.musicbrainz.mp3.tagger.Tools;


public class SongMBID extends SongFunctions {


	/**
	 * Looks up the musicbrainz recording from a given mbid

	 * @param audioFile
	 * @return Song
	 */
	public static SongMBID fetchSong(String mbid) {
		return new SongMBID(mbid);
	}

	private SongMBID(String mbid) {

		query = createQueryFromFile(mbid);

		json = fetchMBRecordingJSONFromQuery(query);


	}

	@Override
	public String createQueryFromFile(Object o) {
		
		String mbid = (String) o;
		
		StringBuilder s = new StringBuilder();
		s.append("http://musicbrainz.org/ws/2/recording/");
		s.append(mbid);
		s.append("?inc=artists+releases+release-groups+media+tags");
		
		
		
		
		return s.toString();
	}


}

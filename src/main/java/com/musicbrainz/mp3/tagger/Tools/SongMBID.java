package com.musicbrainz.mp3.tagger.Tools;

import java.util.LinkedHashSet;
import java.util.Set;

import org.codehaus.jackson.JsonNode;


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
		s.append("?inc=artist-credits+releases+release-groups+media+tags&fmt=json");
		
		
		
		
		return s.toString();
	}

	@Override
	public Set<ReleaseGroupInfo> getReleaseGroupInfos()  {

		Set<ReleaseGroupInfo> releaseGroupInfos = new LinkedHashSet<>();
		Set<String> releaseGroupMBIDs = new LinkedHashSet<>(); // for uniqueness
		JsonNode releases = getFirstRecording().get("releases");

		int i = 0;
		while (releases.has(i)) {
			String cReleaseGroupMBID = releases.get(i).get("release-group").get("id").asText();
			Integer discNo = releases.get(i).get("media").get(0).get("position").asInt();
			String trackNoStr = releases.get(i).get("media").get(0).get("tracks").get(0).get("number").asText();

			String primaryType = (releases.get(i).get("release-group").get("primary-type") != null) ?
					releases.get(i).get("release-group").get("primary-type").asText() : null;

					Set<String> secondaryTypes = null;

					JsonNode secondaryTypesJson = releases.get(i).get("release-group").get("secondary-types");
					if (secondaryTypesJson != null) {
						secondaryTypes = new LinkedHashSet<String>();
						int j = 0;
						while (secondaryTypesJson.has(j)) {
							secondaryTypes.add(secondaryTypesJson.get(j++).asText());
						}
					}


					// This was necessary because some track numbers had letters in them, IE A2
					Integer trackNo = 0;
					try {
						trackNo = Integer.valueOf(trackNoStr.replaceAll("[^\\d.]", ""));
					} catch(NumberFormatException e) {
						log.error("Track # was " + trackNoStr + " , so changed it to 0");
						e.printStackTrace();
					}

					// Only create and add if its a unique releaseGroupMBID
					if (!releaseGroupMBIDs.contains(cReleaseGroupMBID)) {
						ReleaseGroupInfo releaseGroupInfo = ReleaseGroupInfo.create(
								cReleaseGroupMBID, trackNo, discNo, primaryType, secondaryTypes);
						releaseGroupInfos.add(releaseGroupInfo);
						releaseGroupMBIDs.add(cReleaseGroupMBID);
					}

					i++;
		}

		return releaseGroupInfos;


	}

}

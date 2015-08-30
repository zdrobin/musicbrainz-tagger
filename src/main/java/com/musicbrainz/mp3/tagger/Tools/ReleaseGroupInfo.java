package com.musicbrainz.mp3.tagger.Tools;

import java.util.Set;

public class ReleaseGroupInfo {

	private String mbid, primaryType;
	private Set<String> secondaryTypes;
	private Integer trackNo, discNo;

	public static ReleaseGroupInfo create(
			String releaseGroupMBID, Integer trackNo, Integer discNo, String primaryType, 
			Set<String> secondaryTypes) {
		return new ReleaseGroupInfo(releaseGroupMBID, trackNo, discNo, primaryType, secondaryTypes);
	}

	private ReleaseGroupInfo(String releaseGroupMBID, Integer trackNo, Integer discNo, 
			String primaryType, Set<String> secondaryTypes) {
		this.mbid = releaseGroupMBID;
		this.trackNo = trackNo;
		this.discNo = discNo;
		this.primaryType = primaryType;
		this.secondaryTypes = secondaryTypes;
	}

	public String getMbid() {
		return mbid;
	}

	public Integer getTrackNo() {
		return trackNo;
	}

	public String getPrimaryType() {
		return primaryType;
	}


	public Set<String> getSecondaryTypes() {
		return secondaryTypes;
	}

	public Integer getDiscNo() {
		return discNo;
	}

	@Override
	public String toString() {
		return "mbid: " + mbid + " , track #: " + trackNo + " , disc #: " + discNo;
	}



}

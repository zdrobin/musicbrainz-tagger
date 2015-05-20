
==================


[MusicBrainz](http://musicbrainz.org/)-Tagger &mdash; Identify audio files with [MusicBrainz]
==========
![](http://img.shields.io/version/1.0.2.png?color=green)

[MusicBrainz](http://musicbrainz.org/)-Tagger is a java library for taking audio files, and retrieving a musicbrainz-id(MBID) for them based on 6 pieces of information:

* track title
* track #
* artist name
* album name
* album year
* song duration

## Dependency
<pre>
<dependency>
  <groupId>com.github.tchoulihan</groupId>
  <artifactId>musicbrainz-tagger</artifactId>
  <version>1.0.2</version>
  </dependency>
</pre>

## How to Use it
<pre>
Song song = Song.fetchSong(path_to_song);

// Get the song MBID
song.getRecordingMBID();

// Get the song name
song.getRecording();

// Get the album name
song.getRelease();

// Get the artist
song.getArtist();

</pre>

It also lets you fetch the URLs for the album cover art from coverartarchive.org:
<pre>
CoverArt coverArt = CoverArt.fetchCoverArt(song);

// Get the main image
coverArt.getImageURL();

// Get a large thumbnail
coverArt.getLargeThumbnailURL();

// Get a small thumbnail
coverArt.getSmallThumbnailURL();

</pre>

## Bugs and feature requests
Have a bug or a feature request? If your issue isn't [already listed](https://github.com/tchoulihan/musicbrainz-tagger/issues/), then open a [new issue here](https://github.com/tchoulihan/musicbrainz-tagger/issues/new).




==================


[MusicBrainz](http://musicbrainz.org/)-Tagger &mdash; Identify audio files with [MusicBrainz](http://musicbrainz.org/)
==========
![](http://img.shields.io/version/1.0.8.png?color=green)

[MusicBrainz](http://musicbrainz.org/)-Tagger is a java library for taking audio files, and retrieving a musicbrainz-id(MBID) for them based on 6 pieces of information:

* track title
* track #
* artist name
* album name
* album year
* song duration

## Dependency
```xml
<dependency>
  <groupId>com.github.tchoulihan</groupId>
  <artifactId>musicbrainz-tagger</artifactId>
  <version>1.0.8</version>
</dependency>
```

## How to Use it
```java
Song song = Song.fetchSong("path/to/song");

// Get the song MBID
song.getRecordingMBID();

// Get the song name
song.getRecording();

// Get the album name
song.getRelease();

// Get the artist
song.getArtist();

```

It also lets you fetch the URLs for the album cover art from coverartarchive.org:
```java
CoverArt coverArt = CoverArt.fetchCoverArt(song);

coverArt.getImageURL();
coverArt.getLargeThumbnailURL();
coverArt.getSmallThumbnailURL();

```

It also lets you fetch extra links and images for information about a certain artist, given their MBID:
```java
Artist artist = Artist.fetchArtist(mbid);

// A list of the types of links and images you can get:
artist.getWikipedia();
artist.getLyrics();
artist.getDiscography();
artist.getAllMusic();
artist.getIMDB();
artist.getLastFM();
artist.getOfficialHomepage();
artist.getFanPage();
artist.getWikiData();
artist.getYoutube();
artist.getImage();
artist.getSoundCloud();
artist.getSocialNetwork();


## Bugs and feature requests
Have a bug or a feature request? If your issue isn't [already listed](https://github.com/tchoulihan/musicbrainz-tagger/issues/), then open a [new issue here](https://github.com/tchoulihan/musicbrainz-tagger/issues/new).



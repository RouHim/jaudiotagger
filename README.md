This is a fork of [Kaned1as/jaudiotagger](https://github.com/Kaned1as/jaudiotagger).

# Jaudiotagger

Jaudiotagger is a Java library for reading and writing audio metadata. It supports a wide range of audio formats, including MP3, Ogg Vorbis, FLAC, WMA, MP4, MPC, Monkey's Audio, OptimFROG, True Audio, WavPack, Musepack, Speex, WAV, AIFF, AU, RealAudio, Opus, and MP2. It also supports ID3v1, ID3v2, Vorbis Comments, APEv2, WMA, MP4, and ASF metadata.

## Differences to the original
* Bumped dependency versions
* Compile target java 17
* Removed android compatibility
* Migrated to junit 5
* Added: github actions, renovate, semantic releases

## Motivation
Mainly I forked the repository because I want to use a maintained version of jaudiotaggger in my [own software](https://github.com/RouHim/disCoverJ).

## Usage
Add this repository to your pom.xml:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
And then this dependency:
```xml
<dependency>
    <groupId>com.github.RouHim</groupId>
    <artifactId>jaudiotagger</artifactId>
    <version>...</version>
</dependency>
```

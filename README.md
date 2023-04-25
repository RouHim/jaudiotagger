Jaudiotagger
============

**Note: This project is a hard-fork of [ijabs one](https://bitbucket.org/ijabz/jaudiotagger), adding better support for
mp4-dash and opus formats.
I use it for my projects but there's no guarantee it is suitable for you. Patches are welcome.**

*Jaudiotagger* is a Java API for audio metatagging. Both a common API and format
specific APIs are available, currently supports reading and writing metadata for:

- Mp3
- Flac
- OggVorbis
- Mp4
- Mp4 - DASH
- Aiff
- Wav
- Wma
- Dsf
- Opus

Using
-----

Just add this as a Jitpack dependency:

```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.Adonai:jaudiotagger:2.3.14'
}
```

Requirements
------------

*Jaudiotagger* requires Java 1.8 for a full build and install

Contributing
------------

*Jaudiotagger* welcomes contributors, if you make an improvement or bug fix we are
very likely to merge it back into the master branch with a minimum of fuss.

Build
-----

Build is with [Maven](http://maven.apache.org).

- `pom.xml` : Maven build file

Directory structure as follows:

### Under source control

- `src`                  : source code directory
- `srctest`              : source test code directory
- `www`                  : java doc directory
- `testdata`             : test files for use by the junit tests, not all tests are included in the distribution because
  of copyright
- `target`               : contains the `jaudiotagger***.jar` built from maven

### License

- `license.txt` : license file
 
 

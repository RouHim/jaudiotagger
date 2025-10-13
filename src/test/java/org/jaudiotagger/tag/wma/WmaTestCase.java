package org.jaudiotagger.tag.wma;

import org.jaudiotagger.AbstractBaseTestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.tag.Tag;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Base class for WMA test cases.<br>
 *
 * @author Christian Laireiter
 */
public abstract class WmaTestCase extends AbstractBaseTestCase {

    /**
     * The file name of the source file, from which a copy will be created.
     */
    private final String sourceTestFile;
    /**
     * Stores the audio file instance of {@link #testFile}.<br>
     */
    private AudioFile audioFile;
    /**
     * The file on which tests should be performed.<br>
     */
    private File testFile;

    /**
     * Creates an instance, that would perform tests on the given <code>sourceFile</code>.
     *
     * @param sourceFile The filename of the file to perform tests on. ({@linkplain AbstractTestCase#copyAudioToTmp(String) copy} will be created)
     */
    public WmaTestCase(final String sourceFile) {
        this.sourceTestFile = sourceFile;
    }

    /**
     * Returns the tag of the {@linkplain #audioFile}.<br>
     *
     * @return the tag of the {@linkplain #audioFile}.<br>
     * @throws Exception from call of  {@link #getAudioFile()}.
     */
    public Tag getTag() throws Exception {
        return getAudioFile().getTag();
    }

    /**
     * Returns the audio file to perform the tests on.<br>
     *
     * @return audio file to perform the tests on.<br>
     * @throws Exception Upon IO errors, or ASF parsing faults.
     */
    public AudioFile getAudioFile() throws Exception {
        if (this.audioFile == null) {
            this.audioFile = AudioFileIO.read(this.testFile);
        }
        return this.audioFile;
    }

    /**
     * Creates the file copy.
     */
    @BeforeEach
    protected void setUp() throws Exception {
        assertNotNull(sourceTestFile);
        this.testFile = prepareTestFile(null);
    }

    /**
     * Returns a file for testing purposes.
     *
     * @return file for testing.
     */
    public File prepareTestFile(String fileName) {
        assertNotNull(sourceTestFile);
        File result = null;
        if (!Utils.isBlank(fileName)) {
            result = copyAudioToTmp(
                    sourceTestFile,
                    fileName
            );
        } else {
            result = copyAudioToTmp(sourceTestFile);
        }
        return result;
    }

    /**
     * Deletes the copy.
     */
    protected void tearDown() throws Exception {
        //        this.testFile.deleteField();
    }
}

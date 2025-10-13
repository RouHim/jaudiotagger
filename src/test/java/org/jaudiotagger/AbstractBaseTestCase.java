package org.jaudiotagger;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractBaseTestCase {

    private static File tempDirectory;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeAll
    public static void setUpOnce() throws Exception {
        tempDirectory = Files.createTempDirectory("jaudiotagger_").toFile();
    }

    public boolean executeAlsoWithMissingResources() {
        return false;
    }

    /**
     * Copy audiofile to processing dir ready for use in test
     *
     * @param fileName
     * @return
     */
    public File copyAudioToTmp(String fileName) {
        File inputFile = fileResource("testdata", fileName);
        if (inputFile.exists()) {
            File outputFile = tempFileResource(fileName);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            boolean result = copy(inputFile, outputFile);
            assertTrue(result);
            return outputFile;
        } else {
            log.warn("Sourcefile does not exist '" + inputFile + "'");
            return null;
        }
    }

    /**
     * Copy a File
     *
     * @param fromFile The existing File
     * @param toFile   The new File
     * @return <code>true</code> if and only if the renaming succeeded;
     * <code>false</code> otherwise
     */
    public boolean copy(File fromFile, File toFile) {
        try (
                FileInputStream fins = new FileInputStream((fromFile));
                FileOutputStream fouts = new FileOutputStream((toFile))
        ) {
            fins.transferTo(fouts);
            fouts.flush();

            // cleanupif files are not the same length
            long length1 = fromFile.length();
            long length2 = toFile.length();
            if (length1 != length2) {
                toFile.delete();

                return false;
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public File tempFileResource(String fileName) {
        File file;
        if (fileName == null) {
            file = tempDirectory;
        } else {
            file = new File(tempDirectory, fileName);
        }
        return file;
    }

    public File fileResource(String directory, String fileName) {
        String dir;
        if (!directory.endsWith("/")) {
            dir = directory + "/";
        } else {
            dir = directory;
        }
        File file = null;
        try {
            URI resource;
            if (fileName == null) {
                resource = ClassLoader.getSystemResource(dir).toURI();
            } else {
                resource = ClassLoader.getSystemResource(dir + fileName).toURI();
            }
            file = new File(resource);
        } catch (Exception e) {
            log.error("Could not open resource '" + dir + fileName + "'");
        }
        return file;
    }

    /**
     * Copy audiofile to processing dir ready for use in test, use this if using
     * same file in multiple tests because with junit multithreading can have
     * problems otherwise
     *
     * @param fileName
     * @return
     */
    public File copyAudioToTmp(String fileName, String newFileName) {
        File inputFile = fileResource("testdata", fileName);
        File outputFile = tempFileResource(newFileName);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        boolean result = copy(inputFile, outputFile);
        assertTrue(result);
        return outputFile;
    }

    /**
     * Prepends file with tag file in order to create an mp3 with a valid id3
     *
     * @param tagfile
     * @param fileName
     * @return
     */
    public File prependAudioToTmp(String tagfile, String fileName) {
        File inputTagFile = fileResource("testtagdata", tagfile);
        File inputFile = fileResource("testdata", fileName);
        File outputFile = tempFileResource(fileName);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        boolean result = append(inputTagFile, inputFile, outputFile);
        assertTrue(result);
        return outputFile;
    }

    private boolean append(File fromFile1, File fromFile2, File toFile) {
        try {
            FileInputStream in = new FileInputStream(fromFile1);
            FileInputStream in2 = new FileInputStream(fromFile2);
            FileOutputStream out = new FileOutputStream(toFile);
            BufferedInputStream inBuffer = new BufferedInputStream(in);
            BufferedInputStream inBuffer2 = new BufferedInputStream(in2);
            BufferedOutputStream outBuffer = new BufferedOutputStream(out);

            int theByte;

            while ((theByte = inBuffer.read()) > -1) {
                outBuffer.write(theByte);
            }

            while ((theByte = inBuffer2.read()) > -1) {
                outBuffer.write(theByte);
            }

            outBuffer.close();
            inBuffer.close();
            inBuffer2.close();
            out.close();
            in.close();
            in2.close();

            // cleanupif files are not the same length
            if ((fromFile1.length() + fromFile2.length()) != toFile.length()) {
                toFile.delete();

                return false;
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public File fileResource(String directory) {
        return fileResource(directory, null);
    }
}

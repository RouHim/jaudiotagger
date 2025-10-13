package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class AiffInfoReaderTest {

    @Test
    public void testWithSomeLocalChunks()
            throws IOException, CannotReadException {
        final String author = "AUTH4567";
        final String copyright = "(c) 4567";
        final String annotation1 = "ANNO1_67890123456789";
        final String annotation2 = "ANNO2_67890123456789";
        final String name = "NAME456789";
        final PseudoChunk[] pseudoChunks = {
                new PseudoChunk("NAME", name),
                new PseudoChunk("ANNO", annotation1),
                new PseudoChunk("ANNO", annotation2),
                new PseudoChunk("(c) ", copyright),
                new PseudoChunk("AUTH", author),
        };
        final File aiff = createAIFF("FORM", "AIFF", pseudoChunks);

        final AiffInfoReader aiffInfoReader = new AiffInfoReader();
        try (FileChannel fc = new RandomAccessFile(aiff, "rw").getChannel()) {
            final GenericAudioHeader audioHeader = aiffInfoReader.read(
                    fc,
                    aiff.getAbsolutePath()
            );
            assertInstanceOf(AiffAudioHeader.class, audioHeader);
            final AiffAudioHeader aiffAudioHeader = (AiffAudioHeader) audioHeader;
            assertEquals(author, aiffAudioHeader.getAuthor());
            assertEquals(name, aiffAudioHeader.getName());
            assertEquals(copyright, aiffAudioHeader.getCopyright());
            assertEquals(annotation1, aiffAudioHeader.getAnnotations().get(0));
            assertEquals(annotation2, aiffAudioHeader.getAnnotations().get(1));
        }
        aiff.delete();
    }

    private static File createAIFF(
            final String form,
            final String formType,
            final PseudoChunk... chunks
    ) throws IOException {
        final File tempFile = File.createTempFile(
                AiffFileHeaderTest.class.getSimpleName(),
                ".aif"
        );
        tempFile.deleteOnExit();

        try (
                final DataOutputStream out = new DataOutputStream(
                        new FileOutputStream(tempFile)
                )
        ) {
            // create some chunks
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            final DataOutputStream dataOut = new DataOutputStream(bout);

            for (final PseudoChunk chunk : chunks) {
                dataOut.write(chunk.chunkType().getBytes(StandardCharsets.US_ASCII));
                dataOut.writeInt(chunk.text().length());
                dataOut.write(chunk.text().getBytes(StandardCharsets.US_ASCII));
            }
            dataOut.flush();

            // write header

            // write format
            out.write(form.getBytes(StandardCharsets.US_ASCII));
            // write size
            out.writeInt(bout.size() + 4);
            // write format type
            out.write(formType.getBytes(StandardCharsets.US_ASCII));
            out.write(bout.toByteArray());
        }
        return tempFile;
    }

    @Test
    public void testWithUnknownChunk() throws IOException, CannotReadException {
        final String author = "AUTH4567";
        final File aiff = createAIFF(
                "FORM",
                "AIFF",
                new PseudoChunk("XYZ0", "SOME_STUFF"),
                new PseudoChunk("AUTH", author)
        );

        final AiffInfoReader aiffInfoReader = new AiffInfoReader();
        try (FileChannel fc = new RandomAccessFile(aiff, "rw").getChannel()) {
            final GenericAudioHeader audioHeader = aiffInfoReader.read(
                    fc,
                    aiff.getAbsolutePath()
            );
            assertInstanceOf(AiffAudioHeader.class, audioHeader);
            final AiffAudioHeader aiffAudioHeader = (AiffAudioHeader) audioHeader;
            assertEquals(author, aiffAudioHeader.getAuthor());
        }
        aiff.delete();
    }

    private record PseudoChunk(String chunkType, String text) {

    }
}

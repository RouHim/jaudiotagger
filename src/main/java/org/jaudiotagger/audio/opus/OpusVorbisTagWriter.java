package org.jaudiotagger.audio.opus;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.ogg.OggVorbisCommentTagCreator;
import org.jaudiotagger.audio.ogg.util.OggPage;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Write Vorbis Tag within an ogg
 * <p>
 * VorbisComment holds the tag information within an ogg file
 */
public class OpusVorbisTagWriter {

    private static final Logger logger = LoggerFactory.getLogger("org.jaudiotagger.audio.opus");

    private final OggVorbisCommentTagCreator tc = new OggVorbisCommentTagCreator(new byte[0], OpusHeader.TAGS_CAPTURE_PATTERN_AS_BYTES, false);
    private final OpusVorbisTagReader reader = new OpusVorbisTagReader();

    public void delete(RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotReadException, CannotWriteException {
        try {
            reader.read(raf);
        } catch (CannotReadException e) {
            write(VorbisCommentTag.createNewTag(), raf, tempRaf);
            return;
        }

        VorbisCommentTag emptyTag = VorbisCommentTag.createNewTag();

        //Go back to start of file
        raf.seek(0);
        write(emptyTag, raf, tempRaf);
    }

    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException {
        logger.debug("Starting to write file: " + raf);

        FileChannel fi = raf.getChannel();
        FileChannel fo = rafTemp.getChannel();

        //1st Page:Identification Header
        logger.debug("Read 1st Page: identificationHeader");
        List<OggPage> originalHeaders = readPages(fi);

        //Convert the OggVorbisComment header to raw packet data
        ByteBuffer newComment = tc.convert(tag);

        // write identification header
        OggPage identPage = originalHeaders.remove(0);
        writePage(fo, identPage);

        // second page is OpusTags, skip all OpusTags
        originalHeaders.remove(0); // skip first tag page
        while (originalHeaders.get(0).getHeader().isContinuedPage()) {
            originalHeaders.remove(0); // skip continued tag pages
        }

        final int fullPagesNeeded = newComment.capacity() / OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        final int pagesRemainder = newComment.capacity() % OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        final int streamNo = identPage.getHeader().getSerialNumber();
        int sequenceNo = 1;
        for (int page = 0; page < fullPagesNeeded; page++) {
            OggPageHeader header = OggPageHeader.createCommentHeader(OggPageHeader.MAXIMUM_PAGE_DATA_SIZE, page != 0, streamNo, sequenceNo++);
            ByteBuffer content = newComment.slice();
            content.limit(OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);

            writePage(fo, new OggPage(header, content));
            Utils.skip(newComment, OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
        }

        if (pagesRemainder > 0) {
            OggPageHeader header = OggPageHeader.createCommentHeader(pagesRemainder, fullPagesNeeded > 0, streamNo, sequenceNo++);
            ByteBuffer content = newComment.slice();
            writePage(fo, new OggPage(header, content));
        }

        for (OggPage page : originalHeaders) {
            page.setSequenceNo(sequenceNo++);
            writePage(fo, page);
        }
    }

    private void writePage(FileChannel fo, OggPage oggPage) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(oggPage.size());
        oggPage.write(buf);

        buf.rewind();
        fo.write(buf);
    }

    private List<OggPage> readPages(FileChannel fi) throws IOException, CannotReadException {
        List<OggPage> output = new ArrayList<>();
        ByteBuffer buf = Utils.fetchFromChannel(fi, (int) fi.size());
        while (buf.remaining() > 0) {
            output.add(OggPage.parse(buf));
        }
        return output;
    }
}
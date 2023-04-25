package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.AbstractTestCase;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrameBodyETCOTest extends AbstractTestCase {

    public static FrameBodyETCO getInitialisedBody() {
        FrameBodyETCO fb = new FrameBodyETCO();
        fb.addTimingCode(0, 1, 2);
        fb.addTimingCode(5, 1);
        fb.setTimestampFormat(2);
        return fb;
    }

    @Test
    public void testAddTimingCode() {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);
        final Map<Long, int[]> timingCodes = body.getTimingCodes();

        // verify content
        assertArrayEquals(new int[]{0}, timingCodes.get(10L));
        assertArrayEquals(new int[]{0, 1}, timingCodes.get(5L));
        assertArrayEquals(new int[]{1, 2}, timingCodes.get(11L));

        // verify order
        long lastTimestamp = 0;
        for (final Long timestamp : timingCodes.keySet()) {
            assertTrue(timestamp >= lastTimestamp);
            lastTimestamp = timestamp;
        }
    }

    @Test
    public void testRemoveTimingCode() {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);

        body.removeTimingCode(5, 0);

        final Map<Long, int[]> timingCodes = body.getTimingCodes();
        assertArrayEquals(new int[]{0}, timingCodes.get(10L));
        assertArrayEquals(new int[]{1}, timingCodes.get(5L));
        assertArrayEquals(new int[]{1, 2}, timingCodes.get(11L));
    }

    @Test
    public void testClearTimingCode() {
        final FrameBodyETCO body = new FrameBodyETCO();
        body.addTimingCode(10, 0);
        body.addTimingCode(5, 0);
        body.addTimingCode(5, 1);
        body.addTimingCode(11, 1, 2);

        body.clearTimingCodes();

        final Map<Long, int[]> timingCodes = body.getTimingCodes();
        assertTrue(timingCodes.isEmpty());
    }

}

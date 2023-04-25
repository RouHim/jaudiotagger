package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertNull;

public class NetworkTest extends AbstractTestCase {
    private static final AtomicInteger count = new AtomicInteger(0);

    private void loadFiles(final File dir) throws Exception {
        File[] files = dir.listFiles(new AudioFileFilter());
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    loadFiles(file);
                } else {
                    System.out.println(new Date() + ":Start File:" + file.getPath());
                    AudioFileIO.read(file);
                    //FileChannel fc = new FileInputStream(file).getChannel();
                    //ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY,0,500000);
                    System.out.println(new Date() + ":End File:" + file.getPath());
                    count.incrementAndGet();
                }
            }

        }
    }

    @Test
    public void testNetworkSpeed() {
        Exception caught = null;
        try {
            System.out.println("Start:" + new Date());
            File file = new File("Z:\\Music\\Replay Music Recordings");
            //File file = new File("C:\\Users\\MESH\\Music\\Replay Music Recordings");
            loadFiles(file);
            System.out.println("Loaded:" + count.get());
            System.out.println("End:" + new Date());

        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        assertNull(caught);
    }

    /*
    @Test
    public void testDataCopySpeed() throws Exception
    {
        File file = new File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");
        
        System.out.println("start:"+new Date());
        FileChannel fc = new FileInputStream(file).getChannel();
        ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY,0,500000);
        fc.close();
        System.out.println("end:"+new Date());

    }

    @Test
    public void testDataCopySpeed2() throws Exception
    {
        File file = new File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");

        System.out.println("start:"+new Date());
        FileChannel fc = new FileInputStream(file).getChannel();
        ByteBuffer bb = ByteBuffer.allocate(500000);
        fc.read(bb);
        fc.close();
        System.out.println("end:"+new Date());

    } */


    /*public void testDataCopyBufferedStream() throws Exception
    {

        File file = new File("Z:\\Music\\Replay Music Recordings\\Beirut\\The Rip Tide\\Beirut-The Rip Tide-05-Payne's Bay.mp3");
        Date start = new Date();

        FileChannel fc = new FileInputStream(file).getChannel();
        ByteBuffer bb = ByteBuffer.allocate(500000);
        WritableByteChannel wbc = bb.
        //fc.read(bb);
        //fc.close();
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime());
      )
      */
}
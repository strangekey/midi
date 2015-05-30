/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MidiFileWriterTest {

    private static final int HEADER_ID = 4;
    private static final int HEADER_LENGTH = 4;
    private static final int MIDI_FILE_FORMAT = 2;
    private static final int NUM_TRACKS = 2;
    private static final int DIVISION = 2;
    private static final int HEAD_CHUNK_SIZE_BYTES = HEADER_ID + HEADER_LENGTH + MIDI_FILE_FORMAT + NUM_TRACKS + DIVISION;
    private static final int TRACK_ID = 4;
    private static final int TRACK_LENGTH = 4;
    private static final int TRACK_HEADER_SIZE_BYTES = TRACK_ID + TRACK_LENGTH;

    @Test
    public void testWriteSequenceSingleTrackOneEvent() throws MidiException, IOException {
        Sequence sequence = new Sequence();
        Track track = sequence.createTrack();
        track.add(new MidiEvent(ChannelMessage.noteOn(0, 60, 50), 5));

        final List<Byte> midiFileBytes = new ArrayList<Byte>();
        OutputStream out = getFakeOutputStream(midiFileBytes);

        MidiFileWriter.write(sequence, out);
        assertEquals(getTrackZeroOffset() + getTrackByteSize(track), midiFileBytes.size());
        checkHeader(midiFileBytes, 1);
        checkTrackHeader(midiFileBytes, getTrackZeroOffset(), track.getLength());
        assertTrue(track.getLength() == midiFileBytes.size() - (getTrackZeroOffset() + TRACK_HEADER_SIZE_BYTES));
    }

    @Test
    public void testWriteSequenceSingleTrackOneEventCorrectSizeReflectingDeltaTime() throws MidiException, IOException {
        Sequence sequence = new Sequence();
        Track track = sequence.createTrack();
        // each absolute time is two bytes, but delta is just one byte
        track.add(new MidiEvent(ChannelMessage.noteOn(0, 60, 50), 400));
        track.add(new MidiEvent(ChannelMessage.noteOn(0, 60, 50), 500));

        final List<Byte> midiFileBytes = new ArrayList<Byte>();
        OutputStream out = getFakeOutputStream(midiFileBytes);

        MidiFileWriter.write(sequence, out);
        assertEquals(getTrackZeroOffset() + getTrackByteSize(track), midiFileBytes.size());
        checkHeader(midiFileBytes, 1);
        checkTrackHeader(midiFileBytes, getTrackZeroOffset(), track.getLength());
        assertTrue(track.getLength() == midiFileBytes.size() - (getTrackZeroOffset() + TRACK_HEADER_SIZE_BYTES));
    }

    @Test
    public void testWriteSequenceSingleTrackMultipleEvents() throws MidiException, IOException {
        Sequence sequence = new Sequence();
        Track track = sequence.createTrack();
        track.add(new MidiEvent(ChannelMessage.noteOn(0, 60, 50), 500));
        track.add(new MidiEvent(ChannelMessage.noteOff(0, 60, 50), 3000));

        final List<Byte> midiFileBytes = new ArrayList<Byte>();
        OutputStream out = getFakeOutputStream(midiFileBytes);

        MidiFileWriter.write(sequence, out);
        assertEquals(getTrackZeroOffset() + getTrackByteSize(track), midiFileBytes.size());
        checkHeader(midiFileBytes, 1);
        checkTrackHeader(midiFileBytes, getTrackZeroOffset(), track.getLength());
        assertTrue(track.getLength() == midiFileBytes.size() - (getTrackZeroOffset() + TRACK_HEADER_SIZE_BYTES));
    }

    @Test
    public void testWriteSequenceMultipleTracks() throws MidiException, IOException {
        Sequence sequence = new Sequence();
        Track trackZero = sequence.createTrack();
        trackZero.add(new MidiEvent(MetaMessage.tempoMessage(60), 0));

        Track trackOne = sequence.createTrack();
        trackOne.add(new MidiEvent(ChannelMessage.programChange(0, 33), 0));
        trackOne.add(new MidiEvent(ChannelMessage.noteOn(0, 70, 80), 50));
        trackOne.add(new MidiEvent(ChannelMessage.noteOff(0, 70, 81), 120));

        Track trackTwo = sequence.createTrack();
        trackTwo.add(new MidiEvent(ChannelMessage.programChange(0, 21), 0));

        final List<Byte> midiFileBytes = new ArrayList<Byte>();

        OutputStream out = getFakeOutputStream(midiFileBytes);

        MidiFileWriter.write(sequence, out);
        assertEquals(getTrackZeroOffset() + getTrackByteSize(trackZero) + getTrackByteSize(trackOne) + getTrackByteSize(trackTwo), midiFileBytes.size());
        checkHeader(midiFileBytes, 3);
        checkTrackHeader(midiFileBytes, getTrackZeroOffset(), trackZero.getLength());
        checkTrackHeader(midiFileBytes, getTrackOneOffset(trackZero), trackOne.getLength());
        checkTrackHeader(midiFileBytes, getTrackTwoOffset(trackZero, trackOne), trackTwo.getLength());
    }

    private int getTrackTwoOffset(Track trackZero, Track trackOne) {
        return getTrackOneOffset(trackZero) + TRACK_HEADER_SIZE_BYTES + trackOne.getLength();
    }

    private int getTrackOneOffset(Track trackZero) {
        return getTrackZeroOffset() + TRACK_HEADER_SIZE_BYTES + trackZero.getLength();
    }

    private int getTrackZeroOffset() {
        return HEAD_CHUNK_SIZE_BYTES;
    }

    private int getTrackByteSize(Track track) {
        return TRACK_HEADER_SIZE_BYTES + track.getLength();
    }

    private void checkHeader(final List<Byte> midiFileBytes, int numTracks) {
        // header id
        assertTrue(midiFileBytes.get(0) == MidiFileWriter.M);
        assertTrue(midiFileBytes.get(1) == MidiFileWriter.T);
        assertTrue(midiFileBytes.get(2) == MidiFileWriter.h);
        assertTrue(midiFileBytes.get(3) == MidiFileWriter.d);

        // header size
        assertTrue(midiFileBytes.get(4) == 0);
        assertTrue(midiFileBytes.get(5) == 0);
        assertTrue(midiFileBytes.get(6) == 0);
        assertTrue(midiFileBytes.get(7) == 6);

        // format
        assertTrue(midiFileBytes.get(8) == 0);
        assertTrue(midiFileBytes.get(9) == 1);

        // num tracks
        assertTrue(midiFileBytes.get(10) == 0);
        assertTrue(midiFileBytes.get(11) == numTracks);

        // resolution
        assertTrue(midiFileBytes.get(12) == 0);
        assertTrue(midiFileBytes.get(13) == 128 - 192);
    }

    private void checkTrackHeader(final List<Byte> midiFileBytes, int offset, int trackLength) {
        // track id
        assertTrue(midiFileBytes.get(offset++) == MidiFileWriter.M);
        assertTrue(midiFileBytes.get(offset++) == MidiFileWriter.T);
        assertTrue(midiFileBytes.get(offset++) == MidiFileWriter.r);
        assertTrue(midiFileBytes.get(offset++) == MidiFileWriter.k);

        // track length
        byte[] trackLengthBytes = MidiFileWriter.intToBytes(trackLength, 4);
        assertTrue(midiFileBytes.get(offset++) == trackLengthBytes[0]);
        assertTrue(midiFileBytes.get(offset++) == trackLengthBytes[1]);
        assertTrue(midiFileBytes.get(offset++) == trackLengthBytes[2]);
        assertTrue(midiFileBytes.get(offset++) == trackLengthBytes[3]);

        int dataOffset = offset;

        byte b = -1;
        while (MidiFileWriter.M != b && offset < midiFileBytes.size()) {
            b = midiFileBytes.get(offset++);
        }

        int endOffset = b == MidiFileWriter.M ? offset - 1 : offset;
        assertTrue(endOffset - dataOffset == trackLength);
    }

    private OutputStream getFakeOutputStream(final List<Byte> midiFileBytes) {
        OutputStream out = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                fail();
                write(new byte[]{(byte) (b >>> 24), (byte) (b >>> 16), (byte) (b >>> 8), (byte) b});
            }

            @Override
            public void write(byte[] bytes) throws IOException {
                for (byte b : bytes) {
                    midiFileBytes.add(b);
                }
            }
        };
        return out;
    }
}

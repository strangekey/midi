/**
 * Copyright 2010 Leeor Engel.
 *
 * Midi format 1 (multi-track) file writer
 *
 * http://home.roadrunner.com/~jgglatt/tech/midifile.htm for details on the midi file format
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MidiFileWriter {

    static final byte M = 0x4D;
    static final byte T = 0x54;
    static final byte h = 0x68;
    static final byte d = 0x64;
    static final byte r = 0x72;
    static final byte k = 0x6B;

    private static final byte HEADER_LENGTH = 6;

    public static final void write(Sequence sequence, OutputStream out) throws IOException {
        writeHeader(sequence, out);

        int numTracks = sequence.getNumTracks();
        for (int i = 0; i < numTracks; i++) {
            writeTrack(sequence.getTrackAt(i), out);
        }
    }

    private static void writeHeader(Sequence sequence, OutputStream out) throws IOException {
        writeHeaderId(out);
        writeHeaderLength(out);
        writeMidiFormatOne(out);
        out.write(intToBytes(sequence.getNumTracks(), 2));
        out.write(intToBytes(sequence.getResolution(), 2));
    }

    private static void writeHeaderId(OutputStream out) throws IOException {
        out.write(new byte[]{M, T, h, d});
    }

    private static void writeHeaderLength(OutputStream out) throws IOException {
        out.write(new byte[]{0, 0, 0, HEADER_LENGTH});
    }

    private static void writeMidiFormatOne(OutputStream out) throws IOException {
        out.write(new byte[]{0, 1});
    }

    private static void writeTrack(Track track, OutputStream out) throws IOException {
        writeTrackId(out);
        writeTrackLength(track, out);

        MidiEvent firstEvent = track.get(0);
        writeMidiEvent(firstEvent, out, firstEvent.getTimeIndex());

        MidiEvent previousEvent = firstEvent;

        for (int i = 1; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            writeMidiEvent(event, out, getDeltaTime(previousEvent, event));
            previousEvent = event;
        }
    }

    private static void writeTrackLength(Track track, OutputStream out) throws IOException {
        out.write(intToBytes(track.getLength(), 4));
    }

    private static void writeMidiEvent(MidiEvent event, OutputStream out, long actualTimeIndex) throws IOException {
        out.write(MidiFileUtils.getVariableLengthFieldBytes(actualTimeIndex));
        out.write(event.getMessage().toBytes());
    }

    private static long getDeltaTime(MidiEvent previousEvent, MidiEvent event) {
        return event.getTimeIndex() - previousEvent.getTimeIndex();
    }

    private static void writeTrackId(OutputStream out) throws IOException {
        out.write(new byte[]{M, T, r, k});
    }

    static byte[] intToBytes(int value, int size) {
        switch (size) {
            case 1:
                return new byte[]{(byte) value};
            case 2:
                return new byte[]{(byte) (value >>> 8), (byte) value};
            case 3:
                return new byte[]{(byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
            case 4:
            default:
                return new byte[]{(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
        }
    }

    public static void main(String[] args) throws MidiException, IOException {
        Sequence sequence = new Sequence();
        Track trackOne = sequence.createTrack();
        trackOne.add(new MidiEvent(MetaMessage.tempoMessage(120), 50));
        Track trackTwo = sequence.createTrack();
        trackTwo.add(new MidiEvent(ChannelMessage.noteOn(0, 40, 55), 50));
        trackTwo.add(new MidiEvent(ChannelMessage.noteOff(0, 40, 100), 340));

        FileOutputStream out = new FileOutputStream(new File("test.mid"));
        MidiFileWriter.write(sequence, out);
    }
}

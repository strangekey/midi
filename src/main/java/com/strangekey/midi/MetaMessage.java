/**
 * Copyright 2010 Leeor Engel.
 *
 * Represents a variable length midi meta message
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import java.util.Arrays;

public class MetaMessage implements MidiMessage {

    static final byte META_STATUS_BYTE = (byte) 0xFF;
    private static final int MASK = 0xFF;

    private static final int MIDI_CLOCKS_PER_METRONOME_CLICK = 1;
    private static final int THIRTY_SECOND_NOTES_PER_QUARTER_NOTE = 8;
    private byte type;
    private byte[] data;
    private int length;

    private MetaMessage(Type type, byte[] data) {
        this.type = type.value;
        this.data = data;
        this.length = data.length;
    }

    public static MetaMessage tempoMessage(int bpm) throws MidiException {
        if (bpm < 0) {
            throw new MidiException("invalid bpm");
        }
        int tempoInMPQ = 60000000 / bpm;
        return new MetaMessage(Type.TEMPO, shiftTempoInMPQIntoDataBytes(tempoInMPQ));
    }

    public static MetaMessage timeSignatureMessage(int beatsPerBar, int beatValue) {
        byte[] data = new byte[4];
        data[0] = (byte) (beatsPerBar & MASK);
        data[1] = (byte) (getDenominator(beatValue) & MASK);
        data[2] = (byte) (MIDI_CLOCKS_PER_METRONOME_CLICK & MASK);
        data[3] = (byte) (THIRTY_SECOND_NOTES_PER_QUARTER_NOTE & MASK);
        return new MetaMessage(Type.TIME_SIGNATURE, data);
    }

    static int getDenominator(int denominator) {
        int exponent = 0;
        double remainder = denominator;

        do {
            remainder /= 2;
            exponent++;
        } while (remainder > 1);
        return exponent;

    }

    public static MetaMessage textMessage(String data) {
        return new MetaMessage(Type.TEXT, data.getBytes());
    }

    public static MetaMessage lyricTextMessage(String data) {
        return new MetaMessage(Type.LYRIC_TEXT, data.getBytes());
    }

    public static MetaMessage markerTextMessage(String data) {
        return new MetaMessage(Type.MARKER_TEXT, data.getBytes());
    }

    private static byte[] shiftTempoInMPQIntoDataBytes(int tempoInMPQ) {
        byte[] data = new byte[3];
        data[0] = (byte) ((tempoInMPQ >> 16) & MASK);
        data[1] = (byte) ((tempoInMPQ >> 8) & MASK);
        data[2] = (byte) (tempoInMPQ & MASK);
        return data;
    }

    public byte getType() {
        return type;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public int getLength() {
        return getTotalMessageLength(getLengthNumBytes());
    }

    private int getTotalMessageLength(int lengthNumBytes) {
        return 1 + 1 + length + lengthNumBytes;
    }

    @Override
    public byte[] toBytes() {
        int lengthNumBytes = getLengthNumBytes();
        int size = getTotalMessageLength(lengthNumBytes);
        byte[] fileBytes = new byte[size];
        fileBytes[0] = META_STATUS_BYTE;
        fileBytes[1] = type;
        System.arraycopy(MidiFileUtils.getVariableLengthFieldBytes(length), 0, fileBytes, 2, lengthNumBytes);
        System.arraycopy(data, 0, fileBytes, 3, length);
        return fileBytes;
    }

    private int getLengthNumBytes() {
        return MidiFileUtils.getVariableLengthFieldByteLength(length);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.data);
        result = prime * result + this.length;
        result = prime * result + this.type;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetaMessage other = (MetaMessage) obj;
        if (!Arrays.equals(this.data, other.data))
            return false;
        if (this.length != other.length)
            return false;
        if (this.type != other.type)
            return false;
        return true;
    }

    enum Type {
        END_OF_TRACK(47),
        TEXT(1),
        LYRIC_TEXT(5),
        MARKER_TEXT(6),
        TEMPO(81),
        TIME_SIGNATURE(88);

        final byte value;

        Type(int value) {
            this.value = (byte) value;
        }
    }
}

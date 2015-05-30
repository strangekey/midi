/**
 * Copyright 2010 Leeor Engel.
 *
 * Represents a midi channel voice message.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import java.util.Arrays;

public class ChannelMessage implements MidiMessage {

    private byte[] data;

    private ChannelMessage(Type type, int channel, int dataOne, int dataTwo) {
        this(getStatusWithChannel(type, channel), dataOne, dataTwo);
    }

    private ChannelMessage(Type type, int channel, int dataOne) {
        this(getStatusWithChannel(type, channel), dataOne);
    }

    private ChannelMessage(int dataOne, int dataTwo, int dataThree) {
        data = new byte[3];
        data[0] = truncateToLeastSignificantByte(dataOne);
        data[1] = truncateToLeastSignificantByte(dataTwo);
        data[2] = truncateToLeastSignificantByte(dataThree);
    }

    private ChannelMessage(int dataOne, int dataTwo) {
        data = new byte[2];
        data[0] = truncateToLeastSignificantByte(dataOne);
        data[1] = truncateToLeastSignificantByte(dataTwo);
    }

    public static ChannelMessage noteOn(int channel, int noteNumber, int velocity) throws MidiException {
        checkNoteMessage(channel, noteNumber, velocity);
        return new ChannelMessage(Type.NOTE_ON, channel, noteNumber, velocity);
    }

    public static ChannelMessage noteOff(int channel, int noteNumber, int velocity) throws MidiException {
        checkNoteMessage(channel, noteNumber, velocity);
        return new ChannelMessage(Type.NOTE_OFF, channel, noteNumber, velocity);
    }

    public static ChannelMessage polyphonicKeyPressure(int channel, int noteNumber, int pressure) throws MidiException {
        checkNoteMessage(channel, noteNumber, pressure);
        return new ChannelMessage(Type.POLYPHONIC_KEY_PRESSURE, channel, noteNumber, pressure);
    }

    public static ChannelMessage programChange(int channel, int instrumentNumber) throws MidiException {
        checkChannel(channel);
        checkInstrumentNumber(instrumentNumber);
        return new ChannelMessage(Type.PROGRAM_CHANGE, channel, instrumentNumber);
    }

    private static int getStatusWithChannel(Type type, int channel) {
        return type.baseValue & 0xF0 | channel & 0x0F;
    }

    private static void checkNoteMessage(int channel, int noteNumber, int velocity) throws MidiException {
        checkChannel(channel);
        checkNoteNumber(noteNumber);
        checkVelocity(velocity);
    }

    private static void checkChannel(int channel) throws MidiException {
        if ((channel & 0xFFFFFFF0) != 0) {
            throw new MidiException("Invalid channel");
        }
    }

    private static void checkNoteNumber(int noteNumber) throws MidiException {
        if (dataOutOfRange(noteNumber)) {
            throw new MidiException("Invalid note number");
        }
    }

    private static void checkInstrumentNumber(int instrumentNumber) throws MidiException {
        if (dataOutOfRange(instrumentNumber)) {
            throw new MidiException("Invalid instrument number");
        }
    }

    private static void checkVelocity(int velocity) throws MidiException {
        if (dataOutOfRange(velocity)) {
            throw new MidiException("Invalid velocity");
        }
    }

    private static boolean dataOutOfRange(int noteNumber) {
        return noteNumber < 0 || noteNumber > 127;
    }

    private static byte truncateToLeastSignificantByte(int data) {
        return (byte) (data & 0xFF);
    }

    public byte getStatus() {
        return data[0];
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public byte[] toBytes() {
        return data;
    }

    @Override
    public int getLength() {
        return data.length;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.data);
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
        ChannelMessage other = (ChannelMessage) obj;
        if (!Arrays.equals(this.data, other.data))
            return false;
        return true;
    }

    enum Type {
        NOTE_OFF(128),
        NOTE_ON(144),
        POLYPHONIC_KEY_PRESSURE(160),
        CONTROL_CHANGE(176),
        PROGRAM_CHANGE(192),
        CHANNEL_PRESSURE(208),
        PITCH_WHEEL_CHANGE(224);

        final int baseValue;

        private Type(int value) {
            this.baseValue = value;
        }
    }
}

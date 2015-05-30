/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ChannelMessageTest {

    @Test
    public void testNoteOnFirstChannel() throws MidiException {
        ChannelMessage noteOn = ChannelMessage.noteOn(0, 60, 50);

        byte[] expectedData = new byte[]{(byte) (ChannelMessage.Type.NOTE_ON.baseValue & 0xFF), 60, 50};
        assertNotNull(noteOn);
        assertTrue(Arrays.equals(expectedData, noteOn.getData()));
    }

    @Test
    public void testNoteOnHigherChannel() throws MidiException {
        ChannelMessage noteOn = ChannelMessage.noteOn(5, 60, 50);

        byte[] expectedData = new byte[]{(byte) (ChannelMessage.Type.NOTE_ON.baseValue + 5), 60, 50};
        assertNotNull(noteOn);
        assertTrue(Arrays.equals(expectedData, noteOn.getData()));
    }

    @Test(expected = MidiException.class)
    public void testNoteOnInvalidChannelTooHigh() throws MidiException {
        ChannelMessage.noteOn(16, 60, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOnInvalidChannelTooLow() throws MidiException {
        ChannelMessage.noteOn(-1, 60, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOnInvalidNoteNumberTooHigh() throws MidiException {
        ChannelMessage.noteOn(0, 128, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOnInvalidNoteNumberTooLow() throws MidiException {
        ChannelMessage.noteOn(0, -1, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOnInvalidVelocityTooHigh() throws MidiException {
        ChannelMessage.noteOn(0, 127, 128);
    }

    @Test(expected = MidiException.class)
    public void testNoteOnInvalidVelocityTooLow() throws MidiException {
        ChannelMessage.noteOn(0, 127, -1);
    }

    @Test
    public void testNoteOffFirstChannel() throws MidiException {
        ChannelMessage noteOff = ChannelMessage.noteOff(0, 60, 50);

        byte[] expectedData = new byte[]{(byte) ChannelMessage.Type.NOTE_OFF.baseValue, 60, 50};
        assertNotNull(noteOff);
        assertTrue(Arrays.equals(expectedData, noteOff.getData()));
    }

    @Test
    public void testNoteOffHigherChannel() throws MidiException {
        ChannelMessage noteOff = ChannelMessage.noteOff(5, 60, 50);

        byte[] expectedData = new byte[]{(byte) (ChannelMessage.Type.NOTE_OFF.baseValue + 5), 60, 50};
        assertNotNull(noteOff);
        assertTrue(Arrays.equals(expectedData, noteOff.getData()));
    }

    @Test(expected = MidiException.class)
    public void testNoteOffInvalidChannelTooHigh() throws MidiException {
        ChannelMessage.noteOff(16, 60, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOffInvalidChannelTooLow() throws MidiException {
        ChannelMessage.noteOff(-1, 60, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOffInvalidNoteNumberTooHigh() throws MidiException {
        ChannelMessage.noteOff(0, 128, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOffInvalidNoteNumberTooLow() throws MidiException {
        ChannelMessage.noteOff(0, -1, 50);
    }

    @Test(expected = MidiException.class)
    public void testNoteOffInvalidVelocityTooHigh() throws MidiException {
        ChannelMessage.noteOff(0, 127, 128);
    }

    @Test(expected = MidiException.class)
    public void testNoteOffInvalidVelocityTooLow() throws MidiException {
        ChannelMessage.noteOff(0, 127, -1);
    }

    @Test
    public void testPolyphonicKeyPressureFirstChannel() throws MidiException {
        ChannelMessage polyPressure = ChannelMessage.polyphonicKeyPressure(0, 60, 50);

        byte[] expectedData = new byte[]{(byte) ChannelMessage.Type.POLYPHONIC_KEY_PRESSURE.baseValue, 60, 50};
        assertNotNull(polyPressure);
        assertTrue(Arrays.equals(expectedData, polyPressure.getData()));
    }

    @Test
    public void testPolyphonicKeyPressureHigherChannel() throws MidiException {
        ChannelMessage polyPressure = ChannelMessage.polyphonicKeyPressure(5, 60, 50);

        byte[] expectedData = new byte[]{(byte) (ChannelMessage.Type.POLYPHONIC_KEY_PRESSURE.baseValue + 5), 60, 50};
        assertNotNull(polyPressure);
        assertTrue(Arrays.equals(expectedData, polyPressure.getData()));
    }

    @Test(expected = MidiException.class)
    public void testPolyphonicKeyPressureInvalidChannelTooHigh() throws MidiException {
        ChannelMessage.polyphonicKeyPressure(16, 60, 50);
    }

    @Test(expected = MidiException.class)
    public void testPolyphonicKeyPressureInvalidChannelTooLow() throws MidiException {
        ChannelMessage.polyphonicKeyPressure(-1, 60, 50);
    }

    @Test(expected = MidiException.class)
    public void testPolyphonicKeyPressureInvalidNoteNumberTooHigh() throws MidiException {
        ChannelMessage.polyphonicKeyPressure(0, 128, 50);
    }

    @Test(expected = MidiException.class)
    public void testPolyphonicKeyPressureInvalidNoteNumberTooLow() throws MidiException {
        ChannelMessage.polyphonicKeyPressure(0, -1, 50);
    }

    @Test(expected = MidiException.class)
    public void testPolyphonicKeyPressureInvalidVelocityTooHigh() throws MidiException {
        ChannelMessage.polyphonicKeyPressure(0, 127, 128);
    }

    @Test(expected = MidiException.class)
    public void testPolyphonicKeyPressureInvalidVelocityTooLow() throws MidiException {
        ChannelMessage.polyphonicKeyPressure(0, 127, -1);
    }

    // /

    @Test
    public void testProgramChangeFirstChannel() throws MidiException {
        ChannelMessage programChange = ChannelMessage.programChange(0, 60);

        byte[] expectedData = new byte[]{(byte) ChannelMessage.Type.PROGRAM_CHANGE.baseValue, 60};
        assertNotNull(programChange);
        assertTrue(Arrays.equals(expectedData, programChange.getData()));
    }

    @Test
    public void testProgramChangeHigherChannel() throws MidiException {
        ChannelMessage programChange = ChannelMessage.programChange(5, 60);

        byte[] expectedData = new byte[]{(byte) (ChannelMessage.Type.PROGRAM_CHANGE.baseValue + 5), 60};
        assertNotNull(programChange);
        assertTrue(Arrays.equals(expectedData, programChange.getData()));
    }

    @Test(expected = MidiException.class)
    public void testProgramChangeInvalidChannelTooHigh() throws MidiException {
        ChannelMessage.programChange(16, 60);
    }

    @Test(expected = MidiException.class)
    public void testProgramChangeInvalidChannelTooLow() throws MidiException {
        ChannelMessage.programChange(-1, 60);
    }

    @Test(expected = MidiException.class)
    public void testProgramChangeInvalidInstrumentNumberTooHigh() throws MidiException {
        ChannelMessage.programChange(0, 128);
    }

    @Test(expected = MidiException.class)
    public void testProgramChangeInvalidInstrumentNumberTooLow() throws MidiException {
        ChannelMessage.programChange(0, -1);
    }
}

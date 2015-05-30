/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TrackTest {

    @Test
    public void testCreateTrack() {
        Track track = new Track();
        assertTrue(track.size() == 1);
        assertEventIsEndOfTrack(track.get(0));
    }

    private void assertEventIsEndOfTrack(MidiEvent event) {
        assertEquals(event.getTimeIndex(), 0);
        assertEquals(event.getMessage().getLength(), 3);
        assertTrue(Arrays.equals(event.getMessage().getData(), new byte[]{-1, 47, 0}));
    }

    @Test
    public void testAddEvent() throws MidiException {
        Track track = new Track();
        assertTrue(track.add(new MidiEvent(ChannelMessage.programChange(0, 60), 0)));
        assertTrue(track.size() == 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullEvent() {
        Track track = new Track();
        track.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventInvalidTimeIndex() throws MidiException {
        Track track = new Track();
        track.add(new MidiEvent(ChannelMessage.programChange(0, 60), -1));
    }

    @Test
    public void testAddEvents() throws MidiException {
        Track track = new Track();
        assertTrue(track.add(new MidiEvent(ChannelMessage.programChange(0, 60), 4)));
        assertTrue(track.size() == 2);
        assertTrue(track.get(0).getTimeIndex() == 4);
        assertTrue(track.get(1).getTimeIndex() == 4);
        assertTrue(track.getTotalTime() == 4);
        assertTrue(track.add(new MidiEvent(ChannelMessage.programChange(0, 60), 7)));
        assertTrue(track.size() == 3);
        assertTrue(track.get(1).getTimeIndex() == 7);
        assertTrue(track.getTotalTime() == 7);
        assertTrue(track.get(2).getTimeIndex() == 7);

        assertTrue(track.add(new MidiEvent(ChannelMessage.noteOn(0, 60, 50), 9)));
        assertTrue(track.add(new MidiEvent(ChannelMessage.noteOn(0, 60, 50), 11)));
        assertTrue(track.add(new MidiEvent(ChannelMessage.noteOn(0, 60, 50), 50)));
    }

    @Test
    public void testGetTotalTime() throws MidiException {
        Track track = new Track();
        assertTrue(track.add(new MidiEvent(ChannelMessage.programChange(0, 60), 5)));
        assertTrue(track.add(new MidiEvent(ChannelMessage.programChange(0, 60), 6)));
        assertTrue(track.getTotalTime() == 6);
    }

    @Test
    public void testGetLengthMetaMessage() throws MidiException {
        Track track = new Track();
        ChannelMessage programChange = ChannelMessage.programChange(0, 60);
        MidiEvent programChangeEvent = new MidiEvent(programChange, 5);
        assertTrue(track.add(programChangeEvent));
        assertEquals(programChangeEvent.getMessage().getLength() + 1 + getEndOfTrackEventLength(), track.getLength());
    }

    @Test
    public void testGetLengthChannelMessage() throws MidiException {
        Track track = new Track();
        ChannelMessage noteOn = ChannelMessage.noteOn(0, 60, 50);
        MidiEvent noteOnEvent = new MidiEvent(noteOn, 5);
        MidiEvent endOfTrackEvent = track.get(0);
        assertEquals(getEndOfTrackLength(endOfTrackEvent), track.getLength());
        assertTrue(track.add(noteOnEvent));
        assertEquals(noteOnEvent.getMessage().getLength() + 1 + getEndOfTrackLength(endOfTrackEvent), track.getLength());
    }

    private int getEndOfTrackLength(MidiEvent endOfTrackEvent) {
        return endOfTrackEvent.getMessage().getLength() + 1;
    }

    @Test
    public void testGetLengthEveryKindOfMessage() throws MidiException {
        Track track = new Track();

        // channel message - 3 bytes
        ChannelMessage noteOn = ChannelMessage.noteOn(0, 60, 50);
        MidiEvent noteOnEvent = new MidiEvent(noteOn, 500);

        // channel message - 2 bytes
        ChannelMessage programChange = ChannelMessage.programChange(0, 20);
        MidiEvent programChangeEvent = new MidiEvent(programChange, 0);

        // meta message
        MetaMessage tempoSetting = MetaMessage.tempoMessage(60);
        MidiEvent tempoSettingEvent = new MidiEvent(tempoSetting, 200);

        track.add(noteOnEvent);
        track.add(programChangeEvent);
        track.add(tempoSettingEvent);

        int expectedTrackLength = 0;
        expectedTrackLength += noteOnEvent.getMessage().getLength() + 2;
        expectedTrackLength += programChangeEvent.getMessage().getLength() + 1;
        expectedTrackLength += tempoSettingEvent.getMessage().getLength() + 2;
        expectedTrackLength += getEndOfTrackEventLength();

        assertEquals(expectedTrackLength, track.getLength());
    }

    @Test
    public void testGetLengthSimultaneousEvents() throws MidiException {
        Track track = new Track();

        // channel message - 3 bytes
        ChannelMessage noteOn = ChannelMessage.noteOn(0, 60, 50);
        MidiEvent noteOnEvent = new MidiEvent(noteOn, 228);

        int numEvents = 5;

        for (int i = 0; i < numEvents; i++) {
            track.add(noteOnEvent);
        }

        int expectedTrackLength = 0;
        expectedTrackLength += noteOnEvent.getMessage().getLength() + 2;
        expectedTrackLength += (numEvents - 1) * (noteOnEvent.getMessage().getLength() + 1);
        expectedTrackLength += getEndOfTrackEventLength();

        assertEquals(expectedTrackLength, track.getLength());
    }

    @Test
    public void testTrackInsertionOrderEquivalence() throws MidiException {
        Track trackOne = new Track();
        trackOne.add(new MidiEvent(ChannelMessage.programChange(0, 1), 0));
        trackOne.add(new MidiEvent(ChannelMessage.noteOn(0, 67, 80), 0));
        trackOne.add(new MidiEvent(ChannelMessage.noteOff(0, 67, 80), 192));
        trackOne.add(new MidiEvent(ChannelMessage.noteOn(0, 78, 80), 169));
        trackOne.add(new MidiEvent(ChannelMessage.noteOff(0, 78, 80), 361));

        Track trackTwo = new Track();
        trackTwo.add(new MidiEvent(ChannelMessage.programChange(0, 1), 0));
        trackTwo.add(new MidiEvent(ChannelMessage.noteOn(0, 67, 80), 0));
        trackTwo.add(new MidiEvent(ChannelMessage.noteOn(0, 78, 80), 169));
        trackTwo.add(new MidiEvent(ChannelMessage.noteOff(0, 67, 80), 192));
        trackTwo.add(new MidiEvent(ChannelMessage.noteOff(0, 78, 80), 361));

        assertEquals(trackOne.getLength(), trackTwo.getLength());

        for (int i = 0; i < trackOne.getNumEvents(); i++) {
            assertEquals(trackOne.get(i), trackTwo.get(i));
        }

    }

    private int getEndOfTrackEventLength() {
        return 4;
    }
}

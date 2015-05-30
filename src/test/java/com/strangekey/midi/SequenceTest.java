/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SequenceTest {

    @Test
    public void testGetTotalTimeSingleTrack() throws MidiException {
        Sequence seq = new Sequence(192);
        Track track = seq.createTrack();

        track.add(new MidiEvent(ChannelMessage.programChange(0, 60), 50));
        assertTrue(seq.getTimeIndexLength() == 50);
    }

    @Test
    public void testGetTotalTimeMultipleTracks() throws MidiException {
        Sequence seq = new Sequence(192);
        Track trackOne = seq.createTrack();
        Track trackTwo = seq.createTrack();

        trackOne.add(new MidiEvent(ChannelMessage.programChange(0, 60), 20));
        trackTwo.add(new MidiEvent(ChannelMessage.programChange(0, 60), 50));

        assertTrue(seq.getTimeIndexLength() == 50);
    }
}

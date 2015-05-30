/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import com.strangekey.midi.MetaMessage.Type;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MetaMessageTest {

    @Test
    public void testTempoMessage() throws MidiException {
        MetaMessage setTempo = MetaMessage.tempoMessage(60);

        assertNotNull(setTempo);
        assertTrue(setTempo.getType() == Type.TEMPO.value);
        assertTrue(toInt(setTempo.getData()) == 60000000 / 60);
    }

    @Test
    public void testTempoLength() throws MidiException {
        MetaMessage setTempo = MetaMessage.tempoMessage(60);
        assertEquals(3 + 1 + 1 + 1, setTempo.getLength());
    }

    @Test
    public void testTempoToBytes() throws MidiException {
        MetaMessage setTempo = MetaMessage.tempoMessage(60);
        byte[] actual = setTempo.toBytes();
        byte[] varFieldBytes = MidiFileUtils.getVariableLengthFieldBytes(3);
        byte[] expected = new byte[]{MetaMessage.META_STATUS_BYTE, Type.TEMPO.value, varFieldBytes[0], setTempo.getData()[0], setTempo.getData()[1], setTempo.getData()[2]};
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testTimeSignatureMessage() throws MidiException {
        MetaMessage timeSignatureMessage = MetaMessage.timeSignatureMessage(4, 4);

        assertNotNull(timeSignatureMessage);
        assertTrue(timeSignatureMessage.getType() == Type.TIME_SIGNATURE.value);
        byte[] expected = new byte[]{MetaMessage.META_STATUS_BYTE, Type.TIME_SIGNATURE.value, 4, 4, 2, 1, 8};
        byte[] actual = timeSignatureMessage.toBytes();
        assertTrue(Arrays.equals(expected, actual));

        timeSignatureMessage = MetaMessage.timeSignatureMessage(6, 8);

        assertNotNull(timeSignatureMessage);
        assertTrue(timeSignatureMessage.getType() == Type.TIME_SIGNATURE.value);
        expected = new byte[]{MetaMessage.META_STATUS_BYTE, Type.TIME_SIGNATURE.value, 4, 6, 3, 1, 8};
        actual = timeSignatureMessage.toBytes();
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testTimeSignatureLength() throws MidiException {
        MetaMessage timeSignatureMessage = MetaMessage.timeSignatureMessage(4, 4);
        assertEquals(1 + 1 + 1 + 4, timeSignatureMessage.getLength());
    }

    @Test
    public void testTextMessage() throws MidiException {
        MetaMessage textMessage = MetaMessage.textMessage("test");

        assertNotNull(textMessage);
        assertTrue(textMessage.getType() == Type.TEXT.value);
        assertTrue(Arrays.equals(textMessage.getData(), "test".getBytes()));
        assertEquals(4 + 1 + 1 + MidiFileUtils.getVariableLengthFieldByteLength(4), textMessage.getLength());
    }

    @Test
    public void testTextMessageToBytes() throws MidiException {
        MetaMessage textMessage = MetaMessage.textMessage("test");
        byte[] actual = textMessage.toBytes();
        byte[] varFieldBytes = MidiFileUtils.getVariableLengthFieldBytes(4);
        byte[] expected = new byte[]{MetaMessage.META_STATUS_BYTE, Type.TEXT.value, varFieldBytes[0], textMessage.getData()[0], textMessage.getData()[1], textMessage.getData()[2],
                textMessage.getData()[3]};
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testLyricTextMessage() throws MidiException {
        MetaMessage lyricTextMessage = MetaMessage.lyricTextMessage("test");

        assertNotNull(lyricTextMessage);
        assertTrue(lyricTextMessage.getType() == Type.LYRIC_TEXT.value);
        assertTrue(Arrays.equals(lyricTextMessage.getData(), "test".getBytes()));
        assertEquals(4 + 1 + 1 + MidiFileUtils.getVariableLengthFieldByteLength(4), lyricTextMessage.getLength());
    }

    @Test
    public void testLyricTextMessageToBytes() throws MidiException {
        MetaMessage lyricTextMessage = MetaMessage.lyricTextMessage("test");
        byte[] actual = lyricTextMessage.toBytes();
        byte[] varFieldBytes = MidiFileUtils.getVariableLengthFieldBytes(4);
        byte[] expected = new byte[]{MetaMessage.META_STATUS_BYTE, Type.LYRIC_TEXT.value, varFieldBytes[0], lyricTextMessage.getData()[0], lyricTextMessage.getData()[1],
                lyricTextMessage.getData()[2], lyricTextMessage.getData()[3]};
        assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void testMarkerTextMessage() throws MidiException {
        MetaMessage markerTextMessage = MetaMessage.markerTextMessage("test");

        assertNotNull(markerTextMessage);
        assertTrue(markerTextMessage.getType() == Type.MARKER_TEXT.value);
        assertTrue(Arrays.equals(markerTextMessage.getData(), "test".getBytes()));
        assertEquals(4 + 1 + 1 + MidiFileUtils.getVariableLengthFieldByteLength(4), markerTextMessage.getLength());
    }

    @Test
    public void testMarkerTextMessageToBytes() throws MidiException {
        MetaMessage markerTextMessage = MetaMessage.markerTextMessage("test");
        byte[] actual = markerTextMessage.toBytes();
        byte[] varFieldBytes = MidiFileUtils.getVariableLengthFieldBytes(4);
        byte[] expected = new byte[]{MetaMessage.META_STATUS_BYTE, Type.MARKER_TEXT.value, varFieldBytes[0], markerTextMessage.getData()[0], markerTextMessage.getData()[1],
                markerTextMessage.getData()[2], markerTextMessage.getData()[3]};
        assertTrue(Arrays.equals(expected, actual));
    }

    private static int toInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (value << 8) + (bytes[i] & 0xff);
        }
        return value;
    }

    @Test
    public void testGetDenominator() {
        assertEquals(1, MetaMessage.getDenominator(2));
        assertEquals(2, MetaMessage.getDenominator(4));
        assertEquals(3, MetaMessage.getDenominator(8));
        assertEquals(4, MetaMessage.getDenominator(16));
        assertEquals(5, MetaMessage.getDenominator(32));
    }
}

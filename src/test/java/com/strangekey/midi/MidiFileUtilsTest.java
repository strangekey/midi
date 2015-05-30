/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class MidiFileUtilsTest {

    @Test
    public void testGetVariableLengthFieldBytesOneByte() {
        byte[] min = MidiFileUtils.getVariableLengthFieldBytes(0x00);
        byte[] expectedMin = new byte[]{(byte) 0x00};
        assertTrue(Arrays.equals(expectedMin, min));

        byte[] middle = MidiFileUtils.getVariableLengthFieldBytes(0x40);
        byte[] expectedMiddle = new byte[]{(byte) 0x40};
        assertTrue(Arrays.equals(expectedMiddle, middle));

        byte[] max = MidiFileUtils.getVariableLengthFieldBytes(0x7F);
        byte[] expectedMax = new byte[]{(byte) 0x7F};
        assertTrue(Arrays.equals(expectedMax, max));
    }

    @Test
    public void testGetVariableLengthFieldBytesTwoBytes() {
        byte[] min = MidiFileUtils.getVariableLengthFieldBytes(0x80);
        byte[] expectedMin = new byte[]{(byte) 0x81, (byte) 0x00};
        assertTrue(Arrays.equals(expectedMin, min));

        byte[] middle = MidiFileUtils.getVariableLengthFieldBytes(0x2000);
        byte[] expectedMiddle = new byte[]{(byte) 0xC0, (byte) 0x00};
        assertTrue(Arrays.equals(expectedMiddle, middle));

        byte[] max = MidiFileUtils.getVariableLengthFieldBytes(0x3FFF);
        byte[] expectedMax = new byte[]{(byte) 0xFF, (byte) 0x7F};
        assertTrue(Arrays.equals(expectedMax, max));
    }

    @Test
    public void testGetVariableLengthFieldBytesThreeBytes() {
        byte[] min = MidiFileUtils.getVariableLengthFieldBytes(0x4000);
        byte[] expectedMin = new byte[]{(byte) 0x81, (byte) 0x80, (byte) 0x00};
        assertTrue(Arrays.equals(expectedMin, min));

        byte[] middle = MidiFileUtils.getVariableLengthFieldBytes(0x100000);
        byte[] expectedMiddle = new byte[]{(byte) 0xC0, (byte) 0x80, (byte) 0x00};
        assertTrue(Arrays.equals(expectedMiddle, middle));

        byte[] max = MidiFileUtils.getVariableLengthFieldBytes(0x1FFFFF);
        byte[] expectedMax = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0x7F};
        assertTrue(Arrays.equals(expectedMax, max));
    }

    @Test
    public void testGetVariableLengthFieldBytesFourBytes() {
        byte[] min = MidiFileUtils.getVariableLengthFieldBytes(0x200000);
        byte[] expectedMin = new byte[]{(byte) 0x81, (byte) 0x80, (byte) 0x80, (byte) 0x00};
        assertTrue(Arrays.equals(expectedMin, min));

        byte[] middle = MidiFileUtils.getVariableLengthFieldBytes(0x8000000);
        byte[] expectedMiddle = new byte[]{(byte) 0xC0, (byte) 0x80, (byte) 0x80, (byte) 0x00};
        assertTrue(Arrays.equals(expectedMiddle, middle));

        byte[] max = MidiFileUtils.getVariableLengthFieldBytes(0xFFFFFFF);
        byte[] expectedMax = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x7F};
        assertTrue(Arrays.equals(expectedMax, max));
    }

    @Test
    public void testGetVariableLengthFieldByteLengthOneByte() {
        int min = MidiFileUtils.getVariableLengthFieldByteLength(0x00);
        assertTrue(min == 1);

        int middle = MidiFileUtils.getVariableLengthFieldByteLength(0x40);
        assertTrue(middle == 1);

        int max = MidiFileUtils.getVariableLengthFieldByteLength(0x7F);
        assertTrue(max == 1);
    }

    @Test
    public void testGetVariableLengthFieldByteLengthTwoBytes() {
        int min = MidiFileUtils.getVariableLengthFieldByteLength(0x80);
        assertTrue(min == 2);

        int middle = MidiFileUtils.getVariableLengthFieldByteLength(0x2000);
        assertTrue(middle == 2);

        int max = MidiFileUtils.getVariableLengthFieldByteLength(0x3FFF);
        assertTrue(max == 2);
    }

    @Test
    public void testGetVariableLengthFieldByteLengthThreeBytes() {
        int min = MidiFileUtils.getVariableLengthFieldByteLength(0x4000);
        assertTrue(min == 3);

        int middle = MidiFileUtils.getVariableLengthFieldByteLength(0x100000);
        assertTrue(middle == 3);

        int max = MidiFileUtils.getVariableLengthFieldByteLength(0x1FFFFF);
        assertTrue(max == 3);
    }

    @Test
    public void testGetVariableLengthFieldByteLengthFourBytes() {
        int min = MidiFileUtils.getVariableLengthFieldByteLength(0x200000);
        assertTrue(min == 4);

        int middle = MidiFileUtils.getVariableLengthFieldByteLength(0x8000000);
        assertTrue(middle == 4);

        int max = MidiFileUtils.getVariableLengthFieldByteLength(0xFFFFFFF);
        assertTrue(max == 4);
    }
}

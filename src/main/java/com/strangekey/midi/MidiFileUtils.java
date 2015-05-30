/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

class MidiFileUtils {

    private final static long mask = 0x7F;

    static byte[] getVariableLengthFieldBytes(long value) {
        int index = 0;
        byte[] bytes = new byte[getVariableLengthFieldByteLength(value)];
        int shift = 63;

        while ((shift > 0) && ((value & (mask << shift)) == 0)) {
            shift -= 7;
        }

        while (shift > 0) {
            bytes[index++] = (byte) (((value & (mask << shift)) >> shift) | 0x80);
            shift -= 7;
        }
        bytes[index] = (byte) (value & mask);
        return bytes;
    }

    static int getVariableLengthFieldByteLength(long value) {
        int length = 0;
        do {
            value >>= 7;
            length++;
        } while (value > 0L);
        return length;
    }
}

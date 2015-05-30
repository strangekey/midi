/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

public interface MidiMessage {

    byte[] getData();

    byte[] toBytes();

    int getLength();
}

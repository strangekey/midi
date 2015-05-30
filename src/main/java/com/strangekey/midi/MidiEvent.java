/**
 * Copyright 2010 Leeor Engel.
 *
 * @author Leeor Engel
 */
package com.strangekey.midi;

public class MidiEvent {

    private final MidiMessage message;
    private long timeIndex;

    public MidiEvent(MidiMessage message, long timeIndex) {
        this.message = message;
        this.timeIndex = timeIndex;
    }

    public MidiMessage getMessage() {
        return message;
    }

    public long getTimeIndex() {
        return timeIndex;
    }

    void setTimeIndex(long timeIndex) {
        this.timeIndex = timeIndex;
    }

    @Override
    public String toString() {
        return "MidiEvent [message=" + this.message + ", timeIndex=" + this.timeIndex + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
        result = prime * result + (int) (this.timeIndex ^ (this.timeIndex >>> 32));
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
        MidiEvent other = (MidiEvent) obj;
        if (this.message == null) {
            if (other.message != null)
                return false;
        } else if (!this.message.equals(other.message))
            return false;
        if (this.timeIndex != other.timeIndex)
            return false;
        return true;
    }
}

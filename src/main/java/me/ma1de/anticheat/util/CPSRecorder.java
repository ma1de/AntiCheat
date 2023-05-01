package me.ma1de.anticheat.util;

import lombok.Getter;
import me.ma1de.anticheat.processor.Processor;
import me.ma1de.anticheat.user.User;

@Getter
public class CPSRecorder {
    private final User user;
    private final Processor processor;
    private final long timestamp;

    private int swings;

    public CPSRecorder(User user, Processor processor) {
        this.user = user;
        this.processor = processor;
        this.timestamp = System.currentTimeMillis();
    }

    public void record(int toAdd) {
        swings += toAdd;
    }

    public boolean hasPassed(final long millis) {
        return System.currentTimeMillis() - timestamp > millis;
    }
}

package me.ma1de.anticheat.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ma1de.anticheat.check.Check;

import java.util.concurrent.TimeUnit;

@Getter @AllArgsConstructor
public class UserViolation {
    private User user;
    private Check check;
    private String info;
    private long timestamp;

    public boolean isExpired() {
        return System.currentTimeMillis() - timestamp > TimeUnit.SECONDS.toMillis(45);
    }
}

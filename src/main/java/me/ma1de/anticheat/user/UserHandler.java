package me.ma1de.anticheat.user;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class UserHandler {
    private final List<User> users = Lists.newArrayList();

    public User getUser(final UUID uuid) {
        return this.users
                .stream()
                .filter(user -> user.getUuid().equals(uuid))
                .findAny()
                .orElse(null);
    }

    public void addUser(final UUID uuid) {
        final User user = new User(uuid);
        user.loadChecks();

        this.users.add(user);
    }

    public void removeUser(final UUID uuid) {
        this.users.remove(this.getUser(uuid));
    }
}

package me.ma1de.anticheat.command;

import org.bukkit.command.CommandSender;

public interface ACCommand {
    void handle(final CommandSender sender, final String[] args);

    default String getName() {
        return this.getClass().getAnnotation(CommandInfo.class).name();
    }

    default String getPermission() {
        return this.getClass().getAnnotation(CommandInfo.class).permission();
    }

    default boolean isPlayerOnly() {
        return this.getClass().getAnnotation(CommandInfo.class).playerOnly();
    }
}

package me.ma1de.anticheat.command;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor {
    private final List<ACCommand> commands = Lists.newArrayList();

    @SneakyThrows
    public CommandHandler() {
        for (final Class<? extends ACCommand> clazz : new Reflections("me.ma1de.anticheat.command.impl").getSubTypesOf(ACCommand.class)) {
            if (clazz.getAnnotation(CommandInfo.class) == null) {
                continue;
            }

            this.commands.add(clazz.newInstance());
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (Arrays.asList(strings).isEmpty()) {
            commandSender.sendMessage(ChatColor.RED + "Available sub-commands: alerts, debug, exempt, summary");
            return true;
        }

        for (final ACCommand cmd : this.commands) {
            if (!strings[0].equals(cmd.getName())) {
                continue;
            }

            if (!(commandSender instanceof Player) && cmd.isPlayerOnly()) {
                commandSender.sendMessage(ChatColor.RED + "This command is player-only!");
                return true;
            }

            if (!commandSender.hasPermission(cmd.getPermission())) {
                commandSender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }

            cmd.handle(commandSender, strings);
            return true;
        }

        commandSender.sendMessage(ChatColor.RED + "Unknown sub-command: " + strings[0]);

        return true;
    }
}

package me.ma1de.anticheat.command.impl;

import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.command.ACCommand;
import me.ma1de.anticheat.command.CommandInfo;
import me.ma1de.anticheat.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "exempt", permission = "cpsdebugger.exempt")
public class ExemptCommand implements ACCommand {
    @Override
    public void handle(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.RED + "WIP");

        /**if (args[1] == null) {
            sender.sendMessage(ChatColor.RED + "Usage: /cpsdetector exempt <player>");
            return;
        }

        final String name = args[1];

        if (Bukkit.getPlayer(name) == null) {
            sender.sendMessage(ChatColor.RED + "Unable to find a player with name " + name);
            return;
        }

        final User user = AntiCheat.getInstance().getUserHandler().getUser(Bukkit.getPlayer(name).getUniqueId());

        if (user == null) {
            throw new RuntimeException("user is null");
        }

        user.setExempt(!user.isExempt());

        AntiCheat.getInstance().getUserHandler().update(Bukkit.getPlayer(name).getUniqueId(), user);

        if (!user.isExempt()) {
            sender.sendMessage(ChatColor.RED + name + " is no longer exempted.");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + name + " is exempt.");**/
    }
}

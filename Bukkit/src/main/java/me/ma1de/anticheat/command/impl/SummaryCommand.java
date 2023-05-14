package me.ma1de.anticheat.command.impl;

import io.github.retrooper.packetevents.PacketEvents;
import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.command.ACCommand;
import me.ma1de.anticheat.command.CommandInfo;
import me.ma1de.anticheat.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "summary", permission = "cpsdetector.summary")
public class SummaryCommand implements ACCommand {
    @Override
    public void handle(CommandSender sender, String[] args) {
        if (args[1] == null) {
            sender.sendMessage(ChatColor.RED + "Usage: /cpsdetector summary <playerName>");
            return;
        }

        final String name = args[1];

        if (Bukkit.getPlayer(name) == null) {
            sender.sendMessage(ChatColor.RED + "There is no such player as " + name + ".");
            return;
        }

        final User user = AntiCheat.getInstance().getUserHandler().getUser(Bukkit.getPlayer(name).getUniqueId());

        if (user == null) return;

        sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Summary of a Player " + name);
        sender.sendMessage(ChatColor.YELLOW + "Current Click Speed: " + ChatColor.WHITE + user.getCombatProcessor().getCps());
        sender.sendMessage(ChatColor.YELLOW + "Average Click Speed: " + ChatColor.WHITE + user.getCombatProcessor().getAverageCps());
        sender.sendMessage(ChatColor.YELLOW + "Lowest Click Speed: " + ChatColor.WHITE + user.getCombatProcessor().getLowestCps());
        sender.sendMessage(ChatColor.YELLOW + "Highest Click Speed: " + ChatColor.WHITE + user.getCombatProcessor().getHighestCps());
        sender.sendMessage(ChatColor.YELLOW + "Ping: " + ChatColor.WHITE + PacketEvents.get().getPlayerUtils().getPing(Bukkit.getPlayer(user.getUuid())) + "ms");
    }
}

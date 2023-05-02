package me.ma1de.anticheat.command.impl;

import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.command.ACCommand;
import me.ma1de.anticheat.command.CommandInfo;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "alerts", permission = "cpsdetector.alerts")
public class AlertsCommand implements ACCommand {
    @Override
    public void handle(CommandSender sender, String[] args) {
        final Player player = (Player) sender;

        if (AntiCheat.getInstance().getAlertsEnabled().contains(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You have disabled CPS Detector alerts.");

            AntiCheat.getInstance().getAlertsEnabled().remove(player.getUniqueId());
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "You have enabled your CPS Detector alerts.");

        AntiCheat.getInstance().getAlertsEnabled().add(player.getUniqueId());
    }
}

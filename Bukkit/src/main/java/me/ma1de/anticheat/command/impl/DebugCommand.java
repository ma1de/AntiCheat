package me.ma1de.anticheat.command.impl;

import me.ma1de.anticheat.AntiCheat;
import me.ma1de.anticheat.command.ACCommand;
import me.ma1de.anticheat.command.CommandInfo;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "debug", permission = "cpsdetector.debug")
public class DebugCommand implements ACCommand {
    @Override
    public void handle(CommandSender sender, String[] args) {
        final Player player = (Player) sender;

        if (AntiCheat.getInstance().getDebugEnabled().contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have disabled CPS Detector debug.");

            AntiCheat.getInstance().getDebugEnabled().remove(player.getUniqueId());
            return;
        }

        player.sendMessage(ChatColor.GREEN + "You have enabled CPS Detector debug.");

        AntiCheat.getInstance().getDebugEnabled().add(player.getUniqueId());
    }
}

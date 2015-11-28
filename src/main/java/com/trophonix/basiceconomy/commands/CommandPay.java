package main.java.com.trophonix.basiceconomy.commands;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import main.java.com.trophonix.basiceconomy.Main;
import main.java.com.trophonix.basiceconomy.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandPay extends Utils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You can't execute this command, silly non-player!");
		}
		
		Player player = (Player) sender;

		if (!(Main.config.contains("worlds." + player.getWorld().getName()))) {
			Main.config.set("worlds." + player.getWorld().getName(), true);
		}
		
		if (!Main.config.getBoolean("worlds." + player.getWorld().getName())) {
			player.sendMessage("Unknown command. Type ''/help'' for help.");
			return true;
		}
		
		if (!(player.hasPermission("basiceconomy.commands.pay") || player.isOp())) {
			player.sendMessage(ChatColor.RED + "Sorry, you don't have permission to do that!");
			return true;
		}
		
		if (args.length == 2) {
			if (isDouble(args[1])) {
				double amt = Double.parseDouble(args[1]);
				if (playerIsOnline(args[0])) {
					if (amt <= 0) {
						player.sendMessage(ChatColor.RED + "Invalid amount!");
						return true;
					}
					if (Main.config.getDouble("balances." + player.getUniqueId()) >= amt) {						
						Main.config.set("balances." + player.getUniqueId(), Main.config.getDouble("balances." + player.getUniqueId()) - amt);
						addTransaction(player.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId(), -amt);
						Main.config.set("balances." + Bukkit.getOfflinePlayer(args[0]).getUniqueId(), Main.config.getDouble("balances." + args[0]) + amt);
						addTransaction(player.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId(), amt);
						Bukkit.getPlayer(args[0]).sendMessage(ChatColor.DARK_GREEN + player.getName() + " sent you " + ChatColor.GREEN + "$" + amt);
						player.sendMessage(ChatColor.DARK_GREEN + "You sent " + args[0] + ChatColor.GREEN + " $" + amt);
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "You can't afford to do that!");
						return true;
					}
				} else {
					if (playerExists(args[0])) {
						Main.config.set("balances." + player.getUniqueId(), Main.config.getDouble("balances." + player.getUniqueId()) - amt);
						addTransaction(player.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId(), -amt);
						Main.config.set("balances." + Bukkit.getOfflinePlayer(args[0]).getUniqueId(), Main.config.getDouble("balances." + args[0]) + amt);
						addTransaction(player.getUniqueId(), Bukkit.getOfflinePlayer(args[0]).getUniqueId(), amt);
						player.sendMessage(ChatColor.DARK_GREEN + "You sent " + args[0] + ChatColor.GREEN + " $" + amt);
					} else {
						player.sendMessage(ChatColor.RED + "That player does not exist!");
					}
					return true;
				}
			} else {
				player.sendMessage(ChatColor.RED + "That's not a valid number!");
				return true;
			}
		}

		player.sendMessage(ChatColor.RED + "Invalid arguments. Usage: /pay player_name amount");
		return true;
	}

}

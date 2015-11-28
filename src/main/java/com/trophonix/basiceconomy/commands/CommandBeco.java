package main.java.com.trophonix.basiceconomy.commands;

import java.util.Arrays;
import java.util.List;

import main.java.com.trophonix.basiceconomy.Main;
import main.java.com.trophonix.basiceconomy.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBeco extends Utils implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You can't execute this command, silly non-player!");
			return true;
		}
		Player player = (Player) sender;
		
		if (!(player.hasPermission("basiceconomy.commands.beco") || player.isOp())) {
			player.sendMessage(ChatColor.RED + "Sorry, you don't have permission to do that!");
			return true;
		}
		
		if (args.length > 0) {
			switch (args[0]) {
			case "help":
				String[] h = new String[help.size()];
				for (int i = 0; i < help.size(); i++) {
					String line;
					if (help.get(i).contains(":")) {
						String[] split = help.get(i).split(":");
						line = ChatColor.BLUE + split[0] + ChatColor.YELLOW + split[1];
					} else {
						line = ChatColor.DARK_GREEN + help.get(i);
					}
					h[i] = line;
				}
				player.sendMessage(h);
				return true;
			case "set":
				if (args.length < 3) {
					player.sendMessage(ChatColor.RED + "Not enough arguments! Usage: /beco set <playername> <newamount>");
					return true;
				}

				if (isDouble(args[2])) {
					if (playerIsOnline(args[1])) {
						if (!(Double.parseDouble(args[2]) < 0)) {
							Main.set("balances." + Bukkit.getOfflinePlayer(args[1]).getUniqueId(), Double.parseDouble(args[2]));
							Bukkit.getPlayer(args[1]).sendMessage(ChatColor.DARK_GREEN + "Your balance has been set to " + ChatColor.GREEN + "$" + args[2]);
							player.sendMessage(ChatColor.DARK_GREEN + "You set " + args[1] + "'s balance to " + ChatColor.GREEN + "$" + args[2]);
						} else {
							player.sendMessage(ChatColor.RED + "Invalid amount!");
						}
					} else {
						if (playerExists(args[1])) {
							Main.set("balances." + Bukkit.getOfflinePlayer(args[1]).getUniqueId(), Double.parseDouble(args[2]));
							player.sendMessage(ChatColor.DARK_GREEN + "You set " + args[1] + "'s balance to " + ChatColor.GREEN + "$" + args[2]);
						} else {
							player.sendMessage(ChatColor.RED + "That player does not exist!");
						}
					}
				} else {
					player.sendMessage(ChatColor.RED + "That's not a valid number!");
					return true;
				}

				return true;
			case "world":
				if (args.length < 2) {
					player.sendMessage(ChatColor.RED + "Not enough arguments! Usage: /beco world <enable/disable>");
					return true;
				}
				
				if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("yes")) {
					Main.config.set("worlds." + player.getWorld().getName(), true);
					player.sendMessage(ChatColor.BLUE + "Basic Economy has been enabled in this world!");
				} else if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("no")) {
					Main.config.set("worlds." + player.getWorld().getName(), false);
					player.sendMessage(ChatColor.BLUE + "Basic Economy has been disabled in this world!");
				} else {
					player.sendMessage(ChatColor.RED + "Invalid arguments. Usage: /beco world <enable/disable>");
				}
				
				return true;
			}
		}

		player.sendMessage(ChatColor.RED + "Invalid arguments. Try /beco help");
		return true;
	}
	
	public List<String> help = Arrays.asList(
		"BasicEconomy help - <> = Required, [] = Optional",
		"/beco help: Display this help message",
		"/beco set <player name> <amount>: Set a player's balance",
		"/beco world <enable/disable>: Toggle the plugin in the world",
		"/pay <player name> <amount>: Pay a player money",
		"/bal [player name]: Check a player's balance"
	);

}

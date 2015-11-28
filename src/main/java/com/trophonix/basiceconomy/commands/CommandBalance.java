package main.java.com.trophonix.basiceconomy.commands;

import java.text.DecimalFormat;

import main.java.com.trophonix.basiceconomy.Main;
import main.java.com.trophonix.basiceconomy.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBalance extends Utils implements CommandExecutor {

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
		
		if (!(player.hasPermission("basiceconomy.commands.balance") || player.isOp())) {
			player.sendMessage(ChatColor.RED + "Sorry, you don't have permission to do that!");
			return true;
		}

		if (args.length == 0) {
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			double balance = Double.parseDouble(df.format(Main.config.getDouble("balances." + player.getUniqueId())).replace(",", ""));
			player.sendMessage(ChatColor.DARK_GREEN + "Your current balance is: " + ChatColor.GREEN + "$" + balance);
			return true;
		} else {
			if (playerExists(args[0])) {
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				double balance = Double.parseDouble(df.format(Main.config.getDouble("balances." + Bukkit.getOfflinePlayer(args[0]).getUniqueId())).replace(",", ""));
				player.sendMessage(ChatColor.DARK_GREEN + args[0] + "'s current balance is: " + ChatColor.GREEN + "$" + balance);
			} else {
				player.sendMessage(ChatColor.RED + "That player does not exist!");
			}
		}
		
		return true;
	}

}

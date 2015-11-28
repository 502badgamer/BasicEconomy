package main.java.com.trophonix.basiceconomy;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Utils {

	public static boolean playerIsOnline(String name) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Player sent/received money to/from another player
	 * @param p
	 * @param r
	 * @param amt
	 */
	public void addTransaction(UUID p, UUID r, double amt) {
		FileConfiguration config;
		File file;
		if (amt < 0) {
			file = new File("plugins" + File.separator + "BasicEconomy" + File.separator + "transactions" + File.separator + p.toString() + ".yml");
			config = YamlConfiguration.loadConfiguration(file);
			config.set("transactions." + next(config), Bukkit.getOfflinePlayer(p).getName() + " (" + p + ") sent " + -amt + " to " + Bukkit.getOfflinePlayer(r).getName() + " (" + r + ")");
		} else {
			file = new File("plugins" + File.separator + "BasicEconomy" + File.separator + "transactions" + File.separator + r.toString() + ".yml");
			config = YamlConfiguration.loadConfiguration(file);
			config.set("transactions." + next(config), Bukkit.getOfflinePlayer(r).getName() + " (" + r + ") received " + amt + " from " + Bukkit.getOfflinePlayer(p).getName() + " (" + p + ")");
		}
		try {
			config.save(file);
		} catch (IOException e) {}
	}
	
	/**
	 * Player bought item from shop
	 * @param p
	 * @param m
	 * @param amt
	 */
	public void addTransaction(UUID p, Material m, double price, int amt) {
		FileConfiguration config;
		File file;
		file = new File("plugins" + File.separator + "BasicEconomy" + File.separator + "transactions" + File.separator + p.toString() + ".yml");
		config = YamlConfiguration.loadConfiguration(file);
		if (price < 0) {
			config.set("transactions." + next(config), Bukkit.getOfflinePlayer(p).getName() + " (" + p + ") bought " + amt + " " + m.toString() + " for " + "$" + -price);
		} else {
			config.set("transactions." + next(config), Bukkit.getOfflinePlayer(p).getName() + " (" + p + ") sold " + amt + " " + m.toString() + " for " + "$" + price);
		}
		try {
			config.save(file);
		} catch (IOException e) {}
	}
	
	public int next(FileConfiguration config) {
		int n;
		for (n = 0; n < 99999999; n++) {
			if (!config.contains("transactions." + n)) {
				break;
			}
		}
		return n;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	public static boolean playerExists(String name) {
		if (Main.config.contains("balances." + Bukkit.getOfflinePlayer(name).getUniqueId())) {
			return true;
		}
		return false;
	}

}

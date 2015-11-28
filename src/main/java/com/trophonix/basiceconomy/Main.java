package main.java.com.trophonix.basiceconomy;

import main.java.com.trophonix.basiceconomy.commands.CommandBalance;
import main.java.com.trophonix.basiceconomy.commands.CommandBeco;
import main.java.com.trophonix.basiceconomy.commands.CommandPay;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

	public static FileConfiguration config;

	public void onEnable() {
		System.out.println("[BasicEconomy] Initializing...");
		init();
		initCommands();
		System.out.println("[BasicEconomy] Successfully initialized!");
	}

	public void onDisable() {
		System.out.println("[BasicEconomy] Saving the Config...");
		saveConfig();
		System.out.println("[BasicEconomy] Successfully saved config!");
	}

	public void init() {
		config = getConfig();
		saveConfig();

		new Liztener(this);
	}

	public void initCommands() {
		getCommand("pay").setExecutor(new CommandPay());
		getCommand("balance").setExecutor(new CommandBalance());
		getCommand("beco").setExecutor(new CommandBeco());
	}

	public static void set(String path, Object newValue) {
		config.set(path, newValue);
		Bukkit.getServer().getPluginManager().getPlugin("BasicEconomy").saveConfig();
	}

}

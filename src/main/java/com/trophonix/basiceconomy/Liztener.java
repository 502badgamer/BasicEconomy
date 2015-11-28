package main.java.com.trophonix.basiceconomy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Liztener extends Utils implements Listener {
	public Liztener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!Main.config.contains("balances." + e.getPlayer().getUniqueId())) {
			Main.set("balances." + e.getPlayer().getUniqueId(), (int) 0);
		}
	}

	@EventHandler
	public void onPlaceSign(SignChangeEvent e) {
		Player player = e.getPlayer();
		
		if (!(Main.config.contains("worlds." + player.getWorld().getName()))) {
			Main.config.set("worlds." + player.getWorld().getName(), true);
		}
		
		if (!Main.config.getBoolean("worlds." + player.getWorld().getName())) {
			return;
		}

		if (e.getLine(0).equalsIgnoreCase("[buy]")) {
			if (!(player.hasPermission("basiceconomy.adminshop.place") || player.isOp())) {
				player.sendMessage(ChatColor.RED + "You don't have permission to place adminshops!");
				e.setCancelled(true);
				return;
			}
			
			if (e.getLine(1).isEmpty() || e.getLine(2).isEmpty() || e.getLine(3).isEmpty()) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.BLUE + "Invalid shopsign! Usage:");
				player.sendMessage("Line 1: [buy] or [sell]");
				player.sendMessage("Line 2: Amount");
				player.sendMessage("Line 3: Item ID");
				player.sendMessage("Line 4: Price");
				return;
			}

			if (!isInteger(e.getLine(1))) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(1) + " is not a valid number!");
				return;
			}
			int amount = Integer.parseInt(e.getLine(1));

			if (amount < 0) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + "You can't put a negative value as an amount!");
				return;
			}

			if (!isInteger(e.getLine(2))) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(2) + " is not a valid item ID.");
				return;
			}

			Material m;
			try {
				m = Material.getMaterial(Integer.parseInt(e.getLine(2)));
			} catch (Exception ex) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(2) + " is not a valid item ID.");
				return;
			}

			if (!isDouble(e.getLine(3))) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(3) + " is not a valid price.");
				return;
			}
			double price = Double.parseDouble(e.getLine(3));

			if (price < 0) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + "You can't put a negative value as a price!");
				return;
			}

			e.setLine(0, ChatColor.BLUE + "[Buy]");
			e.setLine(1, amount + "");
			e.setLine(2, m.getId() + "");
			e.setLine(3, "$" + price + "");
			player.sendMessage(ChatColor.BLUE + "Adminshop created successfully!");
		} else if (e.getLine(0).equalsIgnoreCase("[sell]")) {
			
			if (!(player.hasPermission("basiceconomy.adminshop.place") || player.isOp())) {
				player.sendMessage(ChatColor.RED + "You don't have permission to place adminshops!");
				e.setCancelled(true);
				return;
			}
			
			if (e.getLine(1).isEmpty() || e.getLine(2).isEmpty() || e.getLine(3).isEmpty()) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.BLUE + "Invalid shopsign! Usage:");
				player.sendMessage("Line 1: [buy] or [sell]");
				player.sendMessage("Line 2: Amount");
				player.sendMessage("Line 3: Item ID");
				player.sendMessage("Line 4: Price");
				return;
			}

			if (!isInteger(e.getLine(1))) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(1) + " is not a valid number!");
				return;
			}
			int amount = Integer.parseInt(e.getLine(1));

			if (amount < 0) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + "You can't put a negative value as an amount!");
				return;
			}

			if (!isInteger(e.getLine(2))) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(2) + " is not a valid item ID.");
				return;
			}

			Material m;
			try {
				m = Material.getMaterial(Integer.parseInt(e.getLine(2)));
			} catch (Exception ex) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(2) + " is not a valid item ID.");
				return;
			}

			if (!isDouble(e.getLine(3))) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + e.getLine(3) + " is not a valid price.");
				return;
			}
			double price = Double.parseDouble(e.getLine(3));

			if (price < 0) {
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.RED + "You can't put a negative value as a price!");
				return;
			}

			e.setLine(0, ChatColor.RED + "[Sell]");
			e.setLine(1, amount + "");
			e.setLine(2, m.getId() + "");
			e.setLine(3, "$" + price + "");
			player.sendMessage(ChatColor.BLUE + "Adminshop created successfully");
		}

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (!(Main.config.contains("worlds." + player.getWorld().getName()))) {
			Main.config.set("worlds." + player.getWorld().getName(), true);
		}
		
		if (!Main.config.getBoolean("worlds." + player.getWorld().getName())) {
			return;
		}
		
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign) {
			Sign sign = (Sign) e.getClickedBlock().getState();
			Player p = e.getPlayer();
			if (sign.getLine(0).contains("[Buy]")) {
				if (!(p.hasPermission("basiceconomy.adminshop.use") || p.isOp())) {
					p.sendMessage(ChatColor.RED + "You don't have permission to use adminshops!");
					e.setCancelled(true);
					return;
				}
				e.setCancelled(true);
				try {
					int amount = Integer.parseInt(sign.getLine(1));
					double price = Double.parseDouble(sign.getLine(3).replace("$", ""));
					if (p.isSneaking()) {
						price = ((price / amount)) * 64;
						amount = 64;
					}
					
					if (!(Main.config.getDouble("balances." + p.getUniqueId()) > price)) {
						p.sendMessage(ChatColor.RED + "You don't have enough money for that!");
						return;
					}

					if (!hasSpace(p.getInventory(), amount, Material.getMaterial(Integer.parseInt(sign.getLine(2))))) {
						p.sendMessage(ChatColor.RED + "Make more space in your inventory!");
						return;
					}

					Main.config.set("balances." + p.getUniqueId(), Main.config.getDouble("balances." + p.getUniqueId()) - price);
					ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(sign.getLine(2))), amount);
					p.getInventory().addItem(item);

					p.sendMessage(ChatColor.BLUE + "You bought " + amount + " " + Material.getMaterial(Integer.parseInt(sign.getLine(2))).name()
							+ " for " + price);
					addTransaction(p.getUniqueId(), Material.getMaterial(Integer.parseInt(sign.getLine(2))), -price, amount);

				} catch (Exception ex) {
					return;
				}
			} else if (sign.getLine(0).contains("[Sell]")) {
				if (!(p.hasPermission("basiceconomy.adminshop.use") || p.isOp())) {
					p.sendMessage(ChatColor.RED + "You don't have permission to use adminshops!");
					e.setCancelled(true);
					return;
				}
				e.setCancelled(true);
				int amount = Integer.parseInt(sign.getLine(1));
				double price = Double.parseDouble(sign.getLine(3).replace("$", ""));
				if (p.isSneaking()) {
					price = ((price / amount)) * 64;
					amount = 64;
				}

				if (inventoryContains(p.getInventory(), Material.getMaterial(Integer.parseInt(sign.getLine(2))), amount)) {
					inventoryRemove(p, p.getInventory(), Material.getMaterial(Integer.parseInt(sign.getLine(2))), amount);
					p.sendMessage(ChatColor.BLUE + "You sold " + amount + " " + Material.getMaterial(Integer.parseInt(sign.getLine(2))).name() + " for "
							+ price);
					Main.config.set("balances." + p.getUniqueId(), Main.config.getDouble("balances." + p.getUniqueId()) + price);
					addTransaction(p.getUniqueId(), Material.getMaterial(Integer.parseInt(sign.getLine(2))), price, amount);
				} else {
					p.sendMessage(ChatColor.RED + "You don't have enough items!");
					return;
				}
			}

			p.updateInventory();
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!(e.getBlock().getType().equals(Material.SIGN) || e.getBlock().getType().equals(Material.WALL_SIGN))) {
			return;
		}
		Sign sign = (Sign) e.getBlock().getState();
		
		if (!(sign.getLine(0).equalsIgnoreCase("[Buy]") || sign.getLine(0).equalsIgnoreCase("[Sell]"))) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (!(player.isOp() || player.hasPermission("basiceconomy.adminshop.break"))) {
			player.sendMessage(ChatColor.RED + "You don't have permission to break adminshops!");
			e.setCancelled(true);
		}
	}

	public boolean inventoryContains(Inventory inv, Material mat, int amt) {
		int amount = 0;
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null && inv.getItem(i).getType().equals(mat)) {

				for (int j = 0; j < inv.getItem(i).getAmount(); j++) {
					amount++;
					if (amount == amt) {
						return true;
					}
				} 

			}
		}
		return false;
	}

	public void inventoryRemove(Player p, Inventory inv, Material mat, int amt) {
		int amount = 0;
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null && inv.getItem(i).getType().equals(mat)) {
				int stacksize = inv.getItem(i).getAmount();
				for (int j = 0; j < stacksize; j++) {
					amount++;
					if (inv.getItem(i).getAmount() > 1) {
						inv.getItem(i).setAmount(inv.getItem(i).getAmount() - 1);
					} else {
						inv.clear(i);
					}
					if (amount == amt) {
						p.updateInventory();
						return;
					}
				}
			}

		}
	}

	public boolean hasSpace(Inventory inv, int amt, Material mat) {
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				amt -= 64;
			}
			if (amt <= 0) {
				return true;
			}
		}

		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i).getType().equals(mat)) {
				amt = amt - (64 - inv.getItem(i).getAmount());
			}

			if (amt <= 0) {
				return true;
			}
		}

		return false;
	}

}

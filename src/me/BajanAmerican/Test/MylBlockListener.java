package me.BajanAmerican.Test;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.server.ServerListPingEvent;


public class MylBlockListener implements Listener {
	public test plugin;
	int task;

	public MylBlockListener(test plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnBlockBreakEvent(BlockBreakEvent kyle) {
		Player sam = kyle.getPlayer();
		if (sam.isOp()) {
			kyle.setCancelled(false);
		} else {
			sam.sendMessage(ChatColor.LIGHT_PURPLE
					+ "Fuck you, you broke my block, bitch!");
			kyle.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onstuffExplode(ServerListPingEvent event) {
		try {
			event.setServerIcon(Bukkit.loadServerIcon(new File("server.png")));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
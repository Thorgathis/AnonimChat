package com.nightday.anonimchat;


import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{
	public static main plugin;
	HashMap<Player, ArrayList<Block>> anonPlayers = new HashMap<Player, ArrayList<Block>>();
	public void onEnable(){
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
	    
	}
	public void onDisable(){
		
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("anonchat")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players can chat anonymously!");
			}else{
				Player p = (Player) sender;
						if(args.length == 0){
							if(anonPlayers.containsKey(p)){
								p.sendMessage(ChatColor.RED + "You are no longer chatting anonymously.");
								anonPlayers.remove(p);
							}else{
								p.sendMessage(ChatColor.DARK_GREEN + "You are now chatting anonymously!");
								anonPlayers.put(p, null);
							}
						}else{
							p.sendMessage(ChatColor.DARK_RED + "Too many arguments! Usage: /anonchat");
						}
		    }
			return true;
		}
		return false;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player p = event.getPlayer();
		if (anonPlayers.containsKey(p)){
			for(Player player : Bukkit.getOnlinePlayers()){
				if(player.hasPermission(("anonchat.names")) || p.isOp()){
					String f = ChatColor.RESET + getConfig().getString("format").replace('&', '�');
					String dn = p.getDisplayName();
					dn = ChatColor.stripColor(dn);
					event.setFormat(ChatColor.GRAY + "(" + ChatColor.GRAY +  dn + ChatColor.GRAY +  ") " +  f + " " + ChatColor.RESET + event.getMessage());
				}else{
					String f = getConfig().getString("format");
					event.setFormat(f + " " + event.getMessage());
				}
				event.setCancelled(true);
				player.sendMessage(event.getFormat());
			}
		}
	}

} 

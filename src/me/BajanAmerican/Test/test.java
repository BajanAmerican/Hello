package me.BajanAmerican.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class test extends JavaPlugin implements Listener, PluginMessageListener{
	private static test plugin;

	Scoreboard board;
	
	Team blue;
	Team red;
	Team specs;
	
	List<Team> teams;
	List<String> servers;
	
	Map<String, Short> serverport;
	Map<String, Boolean> serverresponse;
	
	private ArrayList<Player> cooldown = new ArrayList<Player>(), nofall = new ArrayList<Player>();
	
	@Override
	public void onDisable() 
	{
		System.out.println("DISABLED!");
	}
	
	@Override
	public void onEnable() 
	{
		plugin=this;
		System.out.println("ENABLED!");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		teams = new ArrayList<Team>();
		servers = new ArrayList<String>();
		serverport = new HashMap<String, Short>();
		serverresponse = new HashMap<String, Boolean>();
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		blue = board.registerNewTeam("blue");
		red = board.registerNewTeam("red");
		specs = board.registerNewTeam("specs");
		teams.add(blue);
		teams.add(red);
		teams.add(specs);
	}
	
	public static test getInstance()
	{
		return plugin;
	}
	
	public Scoreboard getScoreboard()
	{
		return board;
	}
	
	public Team getBlueTeam()
	{
		return blue;
	}
	
	public Team getRedTeam()
	{
		return red;
	}
	
	public Team getSpectators()
	{
		return specs;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] message)
	{
		if (!channel.equals("BungeeCord"))
		{
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GetServers"))
		{
			String[] serverList = in.readUTF().split(", ");
			if(servers.size() == 0 || servers == null)
			{
				for(String s : serverList)
					servers.add(s);
			}
		}
		else if(subchannel.equals("ServerIP"))
		{
			String serverName = in.readUTF();
			String ip = in.readUTF();
			short port = in.readShort();
				System.out.println("SEVERNAME = " + serverName + ", IP = " + ip + ":" + String.valueOf(port));
				serverport.put(serverName, port);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
        event.getPlayer().getInventory().setItem(0, new ItemStack(Material.COMPASS));
        
        getServer().getScheduler().runTaskLater(this, new Runnable()
        {	
        	public void run()
        	{
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("GetServers");
                event.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        	}
        }, 2L);
        
        getServer().getScheduler().runTaskLater(this, new Runnable()
        {	
        	public void run()
        	{
        		for(final String s : servers)
				{
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("ServerIP");
					out.writeUTF(s);
					event.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());					
				}
        	}
        }, 5L);
        
        getServer().getScheduler().runTaskLater(this, new Runnable()
        {
			@Override
			public void run() 
			{
				for(final String s : serverport.keySet())
				{
					if(isOnline(serverport.get(s)))
					{
						System.out.println("SERVER '" + s + "' with port '" + String.valueOf(serverport.get(s)) + "' IS ONLINE!!!");
					}
					else
					{
						System.out.println("SERVER '" + s + "' with port '" + String.valueOf(serverport.get(s)) + "' IS OFFLINE!!!");
					}
				}
			}
        }, 8L);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!(event.getPlayer() instanceof Player))
			return;
		try
		{
			if(event.getPlayer().getItemInHand().getType() == Material.COMPASS)
			{
				if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					openMenu(event.getPlayer());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	private void callPing()
	{
		for(String s : servers)
		{
			getProxy().getServers().get(s).ping(new Callback<ServerPing>() {
		         
	            @Override
	            public void done(ServerPing result, Throwable error) {
	                if(error!=null){
	                    //Means that server is not responding : OFFLINE
	                    //Store this, by example, in a Hashmap<Server,Boolean> serverStatus, false is OFFLINE and true ONLINE
	                    return;
	                }
	             
	            }
	        });
		}
	}
	*/
	
	private void openMenu(final Player p) 
	{
		final Inventory inv = Bukkit.createInventory(null, getSizeForMap(), ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + " *Cauldron Wars*");
		
		getServer().getScheduler().runTaskLater(plugin, new Runnable()
		{
			@Override
			public void run() 
			{
				int count = 0;
				for(int i = 0; i < getSizeForMap(); i++)
				{
					if(count < serverport.size())
					{
						if(isOnline(serverport.get(servers.get(count))))
							inv.setItem(i, setNameAndLore(new ItemStack(Material.EMERALD_BLOCK), ChatColor.GOLD + String.valueOf(servers.get(count)), ChatColor.GREEN + "§lONLINE"));
						else
							inv.setItem(i, setNameAndLore(new ItemStack(Material.REDSTONE_BLOCK), ChatColor.GOLD + String.valueOf(servers.get(count)), ChatColor.DARK_RED + "§lOFFLINE"));
						count++;
					}
				}
				p.openInventory(inv);
			}
		}, 2L);
	}
	
	private int getSizeForMap()
    {
        int size = servers.size();
        while(!(size % 9 == 0))
        {
            ++size;
        }
        return (size > 54) ? 54 : size;
    }
	
	public ItemStack setNameAndLore(ItemStack is, String name, String description)
	{
		List<String> temp = new ArrayList<String>();
		ItemMeta im = is.getItemMeta();
		temp.add(description);
		im.setDisplayName(name);
		im.setLore(temp);
		is.setItemMeta(im);
		return is;
	}
	
	 public boolean isOnline(Short port)
	    {
	        try
	        {
	            Socket s = new Socket();
	            s.connect(new InetSocketAddress("127.0.0.1", Integer.valueOf(port)));
	        //    Bukkit.getServer().broadcastMessage(addr[0] + ":" + addr[1]);
	            s.close();
	            return true;
	        }
	        catch (NumberFormatException e)
	        {

	        }
	        catch (UnknownHostException e)
	        {

	        }
	        catch (IOException e)
	        {

	        }
	        return false;
	    }
	 
	 @SuppressWarnings("deprecation")
     @EventHandler
     public void onGrappleThrow(ProjectileLaunchEvent e) {
             if (!e.getEntityType().equals(EntityType.FISHING_HOOK)) return;
             if (!(e.getEntity().getShooter() instanceof Player)) return;
            
             final Player p = (Player) e.getEntity().getShooter();
            
             if (cooldown.contains(p)) {
                     e.setCancelled(true);
                     return;
             }
            
             Location target = null;
            
             for (Block block : p.getLineOfSight(null, 100)) {
                     if (!block.getType().equals(Material.AIR)) {
                             target = block.getLocation();
                             break;
                     }
             }
            
             if (target == null) {
                     e.setCancelled(true);
                     return;
             }
            
             p.teleport(p.getLocation().add(0, 0.5, 0));
            
             final Vector v = getVectorForPoints(p.getLocation(), target);
            
             e.getEntity().setVelocity(v);
            
             if (!nofall.contains(p)) nofall.add(p);
            
             Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                     public void run() {
                             p.setVelocity(v);
                     }
             }, 5);
            
             cooldown.add(p);
            
             Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                     public void run() {
                             cooldown.remove(p);
                     }
             }, 15);
     }
    
     @EventHandler
     public void onEntityDamage(EntityDamageEvent e) {
             if (!(e.getEntity() instanceof Player)) return;
             if (!e.getCause().equals(DamageCause.FALL)) return;
            
             Player p = (Player) e.getEntity();
            
             if (nofall.contains(p)) {
                     e.setCancelled(true);
                     nofall.remove(p);
             }
     }
    
     private Vector getVectorForPoints(Location l1, Location l2) {
             double g = -0.08;
             double d = l2.distance(l1);
             double t = d;
             double vX = (1.0+0.07*t) * (l2.getX() - l1.getX())/t;
             double vY = (1.0+0.03*t) * (l2.getY() - l1.getY())/t - 0.5*g*t;
             double vZ = (1.0+0.07*t) * (l2.getZ() - l1.getZ())/t;
             return new Vector(vX, vY, vZ);
     }

	public boolean onCommand(final CommandSender sender, Command cmd,
			String commandLabel, final String[] args) 
	{
		if(commandLabel.equalsIgnoreCase("test"))
		{
			int rand = new Random().nextInt(3);
			Team team = teams.get(rand);
			team.addPlayer((Player)sender);
			if(team.getName().equalsIgnoreCase("blue"))
				team.setPrefix(ChatColor.BLUE + "");
			else if(team.getName().equalsIgnoreCase("red"))
				team.setPrefix(ChatColor.RED + "");
			else if(team.getName().equalsIgnoreCase("specs"))
				team.setPrefix(ChatColor.BLACK + "");
			else
				team.setPrefix(ChatColor.GOLD + "");
			((Player)sender).setPlayerListName(team.getPrefix() + ((Player)sender).getName());
			((Player)sender).sendMessage("Your name should now be " + team.getPrefix() + team.getName() + "!");
		}
		else if(commandLabel.equalsIgnoreCase("execute"))
		{
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
	        out.writeUTF("GetServers");
	        ((Player)sender).sendPluginMessage(this, "BungeeCord", out.toByteArray());
	        
	        getServer().getScheduler().runTaskLater(this, new Runnable()
	        {	
	        	public void run()
	        	{
	        		for(final String s : servers)
					{
						System.out.println(s);
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeUTF("ServerIP");
						out.writeUTF(s);
						((Player)sender).sendPluginMessage(plugin, "BungeeCord", out.toByteArray());					
					}
	        	}
	        }, 2L);
	        
	        getServer().getScheduler().runTaskLater(this, new Runnable()
	        {
				@Override
				public void run() 
				{
					for(final String s : serverport.keySet())
					{
						if(isOnline(serverport.get(s)))
						{
							System.out.println("SERVER '" + s + "' with port '" + String.valueOf(serverport.get(s)) + "' IS ONLINE!!!");
						}
						else
						{
							System.out.println("SERVER '" + s + "' with port '" + String.valueOf(serverport.get(s)) + "' IS OFFLINE!!!");
						}
					}
				}
	        }, 5L);
		}
		else if(commandLabel.equalsIgnoreCase("clear"))
		{
			for(Team t : teams)
				if(t.getPlayers().size() !=0)
					t.getPlayers().clear();
			((Player)sender).sendMessage("All teams cleared!");
		}
		return false;
	}
	
}
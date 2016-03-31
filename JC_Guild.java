package edu.seu.ghostsama;


import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
public class JC_Guild extends JavaPlugin 
{
	public void onEnable()
	{
		//初始化代码
		getLogger().info("JC_Guild插件已被加载");
		//创建文件夹
		if(!getDataFolder().exists())
		{
			getDataFolder().mkdir();
		}
	}
	
	public void createHome(Player player,String name)
	{
		Location l=player.getLocation();
		String path="homes/"+player.getName()+".yml";
		File file=new File(getDataFolder(),path);
		FileConfiguration config=load(file);
		config.set("Homes."+name+".World", l.getWorld().getName());
		config.set("Homes."+name+".X", l.getBlockX());
		config.set("Homes."+name+".Y", l.getBlockY());
		config.set("Homes."+name+".Z", l.getBlockZ());
		try{config.save(file);}
		catch(IOException e){e.printStackTrace();}

	}
	public Location getHome(Player player,String name)
	{
		String path="homes/"+player.getName()+".yml";
		File file=new File(getDataFolder(),path);
		FileConfiguration config=load(file);
		if(config.contains("Homes."+name))
		{
			String world=config.getString("Homes."+name+".World");
			int x=config.getInt("Homes."+name+".X");
			int y=config.getInt("Homes."+name+".Y");
			int z=config.getInt("Homes."+name+".Z");
			Location l=new Location(Bukkit.getWorld(world),x,y,z);
			return l;
		}
		return null;
	}
	
	public boolean removeHome(Player player,String name)
	{
		String path="homes/"+player.getName()+".yml";
		File file=new File(getDataFolder(),path);
		FileConfiguration config=load(file);
		if(config.contains("Homes."+name))
		{
			config.set("Homes."+name,null);
			try{config.save(file);}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public FileConfiguration load(File file)
	{
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration load(String path)
	{
		File file=new File(path);
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
	}
	return YamlConfiguration.loadConfiguration(new File(path));
	}
	
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[]args)
	{
		//判断一个sethome
		if(args[0].equalsIgnoreCase("sethome"))
		{
				if(sender instanceof Player)
				{
					if(args.length>2)
					{
						sender.sendMessage("§a参数过长");
						return true;
					}
					if(args.length<2)
					{
						sender.sendMessage("§a参数过短");
						return true;
					}
					Player player=(Player)sender;
					createHome(player,args[1]);
					player.sendMessage("§a设置家成功");
		return true;
				}
			}
		//判断一个gethome
		if(args[0].equalsIgnoreCase("home"))
		{
			if(sender instanceof Player)
			{
				if(args.length>2)
				{
					sender.sendMessage("§a参数过长");
					return true;
				}
				if(args.length<2)
				{
					sender.sendMessage("§a参数过短");
					return true;
				}
				Player player=(Player)sender;
				Location l=getHome(player,args[1]);
				if(l!=null) 
				{
					player.teleport(l);
					return true;
				}
				player.sendMessage("§a该家名不存在,传送失败");
				return true;
			}
		}
		//removeHome参数判断
		if(args[0].equalsIgnoreCase("removeHome"))
		{
			if(sender instanceof Player)
			{
				if(args.length>2)
				{
					sender.sendMessage("§a参数过长");
					return true;
				}
				if(args.length<2)
				{
					sender.sendMessage("§a参数过短");
					return true;
				}
				Player player=(Player)sender;
				if(removeHome(player,args[1]))
				{
					player.sendMessage("§a删除家"+args[1]+"成功");
					return true;
				}
				player.sendMessage("§a该家名不存在,删除失败");
				return true;
			}
		}
		
		if(label.equalsIgnoreCase("jc"))
		{
			//输入为空，输出帮助指令
			if(args.length==0)
			{
				sender.sendMessage("§a/jc message 信息");
				sender.sendMessage("§a/jc kill 自杀");
				sender.sendMessage("§a/jc m [玩家名] [信息] 发送信息");
				return true;
			}
			//jc下的message参数
			if(args[0].equalsIgnoreCase("message"))
			{
				if(sender instanceof Player)
				{
					sender.sendMessage("§a咖喱是变态");
					return true;
				}
				sender.sendMessage("§a该指令只能由玩家执行");
				return true;
			}
			//jc下的kill参数，自杀
			if(args[0].equalsIgnoreCase("kill"))
			{
				if(sender instanceof Player)
				{
					Player player=(Player)sender;
					player.setHealth(0.0);
					player.sendMessage("§a你自杀了");
					return true;
				}
				sender.sendMessage("§a该指令只能由玩家执行");
				return true;
			}
			//jc下的发送消息"m"
			if(args[0].equalsIgnoreCase("m"))
			{
				if(!(sender instanceof Player))
				{
					if(args.length>3)
					{
						sender.sendMessage("§a参数长度过长");
						return true;
					}
					if(args.length<3)
					{
						sender.sendMessage("§a参数长度过短");
						return true;
					}
					if(args.length==3)
					{
						Player p=getServer().getPlayer(args[1]);
						if(p==null)
						{
							sender.sendMessage("§a玩家不存在，发送失败");
							return true;
						}
						if(p!=null)
						{
							p.sendMessage(args[2].replaceAll("&", "§"));
							sender.sendMessage("§a给"+p.getName()+"的信息发送成功");
							return true;
						}
					}
				}
				sender.sendMessage("§a该指令不能由玩家执行");
				return true;
			}
		}
		return false;
	}
}

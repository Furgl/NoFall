package furgl.noFall;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class CommandNoFall extends CommandBase
{
	public CommandNoFall()
	{

	}

	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	public List<java.lang.String> getTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) 
	{
		if (args.length == 1)
		{
			List list = new ArrayList();
			list.add("enable");
			list.add("disable");
			list.add("height");
			list.add("avoid");
			return list;
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("avoid"))
			{
				List list = new ArrayList();
				list.add("lava");
				list.add("water");
				return list;
			}
		}
		else if (args.length == 3)
		{
			if (args[0].equalsIgnoreCase("avoid") && (args[1].equalsIgnoreCase("lava") || args[1].equalsIgnoreCase("water")))
			{
				List list = new ArrayList();
				list.add("true");
				list.add("false");
				return list;
			}
		}
		return null;
	}

	public boolean canCommandSenderUse(ICommandSender sender) 
	{
		return true;
	}

	public void execute(MinecraftServer server,ICommandSender sender, String[] args) throws CommandException 
	{
		if (args.length == 0)
		{
			printUseNoFall(sender);
			printFallHeight(sender);
			printAvoidLava(sender);
			printAvoidWater(sender);
		}
		else if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("enable"))
			{
				Configs.useNoFall = true;
				printUseNoFall(sender);
			}
			else if (args[0].equalsIgnoreCase("disable"))
			{
				Configs.useNoFall = false;
				printUseNoFall(sender);
			}
			else if (args[0].equalsIgnoreCase("height"))
				printFallHeight(sender);
			else if (args[0].equalsIgnoreCase("avoid"))
				sender.sendMessage(new TextComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]"));
			else
				printUsage(sender);
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("height"))
			{
				if (NumberUtils.isNumber(args[1]))
				{
					Configs.airBlocks = Integer.parseInt(args[1]);
					if (Configs.airBlocks > 255)
						Configs.airBlocks = 255;
					else if (Configs.airBlocks < 2)
						Configs.airBlocks = 2;
					printFallHeight(sender);
				}
				else
					sender.sendMessage(new TextComponentTranslation("Usage: /nofall height [<2-255>]").setStyle(new Style().setColor(TextFormatting.RED)));
			}
			else if (args[0].equalsIgnoreCase("avoid"))
			{
				if (args[1].equalsIgnoreCase("lava"))
					printAvoidLava(sender);
				else if (args[1].equalsIgnoreCase("water"))
					printAvoidWater(sender);
				else
					sender.sendMessage(new TextComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setStyle(new Style().setColor(TextFormatting.RED)));
			}
			else
				printUsage(sender);

		}
		else if (args.length == 3)
		{
			if (args[0].equalsIgnoreCase("avoid")) 
			{
				if (args[1].equalsIgnoreCase("lava"))
				{
					if (args[2].equalsIgnoreCase("true"))
					{
						Configs.avoidLava = true;
						printAvoidLava(sender);
					}
					else if (args[2].equalsIgnoreCase("false"))
					{
						Configs.avoidLava = false;
						printAvoidLava(sender);
					}
					else
						sender.sendMessage(new TextComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setStyle(new Style().setColor(TextFormatting.RED)));
				}
				else if (args[1].equalsIgnoreCase("water"))
				{
					if (args[2].equalsIgnoreCase("true"))
					{
						Configs.avoidWater = true;
						printAvoidWater(sender);
					}
					else if (args[2].equalsIgnoreCase("false"))
					{
						Configs.avoidWater = false;
						printAvoidWater(sender);
					}
					else
						sender.sendMessage(new TextComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setStyle(new Style().setColor(TextFormatting.RED)));
				}
				else
					sender.sendMessage(new TextComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setStyle(new Style().setColor(TextFormatting.RED)));
			}
			else
				printUsage(sender);
		}
		else
			printUsage(sender);

		return;
	}

	private void printUsage(ICommandSender sender) 
	{
		sender.sendMessage(new TextComponentTranslation("Usage: /nofall [enable/disable/avoid/height] [...] ").setStyle(new Style().setColor(TextFormatting.RED)));
	}

	private void printAvoidWater(ICommandSender sender)
	{
		sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA+""+TextFormatting.BOLD+"[NoFall]"+TextFormatting.RESET+""+TextFormatting.AQUA+" Avoid water is " + (Configs.avoidWater ? TextFormatting.DARK_GREEN+""+TextFormatting.BOLD+"enabled" : TextFormatting.DARK_RED+""+TextFormatting.BOLD+"disabled")));

	}

	private void printAvoidLava(ICommandSender sender) 
	{	
		sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA+""+TextFormatting.BOLD+"[NoFall]"+TextFormatting.RESET+""+TextFormatting.AQUA+" Avoid lava is " + (Configs.avoidLava ? TextFormatting.DARK_GREEN+""+TextFormatting.BOLD+"enabled" : TextFormatting.DARK_RED+""+TextFormatting.BOLD+"disabled")));
	}

	private void printUseNoFall(ICommandSender sender) 
	{
		if (NoFall.onServer)
			sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA+""+TextFormatting.BOLD+"[NoFall]"+TextFormatting.RESET+""+TextFormatting.AQUA+" No Fall is " + (Configs.useNoFall ? TextFormatting.DARK_GREEN+""+TextFormatting.BOLD+"enabled" : TextFormatting.DARK_RED+""+TextFormatting.BOLD+"disabled")));
		else
			sender.sendMessage(new TextComponentTranslation("[NoFall] No Fall cannot be enabled when not installed on the server.").setStyle(new Style().setColor(TextFormatting.DARK_RED)));
	}

	private void printFallHeight(ICommandSender sender) 
	{
		sender.sendMessage(new TextComponentTranslation(TextFormatting.AQUA+""+TextFormatting.BOLD+"[NoFall]"+TextFormatting.RESET+""+TextFormatting.AQUA+" Fall height is "+TextFormatting.DARK_GREEN+""+TextFormatting.BOLD+Configs.airBlocks));		
	}

	public List getAliases() 
	{
		List aliases = new ArrayList();
		aliases.add("nofall");
		return aliases;
	}

	public String getUsage(ICommandSender sender) 
	{
		return "";
	}

	public String getName() 
	{
		return "nofall";
	}  
        
}

package furgl.noFall;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandNoFall extends CommandBase
{
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	public List addTabCompletionOptions(ICommandSender sender, String[] args) 
	{
		if (args.length == 1)
		{
			List list = new ArrayList();
			list.add("enable");
			list.add("disable");
			list.add("height");
			list.add("avoid");
			return getListOfStringsFromIterableMatchingLastWord(args, list);
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("avoid"))
			{
				List list = new ArrayList();
				list.add("lava");
				list.add("water");
				return getListOfStringsFromIterableMatchingLastWord(args, list);
			}
		}
		else if (args.length == 3)
		{
			if (args[0].equalsIgnoreCase("avoid") && (args[1].equalsIgnoreCase("lava") || args[1].equalsIgnoreCase("water")))
			{
				List list = new ArrayList();
				list.add("true");
				list.add("false");
				return getListOfStringsFromIterableMatchingLastWord(args, list);
			}
		}
		return null;
	}

	public boolean canCommandSenderUseCommand(ICommandSender sender) 
	{
		return true;
	}

	public void processCommand(ICommandSender sender, String[] args) throws CommandException 
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
				Config.useNoFall = true;
				printUseNoFall(sender);
			}
			else if (args[0].equalsIgnoreCase("disable"))
			{
				Config.useNoFall = false;
				printUseNoFall(sender);
			}
			else if (args[0].equalsIgnoreCase("height"))
				printFallHeight(sender);
			else if (args[0].equalsIgnoreCase("avoid"))
				sender.addChatMessage(new ChatComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			else
				printUsage(sender);
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("height"))
			{
				if (NumberUtils.isNumber(args[1]))
				{
					Config.airBlocks = Integer.parseInt(args[1]);
					if (Config.airBlocks > 255)
						Config.airBlocks = 255;
					else if (Config.airBlocks < 2)
						Config.airBlocks = 2;
					printFallHeight(sender);
				}
				else
					sender.addChatMessage(new ChatComponentTranslation("Usage: /nofall height [<2-255>]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			}
			else if (args[0].equalsIgnoreCase("avoid"))
			{
				if (args[1].equalsIgnoreCase("lava"))
					printAvoidLava(sender);
				else if (args[1].equalsIgnoreCase("water"))
					printAvoidWater(sender);
				else
					sender.addChatMessage(new ChatComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
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
						Config.avoidLava = true;
						printAvoidLava(sender);
					}
					else if (args[2].equalsIgnoreCase("false"))
					{
						Config.avoidLava = false;
						printAvoidLava(sender);
					}
					else
						sender.addChatMessage(new ChatComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
				else if (args[1].equalsIgnoreCase("water"))
				{
					if (args[2].equalsIgnoreCase("true"))
					{
						Config.avoidWater = true;
						printAvoidWater(sender);
					}
					else if (args[2].equalsIgnoreCase("false"))
					{
						Config.avoidWater = false;
						printAvoidWater(sender);
					}
					else
						sender.addChatMessage(new ChatComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
				else
					sender.addChatMessage(new ChatComponentTranslation("Usage: /nofall avoid <lava/water> [<true/false>]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			}
			else
				printUsage(sender);
		}
		else
			printUsage(sender);
		Config.syncToConfig();
		return;
	}

	private void printUsage(ICommandSender sender) 
	{
		sender.addChatMessage(new ChatComponentTranslation("Usage: /nofall [enable/disable/avoid/height] [...] ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
	}

	private void printAvoidWater(ICommandSender sender)
	{
		sender.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA+""+EnumChatFormatting.BOLD+"[NoFall]"+EnumChatFormatting.RESET+""+EnumChatFormatting.AQUA+" Avoid water is " + (Config.avoidWater ? EnumChatFormatting.DARK_GREEN+""+EnumChatFormatting.BOLD+"enabled" : EnumChatFormatting.DARK_RED+""+EnumChatFormatting.BOLD+"disabled")));
		
	}

	private void printAvoidLava(ICommandSender sender) 
	{	
		sender.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA+""+EnumChatFormatting.BOLD+"[NoFall]"+EnumChatFormatting.RESET+""+EnumChatFormatting.AQUA+" Avoid lava is " + (Config.avoidLava ? EnumChatFormatting.DARK_GREEN+""+EnumChatFormatting.BOLD+"enabled" : EnumChatFormatting.DARK_RED+""+EnumChatFormatting.BOLD+"disabled")));
	}

	private void printUseNoFall(ICommandSender sender) 
	{
		if (NoFall.onServer)
			sender.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA+""+EnumChatFormatting.BOLD+"[NoFall]"+EnumChatFormatting.RESET+""+EnumChatFormatting.AQUA+" No Fall is " + (Config.useNoFall ? EnumChatFormatting.DARK_GREEN+""+EnumChatFormatting.BOLD+"enabled" : EnumChatFormatting.DARK_RED+""+EnumChatFormatting.BOLD+"disabled")));
		else
			sender.addChatMessage(new ChatComponentTranslation("[NoFall] No Fall cannot be enabled when not installed on the server.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED)));
	}

	private void printFallHeight(ICommandSender sender) 
	{
		sender.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.AQUA+""+EnumChatFormatting.BOLD+"[NoFall]"+EnumChatFormatting.RESET+""+EnumChatFormatting.AQUA+" Fall height is "+EnumChatFormatting.DARK_GREEN+""+EnumChatFormatting.BOLD+Config.airBlocks));		
	}

	public List getCommandAliases() 
	{
		List aliases = new ArrayList();
		aliases.add("nofall");
		return aliases;
	}

	public String getCommandUsage(ICommandSender sender) 
	{
		return "";
	}

	public String getCommandName() 
	{
		return "nofall";
	}
}

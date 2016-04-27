package furgl.noFall;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config 
{
	public static Configuration config;
	public static boolean useNoFall;
	public static int airBlocks;
	public static boolean avoidWater;
	public static boolean avoidLava;

	public static void init(final File file)
	{
		Config.config = new Configuration(file);
		Config.config.load();
		Config.syncFromConfig();
		Config.config.save();
	}
	
	public static void syncFromConfig() 
	{
		Property useNoFallProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Use No Fall", true);
		useNoFallProp.comment = "Enable or disable this mod";
		useNoFall = useNoFallProp.getBoolean();
		Property airBlocksProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Fall Height", 4);
		airBlocksProp.comment = "Height at which No Fall will prevent you from falling";
		airBlocks = airBlocksProp.getInt();
		if (airBlocks > 255)
			airBlocks = 255;
		else if (airBlocks < 2)
			airBlocks = 2;
		Property avoidWaterProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Avoid Water", false);
		avoidWaterProp.comment = "Should No Fall prevent you from falling into water?";
		avoidWater = avoidWaterProp.getBoolean();
		Property avoidLavaProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Avoid Lava", true);
		avoidLavaProp.comment = "Should No Fall prevent you from falling into lava?";
		avoidLava = avoidLavaProp.getBoolean();
		Config.config.save();
	}
	
	public static void syncToConfig()
	{
		Property useNoFallProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Use No Fall", true);
		useNoFallProp.comment = "Enable or disable this mod";
		useNoFallProp.set(useNoFall);
		Property airBlocksProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Fall Height", 4);
		airBlocksProp.comment = "Height at which No Fall will prevent you from falling";
		airBlocksProp.set(airBlocks);
		Property avoidWaterProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Avoid Water", false);
		avoidWaterProp.comment = "Should No Fall prevent you from falling into water?";
		avoidWaterProp.set(avoidWater);
		Property avoidLavaProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Avoid Lava", true);
		avoidLavaProp.comment = "Should No Fall prevent you from falling into lava?";
		avoidLavaProp.set(avoidLava);
		Config.config.save();
	}
}

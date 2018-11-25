package furgl.noFall;


import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;

@Config(modid=NoFall.MODID)
public class Configs
{
        @Comment("Enable or disable this mod")
	public static boolean useNoFall;

        @Comment("Height at which NoFall will prevent you from falling")
        @RangeInt(min = 2, max = 255) 
	public static int airBlocks = 2;

        @Comment("Should No Fall prevent you from falling into water?")
	public static boolean avoidWater;

        @Comment("Should No Fall prevent you from falling into lava?")
	public static boolean avoidLava;

        @Mod.EventBusSubscriber(modid = NoFall.MODID)
        private static class Handler
        {
            public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
            {
                if (event.getModID().equals(NoFall.MODID))
                {
                ConfigManager.sync(NoFall.MODID, Config.Type.INSTANCE);
                }
            }
        }
}

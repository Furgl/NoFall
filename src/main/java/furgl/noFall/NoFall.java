package furgl.noFall;


import furgl.noFall.packet.Handler;
import furgl.noFall.packet.Message;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = NoFall.MODID, name = NoFall.MODNAME, version = NoFall.VERSION)
public class NoFall
{
	public static final String MODID = "nofall";
	public static final String MODNAME = "No Fall";
	public static final String VERSION = "1.0";
	public static SimpleNetworkWrapper network;
	public static boolean onServer = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		network = NetworkRegistry.INSTANCE.newSimpleChannel(NoFall.MODID);
		network.registerMessage(Handler.class, Message.class, 0, Side.CLIENT);

		ClientCommandHandler.instance.registerCommand(new CommandNoFall());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new NoFall());
		FMLCommonHandler.instance().bus().register(new NoFall());
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerEvent.PlayerLoggedInEvent event)
	{
		NoFall.network.sendToAll(new Message(true)); //set onServer to true if on server
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(FMLNetworkEvent.ClientConnectedToServerEvent event)
	{
		NoFall.onServer = false; //reset onServer when connecting to new server
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.player.world.isRemote && !NoFall.onServer && Configs.useNoFall)
		{
			Configs.useNoFall = false;
		}
		if (event.player.world.isRemote && event.player.world.getChunkFromBlockCoords(event.player.getPosition()).isLoaded() && Configs.useNoFall && !event.player.isInWater() && !event.player.capabilities.isCreativeMode && !event.player.capabilities.allowFlying && (!event.player.isSneaking() || (event.player.isSneaking() && !event.player.onGround)) && isAboveAir(event))
		{
			Vec3d destination = null;
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(0, -1, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(0, -2, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(1, -1, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(-1, -1, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(0, -1, 1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(0, -1, -1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(1, -1, 1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(1, -1, -1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(-1, -1, 1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.getEntityBoundingBox().offset(-1, -1, -1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				//System.out.println("Destination not found.");
			}
			else
			{
				//event.player.playSound("tile.piston.out", 0.1F, 1.5F);
				Vec3d vec = destination.subtract(event.player.getPositionVector());
				vec.normalize();
				double multiplier = 0.6D;
				event.player.setVelocity(vec.x*multiplier, event.player.motionY, vec.z*multiplier);
			}
		}
	}

	private Vec3d findDestination(AxisAlignedBB box, TickEvent.PlayerTickEvent event) 
	{
		//Check bounding box corners for non-air block
		if (isSuitableBlock(new BlockPos(box.maxX, box.minY, box.maxZ), event))
			return new Vec3d(box.maxX, box.minY, box.maxZ);
		else if (isSuitableBlock(new BlockPos(box.maxX, box.minY, box.minZ), event))
			return new Vec3d(box.maxX, box.minY, box.minZ);
		else if (isSuitableBlock(new BlockPos(box.minX, box.minY, box.maxZ), event))
			return new Vec3d(box.minX, box.minY, box.maxZ);
		else if (isSuitableBlock(new BlockPos(box.minX, box.minY, box.minZ), event))
			return new Vec3d(box.minX, box.minY, box.minZ);
		else
			return null;
	}

	private boolean isAboveAir(TickEvent.PlayerTickEvent event) 
	{
		for (int i=0; i<=Configs.airBlocks+1; i++)
		{
			Block block = event.player.world.getBlockState(new BlockPos(event.player.getPositionVector()).add(0D, -i, 0D)).getBlock();
			if ((block.equals(Blocks.WATER) && Configs.avoidWater) || (block.equals(Blocks.LAVA) && Configs.avoidLava))
				return true;
			if (!block.equals(Blocks.AIR))
				return false;
		}
		return true;
	}

	private boolean isSuitableBlock(BlockPos pos, TickEvent.PlayerTickEvent event)
	{
		Block state = event.player.world.getBlockState(pos).getBlock();
		Block stateUp = event.player.world.getBlockState(pos.up()).getBlock();
		if ((state.equals(Blocks.WATER) && Configs.avoidWater) || (state.equals(Blocks.LAVA) && Configs.avoidLava))
			return false;
		if (!state.equals(Blocks.AIR) && stateUp.equals(Blocks.AIR))
			return true;
		return false;
	}
}

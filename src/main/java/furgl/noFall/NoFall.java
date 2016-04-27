package furgl.noFall;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import furgl.noFall.packet.Handler;
import furgl.noFall.packet.Message;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = NoFall.MODID, name = NoFall.MODNAME, version = NoFall.VERSION, guiFactory = "furgl.noFall.gui.NoFallGuiFactory")
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
		MinecraftForge.EVENT_BUS.register(new NoFall());
		FMLCommonHandler.instance().bus().register(new NoFall());
		Config.init(event.getSuggestedConfigurationFile());
		ClientCommandHandler.instance.registerCommand(new CommandNoFall());
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
		if (event.player.worldObj.isRemote && !NoFall.onServer && Config.useNoFall)
		{
			Config.useNoFall = false;
		}
		if (event.player.worldObj.isRemote && event.player.worldObj.getChunkFromChunkCoords(event.player.chunkCoordX, event.player.chunkCoordZ).isChunkLoaded && Config.useNoFall && !event.player.isInWater() && !event.player.capabilities.isCreativeMode && !event.player.capabilities.allowFlying && (!event.player.isSneaking() || (event.player.isSneaking() && !event.player.onGround)) && isAboveAir(event))
		{
			Vec3 destination = null;
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(0, -1, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(0, -2, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(1, -1, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(-1, -1, 0);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(0, -1, 1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(0, -1, -1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(1, -1, 1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(1, -1, -1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(-1, -1, 1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				AxisAlignedBB box = event.player.boundingBox.getOffsetBoundingBox(-1, -1, -1);
				destination = findDestination(box, event);
			}
			if (destination == null)
			{
				//System.out.println("Destination not found.");
			}
			else
			{
				event.player.playSound("tile.piston.out", 0.1F, 1.5F);
				//orig Vec3 vec = destination.subtract(event.player.getPosition(1F));
				Vec3 vec = event.player.getPosition(1F).subtract(destination);
				vec.normalize();
				double multiplier = 0.6D;
				event.player.setVelocity(vec.xCoord*multiplier, event.player.motionY, vec.zCoord*multiplier);
			}
		}
	}

	private Vec3 findDestination(AxisAlignedBB box, TickEvent.PlayerTickEvent event) 
	{
		//Check bounding box corners for non-air block
		if (isSuitableBlock(box.maxX, box.minY, box.maxZ, event))
			return Vec3.createVectorHelper(box.maxX, box.minY, box.maxZ);
		else if (isSuitableBlock(box.maxX, box.minY, box.minZ, event))
			return Vec3.createVectorHelper(box.maxX, box.minY, box.minZ);
		else if (isSuitableBlock(box.minX, box.minY, box.maxZ, event))
			return Vec3.createVectorHelper(box.minX, box.minY, box.maxZ);
		else if (isSuitableBlock(box.minX, box.minY, box.minZ, event))
			return Vec3.createVectorHelper(box.minX, box.minY, box.minZ);
		else
			return null;
	}

	private boolean isAboveAir(TickEvent.PlayerTickEvent event) 
	{
		for (int i=0; i<=Config.airBlocks+1 ; i++)
		{
			Block block = event.player.worldObj.getBlock((int)Math.floor(event.player.posX), (int)Math.floor(event.player.posY-i),(int)Math.floor(event.player.posZ));
			if ((block.equals(Blocks.water) && Config.avoidWater) || (block.equals(Blocks.lava) && Config.avoidLava))
				return true;
			if (!block.equals(Blocks.air))
				return false;
		}
		return true;
	}
	
	private boolean isSuitableBlock(double dx, double dy, double dz, TickEvent.PlayerTickEvent event)
	{
		int x = (int) Math.floor(dx);
		int y = (int) Math.floor(dy);
		int z = (int) Math.floor(dz);
		Block state = event.player.worldObj.getBlock(x, y, z);
		Block stateUp = event.player.worldObj.getBlock(x, y+1, z);
		if ((state.equals(Blocks.water) && Config.avoidWater) || (state.equals(Blocks.lava) && Config.avoidLava))
			return false;
		if (!state.equals(Blocks.air) && stateUp.equals(Blocks.air))
			return true;
		return false;
	}
}
package furgl.noFall.packet;

import furgl.noFall.NoFall;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Handler implements IMessageHandler<Message, IMessage> 
{
	public IMessage onMessage(final Message message, final MessageContext ctx) 
	{
		IThreadListener mainThread = Minecraft.getMinecraft();
		mainThread.addScheduledTask(new Runnable() 
		{
			public void run() 
			{
				NoFall.onServer = Message.onServer;
			}
		});
		return null;
	}
} 
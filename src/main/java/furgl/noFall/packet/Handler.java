package furgl.noFall.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import furgl.noFall.NoFall;

public class Handler implements IMessageHandler<Message, IMessage> 
{
	public IMessage onMessage(final Message message, final MessageContext ctx) 
	{
		NoFall.onServer = Message.onServer;
		return null;
	}
} 
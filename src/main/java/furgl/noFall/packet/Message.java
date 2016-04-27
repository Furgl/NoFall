package furgl.noFall.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class Message implements IMessage 
{
	public Message(){}

	protected static boolean onServer = false;

	public Message(boolean b) 
	{
		Message.onServer = b;
	}

	@Override 
	public void toBytes(ByteBuf buf) 
	{
		buf.writeBoolean(onServer);
	}

	@Override 
	public void fromBytes(ByteBuf buf) 
	{
		onServer = buf.readBoolean();
	}
}



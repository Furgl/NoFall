package furgl.noFall.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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



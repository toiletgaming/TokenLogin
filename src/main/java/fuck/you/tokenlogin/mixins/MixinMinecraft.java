package fuck.you.tokenlogin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import fuck.you.tokenlogin.accessors.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

@Mixin( Minecraft.class )
public class MixinMinecraft implements IMinecraft
{
	@Shadow
	private Session session;
	
	@Override
	public void setClientSession( Session session )
	{
		this.session = session;
	}
}

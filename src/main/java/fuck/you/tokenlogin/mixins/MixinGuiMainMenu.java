package fuck.you.tokenlogin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fuck.you.tokenlogin.gui.GuiAltManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

@Mixin( GuiMainMenu.class )
public class MixinGuiMainMenu extends GuiScreen
{
	@Inject( method = "addSingleplayerMultiplayerButtons", at = @At( "RETURN" ) )
	private void addSingleplayerMultiplayerButtons( int p_73969_1_, int p_73969_2_, CallbackInfo info )
	{
		this.buttonList.add( new GuiButton( 93543, this.width / 2 - 100, 5, "Access Token Login" ) );
	}
	
	@Inject( method = "actionPerformed", at = @At( "HEAD" ) )
	public void actionPerformed( GuiButton button, CallbackInfo info )
	{
		if( button.id == 93543 )
			mc.displayGuiScreen( new GuiAltManager( ) );
	}
}

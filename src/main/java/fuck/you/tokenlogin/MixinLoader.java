package fuck.you.tokenlogin;

import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class MixinLoader implements IFMLLoadingPlugin
{
	@SuppressWarnings( "unused" )
	private static boolean isObfuscatedEnvironment = false;
	
	public MixinLoader( )
	{
		MixinBootstrap.init( );
		Mixins.addConfiguration( "mixins.tokenlogin.json" );
		MixinEnvironment.getDefaultEnvironment( ).setObfuscationContext( "searge" );
	}
	
	@Override
	public String[ ] getASMTransformerClass( )
	{
		return new String[ 0 ];
	}
	
	@Override
	public String getModContainerClass( )
	{
		return null;
	}
	
	@Override
	public String getSetupClass( )
	{
		return null;
	}
	
	@Override
	public void injectData( Map< String, Object > data )
	{
		isObfuscatedEnvironment = ( boolean )data.get( "runtimeDeobfuscationEnabled" );
	}
	
	@Override
	public String getAccessTransformerClass( )
	{
		return null;
	}
}

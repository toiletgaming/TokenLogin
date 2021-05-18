package fuck.you.tokenlogin;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
		modid = Main.MODID,
		name = Main.NAME,
		version = Main.VERSION
)
public class Main
{
	public static final String MODID = "tokenlogin";
	public static final String NAME = "TokenLogin";
	public static final String VERSION = "1.0";
	
	private static final Manager manager = new Manager( );
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		manager.start( );
		
		LogManager.getLogger( "TokenLogin" ).info( "Loaded TokenLogin v" + VERSION + " by mrnv/ayywareseller" );
	}
	
	public static Manager getManager( )
	{
		return manager;
	}
}

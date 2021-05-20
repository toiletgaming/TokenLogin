package fuck.you.tokenlogin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public static final String VERSION = "2.0";
	
	private static final Logger logger = LogManager.getLogger( "TokenLogin" );
	
	private static final Manager manager = new Manager( );
	private static AltManager altmanager;
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		manager.start( );
		altmanager = new AltManager( );
		
		logger.info( "Loaded TokenLogin v" + VERSION + " by mrnv/ayywareseller" );
	}
	
	public static Manager getManager( )
	{
		return manager;
	}
	
	public static AltManager getAltManager( )
	{
		return altmanager;
	}
	
	public static Logger getLogger( )
	{
		return logger;
	}
}

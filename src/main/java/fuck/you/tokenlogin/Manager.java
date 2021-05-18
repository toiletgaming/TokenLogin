package fuck.you.tokenlogin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;

import fuck.you.tokenlogin.accessors.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Manager extends Thread
{
	private State state = State.LOGGEDIN;
	private String username;
	private String accessToken;
	
	@Override
	public void run( )
	{
		while( !Thread.currentThread( ).isInterrupted( ) )
		{
			if( this.state == State.LOGGINGIN )
			{
				boolean good = false;
				
				try
				{
					if( isTokenValid( ) )
					{
						String uuid = getUUID( username );
						if( uuid != null )
						{
							Session session = new Session( username, uuid, accessToken, "mojang" );
							( ( IMinecraft )Minecraft.getMinecraft( ) ).setClientSession( session );
							
							good = true;
						}
					}
				}
				catch( Exception loginexception )
				{
					loginexception.printStackTrace( );
				}
				
				this.username = "";
				this.accessToken = "";
				
				if( good )
					this.state = State.LOGGEDIN;
				else
					this.state = State.ERROR;
			}
			
			try
			{
				Thread.sleep( 10 );
			}
			catch( Exception e )
			{
				e.printStackTrace( );
			}
		}
	}
	
	public String getUUID( String username )
	{
		try
		{
			StringBuilder sb = new StringBuilder( );
			URL url = new URL( "https://api.mojang.com/users/profiles/minecraft/" + username );
			BufferedReader reader = new BufferedReader( new InputStreamReader( url.openStream( ) ) );
			
			String line = "";
			while( ( line = reader.readLine( ) ) != null )
				sb.append( line );
			reader.close( );

			JsonObject json = new JsonParser( ).parse( sb.toString( ) ).getAsJsonObject( );
			return json.get( "id" ).getAsString( );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		
		return null;
	}
	
	public boolean isTokenValid( )
	{
		try
		{
			JsonObject json = new JsonObject( );
			json.addProperty( "accessToken", accessToken );
			
			HttpURLConnection connection = ( HttpURLConnection )new URL( "https://authserver.mojang.com/validate" ).openConnection( );
			connection.setRequestMethod( "POST" );
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setDoOutput( true );
			
			OutputStream os = connection.getOutputStream( );
			os.write( json.toString( ).getBytes( StandardCharsets.UTF_8 ) );
			
			int code = connection.getResponseCode( );
			connection.disconnect( );
			return code == 204;
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		
		return false;
	}
	
	public void login( String username, String accessToken )
	{
		if( username == null || accessToken == null ||
			username.equals( "" ) || accessToken.equals( "" ) )
			return;
		
		this.username = username;
		this.accessToken = accessToken;
		this.state = State.LOGGINGIN;
	}
	
	public String getManagerState( )
	{
		switch( state )
		{
		case LOGGINGIN:
			return ChatFormatting.GREEN + "Logging in...";
		case LOGGEDIN:
			return ChatFormatting.GREEN + "You are currently logged as " + ChatFormatting.GOLD + getPlayerName( );
		case ERROR:
			return ChatFormatting.RED + "There was an error while trying to log in";
		default:
			return ChatFormatting.RED + "You shouldn't be able to see this";
		}
	}
	
	public String getPlayerName( )
	{
		try
		{
			return Minecraft.getMinecraft( ).getSession( ).getUsername( );
		}
		catch( Exception e )
		{
			return "unknown";
		}
	}
	
	enum State
	{
		LOGGEDIN,
		LOGGINGIN,
		ERROR
	}
}

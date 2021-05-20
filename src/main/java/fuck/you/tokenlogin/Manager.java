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
	private String clientToken;
	
	@Override
	public void run( )
	{
		while( !Thread.currentThread( ).isInterrupted( ) )
		{
			if( this.state == State.LOGGINGIN )
			{
				boolean good = false;
				boolean uuidfail = false;
				
				try
				{
					if( !isTokenValid( ) )
					{
						if( clientToken != null && clientToken.length( ) > 0 )
						{
							this.state = State.ERROR_PRE;
							
							if( refreshToken( ) )
							{
								Main.getLogger( ).info( "Refreshed access token of " + username );
								good = true;
							}
						}
					}
					else
						good = true;
					
					if( good )
					{
						String uuid = getUUID( username );
						if( uuid != null )
						{
							changeSession( uuid );
							
							// save new access token
							Account account = null;
							for( Account _account : Main.getAltManager( ).getAccounts( ) )
							{
								if( _account.username.equalsIgnoreCase( username ) )
								{
									account = _account;
									break;
								}
							}
							
							if( account != null )
							{
								Main.getAltManager( ).getAccounts( ).remove( account );
								account.accessToken = accessToken;
								Main.getAltManager( ).addAccount( account );
							}
						}
						else
						{
							good = false;
							uuidfail = true;
							this.state = State.ERROR_UUID;
						}
					}
				}
				catch( Exception loginexception )
				{
					loginexception.printStackTrace( );
				}
				
				this.username = "";
				this.accessToken = "";
				this.clientToken = "";
				
				if( good )
					this.state = State.LOGGEDIN;
				else
				{
					if( !uuidfail )
						this.state = State.ERROR;
				}
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
	
	public void changeSession( String uuid )
	{
		Session session = new Session( username, uuid, accessToken, "mojang" );
		( ( IMinecraft )Minecraft.getMinecraft( ) ).setClientSession( session );
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
	
	public boolean refreshToken( )
	{
		try
		{
			JsonObject json = new JsonObject( );
			json.addProperty( "accessToken", accessToken );
			json.addProperty( "clientToken", clientToken );
			
			HttpURLConnection connection = ( HttpURLConnection )new URL( "https://authserver.mojang.com/refresh" ).openConnection( );
			connection.setRequestMethod( "POST" );
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setDoOutput( true );
			
			OutputStream os = connection.getOutputStream( );
			os.write( json.toString( ).getBytes( StandardCharsets.UTF_8 ) );
			
			if( connection.getResponseCode( ) == 403 ) return false;
			
			BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream( ) ) );
			StringBuilder sb = new StringBuilder( );
			
			String line = "";
			while( ( line = reader.readLine( ) ) != null )
				sb.append( line );
			reader.close( );
			connection.disconnect( );
			
			JsonObject parsed = new JsonParser( ).parse( sb.toString( ) ).getAsJsonObject( );
			if( parsed.get( "error" ) != null ) return false;
			
			accessToken = parsed.get( "accessToken" ).getAsString( );
			return true;
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		
		return false;
	}
	
	public void login( String username, String accessToken, String clientToken )
	{
		if( username == null || accessToken == null ||
			username.equals( "" ) || accessToken.equals( "" ) )
			return;
		
		this.username = username;
		this.accessToken = accessToken;
		this.clientToken = clientToken;
		this.state = State.LOGGINGIN;
	}
	
	public String getManagerState( )
	{
		switch( state )
		{
		case LOGGINGIN:
			return "Logging in...";
		case LOGGEDIN:
			return "Currently logged in as " + ChatFormatting.GREEN + getPlayerName( );
		case ERROR_PRE:
			return ChatFormatting.YELLOW + "Trying to refresh access token using client token";
		case ERROR_UUID:
			return ChatFormatting.RED + "Failed to get UUID";
		case ERROR:
			return ChatFormatting.RED + "Invalid access token";
		default:
			return ChatFormatting.RED + "You shouldn't be able to see this";
		}
	}
	
	public void resetState( )
	{
		this.state = State.LOGGEDIN;
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
		ERROR_PRE,
		ERROR_UUID,
		ERROR
	}
}

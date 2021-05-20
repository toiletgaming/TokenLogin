package fuck.you.tokenlogin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AltManager
{
	private List< Account > accounts;
	
	public AltManager( )
	{
		File cfg = new File( "tokenlogin.json" );
		if( !cfg.exists( ) )
		{
			try
			{
				FileWriter writer = new FileWriter( cfg );
				writer.write( "[]" );
				writer.close( );
			}
			catch( Exception e )
			{
				e.printStackTrace( );
			}
			
			accounts = new ArrayList< >( );
			return;
		}
		
		try
		{
			BufferedReader br = new BufferedReader( new FileReader( cfg ) );
			Type type = new TypeToken< List< Account > >( ){ }.getType( );
			accounts = new Gson( ).fromJson( br, type );
			
			if( accounts == null )
				accounts = new ArrayList< >( );
			
			Main.getLogger( ).info( "Loaded " + accounts.size( ) + " accounts" );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
	
	public void save( )
	{
		Gson gson = new GsonBuilder( )
				.setPrettyPrinting( )
				.create( );
		
		try
		{
			try( Writer writer = new FileWriter( "tokenlogin.json" ) )
			{
				gson.toJson( accounts, writer );
			}

			//Main.getLogger( ).info( "Saved accounts" );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
	
	public boolean addAccount( Account account )
	{
		for( Account _account : accounts )
		{
			if( _account == null ) continue;
			
			if( _account.username.equalsIgnoreCase( account.username ) )
				return false; // account with that username already exists
		}
		
		accounts.add( account );
		save( );
		
		return true; // ok
	}
	
	public boolean removeAccount( Account account )
	{
		Account rem = null;
		
		for( Account _account : accounts )
		{
			if( _account == null ) continue;
			
			if( _account.username.equalsIgnoreCase( account.username ) )
			{
				rem = _account;
				break;
			}
		}
		
		if( rem != null )
		{
			accounts.remove( rem );
			save( );
			return true;
		}
		
		return false;
	}
	
	public String getLastUsedAccount( )
	{
		long time = 0;
		String ret = "none";
		
		for( Account account : accounts )
		{
			if( account == null ) continue;
			
			if( account.timeused >= time )
			{
				time = account.timeused;
				ret = account.username;
			}
		}
		
		return ret;
	}
	
	public List< Account > getAccounts( )
	{
		return accounts;
	}
}

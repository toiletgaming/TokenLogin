package fuck.you.tokenlogin.gui;

import fuck.you.tokenlogin.Main;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiTokenLogin extends GuiScreen
{
	private GuiTextField username;
	private GuiTextField accessToken;
	
	public void initGui( )
	{
		int midwidth = this.width / 2;
		int baseheight = this.height / 2 - 30;
		
		this.username = new GuiTextField( 444, this.fontRenderer, midwidth - 100, baseheight, 200, 18 );
		this.accessToken = new GuiTextField( 555, this.fontRenderer, midwidth - 100, baseheight + 23, 200, 18 );
		
		this.username.setMaxStringLength( 999 );
		this.accessToken.setMaxStringLength( 999 );
		
		this.buttonList.add( new GuiButton( 666, midwidth - 100, baseheight + 46, "Login" ) );
		this.buttonList.add( new GuiButton( 1337, 2, 2, 80, 20, "Back" ) );
	}
	
	public void drawScreen( int mouseX, int mouseY, float partialTicks )
	{
		this.drawDefaultBackground( );
		this.username.drawTextBox( );
		this.accessToken.drawTextBox( );
		
		for( GuiButton button : this.buttonList )
			button.drawButton( mc, mouseX, mouseY, partialTicks );

		_drawString( "Username", this.username.x - 2, this.username.y + ( this.username.height / 4 ) );
		_drawString( "Access Token", this.accessToken.x - 2, this.accessToken.y + ( this.accessToken.height / 4 ) );
		
		this.drawCenteredString( this.fontRenderer, Main.getManager( ).getManagerState( ),
				this.width / 2, this.height / 2 + 46, 0xFFFFFFFF );
	}
	
	protected void keyTyped( char typedChar, int keyCode )
	{
		try
		{
			super.keyTyped( typedChar, keyCode );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		
		this.username.textboxKeyTyped( typedChar, keyCode );
		this.accessToken.textboxKeyTyped( typedChar, keyCode );
	}
	
	protected void mouseClicked( int mouseX, int mouseY, int mouseButton )
	{
		try
		{
			super.mouseClicked( mouseX, mouseY, mouseButton );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
		
		this.username.mouseClicked( mouseX, mouseY, mouseButton );
		this.accessToken.mouseClicked( mouseX, mouseY, mouseButton );
	}
	
	protected void actionPerformed( GuiButton button )
	{
		if( button.id == 1337 ) // go back
			mc.displayGuiScreen( null );
		else if( button.id == 666 ) // login
		{
			if( username.getText( ) != null && accessToken.getText( ) != null )
				Main.getManager( ).login( username.getText( ), accessToken.getText( ) );
		}
	}
	
	public void _drawString( String text, int x, int y )
	{
		int width = this.fontRenderer.getStringWidth( text );
		
		this.drawString( this.fontRenderer, text, x - width - 2, y, 0xFFFFFFFF );
	}
}

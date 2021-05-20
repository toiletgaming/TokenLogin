package fuck.you.tokenlogin.gui;

import fuck.you.tokenlogin.Account;
import fuck.you.tokenlogin.Main;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiEditAccount extends GuiScreen
{
	private GuiTextField username;
	private GuiTextField accessToken;
	private GuiTextField clientToken;
	
	private GuiButton edit;
	private GuiButton back;

	private Account account;
	private int index;
	
	public GuiEditAccount( Account account, int index )
	{
		super( );
		this.account = account;
		this.index = index;
	}
	
	public void initGui( )
	{
		int midwidth = this.width / 2;
		int baseheight = this.height / 2 - 48;
		
		// lmao fuck this shit
		this.username = new GuiTextField( 444, this.fontRenderer,
				midwidth - 100, baseheight, 200, 20 );
		
		this.accessToken = new GuiTextField( 555, this.fontRenderer,
				midwidth - 100, baseheight + mc.fontRenderer.FONT_HEIGHT + 20 + 13, 200, 20 );
		
		this.clientToken = new GuiTextField( 666, this.fontRenderer,
				midwidth - 100, baseheight + ( mc.fontRenderer.FONT_HEIGHT * 2 ) + 66, 200, 20 );

		this.username.setMaxStringLength( 16 );
		this.accessToken.setMaxStringLength( 999 );
		this.clientToken.setMaxStringLength( 999 );
		
		this.username.setText( account.username );
		this.accessToken.setText( account.accessToken );
		this.clientToken.setText( account.clientToken );
		
		this.buttonList.add( edit = new GuiButton( 777, midwidth - 100, this.clientToken.y + this.clientToken.height + 5, 98, 20, "Edit" ) );
		this.buttonList.add( back = new GuiButton( 888, midwidth + 2, this.clientToken.y + this.clientToken.height + 5, 100, 20, "Back" ) );
	}
	
	public void drawScreen( int mouseX, int mouseY, float partialTicks )
	{
		edit.enabled =
				( username.getText( ) != null && username.getText( ).length( ) > 0 ) &&
				( accessToken.getText( ) != null && accessToken.getText( ).length( ) > 0 );
		
		this.drawDefaultBackground( );
		super.drawScreen( mouseX, mouseY, partialTicks );
		this.username.drawTextBox( );
		this.accessToken.drawTextBox( );
		this.clientToken.drawTextBox( );
		
		int midwidth = this.width / 2;

		this.drawCenteredString( this.fontRenderer, "Edit Account", midwidth, this.height / 4, -1 );
		
		this.drawCenteredString( this.fontRenderer, "Username", midwidth, this.username.y - mc.fontRenderer.FONT_HEIGHT - 4, -1 );
		this.drawCenteredString( this.fontRenderer, "Access Token", midwidth, this.accessToken.y - mc.fontRenderer.FONT_HEIGHT - 4, -1 );
		this.drawCenteredString( this.fontRenderer, "Client Token (optional)", midwidth, this.clientToken.y - mc.fontRenderer.FONT_HEIGHT - 4, -1 );
	}
	
	protected void actionPerformed( GuiButton button )
	{
		if( button.id == 888 ) // go back
			mc.displayGuiScreen( new GuiAltManager( ) );
		else if( button.id == 777 ) // edit account
		{
			if( edit.enabled )
			{
				Main.getAltManager( ).getAccounts( ).remove( index );
				Main.getAltManager( ).addAccount( account );
				mc.displayGuiScreen( new GuiAltManager( ) );
			}
		}
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
		this.clientToken.textboxKeyTyped( typedChar, keyCode );
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
		this.clientToken.mouseClicked( mouseX, mouseY, mouseButton );
	}
}

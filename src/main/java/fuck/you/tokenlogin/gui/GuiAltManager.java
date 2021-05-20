package fuck.you.tokenlogin.gui;

import com.mojang.realmsclient.gui.ChatFormatting;

import fuck.you.tokenlogin.Account;
import fuck.you.tokenlogin.Main;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiAltManager extends GuiScreen
{
	private GuiAltManager.List list;
	private int currentslot;
	
	private GuiButton login;
	private GuiButton edit;
	private GuiButton add;
	private GuiButton delete;
	
	public void initGui( )
	{
		list = new GuiAltManager.List( );
		
		this.buttonList.add( login = new GuiButton( 354, this.width / 2 - 154, this.height - 26, 70, 20, "Login" ) );
		this.buttonList.add( edit = new GuiButton( 355, this.width / 2 - 74, this.height - 26, 70, 20, "Edit" ) );
		this.buttonList.add( add = new GuiButton( 356, this.width / 2 + 4, this.height - 26, 70, 20, "Add" ) );
		this.buttonList.add( delete = new GuiButton( 357, this.width / 2 + 80, this.height - 26, 70, 20, "Delete" ) );
		this.buttonList.add( new GuiButton( 1337, 2, 2, 100, 20, "Back" ) );
		
		Main.getManager( ).resetState( );
	}
	
	public void drawScreen( int mouseX, int mouseY, float partialTicks )
	{
		if( Main.getAltManager( ).getAccounts( ).size( ) <= 0 )
		{
			login.enabled = false;
			edit.enabled = false;
			delete.enabled = false;
		}
		else
		{
			login.enabled = true;
			edit.enabled = true;
			delete.enabled = true;
		}
		
		list.drawScreen( mouseX, mouseY, partialTicks );
		super.drawScreen( mouseX, mouseY, partialTicks );

		drawCenteredString( mc.fontRenderer, "Access Token Login",
				this.width / 2, 8, -1 );
		drawCenteredString( mc.fontRenderer, Main.getManager( ).getManagerState( ),
				this.width / 2, 32 - mc.fontRenderer.FONT_HEIGHT - 4, -1 );
	}
	
	protected void actionPerformed( GuiButton button )
	{
		// remember: switch doesnt exist
		if( button.id == 1337 )
			mc.displayGuiScreen( null );
		else if( button.id == 356 ) // add
			mc.displayGuiScreen( new GuiAddAccount( ) );
		else if( button.id == 355 ) // edit
			mc.displayGuiScreen( new GuiEditAccount( Main.getAltManager( ).getAccounts( ).get( currentslot ), currentslot ) );
		else if( button.id == 354 ) // login
		{
			Account account = Main.getAltManager( ).getAccounts( ).get( currentslot );
			Main.getAltManager( ).getAccounts( ).remove( account );
			account.timeused = System.currentTimeMillis( );
			Main.getAltManager( ).addAccount( account );
			
			Main.getManager( ).login( account.username, account.accessToken, account.clientToken );
		}
		else if( button.id == 357 ) // delete
		{
			Account account = Main.getAltManager( ).getAccounts( ).get( currentslot );
			Main.getAltManager( ).removeAccount( account );
			mc.displayGuiScreen( new GuiAltManager( ) ); // refresh
		}
	}
	
	@Override
	public void handleMouseInput( )
	{
		try
		{
			super.handleMouseInput( );
		}
		catch( Exception e )
		{
			
		}
		
		list.handleMouseInput( );
	}
	
	class List extends GuiSlot
	{
		//public GuiSlot(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn)
		public List( )
		{
			super( GuiAltManager.this.mc, GuiAltManager.this.width, GuiAltManager.this.height, 32, GuiAltManager.this.height - 32, 14 );
		}

		@Override
		protected int getSize( ) 
		{
			return Main.getAltManager( ).getAccounts( ).size( );
		}

		@Override
		protected void elementClicked( int slotIndex, boolean isDoubleClick, int mouseX, int mouseY )
		{
			GuiAltManager.this.currentslot = slotIndex;
		}

		@Override
		protected boolean isSelected( int slotIndex )
		{
			return GuiAltManager.this.currentslot == slotIndex;
		}

		@Override
		protected void drawBackground( )
		{
			GuiAltManager.this.drawBackground( 0 );
		}

		@Override
		protected void drawSlot( int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks )
		{
			Account account = Main.getAltManager( ).getAccounts( ).get( slotIndex );
			String lastused = Main.getAltManager( ).getLastUsedAccount( );
			
			String username = account.username;
			if( username.equals( lastused ) )
				username += ChatFormatting.DARK_GRAY + " # last used account";
			
			GuiAltManager.this.drawString( mc.fontRenderer, username, xPos + 2, yPos + 1, -1 );
		}
	}
}

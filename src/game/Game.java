package game;

import javax.swing.SwingUtilities;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import controller.MasterController;
import data.MasterData;
import view.MasterView;

public class Game
{
	public static final String	GAMENAME	= "2D Game";
	public static final String	VERSION		= "v0.1";
	public static final int		WIDTH		= 800;
	public static final int		HEIGHT		= 480;

	private MasterData			data;
	private MasterView			view;
	private MasterController	controller;

	public static void main ( String[] args )
	{
		new Game().startGame();
	}

	private void startGame ()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			@Override
			public void run ()
			{
				try
				{
					load();
				}
				catch ( SlickException e )
				{
					e.printStackTrace();
				}
			}
		} );
	}

	private void load () throws SlickException
	{
		data = new MasterData( GAMENAME + " " + VERSION, WIDTH, HEIGHT );
		view = new MasterView( data );
		controller = new MasterController( data, view );
		view.getLocalView().setUpdateListener( controller.getLocalController() );
		view.getMenuView().setUpdateListener( controller.getMenuController() );
		startView();
	}

	private void startView ()
	{
		AppGameContainer appGC;

		try
		{
			appGC = new AppGameContainer( view );
			appGC.setDisplayMode( WIDTH, HEIGHT, false );
			appGC.start();
		}
		catch ( SlickException e )
		{
			e.printStackTrace();
		}
	}
}

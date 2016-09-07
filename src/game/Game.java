package game;

import javax.swing.SwingUtilities;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import view.View;

public class Game
{
	public static final String	GAMENAME	= "2D Game";
	public static final int		WIDTH		= 800;
	public static final int		HEIGHT		= 480;

	private Model		model;
	private View		view;
	private Controller	controller;

	public static void main( String [] args )
	{
		new Game().startGame();
	}

	private void startGame()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					load();
				}
				catch( SlickException e )
				{
					e.printStackTrace();
				}
			}
		} );
	}

	private void load() throws SlickException
	{
		model = new Model( GAMENAME, WIDTH, HEIGHT );
		view = new View( model );
		controller = new Controller( model, view );
		view.getPlay().setUpdateListener( controller );
		startView();
	}

	private void startView()
	{
		AppGameContainer appGC;

		try
		{
			appGC = new AppGameContainer( view );
			appGC.setDisplayMode( WIDTH, HEIGHT, false );
			appGC.start();
		}
		catch( SlickException e )
		{
			e.printStackTrace();
		}
	}
}

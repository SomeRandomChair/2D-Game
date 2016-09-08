package view.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import sun.applet.Main;
import view.StateEnum;

public class Menu extends BasicGameState
{
	final private int	state;

	private Image		menuBackground;
	private Image		playNow;
	private Image		exitGame;
	private Image		rightArrow;

	private int			centreX;
	private int			centreY;

	private int			arrowYPos;
	private boolean		playNowPointer;

	int					buttonTimer	= 0;
	boolean				isButtonStillPressedDown;
	boolean				isButtonStiiiillPressedDown;

	public Menu( int state )
	{
		this.state = state;
	}

	@Override
	public int getID ()
	{
		return state;
	}

	@SuppressWarnings ( "unused" )
	private static void test ()
	{
		InputStream res = Main.class.getResourceAsStream( "/my-resources.txt" );

		BufferedReader reader = new BufferedReader( new InputStreamReader( res ) );
		String line = null;
		try
		{
			while ( ( line = reader.readLine() ) != null )
			{
				System.out.println( line );
			}
			reader.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void init ( GameContainer gc, StateBasedGame sbg ) throws SlickException
	{
		String path = Paths.get( "resources", "Graphics", "Menu" ).toString();
		menuBackground = new Image( Paths.get( path, "menuBackground.PNG" ).toString() );
		playNow = new Image( Paths.get( path, "playNow.PNG" ).toString() );
		exitGame = new Image( Paths.get( path, "exitGame.PNG" ).toString() );
		rightArrow = new Image( Paths.get( path, "rightArrow2.PNG" ).toString() );

		centreX = (int) ( 0.5 * gc.getWidth() );
		centreY = (int) ( 0.5 * gc.getHeight() );

		arrowYPos = centreY + 30;
		playNowPointer = true;
	}

	@Override
	public void render ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{

		menuBackground.draw( 0, 0 );
		playNow.draw( centreX, centreY + 30 );
		exitGame.draw( centreX, centreY + 80 );
		rightArrow.draw( centreX - 120, playNowPointer ? arrowYPos : arrowYPos + 50 );

	}

	@Override
	public void update ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		buttonTimer = buttonTimer + delta;

		Input input = gc.getInput();

		if ( !( input.isKeyDown( Input.KEY_DOWN ) || input.isKeyDown( Input.KEY_UP ) ) )
		{
			isButtonStillPressedDown = false;
			isButtonStiiiillPressedDown = false;
		}

		if ( input.isKeyDown( Input.KEY_DOWN ) || input.isKeyDown( Input.KEY_UP ) )
		{
			if ( !isButtonStillPressedDown )
			{
				playNowPointer = !playNowPointer;
				buttonTimer = 0;
				isButtonStillPressedDown = true;
			}
			else
			{
				if ( buttonTimer > 300 && isButtonStillPressedDown && !isButtonStiiiillPressedDown )
				{
					playNowPointer = !playNowPointer;
					buttonTimer = 0;
					isButtonStiiiillPressedDown = true;

				}
				else if ( buttonTimer > 100 && isButtonStillPressedDown && isButtonStiiiillPressedDown )
				{
					playNowPointer = !playNowPointer;
					buttonTimer = 0;
				}
			}
		}

		if ( input.isKeyDown( Input.KEY_ENTER ) )
		{
			if ( playNowPointer )
			{
				sbg.enterState( StateEnum.PLAY.getValue(), new FadeOutTransition( Color.black, 1000 ),
						new FadeInTransition( Color.black, 400 ) );
			}
			else
				System.exit( 0 );
		}
	}
}

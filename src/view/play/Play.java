package view.play;

import game.Model;
import game.UpdateListener;

import java.util.ArrayList;
import java.util.Collections;

import maps.AbstractGameMap;
import maps.ImageObject;
import maps.RenderableImage;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import save.xml.Save;

public class Play extends BasicGameState
{
	final private int		state;
	private UpdateListener	updateListener;
	private Model			model;

	public Play( Model model, int state )
	{
		this.model = model;
		this.state = state;
	}

	public void setUpdateListener ( UpdateListener updateListener )
	{
		this.updateListener = updateListener;
	}

	@Override
	public int getID ()
	{
		return state;
	}

	@Override
	public void init ( GameContainer gc, StateBasedGame sbg ) throws SlickException
	{
		model.getCharacter().getCharacterAnimation().createCharacterAnimation();
		charCentralPosX = ( (float) 0.5 * ( gc.getWidth() - 32 ) );
		charCentralPosY = ( (float) 0.5 * ( gc.getHeight() - 32 ) );

		loadGame( model.getSaveDataHandler().getSaveFiles().lastEntry().getValue() );
	}

	public void loadGame ( Save save ) throws SlickException
	{
		setupRenderables( save.getGameMap() );
		model.getCharacter().getCharacterPhysics().setX( save.getX() );
		model.getCharacter().getCharacterPhysics().setY( save.getY() );
		model.getCharacter().getCharacterAnimation().loadLookDirection( save.getDirection() );
	}

	private void setupRenderables ( AbstractGameMap newMap ) throws SlickException
	{
		model.setMap( newMap );
		model.getMap().loadMap();

		model.setBackgroundRenderableImages( new ArrayList<RenderableImage>() );
		model.setStandardRenderableImages( new ArrayList<RenderableImage>() );
		model.setTopRenderableImages( new ArrayList<RenderableImage>() );

		for (ImageObject imageObject : model.getMap().getImageObjects())

			for (RenderableImage renderableImage : imageObject.getRenderables())

				switch ( renderableImage.getZType() )
				{
					case BACKGROUND:
						model.getBackgroundRenderableImages().add( renderableImage );
						break;

					case STANDARD:
						model.getStandardRenderableImages().add( renderableImage );
						break;

					case TOP:
						model.getTopRenderableImages().add( renderableImage );
						break;
				}

		Collections.sort( model.getStandardRenderableImages(), model.getZComparator() );
	}

	@Override
	public void render ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{
		drawMap( gc, sbg, g );

		if ( model.isEscMenu() )
		{
			g.drawString( "Resume (R)", 250, 100 );
			g.drawString( "Main Menu (M)", 250, 150 );
			g.drawString( "Quit (Q)", 250, 200 );
			g.drawString( "Save (S)", 250, 250 );
			g.drawString( "Load (L)", 250, 300 );
		}
	}

	private float	backgroundXPos;
	private float	backgroundYPos;
	private boolean	characterDrawnYet;

	private float	charCentralPosX;
	private float	charCentralPosY;
	private float	positionOfCharacterOnScreenX;
	private float	positionOfCharacterOnScreenY;

	private void drawMap ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{
		findScreenPlacement();

		characterDrawnYet = false;

		model.getMap().getBackground().draw( backgroundXPos, backgroundYPos );

		for (RenderableImage backgroundRenderableImage : model.getBackgroundRenderableImages())
			backgroundRenderableImage.getRenderable().draw( backgroundXPos + backgroundRenderableImage.getX(),
					backgroundYPos + backgroundRenderableImage.getY() );

		for (RenderableImage standardRenderableImage : model.getStandardRenderableImages())
		{
			if ( !characterDrawnYet )
				if ( standardRenderableImage.getYRenderPos()
						+ (int) standardRenderableImage.getY() > model.getCharacter().getCharacterPhysics().getY()
								+ model.getCharacter().getCharacterPhysics().getCollBoxYOffset() )
				{
					model.getCharacter().getCharacterAnimation().getCharacterAnimation().draw( positionOfCharacterOnScreenX,
							positionOfCharacterOnScreenY );
					characterDrawnYet = true;
				}
			standardRenderableImage.getRenderable().draw( backgroundXPos + standardRenderableImage.getX(),
					backgroundYPos + standardRenderableImage.getY() );
		}

		if ( !characterDrawnYet )
			model.getCharacter().getCharacterAnimation().getCharacterAnimation().draw( positionOfCharacterOnScreenX,
					positionOfCharacterOnScreenY );

		for (RenderableImage topRenderableImage : model.getTopRenderableImages())
			topRenderableImage.getRenderable().draw( backgroundXPos + topRenderableImage.getX(),
					backgroundYPos + topRenderableImage.getY() );
	}

	private void findScreenPlacement ()
	{
		backgroundXPos = charCentralPosX - (float) ( model.getCharacter().getCharacterPhysics().getX() );
		backgroundYPos = charCentralPosY - (float) ( model.getCharacter().getCharacterPhysics().getY() );
		positionOfCharacterOnScreenX = charCentralPosX;
		positionOfCharacterOnScreenY = charCentralPosY;

		if ( model.getMap().getBackground().getWidth() < model.WIDTH )
		{
			int temp = ( model.WIDTH - model.getMap().getBackground().getWidth() ) >> 1;
			positionOfCharacterOnScreenX += temp - backgroundXPos;
			backgroundXPos = temp;
		}
		else if ( backgroundXPos > 0 )
		{
			positionOfCharacterOnScreenX -= backgroundXPos;
			backgroundXPos = 0;
		}
		else if ( model.getMap().getBackground().getWidth() < model.WIDTH - backgroundXPos )
		{
			positionOfCharacterOnScreenX += model.WIDTH - backgroundXPos - model.getMap().getBackground().getWidth();
			backgroundXPos = model.WIDTH - model.getMap().getBackground().getWidth();
		}

		if ( model.getMap().getBackground().getHeight() < model.HEIGHT )
		{
			int temp = ( model.HEIGHT - model.getMap().getBackground().getHeight() ) >> 1;
			positionOfCharacterOnScreenY += temp - backgroundYPos;
			backgroundYPos = temp;
		}
		else if ( backgroundYPos > 0 )
		{
			positionOfCharacterOnScreenY -= backgroundYPos;
			backgroundYPos = 0;
		}
		else if ( model.HEIGHT - backgroundYPos > model.getMap().getBackground().getHeight() )
		{
			positionOfCharacterOnScreenY += model.HEIGHT - backgroundYPos - model.getMap().getBackground().getHeight();
			backgroundYPos = model.HEIGHT - model.getMap().getBackground().getHeight();
		}
	}

	@Override
	public void update ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		fireUpdateEvent( gc, sbg, delta );
	}

	public void fireUpdateEvent ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		if ( updateListener != null )
			updateListener.update( gc, sbg, delta );
	}

}

package view.local;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import data.MasterData;
import data.local.LocalData;
import maps.RenderableImage;
import save.xml.Save;
import view.MasterView;
import view.UpdateEventFirer;
import view.UpdateListener;

public class LocalView extends BasicGameState implements UpdateEventFirer
{
	private MasterView		masterView;
	final private int		state;
	private UpdateListener	updateListener;
	private MasterData		masterData;
	private LocalData		localData;

	public LocalView( MasterView view, MasterData data, int state )
	{
		this.masterView = view;
		this.masterData = data;
		this.localData = data.getLocalData();
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
		masterData.getCharacter().getCharacterAnimation().createCharacterAnimation();
		characterCentralPosition = new Point ( (float) 0.5 * ( gc.getWidth() - 32 ), (float) 0.5 * ( gc.getHeight() - 32 ) );

		loadGame( masterData.getSaveDataHandler().getSaveFiles().lastEntry().getValue() );
	}

	public void loadGame ( Save save ) throws SlickException
	{
		localData.setupRenderables( save.getGameMap() );
		masterData.getCharacter().getCharacterPhysics().setPosition( save.getX(), save.getY() );
		masterData.getCharacter().getCharacterAnimation().loadLookDirection( save.getDirection() );
	}

	@Override
	public void render ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{
		drawMap( gc, sbg, g );

		if ( localData.isEscMenu() )
		{
			g.drawString( "Resume (R)", 250, 100 );
			g.drawString( "Main Menu (M)", 250, 150 );
			g.drawString( "Quit (Q)", 250, 200 );
			g.drawString( "Save (S)", 250, 250 );
			g.drawString( "Load (L)", 250, 300 );
		}
	}

	private Point	backgroundLocation, characterCentralPosition, characterScreenPosition;
	private boolean	characterDrawnYet;

	private void drawMap ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{
		findScreenPlacement();

		characterDrawnYet = false;

		localData.getMap().getBackground().draw( backgroundLocation.getX(), backgroundLocation.getY() );

		for ( RenderableImage backgroundRenderableImage : localData.getBackgroundRenderableImages() )
			backgroundRenderableImage.getRenderable().draw( backgroundLocation.getX() + backgroundRenderableImage.getX(),
					backgroundLocation.getY() + backgroundRenderableImage.getY() );

		for ( RenderableImage standardRenderableImage : localData.getStandardRenderableImages() )
		{
			if ( !characterDrawnYet )
				if ( standardRenderableImage.getYRenderPos()
						+ (int) standardRenderableImage.getY() > masterData.getCharacter().getCharacterPhysics().getPosition().getY()
								+ masterData.getCharacter().getCharacterPhysics().getCollBoxYOffset() )
				{
					masterData.getCharacter().getCharacterAnimation().getCharacterAnimation().draw( characterScreenPosition.getX(),
							characterScreenPosition.getY() );
					characterDrawnYet = true;
				}
			standardRenderableImage.getRenderable().draw( backgroundLocation.getX() + standardRenderableImage.getX(),
					backgroundLocation.getY() + standardRenderableImage.getY() );
		}

		if ( !characterDrawnYet )
			masterData.getCharacter().getCharacterAnimation().getCharacterAnimation().draw( characterScreenPosition.getX(),
					characterScreenPosition.getY() );

		for ( RenderableImage topRenderableImage : localData.getTopRenderableImages() )
			topRenderableImage.getRenderable().draw( backgroundLocation.getX() + topRenderableImage.getX(),
					backgroundLocation.getY() + topRenderableImage.getY() );
	}

	private void findScreenPlacement ()
	{
		backgroundLocation = new Point( characterCentralPosition.getX() - (float) ( masterData.getCharacter().getCharacterPhysics().getPosition().getX() ),
				characterCentralPosition.getY() - (float) ( masterData.getCharacter().getCharacterPhysics().getPosition().getY() ) );
		
		characterScreenPosition = new Point ( characterCentralPosition.getX(), characterCentralPosition.getY() );

		if ( localData.getMap().getBackground().getWidth() < masterData.WIDTH )
		{
			int temp = ( masterData.WIDTH - localData.getMap().getBackground().getWidth() ) >> 1;
			characterScreenPosition.setX( characterScreenPosition.getX() + temp - backgroundLocation.getX() );
			backgroundLocation.setX( temp );
		}
		else if ( backgroundLocation.getX() > 0 )
		{
			characterScreenPosition.setX( characterScreenPosition.getX() - backgroundLocation.getX() );
			backgroundLocation.setX( 0 );
		}
		else if ( localData.getMap().getBackground().getWidth() < masterData.WIDTH - backgroundLocation.getX() )
		{
			characterScreenPosition.setX( characterScreenPosition.getX() + masterData.WIDTH - backgroundLocation.getX() - localData.getMap().getBackground().getWidth() );
			backgroundLocation.setX( masterData.WIDTH - localData.getMap().getBackground().getWidth() );
		}

		if ( localData.getMap().getBackground().getHeight() < masterData.HEIGHT )
		{
			int temp = ( masterData.HEIGHT - localData.getMap().getBackground().getHeight() ) >> 1;
			characterScreenPosition.setY( characterScreenPosition.getY() + temp - backgroundLocation.getY() );
			backgroundLocation.setY( temp );
		}
		else if ( backgroundLocation.getY() > 0 )
		{
			characterScreenPosition.setY( characterScreenPosition.getY() - backgroundLocation.getY() );
			backgroundLocation.setY( 0 );
		}
		else if ( localData.getMap().getBackground().getHeight() < masterData.HEIGHT - backgroundLocation.getY() )
		{
			characterScreenPosition.setY( characterScreenPosition.getY() + masterData.HEIGHT - backgroundLocation.getY() - localData.getMap().getBackground().getHeight() );
			backgroundLocation.setY( masterData.HEIGHT - localData.getMap().getBackground().getHeight() );
		}
	}

	@Override
	public void update ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		if ( updateListener != null )
			updateListener.update( gc, sbg, delta );
	}
}

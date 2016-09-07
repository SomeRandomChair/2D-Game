package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import maps.AbstractGameMap;
import maps.ImageObject;
import maps.MapChange;
import maps.RenderableImage;

import org.newdawn.slick.SlickException;

import save.xml.SaveDataHandler;
import character.PlayerCharacter;
import character.PlayerCharacterCreation;

public class Model
{
	public final String					GAMENAME;
	public final int					WIDTH;
	public final int					HEIGHT;

	private SaveDataHandler				saveDataHandler;
	private PlayerCharacter				character;
	private AbstractGameMap				map;

	private Comparator<RenderableImage>	zComparator;

	private List<RenderableImage>		backgroundRenderableImages;
	private List<RenderableImage>		standardRenderableImages;
	private List<RenderableImage>		topRenderableImages;

	private boolean						escMenu;

	public Model( String gamename, int width, int height )
	{
		this.GAMENAME = gamename;
		this.WIDTH = width;
		this.HEIGHT = height;

		character = PlayerCharacterCreation.createKnight( "Alex" );

		saveDataHandler = new SaveDataHandler();
		saveDataHandler.read();

		zComparator = new Comparator<RenderableImage>()
		{
			@Override
			public int compare( RenderableImage obj1, RenderableImage obj2 )
			{
				return Integer.signum( ( (int) obj1.getY() + obj1.getYRenderPos() ) - ( (int) obj2.getY() + obj2.getYRenderPos() ) );
			}
		};
	}

	public SaveDataHandler getSaveDataHandler()
	{
		return saveDataHandler;
	}

	public void setSaveDataHandler( SaveDataHandler saveDataHandler )
	{
		this.saveDataHandler = saveDataHandler;
	}

	public PlayerCharacter getCharacter()
	{
		return character;
	}

	public void setCharacter( PlayerCharacter character )
	{
		this.character = character;
	}

	public AbstractGameMap getMap()
	{
		return map;
	}

	public void setMap( AbstractGameMap map )
	{
		this.map = map;
	}

	public Comparator<RenderableImage> getZComparator()
	{
		return zComparator;
	}

	public List<RenderableImage> getBackgroundRenderableImages()
	{
		return backgroundRenderableImages;
	}

	public void setBackgroundRenderableImages( List<RenderableImage> backgroundRenderableImages )
	{
		this.backgroundRenderableImages = backgroundRenderableImages;
	}

	public List<RenderableImage> getStandardRenderableImages()
	{
		return standardRenderableImages;
	}

	public void setStandardRenderableImages( List<RenderableImage> standardRenderableImages )
	{
		this.standardRenderableImages = standardRenderableImages;
	}

	public List<RenderableImage> getTopRenderableImages()
	{
		return topRenderableImages;
	}

	public void setTopRenderableImages( List<RenderableImage> topRenderableImages )
	{
		this.topRenderableImages = topRenderableImages;
	}

	public boolean isEscMenu()
	{
		return escMenu;
	}

	public void setEscMenu( boolean escMenu )
	{
		this.escMenu = escMenu;
	}

	public void changeMap( MapChange mapChange ) throws SlickException
	{
		setupRenderables( mapChange.getMapTo() );

		getCharacter().getCharacterPhysics().setX( getCharacter().getCharacterPhysics().getX() + mapChange.getChangeInX() );
		getCharacter().getCharacterPhysics().setY( getCharacter().getCharacterPhysics().getY() + mapChange.getChangeInY() );
	}

	public void setupRenderables( AbstractGameMap newMap ) throws SlickException
	{
		this.map = newMap;
		map.loadMap();

		backgroundRenderableImages = new ArrayList<RenderableImage>();
		standardRenderableImages = new ArrayList<RenderableImage>();
		topRenderableImages = new ArrayList<RenderableImage>();

		for( ImageObject imageObject : map.getImageObjects() )

			for( RenderableImage renderableImage : imageObject.getRenderables() )

				switch( renderableImage.getZType() )
				{
					case BACKGROUND :
						backgroundRenderableImages.add( renderableImage );
						break;

					case STANDARD :
						standardRenderableImages.add( renderableImage );
						break;

					case TOP :
						topRenderableImages.add( renderableImage );
						break;
				}

		Collections.sort( standardRenderableImages, zComparator );
	}

}

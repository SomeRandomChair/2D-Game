package data;

import org.newdawn.slick.SlickException;

import character.PlayerCharacter;
import character.PlayerCharacterCreation;
import data.battle.BattleData;
import data.local.LocalData;
import data.menu.MenuData;
import data.world.WorldData;
import maps.AbstractGameMap;
import save.xml.SaveDataHandler;

public class MasterData
{
	public final String		GAMENAME;
	public final int		WIDTH;
	public final int		HEIGHT;

	private SaveDataHandler	saveDataHandler;
	private PlayerCharacter	character;

	private BattleData		battleData;
	private LocalData		localData;
	private MenuData		menuData;
	private WorldData		worldData;

	public MasterData( String gamename, int width, int height ) throws SlickException
	{
		this.GAMENAME = gamename;
		this.WIDTH = width;
		this.HEIGHT = height;
		
		character = PlayerCharacterCreation.createKnight( "Alex" );
		
		saveDataHandler = new SaveDataHandler();
		saveDataHandler.read();

		menuData = new MenuData( this );
		localData = new LocalData( this );
	}

	public BattleData getBattleData ()
	{
		return battleData;
	}

	public LocalData getLocalData ()
	{
		return localData;
	}

	public MenuData getMenuData ()
	{
		return menuData;
	}

	public WorldData getWorldData ()
	{
		return worldData;
	}

	public SaveDataHandler getSaveDataHandler ()
	{
		return saveDataHandler;
	}

	public PlayerCharacter getCharacter ()
	{
		return character;
	}
}

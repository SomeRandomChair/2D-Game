package character;

import java.util.ArrayList;
import java.util.List;

import maps.AbstractGameMap;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;

import quests.Quest;
import save.xml.Save;
import equipment.ForearmArmour;
import equipment.HeadArmour;
import equipment.LegArmour;
import equipment.TorsoArmour;
import equipment.Weapon;

public class PlayerCharacter
{
	private String						name;
	private PlayerCharacterStats		characterStats;
	private PlayerCharacterEquipment	characterEquipment;
	private PlayerCharacterPhysics		physics;
	private PlayerCharacterAnimation	animation;
	private List<Quest>					quests;

	PlayerCharacter( String name, Direction direction, PlayerCharacterStats characterStats, Weapon weapon, HeadArmour headArmour,
			TorsoArmour torsoArmour, LegArmour legArmour, ForearmArmour forearmArmour )
	{
		this.name = name;
		this.characterStats = characterStats;
		this.characterEquipment = new PlayerCharacterEquipment( weapon, headArmour, torsoArmour, legArmour, forearmArmour );
		this.physics = new PlayerCharacterPhysics();
		this.animation = new PlayerCharacterAnimation( direction, physics.getSpeed(), 0.57 );
		this.quests = new ArrayList<Quest>();
	}

	public PlayerCharacter( String name, Save save )
	{
		this( name, null, null, null, null, null, null, null );
		this.name = name;
		this.physics = new PlayerCharacterPhysics();
		this.animation = new PlayerCharacterAnimation( save.getDirection(), physics.getSpeed(), 0.57 );

		List<Shape> collShapes = new ArrayList<Shape>();
		collShapes.add( physics.getCollisionBox() );
	}

	public String getName ()
	{
		return name;
	}

	public PlayerCharacterStats getCharacterStats ()
	{
		return characterStats;
	}

	public PlayerCharacterEquipment getCharacterEquipment ()
	{
		return characterEquipment;
	}

	public List<Quest> getQuests ()
	{
		return quests;
	}

	public PlayerCharacterPhysics getCharacterPhysics ()
	{
		return physics;
	}

	public PlayerCharacterAnimation getCharacterAnimation ()
	{
		return animation;
	}

	private int		relX;
	private int		relY;
	private boolean	isShiftDown;

	public boolean moveCharacter ( int delta, Input input, AbstractGameMap map )
	{
		relX = 0;
		relY = 0;

		if ( input.isKeyDown( Input.KEY_UP ) )
			relY++;

		if ( input.isKeyDown( Input.KEY_RIGHT ) )
			relX++;

		if ( input.isKeyDown( Input.KEY_DOWN ) )
			relY--;

		if ( input.isKeyDown( Input.KEY_LEFT ) )
			relX--;

		isShiftDown = input.isKeyDown( Input.KEY_LSHIFT );
		boolean return_bool = physics.moveCharacter( map, delta, relX, relY, isShiftDown );
		animation.moveCharacter( delta, relX, relY, isShiftDown );
		return return_bool;
	}
}

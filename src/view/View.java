package view;

import game.Model;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import view.battle.Battle;
import view.menu.Menu;
import view.play.Play;

public class View extends StateBasedGame
{
	private Model	model;

	private Play	play;
	private Menu	menu;
	private Battle	battle;

	public View( Model model )
	{
		super( model.GAMENAME );
		this.model = model;
		this.play = new Play( model, StateEnum.PLAY.getValue() );
		this.menu = new Menu( StateEnum.MENU.getValue() );
		this.battle = new Battle( StateEnum.BATTLE.getValue() );

		this.addState( menu );
		this.addState( play );
		this.addState( battle );
	}

	@Override
	public void initStatesList ( GameContainer gc ) throws SlickException
	{
		this.getState( StateEnum.MENU.getValue() ).init( gc, this );
		this.getState( StateEnum.PLAY.getValue() ).init( gc, this );
		this.getState( StateEnum.BATTLE.getValue() ).init( gc, this );

		this.enterState( StateEnum.MENU.getValue() );
	}

	public void start ()
	{
		AppGameContainer appGC;

		try
		{
			appGC = new AppGameContainer( this );
			appGC.setDisplayMode( model.WIDTH, model.HEIGHT, false );
			appGC.start();
		}
		catch ( SlickException e )
		{
			e.printStackTrace();
		}
	}

	public Play getPlay ()
	{
		return play;
	}

	public Menu getMenu ()
	{
		return menu;
	}

	public Battle getBattle ()
	{
		return battle;
	}
}

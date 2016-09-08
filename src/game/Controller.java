package game;

import java.time.LocalDateTime;

import maps.MapChange;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import save.xml.Save;
import view.StateEnum;
import view.View;

public class Controller implements UpdateListener
{
	private Model	model;
	private View	view;

	Controller( Model model, View view )
	{
		this.model = model;
		this.view = view;
	}

	@Override
	public void update ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		Input input = gc.getInput();

		if ( input.isKeyDown( Input.KEY_ESCAPE ) )
		{
			model.setEscMenu( true );
		}

		if ( model.isEscMenu() )
		{
			escapePressed( gc, sbg, delta );
		}

		boolean hasCharacterMoved = model.getCharacter().moveCharacter( delta, input, model.getMap() );

		if ( hasCharacterMoved && model.getMap().startBattle( model.getCharacter().getCharacterPhysics().getX(),
				model.getCharacter().getCharacterPhysics().getY(), delta ) )
		{
			model.getCharacter().getCharacterAnimation().stopCharacterAnimation();
			sbg.enterState( StateEnum.BATTLE.getValue(), new FadeOutTransition( Color.black, 1000 ),
					new FadeInTransition( Color.black, 1000 ) );
		}

		for (MapChange mapChangeArea : model.getMap().getMapChangeAreas())

			if ( mapChangeArea.getMapChangeArea().intersects( model.getCharacter().getCharacterPhysics().getCollisionBox() ) )
			{
				model.changeMap( mapChangeArea );
			}

	}

	private boolean escapePressed ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		Input input = gc.getInput();

		if ( input.isKeyDown( Input.KEY_R ) )
			model.setEscMenu( false );

		if ( input.isKeyDown( Input.KEY_M ) )
		{
			model.setEscMenu( false );
			try
			{
				model.getSaveDataHandler().write();
			}
			catch ( Exception e )
			{
				System.out.println( "Can't write new save files!" );
			}
			sbg.enterState( 0 );
		}
		if ( input.isKeyDown( Input.KEY_Q ) )
		{
			model.setEscMenu( false );
			try
			{
				model.getSaveDataHandler().write();
			}
			catch ( Exception e )
			{
				System.out.println( "Can't write new save files!" );
			}
			System.exit( 0 );
		}
		if ( input.isKeyDown( Input.KEY_S ) )
		{
			int charXPos = (int) model.getCharacter().getCharacterPhysics().getX();
			int charYPos = (int) model.getCharacter().getCharacterPhysics().getY();
			Save saveData = new Save( "SaveName", model.getMap(), charXPos, charYPos,
					model.getCharacter().getCharacterAnimation().getLookDirection(), "No Data" );

			model.getSaveDataHandler().addSave( LocalDateTime.now().toString(), saveData );
			try
			{
				Thread.sleep( 500 );
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
		if ( input.isKeyDown( Input.KEY_L ) )
		{
			view.getPlay().loadGame( model.getSaveDataHandler().getSaveFiles().lastEntry().getValue() );
			model.setEscMenu( false );
		}
		return true;
	}
}

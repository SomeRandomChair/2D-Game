package controller;

import controller.local.LocalController;
import controller.menu.MenuController;
import data.MasterData;
import view.MasterView;

public class MasterController
{
	private MasterData		masterData;
	private MasterView		masterView;

	private LocalController	localController;
	private MenuController	menuController;

	public MasterController( MasterData masterData, MasterView masterView )
	{
		this.masterData = masterData;
		this.masterView = masterView;

		localController = new LocalController( this );
		menuController = new MenuController( this );
	}

	public MasterData getMasterData ()
	{
		return masterData;
	}

	public MasterView getMasterView ()
	{
		return masterView;
	}

	public LocalController getLocalController ()
	{
		return localController;
	}

	public MenuController getMenuController ()
	{
		return menuController;
	}

}

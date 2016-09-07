package view.font;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.SlickException;

import save.SaveLocation;

public class MaiandraGD
{
	private AngelCodeFont the32ptFont;
	private AngelCodeFont the24ptFont;
	private AngelCodeFont the20ptFont;
	private AngelCodeFont the16ptFont;
	
	public MaiandraGD () throws SlickException
	{
		the32ptFont = new AngelCodeFont ( SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 32pt.fnt", SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 32pt_0.png" );
		the24ptFont = new AngelCodeFont ( SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 24pt.fnt", SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 24pt_0.png" );
		the20ptFont = new AngelCodeFont ( SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 20pt.fnt", SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 20pt_0.png" );
		the16ptFont = new AngelCodeFont ( SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 16pt.fnt", SaveLocation.LOCATION + "\\Graphics\\Fonts\\Maiandra GD Bitmap Font 16pt_0.png" );
	}

	public AngelCodeFont get32ptFont ()
	{
		return the32ptFont;
	}

	public AngelCodeFont get24ptFont ()
	{
		return the24ptFont;
	}

	public AngelCodeFont get20ptFont ()
	{
		return the20ptFont;
	}

	public AngelCodeFont get16ptFont ()
	{
		return the16ptFont;
	}
}

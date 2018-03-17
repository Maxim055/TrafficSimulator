package Traff;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TraffLight {
	public String color = "green";
	private static int i = 0;
	public double X;
	public double ImagineX;
	public double Y;
	public ImageView Tl;
	private Image imgreen = new Image("file:src/images/tlgreen.png");
	private Image imred = new Image("file:src/images/tlred.png");
	
	public TraffLight(double d, double e, Image  Way) {
		X = d;
		Y = e;
		if(Y >  Way.getHeight() + 50)
		{
			ImagineX = X + Way.getWidth();
		}
		else
		{
			ImagineX = X;
		}
		Tl = new ImageView("file:src/images/tlgreen.png");
		Tl.setPickOnBounds(true);
		Tl.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		
	     @Override
	     public void handle(MouseEvent event) {
	    	 	 ChangeIm(i);
	    	 	 i++;
	         event.consume();
	     }
		});
	}
	
	public void ChangeIm(int i) {
		if(i % 2 == 0) {
			Tl.setImage(imgreen);
			color = "green";
		}
		else {
			Tl.setImage(imred);
			color = "red";
		}
	}
}

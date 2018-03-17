package Traff;

import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Car {
	public double DistanceFromRedTrLight;
	public boolean StoppedUnderTRL = false;
	public boolean StoppedWithButton  = false;
	public double DeltaX = 3000;
	public boolean stopped = false;
	public boolean accidented = false;
	public double V = 0;
	public double X = 10;
	public double ImagineX = X;
	public double Vmax;
	public Image CarIm;
	public int Y = 160;
	public double aride;
	public double a = 30;
	private double WayHeight;
	private double WayWidth;
	
	//Car constructor
	public Car(int v, Image Way) {
		if(v == 2)
		{
			Y = 100;
		}
		WayHeight = Way.getHeight();
		WayWidth = Way.getWidth();
		aride = a;
		Random r = new Random();
		int Low = 50;
		int High = 100;
		int Result = r.nextInt(High-Low) + Low;
		int res = r.nextInt(3) + 1;
		CarIm   = new Image( "file:src/images/" + res + ".png");
		this.Vmax = Result;
	}
	
	//GetBoundary		
	public Rectangle2D getBoundary() {
		return new Rectangle2D(X, Y, CarIm.getWidth() - 10, CarIm.getHeight());
	}
	
	//solving cars position	
	public void ride(double time) {
		if(!accidented) {
			if(V < 0) {
				stopped = true;
				V  = 0;
				a = 0;
			}
			else
			{
				if(V < Vmax) {
					V += this.a * time;
				}
				else
				{
					if(a < 0) { 
						V += this.a * time;
					}
					else {
						V = Vmax;
					}			
				}
				X += V * time;
				if(Y >  WayHeight + 50)
				{
					ImagineX = X + WayWidth;
				}
				else
				{
					ImagineX = X;
				}
			}
		}		
	}
	
	//change a
	public void ChangeA(double t) {
		a = t;
	}
	
	//car stop
	public void stop(Car car) {
		this.a = ( car.V * car.V - this.V * this.V) / (2 * (this.DeltaX - 2*this.CarIm.getHeight()));
		if(this.a < -70) {
			this.a = - 70;
		}
		if(this.a > 30) {
			this.a = 30;
		}
	}
	
	public void StopUnderTRL(double distance)
	{
		this.a =  - (this.V * this.V) / (2 * distance); 
		this.StoppedUnderTRL = true;
	}
}
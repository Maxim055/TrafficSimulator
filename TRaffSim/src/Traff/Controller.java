package Traff;

import java.util.Vector;

import javafx.scene.image.Image;

 class Controller {
	public StreetLine st1 = new StreetLine();
	public StreetLine st2 = new StreetLine();
	private Image way = new Image("file:src/images/d.jpg");;
	public Vector<TraffLight> TrLight = new Vector<TraffLight>();
	
	private boolean intersects(Car car1, Car car2) {
		return car1.getBoundary().intersects(car2.getBoundary());	
	}
	
	private void SetDistFromRedTRL(int CarsIndex, StreetLine st)
	{
		double min = 32000;
		for(int i = 0; i < TrLight.size(); ++i)
		{
			if(TrLight.get(i).color == "red" && TrLight.get(i).ImagineX > st.CarList.get(CarsIndex).ImagineX)
			{
				if(TrLight.get(i).ImagineX - st.CarList.get(CarsIndex).ImagineX < min)
				{
					min = TrLight.get(i).ImagineX - st.CarList.get(CarsIndex).ImagineX;
				}
			}
		}
		st.CarList.get(CarsIndex).DistanceFromRedTrLight = min;
	}
		
	public void OurAppMozg(StreetLine st, double elapsedTime)
	{
		for(int i = 0; i < st.CarList.size(); ++i) {
    		if(!st.CarList.get(i).stopped) {
    			st.CarList.get(i).ride(elapsedTime);
    		}   		
    		SetDistFromRedTRL(i, st);
    		
    		if(!AgainstHasREDTRL(st, i))
    		{
    			st.CarList.get(i).StoppedUnderTRL = false;
    		}
    		
    		//setting deltaX 
    		if(i > 0) {
    			if(st.CarList.get(i).Y == st.CarList.get(i - 1).Y) {
    				st.CarList.get(i).DeltaX = st.CarList.get(i - 1).X - st.CarList.get(i).X;
    			}
    			else {
    				st.CarList.get(i).DeltaX = way.getWidth() - st.CarList.get(i).X + st.CarList.get(i - 1).X;
    			}
    			
    	     	if(intersects(st.CarList.get(i), st.CarList.get(i - 1)) ) {
    	     		st.CarList.get(i).accidented = true;
    	     		st.CarList.get(i - 1).accidented = true;
    	     	}
    		}
    		
    		// ste naiumenq ete uje hasnuma tormuza talis 
    		if(!st.CarList.get(i).StoppedWithButton || !st.CarList.get(i).StoppedUnderTRL) {
    			if(st.CarList.get(i).DeltaX <= 3*st.CarList.get(i).CarIm.getHeight()) {
    	     		st.CarList.get(i).stop(st.CarList.get(i - 1));
    	     	}
    	     	else {
    	     		if(st.CarList.get(i).DistanceFromRedTrLight < 3*st.CarList.get(i).CarIm.getWidth())
    	     		{
    	     			if(st.CarList.get(i).DeltaX > st.CarList.get(i).DistanceFromRedTrLight)
    	     			{
	    	     			st.CarList.get(i).StopUnderTRL(st.CarList.get(i).DistanceFromRedTrLight);
	    	     			System.out.println(st.CarList.get(i).DistanceFromRedTrLight);
	    	     		}
    	     		}
    	     		else
    	     		{
    	     			st.CarList.get(i).ChangeA(st.CarList.get(i).aride);
    	     			st.CarList.get(i).stopped = false;
    	     		}
    	     	}
    		}
    		
    		// erkrod trasinenq qcum ete ekranic dus ekav
    		if(st.CarList.get(i).X > 1500) {
        		if(st.CarList.get(i).Y > way.getHeight() ) {
        			st.CarList.remove(i);
        			if(i == 0) {	
            			if(!st.CarList.isEmpty()) {
            				st.CarList.get(0).DeltaX = 3000;
            			}
        			}
        		}
        		else {
        			st.CarList.get(i).X = 0;
        			st.CarList.get(i).Y += way.getHeight();
        		}
    			
			}            
        }
	}
	
	public boolean AgainstHasREDTRL(StreetLine st, int index)
	{
		if(st.CarList.get(index).DistanceFromRedTrLight == 32000)
		{
			return false;
		}
		return true;
	}
	
	public void run(double elapsedTime)
	{
		OurAppMozg(st1, elapsedTime);
		OurAppMozg(st2, elapsedTime);
	}
}

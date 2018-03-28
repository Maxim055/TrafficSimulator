package Traff;

import java.util.Vector;

import javafx.scene.image.Image;

 class Controller {
	public StreetLine st1 = new StreetLine(160);
	public StreetLine st2 = new StreetLine(100);
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
    	     	
    	     	if(st.CarList.get(i).stopped && HasAccidentedCarAgainstMe(st, i) && HasSpaceAgainstMe(st, i))
    	     	{
    	     		if(!HasCarbeforeMe(st, i))
        	     	{
        	     		ChangeLineEasy(st, i);
        	     	}
    	     		else
    	     		{
    	     			ChangeLine(st, i, NearestCarDistanceIndexFromAnotherSTLineCAR(st , i));
    	     		}
    	     	}
    	     	
    		}
    		
    		// ste naiumenq ete uje hasnuma tormuza talis 
    		if(!st.CarList.get(i).StoppedWithButton || !st.CarList.get(i).StoppedUnderTRL) {
    			if(st.CarList.get(i).DeltaX <= 3*st.CarList.get(i).CarIm.getHeight()) {
    	     		st.CarList.get(i).stop(st.CarList.get(i - 1));
    	     	}
    			else
	     		{
	     			st.CarList.get(i).ChangeA(st.CarList.get(i).aride);
	     			st.CarList.get(i).stopped = false;
	     		}
    			//if(st.CarList.get(i))
	     		if(st.CarList.get(i).DistanceFromRedTrLight < 2*st.CarList.get(i).CarIm.getWidth())
	     		{
	     			if(st.CarList.get(i).DeltaX >= st.CarList.get(i).DistanceFromRedTrLight)
	     			{
	     				
    	     			st.CarList.get(i).StopUnderTRL(st.CarList.get(i).DistanceFromRedTrLight);
    	     			//System.out.println(st.CarList.get(i).DistanceFromRedTrLight);
    	     		}
	     		}
    		}
    		
    		// erkrod trasinenq qcum ete ekranic dus ekav
    		if(st.CarList.get(i).X > way.getWidth()) {
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
	
	// Change Street Line of car with streetline st and index i 
	public void ChangeLine(StreetLine st , int i , int IndexOfNearestCar)
	{
		if(st == st1)
		{
			double X = st.CarList.get(i).X - st2.CarList.get(IndexOfNearestCar).X;
			double V0 = st2.CarList.get(IndexOfNearestCar).V;
			double a1 = st.CarList.get(i).aride;
			double a2 = st2.CarList.get(IndexOfNearestCar).astop;
			if(X > (V0 * V0) / (2*(Math.abs(a1) + Math.abs(a2))))
			{
				Car car = new Car();
				car.stopped = false;
				car.X = st.CarList.get(i).X;
				car.CarIm = st.CarList.get(i).CarIm;
				car.ImagineX = st.CarList.get(i).ImagineX;
				car.Y = st2.Y;
				car.Vmax = st.CarList.get(i).Vmax;
				car.WayHeight = st.CarList.get(i).WayHeight;
				car.WayWidth = st.CarList.get(i).WayWidth;
				car.aride = st.CarList.get(i).aride;
				st2.CarList.add(car);
				st.CarList.removeElementAt(i);
			}
		}
		else
		{
			double X = st.CarList.get(i).X - st1.CarList.get(IndexOfNearestCar).X;
			double V0 = st1.CarList.get(IndexOfNearestCar).V;
			double a1 = st.CarList.get(i).aride;
			double a2 = st1.CarList.get(IndexOfNearestCar).astop;
			if(X > (V0 * V0) / (2*(Math.abs(a1) + Math.abs(a2))))
			{
				Car car = new Car();
				car.stopped = false;
				car.X = st.CarList.get(i).X;
				car.CarIm = st.CarList.get(i).CarIm;
				car.ImagineX = st.CarList.get(i).ImagineX;
				car.Y = st1.Y;
				car.Vmax = st.CarList.get(i).Vmax;
				car.aride = st.CarList.get(i).aride;
				st1.CarList.add(car);
				st.CarList.removeElementAt(i);
			}
		}
	}
	
	public void ChangeLineEasy(StreetLine st, int i)
	{
		if(st == st1)
		{
			//change(st, i, st2.CarList.size());
			Car car = new Car();
			car.stopped = false;
			car.X = st.CarList.get(i).X;
			car.CarIm = st.CarList.get(i).CarIm;
			car.ImagineX = st.CarList.get(i).ImagineX;
			car.Y = st2.Y;
			car.Vmax = st.CarList.get(i).Vmax;
			car.WayHeight = st.CarList.get(i).WayHeight;
			car.WayWidth = st.CarList.get(i).WayWidth;
			car.aride = st.CarList.get(i).aride;
			st2.CarList.add(car);
			st.CarList.removeElementAt(i);
		}
		else
		{
			//change(st, i, st1.CarList.size());
			Car car = new Car();
			car.stopped = false;
			car.X = st.CarList.get(i).X;
			car.CarIm = st.CarList.get(i).CarIm;
			car.ImagineX = st.CarList.get(i).ImagineX;
			car.Y = st1.Y;
			car.Vmax = st.CarList.get(i).Vmax;
			car.aride = st.CarList.get(i).aride;
			st1.CarList.add(car);
			st.CarList.removeElementAt(i);
		}
		
	}
	
	/*public void change(StreetLine st, int i, int changeable)
	{
		if(st == st1)
		{
			Car car = new Car();
			car.X = st.CarList.get(i).X;
			car.CarIm = st.CarList.get(i).CarIm;
			car.ImagineX = st.CarList.get(i).ImagineX;
			
			System.out.println("Iam trying to removeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
			//st.CarList.remove(i);
			System.out.print("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERE");
			st2.CarList.add(changeable, car);
		}
		else
		{
			Car car = new Car();
			car = st.CarList.get(i);
			st.CarList.remove(i);
			st1.CarList.add(changeable, car);	
		}
	}*/
	
	// this function cheks if car with index index-1 is accidented
	public boolean HasAccidentedCarAgainstMe(StreetLine st, int index)
	{
		if(index == 0)
		{
			return false;
		}
		else
		{
			if(st.CarList.get(index - 1).accidented)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean HasSpaceAgainstMe(StreetLine st, int index)
	{
		if(st == st1)
		{
			if(st2.CarList.size() == 0)
			{
				return true;
			}
			else
			{
				for(int i = 0; i < st2.CarList.size(); ++i)
				{
					double dist = st2.CarList.get(i).ImagineX - st.CarList.get(index).ImagineX;
					if(dist >= 0 && dist < st.CarList.get(index).CarIm.getWidth())
					{
						return false;
					}
				}
				return true;
			}
		}
		else
		{
			if(st1.CarList.size() == 0)
			{
				return true;
			}
			else
			{
				for(int i = 0; i < st1.CarList.size(); ++i)
				{
					double dist = st1.CarList.get(i).ImagineX - st.CarList.get(index).ImagineX;
					if(dist >= 0 && dist < st.CarList.get(index).CarIm.getWidth())
					{
						return false;
					}
				}
				return true;
			}
		}
	}
	
	// cheks if this car has car above him
	public boolean HasCarbeforeMe(StreetLine st, int index)
	{
		if(st == st1)
		{
			for(int i = 0; i < st2.CarList.size(); ++i)
			{
				if(st2.CarList.get(i).ImagineX <= st.CarList.get(index).ImagineX)
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			for(int i = 0; i < st1.CarList.size(); ++i)
			{
				if(st1.CarList.get(i).ImagineX <= st.CarList.get(index).ImagineX)
				{
					return true;
				}
			}
			return false;
		}
	}
	
	// you give this function car , this function returns distance
	// from nearest car in another street line 
	public int NearestCarDistanceIndexFromAnotherSTLineCAR(StreetLine st, int index)
	{
		double min = 32000;
		int in = 0;				
		if(st == st1)
		{
			for(int i = 0; i < st2.CarList.size(); ++i)
			{
				double min2 = st.CarList.get(index).ImagineX - st2.CarList.get(i).ImagineX;
				if(min2 < min)
				{
					in = i;
					min = min2;
				}
			}
			return in;
		}
		else
		{
			for(int i = 0; i < st1.CarList.size(); ++i)
			{
				double min2 = st.CarList.get(index).ImagineX - st1.CarList.get(i).ImagineX;
				if(min2 < min)
				{
					in = i;
					min = min2;
				}
			}
			return in;
		}
	}
	
	
	// this function returns true if car in streetline st and with index index has Red TraffLight
	// against him
	public boolean AgainstHasREDTRL(StreetLine st, int index)
	{
		if(st.CarList.get(index).DistanceFromRedTrLight == 32000)
		{
			return false;
		}
		return true;
	}
	
	// start controller
	public void run(double elapsedTime)
	{
		OurAppMozg(st1, elapsedTime);
		OurAppMozg(st2, elapsedTime);
	}
}

package Traff;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class View extends Application {
	
    private Image grl;
    private ImageView img;
	public static Image Way;
	private Image Accident;
	private  HBox hb = new HBox();
	private Pane pane = new Pane();
	private Controller controller = new Controller();
	private GraphicsContext gc;
	
	//Load images
	public void LoadImages()
	{

		//setting images
		Way = new Image("file:src/images/d.jpg");
		Accident = new Image("file:src/images/accident.png");
		grl = new Image("file:src/images/tlgreen.png");
		img = new ImageView(grl);
		img.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
            	
                Dragboard db = img.startDragAndDrop(TransferMode.ANY);
                
                ClipboardContent content = new ClipboardContent();
                content.putImage(grl);
                db.setContent(content);
                
                event.consume();
            }
        });
		
		pane.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    success = true;
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                TraffLight tl = new TraffLight(event.getX(), event.getY(), Way);
                controller.TrLight.add(tl);
                tl.Tl.relocate(event.getX(),event.getY());
                pane.getChildren().add(tl.Tl);
                
                event.consume();
            }
        });
		
		pane.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
	}
	
	public void Print()
	{
		for(int i = 0; i < controller.st1.CarList.size(); ++i)
		{
			if(controller.st1.CarList.get(i).accidented)
			{
				gc.drawImage(controller.st1.CarList.get(i).CarIm, controller.st1.CarList.get(i).X, controller.st1.CarList.get(i).Y);
	     		gc.drawImage(Accident, controller.st1.CarList.get(i).X + controller.st1.CarList.get(i).CarIm.getHeight(), controller.st1.CarList.get(i).Y);
			}
			gc.drawImage(controller.st1.CarList.get(i).CarIm, controller.st1.CarList.get(i).X, controller.st1.CarList.get(i).Y);
		}
		for(int i = 0; i < controller.st2.CarList.size(); ++i)
		{
			if(controller.st2.CarList.get(i).accidented)
			{
				gc.drawImage(controller.st2.CarList.get(i).CarIm, controller.st2.CarList.get(i).X, controller.st2.CarList.get(i).Y);
	     		gc.drawImage(Accident, controller.st2.CarList.get(i).X + controller.st2.CarList.get(i).CarIm.getHeight(), controller.st2.CarList.get(i).Y);
			}
			gc.drawImage(controller.st2.CarList.get(i).CarIm, controller.st2.CarList.get(i).X, controller.st2.CarList.get(i).Y);
		}
	}
	
	// our application menu
	public void createMenuHB()
	{
		// HBox
	    hb.setPadding(new Insets(15, 12, 0, 0));
	    hb.setSpacing(10);

	    // Buttons
	    Button btn1 = new Button();
	    btn1.setText("New Car 1st line");
	    hb.getChildren().add(btn1);

	    Button btn2 = new Button();
	    btn2.setText("Restart");
	    hb.getChildren().add(btn2);

	    Button btn3 = new Button();
	    btn3.setText("Stop First Car");
	    hb.getChildren().add(btn3);

	    Button btn4 = new Button();
	    btn4.setText("New Car 2nd Line");
	    hb.getChildren().add(btn4);
	    
	    Button btn5 = new Button();
	    btn5.setText("Ride First Car");
	    hb.getChildren().add(btn5);
	    
	    hb.getChildren().add(img);

	    //Button Actions
	    btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
    	     		Car car = new Car(1, Way);
    	     		controller.st1.CarList.add(car);	
            }
        });
	    
	    btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	restart();
            }
        });
	    
	    btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            		controller.st1.CarList.get(0).a = -30;
            		controller.st1.CarList.get(0).StoppedWithButton = true;
            		controller.st2.CarList.get(0).a = -30;
            		controller.st2.CarList.get(0).StoppedWithButton = true;
            }
        });
	    
	    btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
	     		Car car = new Car(2, Way);
	     		controller.st2.CarList.add(car);	           	
            }
        });
	    
	    btn5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            		controller.st1.CarList.get(0).a = 30;
            		controller.st1.CarList.get(0).StoppedWithButton = false;
            }
        });	    
	}
	
	//main
	public static void main(String[] args) {
			Application.launch(args);
		}
		
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		LoadImages();  
		createMenuHB();
		primaryStage.setTitle("Traffic Simulator");
		primaryStage.setResizable(false);
		Group root = new Group();
		Scene theScene = new Scene( root );
		primaryStage.setScene( theScene );
											
		Canvas canvas = new Canvas(1500, 460);
		pane.getChildren().add(canvas);
		root.getChildren().addAll(pane , hb); 
		
		gc = canvas.getGraphicsContext2D();
	
		//Time
		LongValue lastNanoTime = new LongValue( System.nanoTime() );
			    
		//our application timer
		new AnimationTimer()
		{
			public void handle(long currentNanoTime)
			{
				double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
	            lastNanoTime.value = currentNanoTime;
	            
	            //background picture
	            gc.drawImage(Way, 0, 50);
	            gc.drawImage(Way, 0, Way.getHeight() + 50);
	            Print();
	            controller.run(elapsedTime);	            
			}
		}.start();
		
		primaryStage.show();
	}
	
	public void restart() {
		controller.st1.CarList.clear();
		controller.st2.CarList.clear();
		for(int i = 0; i < controller.TrLight.size(); ++i) {
			pane.getChildren().remove(controller.TrLight.get(i).Tl);
		}
		for(int i = 0; i < controller.TrLight.size(); ++i) {
			pane.getChildren().remove(controller.TrLight.get(i).Tl);
		}
		controller.TrLight.clear();
	}
}
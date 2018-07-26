

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
public class Initializer extends Application {

	Circle placeholderCircle = new Circle();
	Circle firstClicked = placeholderCircle;
	Circle secondClicked = placeholderCircle;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		placeholderCircle.setId("-1");
		
		Group  pegGroup = new Group();
		Stage pegStage = new Stage();
		pegStage.setTitle("Brody's Peg Solitare");
		Scene pegScene = new Scene(pegGroup, 600, 600);
		pegStage.setScene(pegScene);
		//BUILD LOGIC TO SUPPORT CLICKING, LIKE BELOW. BREAK OUT INTO CONTROLLER? AT LEAST A FUNCTION
		
		//X&Y = Coordinates where to draw circles
		double x = 50;
		double y = 50;
		//ID = ID of circle
		double id = 1;
		double idincr = 1;
		//cl = circle list, keep track of circles to count remaining at end
		List<Circle> cl = new ArrayList<Circle>();
		//Test fields to show ids of circles
		TextField tf1 = new TextField();
		pegGroup.getChildren().add(tf1);
		tf1.setEditable(false);
		//Test fields to show ids of circles
		TextField tf2 = new TextField();
		pegGroup.getChildren().add(tf2);
		tf2.setEditable(false);
		tf2.setTranslateX(300);
		
		//CIRCLES INITIALIZATION
		
		//Row by Row
		for (int i = 0; i < 5; ++i) {
			double offx = 0;
			//Column by column 
			for (int j = i; 5 > j; ++j) {
				Circle tempCircle = new Circle(x + offx,y,10, Color.GOLD);
				//Make Gold
				tempCircle.setStroke(Color.GOLD);
				//Keep track of circles
				tempCircle.setId(Double.toString(id));
				//Toggle outline color on click
				tempCircle.setOnMouseClicked((MouseEvent e) -> {
					if (tempCircle.getStroke().equals(Color.BLUE)) {
						tempCircle.setStroke(Color.GOLD);
					}
					else {
					tempCircle.setStroke(Color.BLUE);
					}
					
					//THIS LOGIC SHOULD PROBABLY GO WHEN YOU CLICK ON SCENE
					System.out.println(tempCircle.getId());
					tf1.setText(tempCircle.getId());
					tf2.setText(firstClicked.getId());
					//if 0/2 circles clicked, set to first. Else Assume you clicked 1/2 already, clicking second.(reseting on 2) 
					if (firstClicked.getId().equals("-1"))
					{
						firstClicked = tempCircle;
					}
					else {
						secondClicked = tempCircle;
						//Check valid move   //IF (isMoveValid()){doMove();}
						//Do valid move
						//reset first & secondClicked
						firstClicked.setStroke(Color.GOLD);
						secondClicked.setStroke(Color.GOLD);
						//reset placeholders to placeholder Circle. 
						firstClicked = placeholderCircle;
						secondClicked = placeholderCircle;
					}
				});
				cl.add(tempCircle);
				pegGroup.getChildren().add(tempCircle);
				offx += 60;
				id += 2;
			}
			id += idincr;
			idincr += 2;
			x += 30;
			y += 50;
		}
		//Circle[] pegArray = pegGroup.getChildren().toArray(new Circle[15]);
		for (Circle c : cl) {
			//System.out.println(c.getId());
		}
		
		pegStage.show();
	}

}

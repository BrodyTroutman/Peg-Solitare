
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Initializer extends Application {

	Circle placeholderCircle = new Circle();
	Circle firstClicked = placeholderCircle;
	Circle secondClicked = placeholderCircle;
	Circle middlePegc = placeholderCircle;
	Scene pegScene;
	List<Circle> cl;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		placeholderCircle.setId("-1");

		Group pegGroup = new Group();
		Stage pegStage = new Stage();
		pegStage.setTitle("Brody's Peg Solitare");
		pegScene = new Scene(pegGroup, 350, 400);
		pegStage.setScene(pegScene);
		// BUILD LOGIC TO SUPPORT CLICKING, LIKE BELOW. BREAK OUT INTO CONTROLLER? AT
		// LEAST A FUNCTION

		// X&Y = Coordinates where to draw circles
		double x = 50;
		double y = 150;
		// ID = ID of circle
		int id = 1;
		int idincr = 1;
		// cl = circle list, keep track of circles to count remaining at end
		cl = new ArrayList<Circle>();

		// Test fields to show ids of circles
		TextField tf1 = new TextField();
		pegGroup.getChildren().add(tf1);
		tf1.setEditable(false);

		// Test fields to show ids of circles
		TextField tf2 = new TextField();
		pegGroup.getChildren().add(tf2);
		tf2.setEditable(false);
		tf2.setTranslateX(200);

		// Valid Checker
		TextField tf3 = new TextField();
		pegGroup.getChildren().add(tf3);
		tf3.setEditable(false);
		tf3.setTranslateY(30);
		
		createResetButton(pegGroup);
		
		// CIRCLES INITIALIZATION
		// Row by Row
		for (int i = 0; i < 5; ++i) {
			double offx = 0;
			// Column by column
			for (int j = i; 5 > j; ++j) {
				Circle tempCircle = new Circle(x + offx, y, 10, Color.GOLD);
				// Make Gold
				tempCircle.setStrokeWidth(2);
				tempCircle.setStroke(Color.GOLD);
				// Keep track of circles
				tempCircle.setId(Integer.toString(id));
				// Toggle outline color on click
				tempCircle.setOnMouseClicked((MouseEvent e) -> {
					if (tempCircle.getStroke().equals(Color.BLUE)) {
						tempCircle.setStroke(Color.GOLD);
					} else {
						tempCircle.setStroke(Color.BLUE);
					}

					// THIS LOGIC SHOULD PROBABLY GO WHEN YOU CLICK ON SCENE
					//System.out.println(tempCircle.getId());
					tf1.setText(tempCircle.getId());
					tf2.setText(firstClicked.getId());
					// if 0/2 circles clicked, set to first. Else Assume you clicked 1/2 already,
					// clicking second.(reseting on 2)
					if (firstClicked.getId().equals("-1")) {
						firstClicked = tempCircle;
					} else {
						secondClicked = tempCircle;
						// Check valid move //IF (isMoveValid()){doMove();}
						if (isValidMove() && pegsExist()) {
							tf3.setText("VALID");
							firstClicked.setFill(Color.BLUE);
							middlePegc.setFill(Color.BLUE);
							secondClicked.setFill(Color.GOLD);
						} else {
							tf3.setText("INVALID");
						}

						// Do valid move

						// reset first & secondClicked
						firstClicked.setStroke(Color.GOLD);
						secondClicked.setStroke(Color.GOLD);
						// reset placeholders to placeholder Circle.
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
		Random rand = new Random();
		int n = rand.nextInt(14) + 4;
		Circle myC = (Circle) pegGroup.getChildren().get(n);
		myC.setFill(Color.BLUE);
		for (Circle c : cl) {
		}
		pegStage.show();
	}

	public boolean isValidMove() {
		double difference = Math.abs(Double.parseDouble(firstClicked.getId()) - Double.parseDouble(secondClicked.getId()));
		return (difference == 4 || difference == 18 || difference == 22);
	}
	
	public boolean pegsExist() {
		int difference = (int) Math.abs(Double.parseDouble(firstClicked.getId()) - Double.parseDouble(secondClicked.getId()));
		//System.out.println(difference/2);
		int middlePeg;
		if (Double.parseDouble(firstClicked.getId()) < Double.parseDouble(secondClicked.getId())){
			middlePeg = (Integer.parseInt(firstClicked.getId()) + (difference/2) );
		}
		else {
			middlePeg = (Integer.parseInt(firstClicked.getId()) - (difference/2) );
		}
		
		//System.out.println("middle peg #id = " + middlePeg);
		for (Circle c : cl) {
			int cid = Integer.parseInt(c.getId());
			if (cid == middlePeg) {
				middlePegc = c;
			}
		}
		
		return (firstClicked.getFill() == Color.GOLD
				&& secondClicked.getFill() == Color.BLUE
				&& middlePegc.getFill() == Color.GOLD);	
	}
	
	public void createResetButton(Group pegGroup) {
		Button reset = new Button("RESET");
		reset.setId("RESET");
		reset.setOnAction((event) -> {
			for (Circle c : cl) {
				c.setFill(Color.GOLD);
				c.setStroke(Color.GOLD);
			}
			Random rand = new Random();
			int n = rand.nextInt(15) + 4;
			Circle myC = (Circle) pegGroup.getChildren().get(n);
			myC.setFill(Color.BLUE);
		});
		reset.setLayoutY(30);
		reset.setLayoutX(200);
		pegGroup.getChildren().add(reset);
	}
}

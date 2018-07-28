
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
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
	Group pegGroup;
	Scene pegScene;
	List<Circle> cl;
	TextField tf1,tf2,tf3;
	TextField tf4;
	TextField tf5;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		placeholderCircle.setId("-1");

		pegGroup = new Group();
		Stage pegStage = new Stage();
		pegStage.setResizable(false);
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
		tf1 = new TextField();
		pegGroup.getChildren().add(tf1);
		tf1.setEditable(false);

		// Test fields to show ids of circles
		tf2 = new TextField();
		pegGroup.getChildren().add(tf2);
		tf2.setEditable(false);
		tf2.setTranslateX(200);

		// Valid Checker
		tf3 = new TextField();
		pegGroup.getChildren().add(tf3);
		tf3.setEditable(false);
		tf3.setTranslateY(30);
		
		tf4 = new TextField();
		pegGroup.getChildren().add(tf4);
		tf4.setEditable(false);
		tf4.setTranslateY(60);
		
		tf5 = new TextField();
		pegGroup.getChildren().add(tf5);
		tf5.setEditable(false);
		tf5.setTranslateY(90);
		tf5.setPrefWidth(350);
		tf5.setAlignment(Pos.CENTER);
		
		createResetButton();
		
		// CIRCLES INITIALIZATION
		// Row by Row
		for (int i = 0; i < 5; ++i) {
			double offx = 0;
			// Column by column
			for (int j = i; 5 > j; ++j) {
				Circle tempCircle = new Circle(x + offx, y , 10, Color.GREEN);
				
				Text tempText = new Text(x + offx - 5, y - 15, Integer.toString(id));
				tempText.setFill(Color.BLACK);
				tempText.setStrokeWidth(.5);
				// Make GREEN
				tempCircle.setStrokeWidth(2);
				tempCircle.setStroke(Color.GREEN);
				// Keep track of circles
				tempCircle.setId(Integer.toString(id));
				// Toggle outline color on click
				tempCircle.setOnMouseClicked((MouseEvent e) -> {
					if (tempCircle.getStroke().equals(Color.BLUE)) {
						tempCircle.setStroke(Color.GREEN);
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
							secondClicked.setFill(Color.GREEN);
							checkIfSolved();
						} else {
							tf3.setText("INVALID");
						}

						// Do valid move

						// reset first & secondClicked
						firstClicked.setStroke(Color.GREEN);
						secondClicked.setStroke(Color.GREEN);
						// reset placeholders to placeholder Circle.
						firstClicked = placeholderCircle;
						secondClicked = placeholderCircle;
					}
				});
				cl.add(tempCircle);
				pegGroup.getChildren().add(tempCircle);
				pegGroup.getChildren().add(tempText);
				offx += 60;
				id += 2;
			}
			id += idincr;
			idincr += 2;
			x += 30;
			y += 50;
		}
		Random rand = new Random();
		int n = rand.nextInt(15) + 0;
		cl.get(n).setFill(Color.BLUE);
		
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
		
		return (firstClicked.getFill() == Color.GREEN
				&& middlePegc.getFill() == Color.GREEN
				&& secondClicked.getFill() == Color.BLUE
				);	
	}
	
	public void createResetButton() {
		Button reset = new Button("RESET");
		reset.setId("RESET");
		reset.setOnAction((event) -> {
			for (Circle c : cl) {
				c.setFill(Color.GREEN);
				c.setStroke(Color.GREEN);
			}
			Random rand = new Random();
			int n = rand.nextInt(15) + 0;
			cl.get(n).setFill(Color.BLUE);
			tf1.setText("");
			tf2.setText("");
			tf3.setText("");
			tf4.setText("");
			tf5.setText("");
			
		});
		reset.setLayoutY(30);
		reset.setLayoutX(200);
		pegGroup.getChildren().add(reset);
	}
	
	public void checkIfSolved() {
		boolean foundAMove = false;
		for (Circle c : cl) {
			int cid = Integer.parseInt(c.getId());
			if (
				pegsExist(cid, cid + 4) ||
				pegsExist(cid, cid - 4) ||
				pegsExist(cid, cid + 18) ||
				pegsExist(cid, cid - 18) ||
				pegsExist(cid, cid + 22) ||
				pegsExist(cid, cid - 22)
			)
			{
				//System.out.println("You have at least one move left");
				foundAMove = true;
				break;
			}
			
			}
		if (!foundAMove) {
		tf4.setText("No Moves Available");
		int remain = 0;
		for (Circle c2 : cl) {
			if (c2.getFill() == Color.GREEN) { ++remain; }
		}
		switch (remain) {
			case 1: tf5.setText("You're Genius"); break;
			case 2: tf5.setText("You're Purty Smart"); break;
			case 3: tf5.setText("You're Just Plain Dumb"); break;
			default: tf5.setText("You're Just Plain \"EG-NO-RA-MOOSE");
		}
		}
		
	}
	
	public boolean pegsExist(int first, int second) {
		int difference = Math.abs(first - second);
		int middle;
		if (first < second) {
			middle = first + (difference/2);
		}
		else {
			middle = first - (difference/2);
		}
		
		Circle fpeg = null;
		Circle mpeg = null;
		Circle speg = null;
		for (Circle c : cl) {
			int cid = Integer.parseInt(c.getId());
			if (cid == first) {
				fpeg = c;
			}
			if (cid == middle) {
				mpeg = c;
			}
			if (cid == second) {
				speg = c;
			}
		}
		
		if (fpeg != null && mpeg != null && speg != null) {
			if (fpeg.getFill() == Color.GREEN &&
					mpeg.getFill() == Color.GREEN &&
					speg.getFill() == Color.BLUE) {

				tf4.setText("Possible Move " + fpeg.getId() + " " + mpeg.getId() + " " + speg.getId() );
				return true;
			}
		}
		
		return false;
	}
}

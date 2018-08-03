
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
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
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.application.Platform;
import java.io.InputStream;

public class Initializer extends Application {

	Circle placeholderCircle = new Circle();
	Circle firstClicked = placeholderCircle;
	Circle secondClicked = placeholderCircle;
	Circle middlePegc = placeholderCircle;
	Group pegGroup;
	Scene pegScene;
	List<Circle> cl;
	TextField tf1, tf2, tf3;
	TextField tf4;
	Text tf5;
	Color color1 = Color.GREEN;
	Color color2 = Color.BLUE;
	String p1Value = "";
	String p2Value = "";
	SingleSelectionModel<String> model;
	String validMove = "";
	boolean showHints;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		placeholderCircle.setId("-1");

		pegGroup = new Group();
		Stage pegStage = new Stage();
		pegStage.setResizable(false);
		pegStage.setTitle("Bemu's Peg Solitare");
		pegScene = new Scene(pegGroup, 400, 400);
		pegStage.setScene(pegScene);
		// BUILD LOGIC TO SUPPORT CLICKING, LIKE BELOW. BREAK OUT INTO CONTROLLER? AT
		// LEAST A FUNCTION

		// X&Y = Coordinates where to draw circles
		double x = 80;
		double y = 180;
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

		tf5 = new Text();
		tf5.setStyle("-fx-font: 18 arial;");
		HBox box = new HBox();
		box.setTranslateY(120);
		box.setPrefWidth(pegScene.getWidth());
		box.getChildren().add(tf5);
		box.setAlignment(Pos.CENTER);
		pegGroup.getChildren().add(box);

		Button hints = new Button("SHOW HINTS");
		hints.setLayoutX(200);
		hints.setLayoutY(60);
		pegGroup.getChildren().add(hints);
		hints.setOnAction((event) -> {
			if (hints.getText().equals("SHOW HINTS")) {
				hints.setText("HIDE HINTS");
				tf4.setText(validMove);
				showHints = true;
			}else {
				hints.setText("SHOW HINTS");
				tf4.setText("");
				showHints = false;
			}
		});
		
		
		
		
		ComboBox<String> picker1 = new ComboBox<String>();
		ComboBox<String> picker2 = new ComboBox<String>();
		//PULL LIST OF COLORS FROM FILE & ADD TO BOTH COMBOBOXES
		InputStream in = getClass().getResourceAsStream("/colorlist.txt");
		Scanner input = new Scanner(in);
		while (input.hasNextLine()) {
			String color = input.nextLine();
			picker1.getItems().add(color);
			picker2.getItems().add(color);
		}
		input.close();
		//picker1.getItems().addAll("RED", "BLUE", "GREEN", "AQUAMARINE", "BLACK", "GRAY", "PINK");
		picker1.setValue("GREEN");
		//System.out.print(picker1.getSelectionModel().getSelectedItem());
		p1Value = picker1.getSelectionModel().getSelectedItem();
		picker1.setLayoutY(90);
		pegGroup.getChildren().add(picker1);

		
		//picker2.getItems().addAll("RED", "BLUE", "GREEN", "AQUAMARINE", "BLACK", "GRAY", "PINK");
		picker2.setValue("BLUE");
		//System.out.print(picker2.getSelectionModel().getSelectedItem());
		p2Value = picker2.getSelectionModel().getSelectedItem();
		//System.out.println(p2Value);
		picker2.setLayoutY(90);
		picker2.setLayoutX(200);
		pegGroup.getChildren().add(picker2);
		
		
		

		picker1.setOnAction((event) -> {
			//System.out.println(p1Value);
			if (!picker1.getValue().equals(picker2.getValue())) {
				for (Circle c : cl) {
					c.setStroke(Color.valueOf(picker1.getValue()));
					if (c.getFill().equals(color1)) {
						c.setFill(Color.valueOf(picker1.getValue()));
					}
				}
				color1 = Color.valueOf(picker1.getValue());
				p1Value = picker1.getSelectionModel().getSelectedItem();
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						picker1.setValue(p1Value);
					}
				});
			}

			model = picker2.getSelectionModel();
		});
		picker2.setOnAction((event) -> {
			//System.out.println("Selected " + picker2.getValue());
			//System.out.println("pickr1 " + picker1.getValue());
			//System.out.println("p2Value = " + p2Value);
			if (!picker2.getValue().equals(picker1.getValue())) {
				for (Circle c : cl) {
					if (c.getFill().equals(color2)) {
						c.setFill(Color.valueOf(picker2.getValue()));
					}
				}
				color2 = Color.valueOf(picker2.getValue());
				p2Value = picker2.getSelectionModel().getSelectedItem();
				// System.out.println(p2Value);
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						picker2.setValue(p2Value);
					}
				});
			}

		});

		createResetButton();

		// CIRCLES INITIALIZATION
		// Row by Row
		for (int i = 0; i < 5; ++i) {
			double offx = 0;
			// Column by column
			for (int j = i; 5 > j; ++j) {
				Circle tempCircle = new Circle(x + offx, y, 10, color1);

				Text tempText = new Text(x + offx - 5, y - 15, Integer.toString(id));
				tempText.setFill(Color.BLACK);
				tempText.setStrokeWidth(.5);
				// Make GREEN
				tempCircle.setStrokeWidth(2);
				tempCircle.setStroke(color1);
				// Keep track of circles
				tempCircle.setId(Integer.toString(id));
				// Toggle outline color on click
				tempCircle.setOnMouseClicked((MouseEvent e) -> {
					if (tempCircle.getStroke().equals(color2)) {
						tempCircle.setStroke(color1);
					} else {
						tempCircle.setStroke(color2);
					}

					// THIS LOGIC SHOULD PROBABLY GO WHEN YOU CLICK ON SCENE
					// System.out.println(tempCircle.getId());
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
							firstClicked.setFill(color2);
							middlePegc.setFill(color2);
							secondClicked.setFill(color1);
							checkIfSolved();
						} else {
							tf3.setText("INVALID");
						}

						// Do valid move

						// reset first & secondClicked
						firstClicked.setStroke(color1);
						secondClicked.setStroke(color1);
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
		cl.get(n).setFill(color2);

		pegStage.show();
	}

	public boolean isValidMove() {
		double difference = Math
				.abs(Double.parseDouble(firstClicked.getId()) - Double.parseDouble(secondClicked.getId()));
		return (difference == 4 || difference == 18 || difference == 22);
	}

	public boolean pegsExist() {
		int difference = (int) Math
				.abs(Double.parseDouble(firstClicked.getId()) - Double.parseDouble(secondClicked.getId()));
		// System.out.println(difference/2);
		int middlePeg;
		if (Double.parseDouble(firstClicked.getId()) < Double.parseDouble(secondClicked.getId())) {
			middlePeg = (Integer.parseInt(firstClicked.getId()) + (difference / 2));
		} else {
			middlePeg = (Integer.parseInt(firstClicked.getId()) - (difference / 2));
		}

		// System.out.println("middle peg #id = " + middlePeg);
		for (Circle c : cl) {
			int cid = Integer.parseInt(c.getId());
			if (cid == middlePeg) {
				middlePegc = c;
			}
		}

		return (firstClicked.getFill() == color1 && middlePegc.getFill() == color1
				&& secondClicked.getFill() == color2);
	}

	public void createResetButton() {
		Button reset = new Button("RESET");
		reset.setId("RESET");
		reset.setOnAction((event) -> {
			for (Circle c : cl) {
				c.setFill(color1);
				c.setStroke(color2);
			}
			Random rand = new Random();
			int n = rand.nextInt(15) + 0;
			cl.get(n).setFill(color2);
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
			if (pegsExist(cid, cid + 4) || pegsExist(cid, cid - 4) || pegsExist(cid, cid + 18)
					|| pegsExist(cid, cid - 18) || pegsExist(cid, cid + 22) || pegsExist(cid, cid - 22)) {
				// System.out.println("You have at least one move left");
				foundAMove = true;
				break;
			}

		}
		if (!foundAMove) {
			tf4.setText("No Moves Available");
			validMove = "No Moves Available";
			int remain = 0;
			for (Circle c2 : cl) {
				if (c2.getFill() == color1) {
					++remain;
				}
			}
			switch (remain) {
			case 1:
				tf5.setText("You're Genius");
				break;
			case 2:
				tf5.setText("You're Purty Smart");
				break;
			case 3:
				tf5.setText("You're Just Plain Dumb");
				break;
			default:
				tf5.setText("You're Just Plain \"EG-NO-RA-MOOSE");
			}
		}

	}

	public boolean pegsExist(int first, int second) {
		int difference = Math.abs(first - second);
		int middle;
		if (first < second) {
			middle = first + (difference / 2);
		} else {
			middle = first - (difference / 2);
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
			if (fpeg.getFill() == color1 && mpeg.getFill() == color1 && speg.getFill() == color2) {
				
				validMove = ("Possible Move " + fpeg.getId() + " " + mpeg.getId() + " " + speg.getId());
				if (showHints) {
					tf4.setText(validMove);
				}
				return true;
			}
		}

		return false;
	}
}

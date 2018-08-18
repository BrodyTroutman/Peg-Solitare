
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
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

public class Initializer extends Application {

	Circle placeholderCircle = new Circle();
	Circle firstClicked = placeholderCircle;
	Circle secondClicked = placeholderCircle;
	Circle middlePegc = placeholderCircle;
	Group pegGroup;
	Scene pegScene;
	List<Circle> cl;
	TextField tf4;
	Text tf5;
	Label tf6;
	Color color1 = Color.GREEN;
	Color color2 = Color.BLUE;
	String p1Value = "";
	String p2Value = "";
	SingleSelectionModel<String> model;
	Move validMove = null;
	boolean showHints;
	static ComboBox<String> picker1;
	static ComboBox<String> picker2;
	myTimer timer = new myTimer();
	MoveStack os = new MoveStack();
	MoveStack rs = new MoveStack();
	boolean finished = false;

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
		pegScene = new Scene(pegGroup, 400, 360); // width, height
		pegStage.setScene(pegScene);
		// BUILD LOGIC TO SUPPORT CLICKING, LIKE BELOW. BREAK OUT INTO CONTROLLER? AT
		// LEAST A FUNCTION

		// ID = ID of circle
		int id = 1;
		int idincr = 1;
		// cl = circle list, keep track of circles to count remaining at end
		cl = new ArrayList<Circle>();

		tf4 = new TextField();
		pegGroup.getChildren().add(tf4);
		tf4.setEditable(false);
		tf4.setTranslateY(60);

		tf5 = new Text();
		tf5.setStyle("-fx-font: 18 arial;");
		HBox box = new HBox();
		box.setTranslateY(180);
		box.setPrefWidth(pegScene.getWidth());
		box.getChildren().add(tf5);
		box.setAlignment(Pos.CENTER);
		pegGroup.getChildren().add(box);

		tf6 = new Label();
		tf6.setStyle("-fx-font: 8 arial;");
		HBox box6 = new HBox();
		box6.setTranslateY(460);
		box6.getChildren().add(tf6);
		pegGroup.getChildren().add(box6);
		tf6.textProperty().bind(timer.time.asString());

		Button hints = new Button("SHOW HINTS");
		
		hints.setLayoutX(200);
		hints.setLayoutY(60);
		pegGroup.getChildren().add(hints);
		hints.setOnAction((event) -> {
			if (hints.getText().equals("SHOW HINTS")) {
				hints.setText("HIDE HINTS");
				tf4.setText("Possible Move " + validMove.toString());
				showHints = true;
			} else {
				hints.setText("SHOW HINTS");
				tf4.setText("");
				showHints = false;
			}
		});
		

		picker1 = new ComboBox<String>();
		picker2 = new ComboBox<String>();
		// PULL LIST OF COLORS FROM FILE & ADD TO BOTH COMBOBOXES
		InputStream in = getClass().getResourceAsStream("/colorlist.txt");
		Scanner input = new Scanner(in);
		while (input.hasNextLine()) {
			String color = input.nextLine();
			picker1.getItems().add(color);
			picker2.getItems().add(color);
		}
		input.close();
		picker1.setValue("GREEN");
		picker2.getItems().remove("GREEN");
		p1Value = picker1.getSelectionModel().getSelectedItem();
		picker1.setLayoutY(30);
		pegGroup.getChildren().add(picker1);

		picker2.setValue("BLUE");
		picker1.getItems().remove("BLUE");
		p2Value = picker2.getSelectionModel().getSelectedItem();
		picker2.setLayoutY(30);
		picker2.setLayoutX(200);
		pegGroup.getChildren().add(picker2);

		if (settingsExist()) {
			picker1.setValue(readSettings(0));
			color1 = Color.valueOf(readSettings(0));
			picker2.setValue(readSettings(1));
			color2 = Color.valueOf(readSettings(1));
		}

		picker1.setOnAction((event) -> {
			if (!picker1.getValue().equals(picker2.getValue())) {
				
				picker2.getItems().remove(picker1.getValue());
				picker2.getItems().add(picker1.getItems().indexOf(p1Value),p1Value );
				adjustAllPegs(Color.valueOf(picker1.getValue()), color1);
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
			
			if (!picker2.getValue().equals(picker1.getValue())) {

				picker1.getItems().remove(picker2.getValue());
				picker1.getItems().add(picker2.getItems().indexOf(p2Value),p2Value );
				adjustAllPegs(Color.valueOf(picker2.getValue()), color2);
				color2 = Color.valueOf(picker2.getValue());
				p2Value = picker2.getSelectionModel().getSelectedItem();
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

		createMenuBar();

		// CIRCLES INITIALIZATION
		// Row by Row

		// X&Y = Coordinates where to draw circles
		double x = 80;
		double y = 140;

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

					if (!timer.running && !finished) {
						timer.start();
					}

					if (tempCircle.getStroke().equals(color2)) {
						tempCircle.setStroke(color1);
					} else {
						tempCircle.setStroke(color2);
					}

					// THIS LOGIC SHOULD PROBABLY GO WHEN YOU CLICK ON SCENE

					// if 0/2 circles clicked, set to first. Else Assume you clicked 1/2 already,
					// clicking second.(reseting on 2)
					if (firstClicked.getId().equals("-1")) {
						firstClicked = tempCircle;
					} else {
						secondClicked = tempCircle;
						// Check valid move //IF (isMoveValid()){doMove();}
						if (isValidMove() && pegsExist()) {
							rs.clear();
							os.push(new Move(firstClicked.getId(), middlePegc.getId(), secondClicked.getId()));


							firstClicked.setFill(color2);
							firstClicked.setStroke(color2);
							middlePegc.setFill(color2);
							middlePegc.setStroke(color2);
							secondClicked.setFill(color1);
							checkIfSolved();
						} else {

							// Do valid move

							// reset first & secondClicked
							firstClicked.setStroke(firstClicked.getFill());
							secondClicked.setStroke(secondClicked.getFill());
						}
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
		Circle rando = cl.get(n);
		rando.setStroke(color2);
		rando.setFill(color2);

		Group settings = new Group();

		Rectangle blue = new Rectangle(410, 470); // width, height
		blue.setFill(Color.WHITE);
		settings.getChildren().add(blue);
		// pegGroup.getChildren().add(settings);

		ColorPicker p = new ColorPicker();
		settings.getChildren().add(p);
		
		pegGroup.requestFocus();
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
	
		int middlePeg;
		if (Double.parseDouble(firstClicked.getId()) < Double.parseDouble(secondClicked.getId())) {
			middlePeg = (Integer.parseInt(firstClicked.getId()) + (difference / 2));
		} else {
			middlePeg = (Integer.parseInt(firstClicked.getId()) - (difference / 2));
		}

		for (Circle c : cl) {
			int cid = Integer.parseInt(c.getId());
			if (cid == middlePeg) {
				middlePegc = c;
			}
		}
		return (firstClicked.getFill() == color1 && middlePegc.getFill() == color1
				&& secondClicked.getFill() == color2);
	}
	//GOOD IDEA????
	@SuppressWarnings("static-access")
	public void createResetButton() {
		Button reset = new Button("RESET");
		reset.setId("RESET");
		reset.setOnAction((event) -> {
			timer.stop();
			timer.time.set(0);
			for (Circle c : cl) {
				c.setFill(color1);
				c.setStroke(color1);
			}
			finished = false;
			Random rand = new Random();
			int n = rand.nextInt(15) + 0;
			Circle rando = cl.get(n);
			rando.setStroke(color2);
			rando.setFill(color2);
			tf4.setText("");
			tf5.setText("");
			os.clear();
			rs.clear();

		});
		reset.setLayoutY(90);
		reset.setLayoutX(0);
		pegGroup.getChildren().add(reset);
	}

	public void checkIfSolved() {
		boolean foundAMove = false;
		for (Circle c : cl) {
			int cid = Integer.parseInt(c.getId());
			if (pegsExist(cid, cid + 4) || pegsExist(cid, cid - 4) || pegsExist(cid, cid + 18)
					|| pegsExist(cid, cid - 18) || pegsExist(cid, cid + 22) || pegsExist(cid, cid - 22)) {
				foundAMove = true;
				break;
			}

		}
		//CREATE CUSTOM EVENT TO FIRE AND DISABLE UNDO BUTTON??
		if (!foundAMove) {
			timer.stop();
			if (!finished) { 
			tf4.setText("No Moves Available");
			validMove = null;
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
				tf5.setText("You're Just Plain \"EG-NO-RA-MOOSE\"");
			}
			finished = true;
			}
			else {
				tf5.setText("Good try, cheater");
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
				validMove = new Move(fpeg.getId(),mpeg.getId(),speg.getId());
				if (showHints) {
					tf4.setText("Possible Move " + validMove.toString());
				}
				return true;
			}
		}
		return false;
	}

	public static void ensureSettings() throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "PegGame";
		File customDir = new File(path);
		if (customDir.exists() || customDir.mkdirs()) {
			path += File.separator + "settings.txt";
			File cd2 = new File(path);
			cd2.createNewFile();
		}
	}

	public boolean settingsExist() {
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "PegGame";
		File customDir = new File(path);
		return customDir.exists();
	}

	public String readSettings(int pos) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "PegGame"
				+ File.separator + "settings.txt";
		String settings = readFile(path, Charset.defaultCharset());
		String color = settings.split(",")[pos];
		return color;
	}

	public String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public void adjustAllPegs(Color newColor, Color oldColor) {
		for (Circle c : cl) {
			if (c.getStroke().equals(oldColor)) {
				c.setStroke(newColor);
			}
			if (c.getFill().equals(oldColor)) {
				c.setFill(newColor);
			}

		}

	}

	public void save() {
		String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "PegGame"
				+ File.separator + "settings.txt";
		try {
			ensureSettings();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File customDir = new File(path);
		PrintWriter writer;
		try {
			writer = new PrintWriter(customDir);
			writer.write("");
			writer.append(picker1.getValue() + "," + picker2.getValue());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void load() {
		String color1Temp = null;
		try {
			color1Temp = readSettings(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String color2Temp = null;
		try {
			color2Temp = readSettings(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adjustAllPegs(Color.valueOf(color1Temp), color1);
		adjustAllPegs(Color.valueOf(color2Temp), color2);
		color1 = Color.valueOf(color1Temp);
		color2 = Color.valueOf(color2Temp);
		picker1.setValue(color1Temp);
		picker2.setValue(color2Temp);

	}

	// I like this code
	/*
	 * MenuItem newItem = MenuItemBuilder.create() .text("New") .accelerator(new
	 * KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN)) .onAction(new
	 * EventHandler<ActionEvent>() { public void handle(ActionEvent event) {
	 * System.out.println("Ctrl-N"); } }) .build();
	 */

	public void createMenuBar() {
		MenuBar mb = new MenuBar();
		
		Menu file = new Menu("File");
		
		MenuItem save = new MenuItem("Save Colors");
		save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
		
		MenuItem load = new MenuItem("Load Colors");
		load.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
		
		MenuItem undo = new MenuItem("Undo Last Move");
		undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
		
		
		MenuItem redo = new MenuItem("Redo Last Move");
		redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN));
		
		
		file.getItems().addAll(save, load, undo, redo);
		
		
		
		mb.getMenus().add(file);
		
		
		
		mb.setPrefWidth(pegScene.getWidth());
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				save();
			}
		});
		load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				load();
			}
		});
		redo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (!rs.isEmpty()) {
					Move m = rs.pop();
					redoMove(m);
					os.push(m);
				}
			}
		});
		undo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (!os.isEmpty()) {
					Move m = os.pop();
					undoMove(m);
					rs.push(m);
				}
			}
		});

		pegGroup.getChildren().add(mb);

	}

	
	

	public void undoMove(Move m) {
		// Can make cleaner by making a Peg Class
		String[] movePegs = {m.getFirst(), m.getSecond(), m.getThird()};
		for (Circle c : cl) {
			for (String s : movePegs) {
				if (c.getId().equals(s)) {
					if (c.getFill().equals(color1)) {
						c.setFill(color2);
						c.setStroke(color2);
					}else {
						c.setFill(color1);
						c.setStroke(color1);
					}
				}
			}

		}
		checkIfSolved();
	}
	
	public void redoMove(Move m) {
		// Can make cleaner by making a Peg Class
		String[] movePegs = {m.getFirst(), m.getSecond(), m.getThird()};
		for (Circle c : cl) {
			for (String s : movePegs) {
				if (c.getId().equals(s)) {
					if (c.getFill().equals(color1)) {
						c.setFill(color2);
						c.setStroke(color2);
					}else {
						c.setFill(color1);
						c.setStroke(color1);
					}
				}
			}
		}
		checkIfSolved();
	}
	

}

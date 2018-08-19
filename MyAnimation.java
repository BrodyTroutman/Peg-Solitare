import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.FillTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.animation.StrokeTransition;
import javafx.beans.property.ReadOnlyObjectProperty;

public class MyAnimation {
	SequentialTransition st;
	Color fold1, fold2, sold1, sold2 = null;
	
	public MyAnimation (Circle c, Circle c2) {
		fold1 = (Color) c.getFill();
		fold2 = (Color) c2.getFill();
		sold1 = (Color) c.getStroke();
		sold2 = (Color) c2.getStroke();
		buildST(c, c2);
		
	}
	
	private void buildST(Circle c, Circle c2) {
		FillTransition a = new FillTransition(Duration.millis(1), c, fold1, Color.GOLD);
		StrokeTransition aa = new StrokeTransition(Duration.millis(1), c, sold1, Color.GOLD);
		PauseTransition ab = new PauseTransition(Duration.millis(250));
		FillTransition abc = new FillTransition(Duration.millis(1), c, Color.GOLD, fold1);
		StrokeTransition abcc = new StrokeTransition(Duration.millis(1), c, Color.GOLD, sold1);
		
		PauseTransition p = new PauseTransition(Duration.millis(100));
		PauseTransition p2 = new PauseTransition(Duration.millis(300));
		
		FillTransition a2 = new FillTransition(Duration.millis(1), c2, fold2, Color.GOLD);
		StrokeTransition aa2 = new StrokeTransition(Duration.millis(1), c2, sold2, Color.GOLD);
		PauseTransition ab2 = new PauseTransition(Duration.millis(250));
		FillTransition abc2 = new FillTransition(Duration.millis(1), c2, Color.GOLD, fold2);
		StrokeTransition abcc2 = new StrokeTransition(Duration.millis(1), c2, Color.GOLD, sold2);
		
		st = new SequentialTransition(a, aa,ab, a2, aa2, p, abc, abcc,ab2, abc2, abcc2, p2);
		st.setCycleCount(2);
	}
	
	public void play() {
		st.play();
	}
	
	public void cancel() {
		st.playFromStart();
		st.stop();
	}
	
	public Animation.Status status() {
		return st.getStatus();
	}

	public ReadOnlyObjectProperty<Status> statusProperty() {
		return st.statusProperty();
	}
	
	
	
}

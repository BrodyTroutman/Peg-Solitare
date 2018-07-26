
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
public class boardView {
	Polygon outline;
	Circle[] pegArray;
	
	public boardView() {
		pegArray = new Circle[15];
		double x = 50;
		double y = 50;
		for (int i = 0; i < 5; ++i) {
			for (int j = i; j > 0; --j) {
				double offx = 0;
				Circle tempCircle = new Circle(x + offx,y,10);
				tempCircle.setFill(Color.GOLD);
				//pegArray.
				//offx += 75;
			}
			x += 25;
			y += 50;
		}
	}
}

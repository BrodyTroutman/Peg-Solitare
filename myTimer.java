import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

public class myTimer extends AnimationTimer {

	DoubleProperty time = new SimpleDoubleProperty();
	boolean running = false;
	private long startTime ;

	@Override
	public void start() {
		time.set(0);
		startTime = System.currentTimeMillis();
        running = true;
        super.start();
		
	}

	@Override
	public void handle(long timestamp) {
		// TODO Auto-generated method stub
		long now = System.currentTimeMillis();
        time.set((now - startTime) / 1000.0);
        
		
	}
	
	@Override
	public void stop() {
		running = false;
		super.stop();
	}
	

}

import java.util.ArrayList;

public class Move {
	String first;
	String second;
	String third;
	ArrayList<String> all = new ArrayList<String>();
	
	public Move(String first, String second, String third) {
		this.first = first;
		this.second = second;
		this.third = third;
		all.add(first);
		all.add(second);
		all.add(third);
		
	}
	
	public String toString() {
		return first + "," + second + "," + third;
	}
	
	public String getFirst() {
		return first;
	}
	
	public String getSecond() {
		return second;
	}
	
	public String getThird() {
		return third;
	}
	
	public ArrayList<String> getMove() {
		return all;
		
	}
	
	
}

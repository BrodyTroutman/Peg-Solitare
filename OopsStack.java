import java.util.Stack;

public class OopsStack {
	private static Stack<Move> oops = new Stack<Move>();
	
	public static void push(Move m) {
		oops.push(m);
	}
	
	public static Move pop() {
		return oops.pop();
	}
	
	public int size() {
		return oops.size();
	}
	
	public static boolean isEmpty() {
		return oops.isEmpty();
	}
	
	public static void clear() {
		oops.clear();
	}
	
}

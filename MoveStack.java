import java.util.Stack;

public class MoveStack {
	private Stack<Move> oops = new Stack<Move>();
	
	public void push(Move m) {
		oops.push(m);
	}
	
	public Move pop() {
		return oops.pop();
	}
	
	public int size() {
		return oops.size();
	}
	
	public boolean isEmpty() {
		return oops.isEmpty();
	}
	
	public void clear() {
		oops.clear();
	}
	
}

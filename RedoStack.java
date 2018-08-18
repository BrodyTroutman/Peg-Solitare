import java.util.Stack;


//MAKE REDO AND UNDO COME FROM SAME CLASS, NEW INSTANCES OF EACH????
public class RedoStack {
	private static Stack<Move> redo = new Stack<Move>();
	
	public void push(Move m) {
		redo.push(m);
	}
	
	public Move pop() {
		return redo.pop();
	}
	
	public int size() {
		return redo.size();
	}
	
	public boolean isEmpty() {
		return redo.isEmpty();
	}
	
	public static void clear() {
		redo.clear();
	}
}

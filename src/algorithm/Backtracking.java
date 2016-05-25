package algorithm;

import org.htmlcleaner.TagNode;

public class Backtracking {
	
	int operation = 0;
	
	TagNode source = null;
	
	int cost = 0;
	
	Backtracking[][] next = null;
	
	public Backtracking(){
		
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public TagNode getSource() {
		return source;
	}

	public void setSource(TagNode source) {
		this.source = source;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public Backtracking[][] getNext() {
		return next;
	}

	public void setNext(Backtracking[][] next) {
		this.next = next;
	}
	
	

}

package parser.ast;



public class IntNode implements ValueNode {
	private Integer value;
	
	@Override
	public String toString(){
		return "INT: " + Integer.toString(value);
	}
	
	public IntNode(String text) {
		this.value = new Integer(text);
	}
}

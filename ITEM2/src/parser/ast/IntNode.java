package parser.ast;

import java.util.Objects;

public class IntNode implements ValueNode {
	private Integer value;
	public IntNode(String text) { //생성자
		this.value = new Integer(text);
	}
	
	public Integer getValue() {
		return this.value;
	}//value getter
	
	@Override
	public boolean equals(Object o) {
		if(this==o) return true;
		if(!(o instanceof IntNode)) return false;
		IntNode intNode = (IntNode) o;
		return Objects.equals(value, intNode.value);
	}//새롭게 오버라이딩한 equals 함수
	
	@Override
	public String toString(){
		return value.toString();
	}
	
	
}

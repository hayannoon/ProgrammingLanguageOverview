package parser.ast;

import java.util.Objects;

public class IdNode implements ValueNode{
	String idString;
	
	public IdNode(String text) {
		idString = text;
	}
	
	@Override
	public boolean equals(Object o) {//새롭게 구현된 equals함수
		if(this == o) return true; //객체자체가 같으면 true
		if(!(o instanceof IdNode)) return false;//같은타입아니면false
		IdNode idNode = (IdNode) o; //같은타입이면 일단 객체만들고
		return Objects.equals(idString, idNode.idString);
		//Objects.equals로 비교
	}
	
	@Override
	public String toString(){
		return idString;
	}
}

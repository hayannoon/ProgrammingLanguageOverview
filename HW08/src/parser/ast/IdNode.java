package parser.ast;

import java.util.Objects;

public class IdNode implements ValueNode{
	String idString;
	
	public IdNode(String text) {
		idString = text;
	}
	
	@Override
	public boolean equals(Object o) {//���Ӱ� ������ equals�Լ�
		if(this == o) return true; //��ü��ü�� ������ true
		if(!(o instanceof IdNode)) return false;//����Ÿ�Ծƴϸ�false
		IdNode idNode = (IdNode) o; //����Ÿ���̸� �ϴ� ��ü�����
		return Objects.equals(idString, idNode.idString);
		//Objects.equals�� ��
	}
	
	@Override
	public String toString(){
		return idString;
	}
}

package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;


// ���⸦ �ۼ��ϰ� �ص� ���� �� 18.4.10
public class FunctionNode implements ValueNode{
	public enum FunctionType{
		
		CAR{ TokenType tokenType() {return TokenType.CAR;}},//
		CDR{ TokenType tokenType() {return TokenType.CDR;}},
		CONS{TokenType tokenType() {return TokenType.CONS;}},
		COND{TokenType tokenType() {return TokenType.COND;}},
		DEFINE{TokenType tokenType() {return TokenType.DEFINE;}},
		EQ_Q{TokenType tokenType() {return TokenType.EQ_Q;}},
		LAMBDA{TokenType tokenType(){return TokenType.LAMBDA;}},
		NOT{TokenType tokenType(){return TokenType.NOT;}},
		NULL_Q{TokenType tokenType(){return TokenType.NULL_Q;}} ,
		ATOM_Q{TokenType tokenType(){return TokenType.ATOM_Q;}} ;
		//Enum ����
	
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType,FunctionType>();
		//�ؽø� ����(TokenŸ�԰�,FunctionŸ��)
		static {
			for(FunctionType fType : FunctionType.values()) {
				fromTokenType.put(fType.tokenType(), fType);
			}
		}	//Static �ʱ�ȭ��� -> enhenced for���� ���鼭 staticŸ���� �ؽø� ���ҵ��� �ʱ�ȭ�����ش�.
		
		static FunctionType getFuncType(TokenType fType) {
			return fromTokenType.get(fType);
		} //TokenType ������ ���ڷ� �޾Ƽ�, �� FunctionType�� ��ȯ���ش�.
		abstract TokenType tokenType(); //�߻�޼ҵ� ����
		}
	public FunctionType funcType;	//functionType�� ���� ����
	@Override
	public String toString(){
		return funcType.name(); //value �̸� ����
	}
	//��ū Ÿ���� �Ű������� �޾Ƽ� value���� �����Ѵ�.
	public void setValue(TokenType tType) {
		FunctionType fType = FunctionType.getFuncType(tType);
		funcType = fType;
	}
}

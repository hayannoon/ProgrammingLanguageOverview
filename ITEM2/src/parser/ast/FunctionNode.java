package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;


// 여기를 작성하게 해도 좋을 듯 18.4.10
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
		//Enum 선언
	
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType,FunctionType>();
		//해시맵 생성(Token타입과,Function타입)
		static {
			for(FunctionType fType : FunctionType.values()) {
				fromTokenType.put(fType.tokenType(), fType);
			}
		}	//Static 초기화블록 -> enhenced for문을 돌면서 static타입의 해시맵 원소들을 초기화시켜준다.
		
		static FunctionType getFuncType(TokenType fType) {
			return fromTokenType.get(fType);
		} //TokenType 변수를 인자로 받아서, 그 FunctionType을 반환해준다.
		abstract TokenType tokenType(); //추상메소드 선언
		}
	public FunctionType funcType;	//functionType형 변수 선언
	@Override
	public String toString(){
		return funcType.name(); //value 이름 저장
	}
	//토큰 타입을 매개변수로 받아서 value값을 설정한다.
	public void setValue(TokenType tType) {
		FunctionType fType = FunctionType.getFuncType(tType);
		funcType = fType;
	}
}

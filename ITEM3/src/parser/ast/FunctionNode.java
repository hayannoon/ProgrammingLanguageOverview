package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;
import parser.ast.BinaryOpNode.BinType;

// 여기를 작성하게 해도 좋을 듯 18.4.10
public class FunctionNode implements ValueNode{
	public enum FunctionType { 
		ATOM_Q 	{ TokenType tokenType() {return TokenType.ATOM_Q;} }, 
		CAR 	{ TokenType tokenType() {return TokenType.CAR;} }, 
		CDR 	{ TokenType tokenType() {return TokenType.CDR;} }, 
		CONS	{ TokenType tokenType() {return TokenType.CONS;} },
		COND 	{ TokenType tokenType() {return TokenType.COND;} }, 
		DEFINE 	{ TokenType tokenType() {return TokenType.DEFINE;} }, 
		EQ_Q 	{ TokenType tokenType() {return TokenType.EQ_Q;} }, 
		LAMBDA 	{ TokenType tokenType() {return TokenType.LAMBDA;} },
		NOT 	{ TokenType tokenType() {return TokenType.NOT;} },
		NULL_Q 	{ TokenType tokenType() {return TokenType.NULL_Q;} };
		
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
		
		static {
			for (FunctionType fType : FunctionType.values()){
				fromTokenType.put(fType.tokenType(), fType);
			}
		}
		
		static FunctionType getFunctionType(TokenType tType){
			return fromTokenType.get(tType);
		}
		
		abstract TokenType tokenType();
		
	}
	public FunctionType funcType;
	
	@Override
	public String toString(){
		return funcType.name();
	}

	public void setValue(TokenType tType) {
		FunctionType fType = FunctionType.getFunctionType(tType);
		funcType = fType;	
	}
}

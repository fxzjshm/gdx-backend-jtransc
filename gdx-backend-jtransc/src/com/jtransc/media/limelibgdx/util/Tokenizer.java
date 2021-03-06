package com.jtransc.media.limelibgdx.util;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
	private final ArrayList<Token> out = new ArrayList<>();
	private final StrReader r;

	private Tokenizer(String str) {
		r = new StrReader(str);
	}

	static public List<Token> tokenize(String str) {
		Tokenizer tokenizer = new Tokenizer(str);
		tokenizer.tokenize();
		return tokenizer.out;
	}

	static public List<String> tokenizeStr(String str) {
		List<String> out = new ArrayList<String>();
		List<Token> tokens = tokenize(str);
		for (Token token : tokens) {
			out.add(token.content);
		}
		return out;
	}

	private void emit(Token.Type type, String str) {
		out.add(new Token(type, str));
	}

	private void tokenize() {
		while (r.hasMore()) {
			int start = r.offset();
			tokenizeStep();
			int end = r.offset();
			if (r.hasMore() && start == end) {
				throw new RuntimeException("Can't tokenize " + r.peekch());
			}
		}
	}

	private void tokenizeStep() {
		r.skipSpaces();
		if (Operators.ALL.contains(r.peek(2))) {
			emit(Token.Type.OPERATOR, r.read(2));
		}
		if (Operators.ALL.contains(r.peek(1))) {
			emit(Token.Type.OPERATOR, r.read(1));
		}
		String number = readNumber();
		if (number.length() > 0) {
			emit(Token.Type.NUMBER, number);
		}
		String id = readId();
		if (id.length() > 0) {
			emit(Token.Type.ID, id);
		}
	}

	@SuppressWarnings("all")
	private String readNumber() {
		final int[] index = {-1};
		return r.readWhile(new StrReader.FilterChar() {
			   @Override
			   public boolean filter(char ch) {
				   index[0]++;
				   if (index[0] == 0) {
					   return Character.isDigit(ch);
				   } else {
					   return Character.isDigit(ch) || ch == '.';
				   }
			   }
		   }
		);
	}

	private String readId() {
		return r.readWhile(ch -> Character.isJavaIdentifierPart(ch));
	}
}

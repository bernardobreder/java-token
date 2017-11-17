package breder.token;

import java.text.ParseException;

public class Grammer {

	private final Token[] tokens;

	private int index;

	public Grammer(Token[] tokens) {
		this.tokens = tokens;
		this.index = 0;
	}

	protected boolean eof() {
		return index >= tokens.length;
	}

	protected boolean eof(int count) {
		return index + count >= tokens.length;
	}

	protected Token read(String word, String message) throws SyntaxException {
		if (eof()) {
			throw new SyntaxException("expected '" + word + "' but was found <EOF> after token", last());
		}
		Token token = token();
		if (token.is(word)) {
			index++;
			return token;
		}
		throw new SyntaxException(message, token);
	}

	protected Token read(char word, String message) throws SyntaxException {
		if (eof()) {
			throw new SyntaxException("expected <" + word + "> but was found <EOF> after token", last());
		}
		Token token = token();
		if (token.is(word)) {
			index++;
			return token;
		}
		throw new SyntaxException(message, token);
	}

	protected Token readIdentifier(String message) throws SyntaxException {
		if (eof()) {
			throw new SyntaxException("expected <id> but was found <EOF> after token", last());
		}
		Token token = token();
		if (token.isIdentifier()) {
			index++;
			return token;
		}
		throw new SyntaxException(message, token);
	}

	protected Token readNumber(String message) throws SyntaxException {
		if (eof()) {
			throw new SyntaxException("expected <number> but was found <EOF> after token", last());
		}
		Token token = token();
		if (token.isNumber()) {
			index++;
			return token;
		}
		throw new SyntaxException(message, token);
	}

	protected Token readString(String message) throws SyntaxException {
		if (eof()) {
			throw new SyntaxException("expected <string> but was found <EOF> after token", last());
		}
		Token token = token();
		if (token.isString()) {
			index++;
			return token;
		}
		throw new SyntaxException(message, token);
	}

	protected boolean can(String word) {
		if (!eof() && token().is(word)) {
			index++;
			return true;
		}
		return false;
	}

	protected boolean can(char word) {
		if (!eof() && token().is(word)) {
			index++;
			return true;
		}
		return false;
	}

	protected boolean can(char word1, char word2) {
		if (!eof(1) && token().is(word1) && token(1).is(word2)) {
			index += 2;
			return true;
		}
		return false;
	}

	protected boolean is(String word) {
		return !eof() && token().is(word);
	}

	protected boolean is(int next, String word) {
		return !eof() && token(next).is(word);
	}

	protected boolean is(char word) {
		return !eof() && token().is(word);
	}

	protected boolean is(int next, char word) {
		return !eof() && token(next).is(word);
	}

	protected boolean isNumber() {
		return !eof() && token().isNumber();
	}

	protected boolean isString() {
		return !eof() && token().isString();
	}

	protected boolean isIdentifier() {
		return !eof() && token().isIdentifier();
	}

	protected Token next() {
		return tokens[index++];
	}

	protected void next(int count) {
		index += count;
	}

	protected Token token() {
		return tokens[index];
	}

	protected Token token(int next) {
		return tokens[index + next];
	}

	protected Token last() {
		return tokens[tokens.length - 1];
	}

	protected SyntaxException error(String message) {
		Token token = token();
		return new SyntaxException("[" + token.word + "," + token.offset + "]" + message, token);
	}

	public static class SyntaxException extends ParseException {

		public SyntaxException(String message, Token token) {
			super(message, token.offset);
		}

	}

}

package breder.token.grammar;

import static java.lang.String.format;

import breder.token.lexer.LexerToken;

public abstract class AbstractGrammar {
	
	protected final LexerToken[] tokens;
	
	protected int index;
	
	/**
	 * @param tokens
	 */
	public AbstractGrammar(LexerToken[] tokens) {
		super();
		this.tokens = tokens;
		this.index = 0;
	}
	
	protected boolean is(Enum<?> type) {
		return is(type.ordinal());
	}
	
	protected boolean is(Enum<?> curr, Enum<?> next) {
		return is(curr.ordinal(), next.ordinal());
	}
	
	protected boolean is(int type) {
		return index < tokens.length && tokens[index].type == type;
	}
	
	protected boolean is(int type, int next) {
		return index + 1 < tokens.length && tokens[index].type == type && tokens[index + 1].type == next;
	}
	
	protected boolean can(Enum<?> type) {
		return can(type.ordinal());
	}
	
	protected boolean can(int type) {
		if (index < tokens.length && tokens[index].type == type) {
			index++;
			return true;
		}
		return false;
	}
	
	protected LexerToken read(Enum<?> type) throws SyntaxException {
		return read(type.ordinal());
	}
	
	protected LexerToken read(int type) throws SyntaxException {
		if (index >= tokens.length) { throw errorEof(); }
		if (tokens[index].type != type) { throw error(type); }
		return tokens[index++];
	}
	
	protected LexerToken next() {
		return tokens[index++];
	}
	
	protected void next(int count) {
		index += count;
	}
	
	protected boolean eof() {
		return index >= tokens.length;
	}
	
	protected SyntaxException errorEof() {
		return new SyntaxException("<EOF>");
	}
	
	protected SyntaxException error(int type) {
		return error(type, tokens[index].type);
	}
	
	protected SyntaxException error(String message) {
		LexerToken token = tokens[index];
		return new SyntaxException(String.format("[%s] at line %d: %s", token.source, token.line, message));
	}
	
	protected SyntaxException error(int expected, int actual) {
		return error(format("expected type %d but was found %d", expected, actual));
	}
}

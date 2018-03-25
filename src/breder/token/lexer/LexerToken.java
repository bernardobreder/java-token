package breder.token.lexer;

public class LexerToken {

	public final String source;

	public final int offset;

	public final int line;

	public final int column;

	public final int type;

	public final String word;

	/**
	 * @param type
	 * @param source
	 * @param word
	 * @param offset
	 * @param line
	 * @param column
	 */
	public LexerToken(int type, String source, String word, int offset, int line, int column) {
		super();
		this.source = source;
		this.offset = offset;
		this.line = line;
		this.column = column;
		this.type = type;
		this.word = word;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("MyToken(%d, \"%s\", \"%s\", %d, %d, %d)", type, source, word, offset, line, column);
	}
}
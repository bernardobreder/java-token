package breder.token.lexer;

public class LexerException extends Exception {
	
	public final String source;
	
	public final String word;
	
	public final int offset;
	
	public final int line;
	
	public final int column;
	
	public LexerException(String source, String word, int offset, int line, int column) {
		super("LexerException [source=" + source + ", word=\"" + word + "\", offset=" + offset + ", line=" + line + ", column=" + column + "]");
		this.source = source;
		this.word = word;
		this.offset = offset;
		this.line = line;
		this.column = column;
	}
}

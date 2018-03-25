package breder.token.lexer;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	
	private List<AbstractMatcher> matchers = new ArrayList<>();
	
	public MyToken[] execute(String source, String content) throws LexerException {
		ArrayList<MyToken> tokens = new ArrayList<>();
		char[] chars = content.toCharArray();
		int offset = 0;
		int line = 1;
		int column = 1;
		int length = chars.length;
		int matcherSize = matchers.size();
		while (offset < length) {
			// Recupera o caracter corrente
			char c = chars[offset];
			// Ignora os caracteres de espaço
			while (c <= ' ') {
				if (c == '\n') {
					line++;
					column = 1;
				} else {
					column++;
				}
				offset++;
				if (offset == length) {
					break;
				}
				c = chars[offset];
			}
			if (offset == length) {
				break;
			}
			boolean found = false;
			for (int n = 0; n < matcherSize; n++) {
				AbstractMatcher matcher = matchers.get(n);
				String word = matcher.match(chars, offset);
				if (word != null) {
					found = true;
					int type = matcher.type;
					tokens.add(createToken(source, offset, line, column, word, type));
					int wordLength = word.length();
					int offsetLength = offset + wordLength;
					for (int m = offset; m < offsetLength; m++) {
						if (chars[m] == '\n') {
							line++;
							column = 1;
						} else {
							column++;
						}
					}
					offset += wordLength;
					break;
				}
			}
			if (!found) { throw new LexerException(source, errorWord(chars, offset), offset, line, column); }
		}
		return tokens.toArray(new MyToken[tokens.size()]);
	}
	
	protected MyToken createToken(String source, int offset, int line, int column, String word, int type) {
		return new MyToken(type, source, word, offset, line, column);
	}
	
	protected String errorWord(char[] chars, int offset) {
		String word = new String(chars, offset, Math.min(32, chars.length - offset));
		while (word.indexOf('\r') >= 0) {
			word = word.replace("\r", "\\r");
		}
		while (word.indexOf('\n') >= 0) {
			word = word.replace("\n", "\\n");
		}
		while (word.indexOf('\t') >= 0) {
			word = word.replace("\t", "\\t");
		}
		return word;
	}
	
	public void addSymbol(char character, int type) {
		matchers.add(new SymbolMatcher(type, character));
	}
	
	public void addKeyword(String keyword, int type) {
		matchers.add(new KeywordMatcher(type, keyword));
	}
	
	public void addPattern(String pattern, int type) {
		matchers.add(new PatternMatcher(type, Pattern.compile("^(" + pattern + ")")));
	}
	
	public static abstract class AbstractMatcher {
		
		protected final int type;
		
		/**
		 * @param type
		 */
		public AbstractMatcher(int type) {
			super();
			this.type = type;
		}
		
		public abstract String match(char[] chars, int offset);
	}
	
	public static class SymbolMatcher extends AbstractMatcher {
		
		protected final char character;
		
		public SymbolMatcher(int type, char character) {
			super(type);
			this.character = character;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String match(char[] chars, int offset) {
			if (chars[offset] == character) { return new String(chars, offset, 1); }
			return null;
		}
	}
	
	public static class KeywordMatcher extends AbstractMatcher {
		
		protected final String keyword;
		
		public KeywordMatcher(int type, String keyword) {
			super(type);
			this.keyword = keyword;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String match(char[] chars, int offset) {
			if (chars[offset] != keyword.charAt(0)) { return null; }
			int keywordLength = keyword.length();
			int offsetLength = offset + keywordLength;
			for (int n = offset + 1, m = 1; n < offsetLength; n++, m++) {
				if (chars[n] != keyword.charAt(m)) { return null; }
			}
			return new String(chars, offset, keywordLength);
		}
	}
	
	public static class PatternMatcher extends AbstractMatcher {
		
		protected final Pattern pattern;
		
		public PatternMatcher(int type, Pattern pattern) {
			super(type);
			this.pattern = pattern;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String match(char[] chars, int offset) {
			CharBuffer buffer = CharBuffer.wrap(chars, offset, chars.length - offset);
			Matcher m = pattern.matcher(buffer);
			if (!m.find()) { return null; }
			return m.group();
		}
	}
	
	public static class MyToken {
		
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
		public MyToken(int type, String source, String word, int offset, int line, int column) {
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
	
	public static class LexerException extends Exception {
		
		public final String source;
		
		public final String word;
		
		public final int offset;
		
		public final int line;
		
		public final int column;
		
		public LexerException(String source, String word, int offset, int line, int column) {
			this.source = source;
			this.word = word;
			this.offset = offset;
			this.line = line;
			this.column = column;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "LexerException [source=" + source + ", word=\"" + word + "\", offset=" + offset + ", line=" + line + ", column=" + column + "]";
		}
	}
}

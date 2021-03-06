package breder.token.lexer;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	private final List<AbstractMatcher> matchers = new ArrayList<>();

	private final Set<String> keywords = new HashSet<>();
	
	public LexerToken[] execute(String source, String content) throws LexerException {
		List<LexerToken> tokens = new ArrayList<>();
		char[] chars = content.toCharArray();
		int offset = 0;
		int line = 1;
		int column = 1;
		int length = chars.length;
		int matcherSize = matchers.size();
		while (offset < length) {
			// Recupera o caracter corrente
			char c = chars[offset];
			// Ignora os caracteres de espa�o
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
			// Verifica os Matchers
			boolean found = false;
			for (int n = 0; n < matcherSize; n++) {
				AbstractMatcher matcher = matchers.get(n);
				String word = matcher.match(chars, offset);
				if (word != null) {
					// Caso tenha efetuado um match
					found = true;
					tokens.add(createToken(source, content, word, matcher.type, offset, line, column));
					int offsetLength = offset + word.length();
					for (int m = offset; m < offsetLength; m++) {
						if (chars[m] == '\n') {
							line++;
							column = 1;
						} else {
							column++;
						}
					}
					offset += word.length();
					break;
				}
			}
			if (!found) { throw lexerException(source, chars, offset, line, column); }
		}
		return tokens.toArray(new LexerToken[tokens.size()]);
	}

	protected LexerToken createToken(String source, String content, String word, int type, int offset, int line, int column) {
		return new LexerToken(type, source, word, offset, line, column);
	}

	protected LexerException lexerException(String source, char[] chars, int offset, int line, int column) {
		return new LexerException(source, errorWord(chars, offset), offset, line, column);
	}

	protected String errorWord(char[] chars, int offset) {
		return errorReplace(new String(chars, offset, Math.min(32, chars.length - offset)));
	}

	protected String errorReplace(String word) {
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
		keywords.add(keyword);
	}

	public void addPattern(String pattern, int type) {
		matchers.add(new PatternMatcher(type, Pattern.compile("^(" + pattern + ")")));
	}

	public void addNotKeyword(String pattern, int type) {
		matchers.add(new NotKeywordMatcher(type, Pattern.compile("^(" + pattern + ")"), keywords));
	}

	protected static abstract class AbstractMatcher {

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

	protected static class SymbolMatcher extends AbstractMatcher {

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

	protected static class KeywordMatcher extends AbstractMatcher {

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

	protected static class PatternMatcher extends AbstractMatcher {

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

	protected static class NotKeywordMatcher extends AbstractMatcher {

		protected final Pattern pattern;

		protected final Set<String> keywords;

		public NotKeywordMatcher(int type, Pattern pattern, Set<String> keywords) {
			super(type);
			this.pattern = pattern;
			this.keywords = keywords;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String match(char[] chars, int offset) {
			CharBuffer buffer = CharBuffer.wrap(chars, offset, chars.length - offset);
			Matcher m = pattern.matcher(buffer);
			if (!m.find()) { return null; }
			String word = m.group();
			if (keywords.contains(word)) { return null; }
			return word;
		}
	}
}

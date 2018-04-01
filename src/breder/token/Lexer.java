package breder.token;

import java.util.ArrayList;
import java.util.List;

import breder.token.Token.TokenType;

/**
 * @deprecated Use {@link breder.token.lexer.Lexer}
 * @author Tecgraf/PUC-Rio
 */
@Deprecated
public class Lexer {

	private final String source;

	private final char[] chars;

	private int line = 1;

	private int column = 1;

	private int index = 0;

	private List<Token> tokens;

	private boolean dot;

	public Lexer(String source, String content) {
		this.source = source;
		this.chars = content.toCharArray();
	}

	public Token[] execute() {
		this.tokens = new ArrayList<>();
		while (!eof()) {
			step();
		}
		return tokens.toArray(new Token[tokens.size()]);
	}

	protected void step() {
		stepWhitespace();
		if (!eof()) {
			if (isIdStart()) {
				stepId();
			} else if (isNumberStart()) {
				stepNumber();
			} else if (isStringStart()) {
				stepString();
			} else {
				stepSymbol();
			}
		}
	}

	protected void stepWhitespace() {
		char c = chars[index];
		for (;;) {
			if (c <= 32) {
				if (c == '\n') {
					line++;
					column = 1;
				} else {
					column++;
				}
				index++;
				if (eof()) { return; }
				c = chars[index];
			} else if (stepComment()) {
				index += 2;
				column += 2;
				if (eof()) { return; }
				c = chars[index];
				while (c != '\n') {
					column++;
					c = chars[index++];
					if (eof()) { return; }
				}
				line++;
				column = 1;
				if (eof()) { return; }
				c = chars[index];
			} else {
				break;
			}
		}
	}

	protected boolean stepComment() {
		if (eof(1)) { return false; }
		if (chars[index] == '/' && chars[index + 1] == '/') { return true; }
		return false;
	}

	protected void stepId() {
		int line = this.line;
		int column = this.column;
		int begin = index;
		index++;
		while (!eof() && isIdPart()) {
			index++;
		}
		this.column += index - begin;
		tokens.add(new Token(source, new String(chars, begin, index - begin), TokenType.ID, line, column, begin));
	}

	protected void stepNumber() {
		int line = this.line;
		int column = this.column;
		int begin = index;
		if (chars[index] == '0' && index + 1 < chars.length && (chars[index + 1] == 'x' || chars[index + 1] == 'X')) {
			index += 2;
			while (!eof() && isHexPart()) {
				index++;
			}
			this.column += index - begin;
			tokens.add(new Token(source, new String(chars, begin, index - begin), TokenType.HEX, line, column, begin));
		} else {
			dot = false;
			index++;
			while (!eof() && isNumberPart()) {
				index++;
			}
			this.column += index - begin;
			tokens.add(new Token(source, new String(chars, begin, index - begin), TokenType.NUM, line, column, begin));
		}
	}

	protected void stepString() {
		int line = this.line;
		int column = this.column;
		int begin = index++;
		while (!eof() && isStringPart()) {
			index++;
		}
		if (!eof()) {
			index++;
		}
		this.column += index - begin;
		tokens.add(new Token(source, new String(chars, begin, index - begin), TokenType.STR, line, column, begin));
	}

	protected void stepSymbol() {
		int begin = index++;
		tokens.add(new Token(source, new String(chars, begin, 1), TokenType.SYM, line, column++, begin));
	}

	protected boolean isStringStart() {
		return chars[index] == '\"';
	}

	protected boolean isStringPart() {
		return chars[index] != '\"' || chars[index - 1] == '\\';
	}

	protected boolean isNumberStart() {
		char c = chars[index];
		return c >= '0' && c <= '9';
	}

	protected boolean isNumberPart() {
		char c = chars[index];
		if (c == '.') {
			if (dot) {
				return false;
			} else {
				dot = true;
				return true;
			}
		}
		return c >= '0' && c <= '9';
	}

	protected boolean isHexPart() {
		char c = chars[index];
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
	}

	protected boolean isIdStart() {
		char c = chars[index];
		return (c >= 'a' && c <= 'z') || //
				(c >= 'A' && c <= 'Z') || //
				c == '_';
	}

	protected boolean isIdPart() {
		char c = chars[index];
		return (c >= 'a' && c <= 'z') || //
				(c >= 'A' && c <= 'Z') || //
				(c >= '0' && c <= '9') || //
				c == '_';
	}

	protected boolean eof() {
		return index >= chars.length;
	}

	protected boolean eof(int next) {
		return index + next >= chars.length;
	}
}

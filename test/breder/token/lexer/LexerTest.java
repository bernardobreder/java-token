package breder.token.lexer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LexerTest {
	
	@Test
	public void test() throws LexerException {
		Lexer lexer = new Lexer();
		lexer.addPattern(";(.*)(\r)?\n", 1);
		lexer.addPattern(";(.*)$", 1);
		lexer.addKeyword("sin", 2);
		lexer.addPattern("[a-zA-Z_][a-zA-Z0-9_]*", 3);
		lexer.addKeyword("++", 4);
		lexer.addSymbol('(', 5);
		lexer.addSymbol(')', 6);
		String code = "\tsin(a++);abc\n\tsin()\n();sss";
		String source = "test.txt";
		LexerToken[] tokens = lexer.execute(source, code);
		assertEquals(string(new LexerToken(2, source, "sin", 1, 1, 2)), string(tokens[0]));
		assertEquals(string(new LexerToken(5, source, "(", 4, 1, 5)), string(tokens[1]));
		assertEquals(string(new LexerToken(3, source, "a", 5, 1, 6)), string(tokens[2]));
		assertEquals(string(new LexerToken(4, source, "++", 6, 1, 7)), string(tokens[3]));
		assertEquals(string(new LexerToken(6, source, ")", 8, 1, 9)), string(tokens[4]));
		assertEquals(string(new LexerToken(1, source, ";abc\n", 9, 1, 10)), string(tokens[5]));
		assertEquals(string(new LexerToken(2, source, "sin", 15, 2, 2)), string(tokens[6]));
		assertEquals(string(new LexerToken(5, source, "(", 18, 2, 5)), string(tokens[7]));
		assertEquals(string(new LexerToken(6, source, ")", 19, 2, 6)), string(tokens[8]));
		assertEquals(string(new LexerToken(5, source, "(", 21, 3, 1)), string(tokens[9]));
		assertEquals(string(new LexerToken(6, source, ")", 22, 3, 2)), string(tokens[10]));
		assertEquals(string(new LexerToken(1, source, ";sss", 23, 3, 3)), string(tokens[11]));
	}
	
	protected String string(LexerToken token) {
		return String.format("{type: %d, line: %d, column: %d, offset: %d, source: '%s', word: '%s'}", token.type, token.line, token.column, token.offset, token.source, token.word);
	}
}

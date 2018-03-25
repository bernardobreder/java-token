package breder.token;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

	@Test
	public void test() {
		assertTokenEquals("a", "a");
		assertTokenEquals("a1", "a1");
		assertTokenEquals("a2_Z", "a2_Z");
		assertTokenEquals("_Z", "_Z");
		assertTokenEquals("abc", "abc");
		assertTokenEquals(" a ", "a");
		assertTokenEquals("1", "1");
		assertTokenEquals("123", "123");
		assertTokenEquals("0x1", "0x1");
		assertTokenEquals("0x1Fb", "0x1Fb");
		assertTokenEquals("1.", "1.");
		assertTokenEquals("1.23", "1.23");
		assertTokenEquals("1.23.", "1.23", ".");
		assertTokenEquals("\"\"", "\"\"");
		assertTokenEquals("\"", "\"");
		assertTokenEquals("\"a\"", "\"a\"");
		assertTokenEquals("\"\\\"\"", "\"\\\"\"");
		assertTokenEquals("\"abc\"", "\"abc\"");
		assertTokenEquals("\"a\"1", "\"a\"", "1");
		assertTokenEquals("a b c", "a", "b", "c");
	}

	protected void assertTokenEquals(String content, String... words) {
		String expected = Arrays.toString(words);
		String actual = Arrays.toString(Arrays.stream(new Lexer("", content).execute()).map(e -> e.word).toArray(String[]::new));
		Assert.assertEquals(expected, actual);
	}
}

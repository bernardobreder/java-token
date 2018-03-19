package breder.token;

import static java.util.Arrays.stream;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import breder.token.grammar.ParserException;
import breder.token.grammar.Tokenizer;
import breder.token.grammar.Tokenizer.Token;

public class TokenizerTest {

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
    assertTokenEquals("sin(ax+b)", "sin", "(", "ax", "+", "b", ")");
  }

  protected void assertTokenEquals(String content, String... words) {
    Tokenizer tokenizer = new Tokenizer();
    tokenizer.add("sin|cos|exp|ln|sqrt", 1);
    tokenizer.add("\\(", 2);
    tokenizer.add("\\)", 3);
    tokenizer.add("\\.", 2);
    tokenizer.add("\\+|-", 4);
    tokenizer.add("\\*|/", 5);
    tokenizer.add("\"(.*)\"", 5);
    tokenizer.add("\\\"", 2);
    tokenizer.add("[1-9][0-9]*(.[0-9]*)?", 6);
    tokenizer.add("0x[0-9a-fA-F]+", 6);
    tokenizer.add("[a-zA-Z_][a-zA-Z0-9_]*", 7);
    tokenizer.add("\\;(.)*", 8);
    Token[] tokens = tokenizer.tokenize(content);
    String expected = Arrays.toString(words);
    String actual = Arrays.toString(stream(tokens) //
      .map(e -> e.sequence).toArray(String[]::new));
    Assert.assertEquals(expected, actual);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    Tokenizer tokenizer = new Tokenizer();
    tokenizer.add("sin|cos|exp|ln|sqrt", 1);
    tokenizer.add("\\(", 2);
    tokenizer.add("\\)", 3);
    tokenizer.add("\\+|-", 4);
    tokenizer.add("\\*|/", 5);
    tokenizer.add("[0-9]+", 6);
    tokenizer.add("[a-zA-Z][a-zA-Z0-9_]*", 7);
    tokenizer.add("\\;(.)*", 8);

    try {
      String code = " sin(x) * (1 - var_12) ";
      for (Tokenizer.Token tok : tokenizer.tokenize(code)) {
        System.out.println("" + tok.token + " " + tok.sequence);
      }
    }
    catch (ParserException e) {
      System.out.println(e.getMessage());
    }

  }
}

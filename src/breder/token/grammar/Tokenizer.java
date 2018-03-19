package breder.token.grammar;

import static java.util.regex.Pattern.compile;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

  private LinkedList<TokenInfo> tokenInfos = new LinkedList<>();

  public void add(String regex, int token) {
    tokenInfos.add(new TokenInfo(compile("^(" + regex + ")"), token));
  }

  public Token[] tokenize(String str) {
    List<Token> tokens = new LinkedList<>();
    String s = str.trim();
    tokens.clear();
    while (!s.equals("")) {
      boolean match = false;
      for (TokenInfo info : tokenInfos) {
        Matcher m = info.regex.matcher(s);
        if (m.find()) {
          match = true;
          String tok = m.group().trim();
          s = m.replaceFirst("").trim();
          tokens.add(new Token(info.token, tok));
          break;
        }
      }
      if (!match) {
        throw new ParserException("Unexpected character in input: " + s);
      }
    }
    return tokens.toArray(new Token[tokens.size()]);
  }

  private class TokenInfo {

    public final Pattern regex;

    public final int token;

    public TokenInfo(Pattern regex, int token) {
      super();
      this.regex = regex;
      this.token = token;
    }

  }

  public class Token {

    public final int token;

    public final String sequence;

    public Token(int token, String sequence) {
      super();
      this.token = token;
      this.sequence = sequence;
    }

  }

}

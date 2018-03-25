package breder.token.grammar;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
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
    String strTrim = str.trim();
    int offset = str.length() - strTrim.length();
    String string = strTrim;
    List<Token> tokens = new ArrayList<>();
    while (!string.equals("")) {
      boolean match = false;
      for (TokenInfo info : tokenInfos) {
        Matcher m = info.pattern.matcher(string);
        if (m.find()) {

          match = true;
          String tok = m.group().trim();
          string = m.replaceFirst("").trim();
          tokens.add(new Token(info.token, offset, tok));
          break;
        }
      }
      if (!match) {
        throw new ParserException("Unexpected character in input: " + string);
      }
    }
    return tokens.toArray(new Token[tokens.size()]);
  }

  private class TokenInfo {

    public final Pattern pattern;

    public final int token;

    public TokenInfo(Pattern regex, int token) {
      super();
      this.pattern = regex;
      this.token = token;
    }

  }

  public static class Token {

    public final int type;

    public final int offset;

    public final String sequence;

    public Token(int type, int offset, String sequence) {
      super();
      this.type = type;
      this.offset = offset;
      this.sequence = sequence;
    }

  }

}

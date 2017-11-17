package breder.token;

import java.util.ArrayList;
import java.util.List;

import breder.token.Token.TokenType;

public class Lexer {

  private char[] chars;

  private int index;

  private List<Token> tokens;

  private boolean dot;

  public Lexer(String content) {
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
      }
      else if (isNumberStart()) {
        stepNumber();
      }
      else if (isStringStart()) {
        stepString();
      }
      else {
        stepSymbol();
      }
    }
  }

  protected void stepWhitespace() {
    char c = chars[index];
    while (c <= 32) {
      index++;
      if (eof()) {
        return;
      }
      c = chars[index];
    }
  }

  protected void stepId() {
    int begin = index;
    index++;
    while (!eof() && isIdPart()) {
      index++;
    }
    tokens.add(new Token(new String(chars, begin, index - begin), TokenType.ID,
      begin));
  }

  protected void stepNumber() {
    int begin = index;
    dot = false;
    index++;
    while (!eof() && isNumberPart()) {
      index++;
    }
    tokens.add(new Token(new String(chars, begin, index - begin), TokenType.NUM,
      begin));
  }

  protected void stepString() {
    int begin = index++;
    while (!eof() && isStringPart()) {
      index++;
    }
    if (!eof()) {
      index++;
    }
    tokens.add(new Token(new String(chars, begin, index - begin), TokenType.STR,
      begin));
  }

  protected void stepSymbol() {
    int begin = index++;
    tokens.add(new Token(new String(chars, begin, 1), TokenType.SYM, begin));
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
      }
      else {
        dot = true;
        return true;
      }
    }
    return c >= '0' && c <= '9';
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

}

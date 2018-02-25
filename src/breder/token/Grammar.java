package breder.token;

import java.text.ParseException;

public class Grammar {

  private final Token[] tokens;

  private int index;

  public Grammar(Token[] tokens) {
    this.tokens = tokens;
    this.index = 0;
  }

  protected int offset() {
    return tokens[index].offset;
  }

  protected int line() {
    return tokens[index].line;
  }

  protected boolean eof() {
    return index >= tokens.length;
  }

  protected boolean eof(int count) {
    return index + count >= tokens.length;
  }

  protected Token read(String word, String message) throws SyntaxException {
    if (eof()) {
      throw new SyntaxException(last(), "expected '" + word
        + "' but was found <EOF> after token");
    }
    Token token = token();
    if (token.is(word)) {
      index++;
      return token;
    }
    throw new SyntaxException(token, message);
  }

  protected Token read(char word, String message) throws SyntaxException {
    if (eof()) {
      throw new SyntaxException(last(), "expected <" + word
        + "> but was found <EOF> after token");
    }
    Token token = token();
    if (token.is(word)) {
      index++;
      return token;
    }
    throw new SyntaxException(token, message);
  }

  protected Token readIdentifier(String message) throws SyntaxException {
    if (eof()) {
      throw new SyntaxException(last(),
        "expected <id> but was found <EOF> after token");
    }
    Token token = token();
    if (token.isIdentifier()) {
      index++;
      return token;
    }
    throw new SyntaxException(token, message);
  }

  protected Token readNumber(String message) throws SyntaxException {
    if (eof()) {
      throw new SyntaxException(last(),
        "expected <number> but was found <EOF> after token");
    }
    Token token = token();
    if (token.isNumber()) {
      index++;
      return token;
    }
    throw new SyntaxException(token, message);
  }

  protected Token readString(String message) throws SyntaxException {
    if (eof()) {
      throw new SyntaxException(last(),
        "expected <string> but was found <EOF> after token");
    }
    Token token = token();
    if (token.isString()) {
      index++;
      return token;
    }
    throw new SyntaxException(token, message);
  }

  protected boolean can(String word) {
    if (!eof() && token().is(word)) {
      index++;
      return true;
    }
    return false;
  }

  protected boolean can(char word) {
    if (!eof() && token().is(word)) {
      index++;
      return true;
    }
    return false;
  }

  protected boolean can(char word1, char word2) {
    if (!eof(1) && token().is(word1) && token(1).is(word2)) {
      index += 2;
      return true;
    }
    return false;
  }

  protected boolean is(String word) {
    return !eof() && token().is(word);
  }

  protected boolean is(int next, String word) {
    return !eof(next) && token(next).is(word);
  }

  protected boolean is(char word) {
    return !eof() && token().is(word);
  }

  protected boolean is(int next, char word) {
    return !eof(next) && token(next).is(word);
  }

  protected boolean is(char char1, char char2) {
    return is(char1) && is(1, char2);
  }

  protected boolean is(String str1, String str2) {
    return is(str1) && is(1, str2);
  }

  protected boolean isNumber() {
    return !eof() && token().isNumber();
  }

  protected boolean isString() {
    return !eof() && token().isString();
  }

  protected boolean isIdentifier() {
    return !eof() && token().isIdentifier();
  }

  protected boolean isNumber(int next) {
    return !eof(next) && token(next).isNumber();
  }

  protected boolean isString(int next) {
    return !eof(next) && token(next).isString();
  }

  protected boolean isIdentifier(int next) {
    return !eof(next) && token(next).isIdentifier();
  }

  protected Token next() {
    return tokens[index++];
  }

  protected Token next(int count) {
    Token begin = token();
    index += count - 1;
    Token end = token();
    index++;
    return begin.join(end);
  }

  protected Token token() {
    return tokens[index];
  }

  protected Token token(int next) {
    return tokens[index + next];
  }

  protected Token last() {
    return tokens[tokens.length - 1];
  }

  protected ParseException error(String message) {
    return error(message, eof() ? last() : token());
  }

  protected ParseException error(String message, Token token) {
    return new SyntaxException(token, "[" + token.word + ", off=" + token.offset
      + ", lin=" + token.line + "] " + message);
  }

  public static class SyntaxException extends ParseException {

    public final Token token;

    public SyntaxException(Token token, String message) {
      super(String.format("at %s with token '%s' at line %d and column %d: %s",
        token.source, token.word, token.line, token.column, message),
        token.offset);
      this.token = token;
    }

  }

}

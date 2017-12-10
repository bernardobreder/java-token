package breder.token;

public class Token {

  public final String source;

  public final String word;

  public final TokenType type;

  public final int line;

  public final int column;

  public final int offset;

  public final int length;

  public Token(String word, Token parent) {
    this(parent.source, word, parent.type, parent.line, parent.column,
      parent.offset, word.length());
  }

  public Token(String source, String word, TokenType type, int line, int column,
    int offset) {
    this(source, word, type, line, column, offset, word.length());
  }

  public Token(String source, String word, TokenType type, int line, int column,
    int offset, int length) {
    this.source = source;
    this.word = word;
    this.type = type;
    this.line = line;
    this.column = column;
    this.offset = offset;
    this.length = word.length();
  }

  public Token join(Token token) {
    return new Token(source, word + "..." + token.word, type, line, column,
      offset, token.offset - offset);
  }

  public boolean is(String word) {
    return type == TokenType.ID && word.hashCode() == word.hashCode()
      && this.word.equals(word);
  }

  public boolean is(char symbol) {
    return type == TokenType.SYM && symbol == word.charAt(0);
  }

  public boolean isIdentifier() {
    return type == TokenType.ID;
  }

  public boolean isSymbol() {
    return type == TokenType.SYM;
  }

  public boolean isNumber() {
    return type == TokenType.NUM;
  }

  public boolean isString() {
    return type == TokenType.STR;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return word;
  }

  public enum TokenType {

    ID,
    STR,
    NUM,
    SYM;

  }

}

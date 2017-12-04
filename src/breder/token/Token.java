package breder.token;

public class Token {

  public final String word;

  public final TokenType type;

  public final int offset;

  public final int length;

  public Token(String word, Token parent) {
    this(word, parent.type, parent.offset, word.length());
  }

  public Token(String word, TokenType type, int offset) {
    this(word, type, offset, word.length());
  }

  public Token(String word, TokenType type, int offset, int length) {
    this.word = word;
    this.type = type;
    this.offset = offset;
    this.length = word.length();
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

package breder.token;

public class Token {

  public final String word;

  public final TokenType type;

  public final int offset;

  public Token(String word, TokenType type, int offset) {
    this.word = word;
    this.type = type;
    this.offset = offset;
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

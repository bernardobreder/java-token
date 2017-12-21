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
      parent.offset, parent.length);
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
    this.length = length;
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + offset;
    result = prime * result + length;
    result = prime * result + ((source == null) ? 0 : source.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Token other = (Token) obj;
    if (offset != other.offset) {
      return false;
    }
    if (length != other.length) {
      return false;
    }
    if (source == null) {
      if (other.source != null) {
        return false;
      }
    }
    else if (!source.equals(other.source)) {
      return false;
    }
    return true;
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

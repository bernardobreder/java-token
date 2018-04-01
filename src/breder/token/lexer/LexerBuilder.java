package breder.token.lexer;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LexerBuilder {

	private final Map<String, Integer> nokeywords = new HashMap<>();

	private final Map<String, Integer> keywords = new HashMap<>();

	private final Map<Character, Integer> symbols = new HashMap<>();

	private final List<Entry<String, Integer>> patterns = new ArrayList<>();

	public void addSymbol(char character, int type) {
		symbols.put(character, type);
	}

	public void addKeyword(String keyword, int type) {
		keywords.put(keyword, type);
	}

	public void addPattern(String pattern, int type) {
		patterns.add(new SimpleEntry<String, Integer>(pattern, type));
	}

	public void addNotKeyword(String pattern, int type) {
		nokeywords.put(pattern, type);
	}

	public Lexer build() {
		Lexer lexer = new Lexer();
		for (Entry<String, Integer> entry : patterns) {
			lexer.addPattern(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Integer> entry : nokeywords.entrySet()) {
			lexer.addNotKeyword(entry.getKey(), entry.getValue());
		}
		keywords.entrySet().stream() //
				.sorted((a, b) -> compareString(b.getKey(), a.getKey())) //
				.forEach(e -> lexer.addKeyword(e.getKey(), e.getValue()));
		symbols.entrySet().stream() //
				.forEach(e -> lexer.addSymbol(e.getKey(), e.getValue()));
		return lexer;
	}

	protected int compareString(String a, String b) {
		int aLen = a.length();
		int bLen = b.length();
		return aLen != bLen ? aLen - bLen : a.compareTo(b);
	}
}

package breder.token;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import breder.token.lexer.LexerTest;

@RunWith(Suite.class)
@SuiteClasses({ //
		LexerTest.class, //
})
public class AllTests {
}

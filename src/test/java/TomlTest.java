
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SE2FightFightTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

	@Test
	public void testComments() {
		assertEquals(Toml.parseToml("#guh\n"), null);
	}

	@Test
	public void testKeyPresent() {
		assertEquals(Toml.parseToml("a = 1\n").get("a"), 1);
	}

	@Test
	public void testKeyAbsent() {
		assertEquals(Toml.parseToml("a = 1\n").get("b"), null);
	}
}

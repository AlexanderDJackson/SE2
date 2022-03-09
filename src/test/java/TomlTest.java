import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SE2FightFightTest 
{
    /**
     * Rigorous Test :-)
     */
    Toml toml;

    @Before
    public void initialize() {
        toml = new Toml();
    }

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testComment() {
        assertEquals(toml.parseToml("#guh/n"), null);
    }

    @Test
    public void testCommentAfter() {
        assertEquals(toml.parseToml("id = 123 # This is a comment"), null);
    }

    @Test
    public void testCommentInString() {
        assertEquals(toml.parseToml("a = \"#b\"").getString("a"), "#b");
    }

    @Test
    public void testCommentInStringArray() {
        assertEquals(toml.parseToml("a = [ \"#b\", \"#c\" ]").getArray("a"), "[ \"#b\", \"#c\" ]");
    }  

}

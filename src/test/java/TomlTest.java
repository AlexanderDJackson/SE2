import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class TomlTest 
{
    /**
     * Rigorous Test :-)
     */
    Toml toml;

    @Before
    public void initialize() {
        toml = new Toml();
    }

    /* @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    } */

    @Test
    public void testComment() {
        assertEquals(toml.parseToml("#guh/n"), new HashMap<String, Object>());
    }

    @Test
    public void testCommentAfter() {
        assertTrue(toml.parseToml("id = 123 # This is a comment").get("id").equals("123"));
    }

    @Test
    public void testCommentAfterString() {
        assertTrue(toml.parseToml("id = \"buh\" # This is a comment").get("id").equals("buh"));
    }

    @Test
    public void testCommentInString() {
        assertTrue(toml.parseToml("a = \"#b\"").get("a").equals("#b"));
    }

    @Test
    public void testCommentInStringArray() {
        assertTrue(toml.parseToml("a = [ \"#b\", \"#c\" ]").get("a").equals("[ \"#b\", \"#c\" ]"));
    }  
}

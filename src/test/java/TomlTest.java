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
    public void testComment2() {
        assertEquals(toml.parseToml("# This is a comment"), new HashMap<String, Object>());
    }

    @Test
    public void testCommentAfter() {
        assertTrue(toml.parseToml("id = 123 # This is a comment").get("id").equals("123"));
    }

    @Test
    public void testCommentAfter2() {
        assertTrue(toml.parseToml("name = \"John\" # This is a comment").get("name").equals("John"));
    }

    @Test
    public void testCommentAfterString() {
        assertTrue(toml.parseToml("huh = \"buh\" # This is a comment").get("huh").equals("buh"));
    }

    @Test
    public void testCommentAfterString2() {
        assertTrue(toml.parseToml("school = \"ACU\" #This is a comment").get("school").equals("ACU"));
    }

    @Test
    public void testCommentInString() {
        assertTrue(toml.parseToml("a = \"# b\"").get("a").equals("# b"));
    }

    @Test
    public void testCommentInString2() {
        assertTrue(toml.parseToml("hashtag = \"#selfie\"").get("hashtag").equals("#selfie"));
    }

    @Test
    public void testCommentInStringArray() {
        assertTrue(toml.parseToml("a = [ \"#b\", \"#c\" ]").get("a").equals("[ \"#b\", \"#c\" ]"));
    }

    @Test
    public void testCommentInStringArray2() {
        assertTrue(toml.parseToml("jobs = [ \"scrum master #1\", \"scrum master #2\" ]").get("jobs").equals("[ \"scrum master #1\", \"scrum master #2\" ]"));
    } 
}

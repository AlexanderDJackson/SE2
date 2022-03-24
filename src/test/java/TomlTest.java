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
        assertTrue((Integer) toml.parseToml("id = 123 # This is a comment").get("id") == 123);
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
    
    @Test
    public void testOpeningBracket() {
        assertTrue(toml.parseToml("bracket = [").get("bracket").equals("["));        
    }

    @Test
    public void testIntPositive() {
        Toml toml = new Toml();
        toml.parseToml("num = 1");
        assertTrue(toml.get("num") == 1);
    }

    @Test
    public void testIntWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +1");
        assertTrue(toml.get("num") == 1);
    }

    @Test
    public void testIntZero() {
        Toml toml = new Toml();
        toml.parseToml("num = 0");
        assertTrue(toml.get("num") == 0);
    }

    @Test
    public void testIntNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -1");
        assertTrue(toml.get("num") == -1);
    }

    @Test
    public void testFloatPositive() {
        Toml toml = new Toml();
        toml.parseToml("num = 3.1415");
        assertTrue(toml.get("num") == 3.1415);
    }

    @Test
    public void testFloatWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +1.0");
        assertTrue(toml.get("num") == 1.0);
    }

    @Test
    public void testFloatZero() {
        Toml toml = new Toml();
        toml.parseToml("num = 0.0");
        assertTrue(toml.get("num") == 0.0);
    }

    @Test
    public void testFloatNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -0.01");
        assertTrue(toml.get("num") == -0.01);
    }

    @Test
    public void testFloatSeparators() {
        Toml toml = new Toml();
        toml.parseToml("num = 224_617.445_991_228");
        assertTrue(toml.get("num") == 224617.445991228);
    }

    @Test
    public void testFloatSeparatorsNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -224_617.445_991_228");
        assertTrue(toml.get("num") == -224617.445991228);
    }

    @Test
    public void testScientific() {
        Toml toml = new Toml();
        toml.parseToml("num = 1e06");
        assertTrue(toml.get("num") == 1e06);
    }

    @Test
    public void testScientific2() {
        Toml toml = new Toml();
        toml.parseToml("num = 5e+22");
        assertTrue(toml.get("num") == 5e22);
        //(5*Math.pow(10,22))
    }

    @Test
    public void testScientific3() {
        Toml toml = new Toml();
        toml.parseToml("num = -2E-2");
        assertTrue(toml.get("num") == -2e-2);
    }

    @Test
    public void testFloatAndScientific() {
        Toml toml = new Toml();
        toml.parseToml("num = 6.626e-34");
        assertTrue(toml.get("num") == 6.626e-34);
    }

    @Test
    public void testInfinity() {
        Toml toml = new Toml();
        toml.parseToml("num = inf");
        assertTrue(toml.get("num") == Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInfinityWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +inf");
        assertTrue(toml.get("num") == Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInfinityNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -inf");
        assertTrue(toml.get("num") == Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testNan() {
        Toml toml = new Toml();
        toml.parseToml("num = nan");
        assertTrue(toml.get("num") == Double.NaN);
    }

    @Test
    public void testNanWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +nan");
        assertTrue(toml.get("num") == Double.NaN);
    }

    @Test
    public void testNanWithMinus() {
        Toml toml = new Toml();
        toml.parseToml("num = -nan");
        assertTrue(toml.get("num") == Double.NaN);
    }

    @Test
    public void testHex() {
        Toml toml = new Toml();
        toml.parseToml("num = 0xBEAD");
        assertTrue(toml.get("num") == 0xBEAD);
    }

    @Test
    public void testHex2() {
        Toml toml = new Toml();
        toml.parseToml("num = 0x123");
        assertTrue(toml.get("num") == 0x123);
    }

    @Test
    public void testHex3() {
        Toml toml = new Toml();
        toml.parseToml("num = 0xbead");
        assertTrue(toml.get("num") == 0xbead);
    }

    @Test
    public void testHex4() {
        Toml toml = new Toml();
        toml.parseToml("num = 0xbead_deed");
        assertTrue(toml.get("num") == 0xbeaddeed);
    }

    @Test
    public void testOctal() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o12");
        assertTrue(toml.get("num") == 012);
    }

    @Test
    public void testOctal2() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o012");
        assertTrue(toml.get("num") == 0012);
    }

    @Test
    public void testOctal3() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o755");
        assertTrue(toml.get("num") == 0755);
    }

    @Test
    public void testOctal4() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o1234567");
        assertTrue(toml.get("num") == 01234567);
    }

    @Test
    public void testBinary() {
        Toml toml = new Toml();
        toml.parseToml("num = 0b11010110");
        assertTrue(toml.get("num") == 0b11010110);
    }

    @Test
    public void testBinary2() {
        Toml toml = new Toml();
        toml.parseToml("num = 0b111");
        assertTrue(toml.get("num") == 0b111);
    }

    @Test
    public void testBinary3() {
        Toml toml = new Toml();
        toml.parseToml("num = 0b000");
        assertTrue(toml.get("num") == 0b000);
    }
}
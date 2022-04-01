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
        Toml toml = new Toml();
        toml.parseToml("#guh/n");
        assertEquals(toml.table, new HashMap<String, Object>());
    }

    @Test
    public void testComment2() {
        Toml toml = new Toml();
        toml.parseToml("# This is a comment");
        assertEquals(toml.table, new HashMap<String, Object>());
    }

    @Test
    public void testCommentAfter() {
        Toml toml = new Toml();
        toml.parseToml("id = 123 # This is a comment");
        assertTrue((Integer) toml.getInt("id") == 123);
    }

    @Test
    public void testCommentAfter2() {
        Toml toml = new Toml();
        toml.parseToml("name = \"John\" # This is a comment");
        assertTrue(toml.getString("name").equals("John"));
    }

    @Test
    public void testCommentAfterString() {
        Toml toml = new Toml();
        toml.parseToml("huh = \"buh\" # This is a comment");
        assertTrue(toml.getString("huh").equals("buh"));
    }

    @Test
    public void testCommentAfterString2() {
        Toml toml = new Toml();
        toml.parseToml("school = \"ACU\" #This is a comment");
        assertTrue(toml.getString("school").equals("ACU"));
    }

    @Test
    public void testCommentInString() {
        Toml toml = new Toml();
        toml.parseToml("a = \"# b\"");
        assertTrue(toml.getString("a").equals("# b"));
    }

    @Test
    public void testCommentInString2() {
        Toml toml = new Toml();
        toml.parseToml("hashtag = \"#selfie\"");
        assertTrue(toml.getString("hashtag").equals("#selfie"));
    }

    @Test
    public void testCommentInStringArray() {
        Toml toml = new Toml();
        toml.parseToml("a = [ \"#b\", \"#c\" ]");
        assertTrue(toml.getString("a").equals("[ \"#b\", \"#c\" ]"));
    }

    @Test
    public void testCommentInStringArray2() {
        Toml toml = new Toml();
        toml.parseToml("jobs = [ \"scrum master #1\", \"scrum master #2\" ]");
        assertTrue(toml.getString("jobs").equals("[ \"scrum master #1\", \"scrum master #2\" ]"));
    }
    
    @Test
    public void testOpeningBracket() {
        Toml toml = new Toml();
        toml.parseToml("bracket = \"[\"");
        assertTrue(toml.getString("bracket").equals("["));        
    }

    @Test
    public void testIntWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +1");
        assertTrue(toml.getInt("num") == 1);
    }

    @Test
    public void testScientific3() {
        Toml toml = new Toml();
        toml.parseToml("num = -2E-2");
        assertTrue(toml.getDouble("num") == -2e-2);
    }

    @Test
    public void testFloatAndScientific() {
        Toml toml = new Toml();
        toml.parseToml("num = 6.626e-34");
        assertTrue(toml.getDouble("num") == 6.626e-34);
    }

    @Test
    public void testInfinity() {
        Toml toml = new Toml();
        toml.parseToml("num = inf");
        assertTrue(toml.getDouble("num") == Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInfinityWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +inf");
        assertTrue(toml.getDouble("num") == Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInfinityNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -inf");
        assertTrue(toml.getDouble("num") == Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testHex() {
        Toml toml = new Toml();
        toml.parseToml("num = 0xBEAD");
        assertTrue(toml.getInt("num") == 0xBEAD);
    }

    @Test
    public void testHex2() {
        Toml toml = new Toml();
        toml.parseToml("num = 0xbead");
        assertTrue(toml.getInt("num") == 0xbead);
    }

    @Test
    public void testHex4() {
        Toml toml = new Toml();
        toml.parseToml("num = 0x123");
        assertTrue(toml.getInt("num") == 0x123);
    }

    @Test
    public void testHex5() {
        Toml toml = new Toml();
        toml.parseToml("num = 0x0123");
        assertTrue(toml.getInt("num") == 0x0123);
    }

    @Test
    public void testOctal() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o12");
        assertTrue(toml.getInt("num") == 012);
    }

    @Test
    public void testOctal2() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o012");
        assertTrue(toml.getInt("num") == 0012);
    }

    @Test
    public void testOctal3() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o755");
        assertTrue(toml.getInt("num") == 0755);
    }

    @Test
    public void testOctal4() {
        Toml toml = new Toml();
        toml.parseToml("num = 0o1234567");
        assertTrue(toml.getInt("num") == 01234567);
    }

    @Test
    public void testBinary() {
        Toml toml = new Toml();
        toml.parseToml("num = 0b11010110");
        assertTrue(toml.getInt("num") == 0b11010110);
    }

    @Test
    public void testBinary2() {
        Toml toml = new Toml();
        toml.parseToml("num = 0b111");
        assertTrue(toml.getInt("num") == 0b111);
    }

    @Test
    public void testBinary3() {
        Toml toml = new Toml();
        toml.parseToml("num = 0b000");
        assertTrue(toml.getInt("num") == 0b000);
    }

    @Test
    public void testBinary4() {
        Toml toml = new Toml();
        toml.parseToml("num = 0b0101");
        assertTrue(toml.getInt("num") == 0b0101);
    }

    @Test
    public void testFloatPositive() {
        Toml toml = new Toml();
        toml.parseToml("num = 3.1415");
        assertTrue(toml.getDouble("num") == 3.1415);
    }

    @Test
    public void testFloatWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +1.0");
        assertTrue(toml.getDouble("num") == 1.0);
    }

    @Test
    public void testFloatZero() {
        Toml toml = new Toml();
        toml.parseToml("num = 0.0");
        assertTrue(toml.getDouble("num") == 0.0);
    }

    @Test
    public void testFloatNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -0.01");
        assertTrue(toml.getDouble("num") == -0.01);
    }

    @Test
    public void testFloatSeparators() {
        Toml toml = new Toml();
        toml.parseToml("num = 224_617.445_991_228");
        assertTrue(toml.getDouble("num") == 224617.445991228);
    }

    @Test
    public void testFloatSeparatorsNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -224_617.445_991_228");
        assertTrue(toml.getDouble("num") == -224617.445991228);
    }

    @Test
    public void testIntNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -1");
        assertTrue(toml.getInt("num") == -1);
    }

    @Test
    public void testNan() {
        Toml toml = new Toml();
        toml.parseToml("num = nan");
        assertTrue(Double.isNaN(toml.getDouble("num")));
    }

    @Test
    public void testNanWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +nan");
        assertTrue(Double.isNaN(toml.getDouble("num")));
    }

    @Test
    public void testNanWithMinus() {
        Toml toml = new Toml();
        toml.parseToml("num = -nan");
        assertTrue(Double.isNaN(toml.getDouble("num")));
    }

    @Test
    public void testScientific() {
        Toml toml = new Toml();
        toml.parseToml("num = 1e06");
        assertTrue(toml.getDouble("num") == 1e06);
    }

    @Test
    public void testHex3() {
        Toml toml = new Toml();
        toml.parseToml("num = 0xbead_deed");
        assertTrue(toml.getLong("num").equals(Long.decode("0xbeaddeed")));
    }

    @Test
    public void testArray() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1,\n2,\n3\n]");
        assertTrue(toml.getString("array").equals("[ 1,2,3]"));
    }

    @Test
    public void testScientific2() {
        Toml toml = new Toml();
        toml.parseToml("num = 5e+22");
        assertTrue(toml.getInt("num") == 5e22);
    }

    @Test
    public void testIntPositive() {
        Toml toml = new Toml();
        toml.parseToml("num = 1");
        assertTrue(toml.getInt("num") == 1);
    }

    @Test
    public void testIntZero() {
        Toml toml = new Toml();
        toml.parseToml("num = 0");
        assertTrue(toml.getInt("num") == 0);
    }
}

import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

import Toml.Toml;

/**
 *  Unit testing for TOML (Tom's Obvious, Minimal Language) Java Implementation
 * 
 *	Authors: 	Brett Hammit (bah20a@acu.edu)
 *			 	Alex Jackson (asj18a@acu.edu)
 *			 	Justin Raitz (jmr18c@acu.edu)
 *	Start Date:	03/08/22
 *	Due Date:	04/27/22
 *	Class:		CS375.01
 *
 *	Compile: 	Locate folder with maven pom.xml from command prompt
 *				Run %mvn compile
 *	Test:		%mvn test
 *  Clean:      %mvn clean
 *	Execute:	%java -cp target/classes/ Toml (***Only useful if you add a main function to Toml.java)
 */
public class TomlTest 
{
    Toml toml;

    @Before
    public void initialize() {
        toml = new Toml();
    }

    @Test
    public void testComment() {
        Toml toml = new Toml();
        toml.parseToml("#guh/n");
        assertTrue(toml.isEmpty());
    }

    @Test
    public void testComment2() {
        Toml toml = new Toml();
        toml.parseToml("# This is a comment");
        assertTrue(toml.isEmpty());
    }

    @Test
    public void testCommentAfter() {
        Toml toml = new Toml();
        toml.parseToml("id = 123 # This is a comment");
        assertTrue(toml.getInt("id") == 123);
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
        assertTrue(toml.getArray("a")[0].equals("#b"));
    }

    @Test
    public void testCommentInStringArray2() {
        Toml toml = new Toml();
        toml.parseToml("jobs = [ \"scrum master #1\", \"scrum master #2\" ]");
        assertTrue(toml.getArray("jobs")[0].equals("scrum master #1"));
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
    public void testIntPositive() {
        Toml toml = new Toml();
        toml.parseToml("num = 1");
        assertTrue(toml.getInt("num") == 1);
    }

    @Test
    public void testIntNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -1");
        assertTrue(toml.getInt("num") == -1);
    }

    @Test
    public void testIntZero() {
        Toml toml = new Toml();
        toml.parseToml("num = 0");
        assertTrue(toml.getInt("num") == 0);
    }

    @Test
    public void testScientific() {
        Toml toml = new Toml();
        toml.parseToml("num = 1e06");
        assertTrue(toml.getDouble("num") == 1e06);
    }

    @Test
    public void testScientific2() {
        Toml toml = new Toml();
        toml.parseToml("num = 5e+22");
        assertTrue(toml.getDouble("num") == 5e22);
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
        assertTrue(toml.getNumber("num").equals(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testInfinityWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +inf");
        assertTrue(toml.getNumber("num").equals(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testInfinityNegative() {
        Toml toml = new Toml();
        toml.parseToml("num = -inf");
        assertTrue(toml.getNumber("num").equals(Double.NEGATIVE_INFINITY));
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
    public void testHex3() {
        Toml toml = new Toml();
        toml.parseToml("num = 0xbead_deed");
        assertTrue(toml.getNumber("num").equals(new BigDecimal(Long.decode("0xbeaddeed"))));
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
    public void testNan() {
        Toml toml = new Toml();
        toml.parseToml("num = nan");
        assertTrue(toml.getNumber("num").equals(Double.NaN));
    }

    @Test
    public void testNanWithPlus() {
        Toml toml = new Toml();
        toml.parseToml("num = +nan");
        assertTrue(toml.getNumber("num").equals(Double.NaN));
    }

    @Test
    public void testNanWithMinus() {
        Toml toml = new Toml();
        toml.parseToml("num = -nan");
        assertTrue(toml.getNumber("num").equals(Double.NaN));
    }

    @Test
    public void testIntArray() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1, 2, 3 ]");
        assertTrue(((BigDecimal) toml.getArray("array")[0]).intValue() == 1);
    }

    @Test
    public void testIntArray2() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1, 2, 3 ]");
        assertTrue(((BigDecimal) toml.getArray("array")[1]).intValue() == 2);
    }

    @Test
    public void testIntArray3() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1, 2, 3 ]");
        assertTrue(((BigDecimal) toml.getArray("array")[2]).intValue() == 3);
    }

    @Test
    public void testIntArray4() {
        Toml toml = new Toml();
        toml.parseToml("array = [11,22,33]");
        assertTrue(((BigDecimal) toml.getArray("array")[0]).intValue() == 11);
    }

    @Test
    public void testIntArray5() {
        Toml toml = new Toml();
        toml.parseToml("array = [11,22,33]");
        assertTrue(((BigDecimal) toml.getArray("array")[1]).intValue() == 22);
    }

    @Test
    public void testIntArray6() {
        Toml toml = new Toml();
        toml.parseToml("array = [11,22,33]");
        assertTrue(((BigDecimal) toml.getArray("array")[2]).intValue() == 33);
    }

    @Test
    public void testFloatArray() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1.1, 2.2, 3.3 ]");
        assertTrue(((BigDecimal) toml.getArray("array")[0]).floatValue() == (float) 1.1);
    }

    @Test
    public void testFloatArray2() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1.1, 2.2, 3.3 ]");
        assertTrue(((BigDecimal) toml.getArray("array")[1]).floatValue() == (float) 2.2);
    }

    @Test
    public void testFloatArray3() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1.1, 2.2, 3.3 ]");
        assertTrue(((BigDecimal) toml.getArray("array")[2]).floatValue() == (float) 3.3);
    }

    @Test
    public void testFloatArray4() {
        Toml toml = new Toml();
        toml.parseToml("array = [1.12,2.23,3.34]");
        assertTrue(((BigDecimal) toml.getArray("array")[0]).floatValue() == (float) 1.12);
    }

    @Test
    public void testFloatArray5() {
        Toml toml = new Toml();
        toml.parseToml("array = [1.12,2.23,3.34]");
        assertTrue(((BigDecimal) toml.getArray("array")[1]).floatValue() == (float) 2.23);
    }

    @Test
    public void testFloatArray6() {
        Toml toml = new Toml();
        toml.parseToml("array = [1.12,2.23,3.34]");
        assertTrue(((BigDecimal) toml.getArray("array")[2]).floatValue() == (float) 3.34);
    }

    @Test
    public void testStringArray() {
        Toml toml = new Toml();
        toml.parseToml("array = [ \"Hi\", \"Hello\", \"Bye\" ]");
        assertTrue(toml.getArray("array")[0].equals("Hi"));
    }

    @Test
    public void testStringArray2() {
        Toml toml = new Toml();
        toml.parseToml("array = [ \"Hi\", \"Hello\", \"Bye\" ]");
        assertTrue(toml.getArray("array")[1].equals("Hello"));
    }

    @Test
    public void testStringArray3() {
        Toml toml = new Toml();
        toml.parseToml("array = [ \"Hi\", \"Hello\", \"Bye\" ]");
        assertTrue(toml.getArray("array")[2].equals("Bye"));
    }

    @Test
    public void testNestedStringArray() {
        Toml toml = new Toml();
        toml.parseToml("array = [[\"Hi\", \"Hello\"], [\"Bye\", \"Good-Bye\"]]");
        assertTrue(((Object[]) toml.getArray("array")[0])[0].equals("Hi"));
    }

    @Test
    public void testNestedStringArray2() {
        Toml toml = new Toml();
        toml.parseToml("array = [[\"Hi\", \"Hello\"], [\"Bye\", \"Good-Bye\"]]");
        assertTrue(((Object[]) toml.getArray("array")[0])[1].equals("Hello"));
    }

    @Test
    public void testNestedStringArray3() {
        Toml toml = new Toml();
        toml.parseToml("array = [[\"Hi\", \"Hello\"], [\"Bye\", \"Good-Bye\"]]");
        assertTrue(((Object[]) toml.getArray("array")[1])[1].equals("Good-Bye"));
    }

    @Test
    public void testMultiLineArray() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1,\n2,\n3\n]");
        assertTrue(((BigDecimal) toml.getArray("array")[0]).intValue() == 1);
    }

    @Test
    public void testMultiLineArray2() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1,\n2,\n3\n]");
        assertTrue(((BigDecimal) toml.getArray("array")[1]).intValue() == 2);
    }

    @Test
    public void testMultiLineArray3() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 1,\n2,\n3\n]");
        assertTrue(((BigDecimal) toml.getArray("array")[2]).intValue() == 3);
    }

    @Test
    public void testMultiLineArray4() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 9,\n 8,\n 7\n ]");
        assertTrue(((BigDecimal) toml.getArray("array")[0]).intValue() == 9);
    }

    @Test
    public void testMultiLineArray5() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 9,\n 8,\n 7\n ]");
        assertTrue(((BigDecimal) toml.getArray("array")[1]).intValue() == 8);
    }

    @Test
    public void testMultiLineArray6() {
        Toml toml = new Toml();
        toml.parseToml("array = [ 9,\n 8,\n 7\n ]");
        assertTrue(((BigDecimal) toml.getArray("array")[2]).intValue() == 7);
    }

    @Test
    public void testCollapseStringLiteral() {
        Toml toml = new Toml();
        assertTrue(toml.collapseLitStrings("lines = '''\nThe first newline is\ntrimmed in raw strings.\nAll other whitespace\nis preserved.\n'''").equals("lines = 'The first newline is\\ntrimmed in raw strings.\\nAll other whitespace\\nis preserved.\\n'"));
    }

    @Test
    public void testCollapseStringLiteral2() {
        Toml toml = new Toml();
        assertTrue(toml.collapseLitStrings("lines = '''\nThe first newline is\ntrimmed in raw strings.\n\tAll other whitespace\n\tis preserved.\n'''").equals("lines = 'The first newline is\\ntrimmed in raw strings.\\n\tAll other whitespace\\n\tis preserved.\\n'"));
    }

    @Test
    public void testCollapseStringLiteral3() {
        Toml toml = new Toml();
        assertTrue(toml.collapseLitStrings("regex2 = '''I [dw]on't need \\d{2} apples'''").equals("regex2 = 'I [dw]on't need \\d{2} apples'"));
    }

    @Test
    public void testCollapseStringLiteral4() {
        Toml toml = new Toml();
        assertTrue(toml.collapseLitStrings("re = '''\\t{2} apps is t[wo]o many'''").equals("re = '\\t{2} apps is t[wo]o many'"));
    }

    @Test
    public void testPreProcess() {
        Toml toml = new Toml();
        assertTrue(toml.preProcess("int = 1\n# guh\nstring = \"hi\"").equals("int = 1\nstring = \"hi\""));
    }

    @Test
    public void testPreProcess2() {
        Toml toml = new Toml();
        assertTrue(toml.preProcess("int = 1\nstring = \"# guh hi\"").equals("int = 1\nstring = \"# guh hi\""));
    }

    @Test
    public void testPreProcess3() {
        Toml toml = new Toml();
        assertTrue(toml.preProcess("int = 1\n\n\tstring = \"hi\"").equals("int = 1\nstring = \"hi\""));
    }

    @Test
    public void testDate() {
        Toml toml = new Toml();
        toml.parseToml("date = 2022-03-31");
        assertTrue(toml.getDate("date").equals(LocalDate.parse("2022-03-31")));
    }

    @Test
    public void testDate2(){
        Toml toml = new Toml();
        toml.parseToml("date = 1979-05-27");
        assertTrue(toml.getDate("date").compareTo(LocalDate.parse("1979-05-27")) == 0);
    }

    @Test
    public void testTime() {
        Toml toml = new Toml();
        toml.parseToml("time = 23:29:09.58760");
        assertTrue(toml.getTime("time").equals(LocalTime.parse("23:29:09.58760")));
    }

    @Test
    public void testDateTime() {
        Toml toml = new Toml();
        toml.parseToml("time = 1979-05-27T07:32:00");
        assertTrue(toml.getDateTime("time").equals(LocalDateTime.parse("1979-05-27T07:32:00")));
    }

    @Test
    public void testAdd() {
        Toml toml = new Toml();
        toml.parseToml("name = \"guh buh\"");
        toml.add("age", new BigDecimal(69));
        assertTrue(toml.getInt("age") == 69);
    }
}

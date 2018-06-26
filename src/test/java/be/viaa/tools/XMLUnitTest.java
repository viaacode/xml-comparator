package be.viaa.tools;

import org.junit.Test;
import org.junit.Assert.*;

import org.xmlunit.*;

import static org.hamcrest.MatcherAssert.*;
import org.xmlunit.matchers.CompareMatcher;
//~ import org.xmlunit.XMLAssert.assertXMLEqual;


/**
 * XML comparison testing
 *
 */
public class XMLUnitTest {
    @Test
    public void test() {
        String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
        String testXml = "<struct><int>3</int><boolean>false</boolean></struct>";

        //~ XMLUnit.setIgnoreWhitespace(true);      // ignore whitespace differences
        //~ XMLUnit.setIgnoreAttributeOrder(true);  // ignore attribute order

        assertThat(testXml, CompareMatcher.isIdenticalTo(controlXml).ignoreWhitespace());
    }
}

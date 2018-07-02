package be.viaa.tools.xmldiff.diff.attribute;

import be.viaa.tools.xmldiff.BaseTest;
import be.viaa.tools.xmldiff.DifferenceComparator;
import be.viaa.tools.xmldiff.model.ComparisonDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test attribute change tests")
public class SingleAttributeChangeTest extends BaseTest {
    final static String CONTROL_FILE_PATH = "test-data/BaseFile.xml";
    final static String TEST_FILE_PATH = "test-data/diff/attribute-change/SingleAttributeChangeFile.xml";

    @BeforeAll
    public void initialize() {
        super.initialize(CONTROL_FILE_PATH, TEST_FILE_PATH);
    }

    @Test
    @DisplayName("Test one attribute difference")
    public void testOneTextValueDifference() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        assertEquals(0, comparisonDetail.getNodeDifferences().size(), "There should be 0 node differences");
        assertEquals(0, comparisonDetail.getTextValueDifferences().size(), "There should be 0 text value difference");
        assertEquals(1, comparisonDetail.getAttributeDifferences().size(), "There should be 1 attribute differences");
        assertNull(comparisonDetail.getAttributeDifferences().get(0).getControlValue(), "The control value should be null");
        assertEquals("name", comparisonDetail.getAttributeDifferences().get(0).getTestValue().getLocalName(), "The attribute name should be 'name'");
        assertEquals("VIAA", comparisonDetail.getAttributeDifferences().get(0).getTestValue().getNodeValue(), "The attribute value should be 'VIAA'");
    }
}

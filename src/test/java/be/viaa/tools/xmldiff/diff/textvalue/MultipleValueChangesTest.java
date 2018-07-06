package be.viaa.tools.xmldiff.diff.textvalue;

import be.viaa.tools.xmldiff.BaseTest;
import be.viaa.tools.xmldiff.DifferenceComparator;
import be.viaa.tools.xmldiff.model.ComparisonDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test value change tests")
public class MultipleValueChangesTest extends BaseTest {
    final static String CONTROL_FILE_PATH = "test-data/BaseFile.xml";
    final static String TEST_FILE_PATH = "test-data/diff/value-change/MultipleValueChangesFile.xml";

    @BeforeAll
    public void initialize() {
        super.initialize(CONTROL_FILE_PATH, TEST_FILE_PATH);
    }

    @Test
    @DisplayName("Test multiple text value difference")
    public void testMultipleTextValueDifference() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        assertEquals(0, comparisonDetail.getNodeDifferences().size(), "There should be 0 node differences");
        assertEquals(2, comparisonDetail.getTextValueDifferences().size(), "There should be 2 text value difference");
        assertEquals(0, comparisonDetail.getAttributeDifferences().size(), "There should be 0 attribute differences");
    }
}
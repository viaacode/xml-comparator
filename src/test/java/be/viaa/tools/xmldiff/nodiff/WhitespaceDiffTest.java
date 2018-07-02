package be.viaa.tools.xmldiff.nodiff;


import be.viaa.tools.xmldiff.BaseTest;
import be.viaa.tools.xmldiff.DifferenceComparator;
import be.viaa.tools.xmldiff.model.ComparisonDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by VIAA on 26/06/2018.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test 2 files with whitespace changes only")
public class WhitespaceDiffTest extends BaseTest {
    final static String CONTROL_FILE_PATH = "test-data/BaseFile.xml";
    final static String TEST_FILE_PATH = "test-data/no-diff/whitespace-diff/WhitespaceDiffFile.xml";

    @BeforeAll
    public void initialize() {
        super.initialize(CONTROL_FILE_PATH, TEST_FILE_PATH);
    }

    @Test
    @DisplayName("ignoring whitespaces")
    public void testWhitespaceDiffFilesIgnoreWhitespace() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        assertEquals(0, comparisonDetail.getNodeDifferences().size(), "There should be 0 node differences");
        assertEquals(0, comparisonDetail.getTextValueDifferences().size(), "There should be 0 text value differences");
        assertEquals(0, comparisonDetail.getAttributeDifferences().size(), "There should be 0 attribute differences");
        assertTrue(comparisonDetail.areEqual(), "Are equal should be true");
    }

    @Test
    @DisplayName("checking whitespaces")
    public void testWhitespaceDiffFilesCheckWhitespaces() {
        comparator = new DifferenceComparator(engine, false);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        assertEquals(0, comparisonDetail.getNodeDifferences().size(), "There should be 0 node differences");
        assertTrue(comparisonDetail.getTextValueDifferences().size() > 0, "There should be more than 0 text value differences");
        assertEquals(0, comparisonDetail.getAttributeDifferences().size(), "There should be 0 attribute differences");
        assertFalse(comparisonDetail.areEqual(), "Are equal should be false");
    }
}

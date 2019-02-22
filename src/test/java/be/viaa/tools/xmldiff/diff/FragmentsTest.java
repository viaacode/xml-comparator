package be.viaa.tools.xmldiff.diff;

import be.viaa.tools.xmldiff.BaseTest;
import be.viaa.tools.xmldiff.DifferenceComparator;
import be.viaa.tools.xmldiff.model.ComparisonDetail;
import be.viaa.tools.xmldiff.model.elementselectorbuilders.FragmentElementSelectorBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test inserted fragment somewhere in the middle")
public class FragmentsTest extends BaseTest {
    final static String CONTROL_FILE_PATH = "test-data/fragments/ControlFragments.xml";
    final static String TEST_FILE_PATH = "test-data/fragments/TestFragments.xml";

    @BeforeAll
    public void initialize() {
        super.initialize(CONTROL_FILE_PATH, TEST_FILE_PATH);
    }

    @Test
    @DisplayName("Test fragment diff")
    public void testExtraNode() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        assertEquals(1, comparisonDetail.getNodeDifferences().size(), "There should be 1 node difference");
        assertEquals(1, comparisonDetail.getTextValueDifferences().size(), "There should be 1 text value differences");
        assertEquals(0, comparisonDetail.getAttributeDifferences().size(), "There should be 0 attribute differences");
    }
}

package be.viaa.tools.xmldiff.diff.node;

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
@DisplayName("Node difference tests")
public class ExtraNodeTest extends BaseTest {
    final static String CONTROL_FILE_PATH = "test-data/BaseFile.xml";
    final static String TEST_FILE_PATH = "test-data/diff/extra-node/ExtraNodeFile.xml";

    @BeforeAll
    public void initialize() {
        super.initialize(CONTROL_FILE_PATH, TEST_FILE_PATH);
    }

    @Test
    @DisplayName("Test extra node")
    public void testExtraNode() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        assertEquals(1, comparisonDetail.getNodeDifferences().size(), "There should be 1 node difference");
        assertEquals(0, comparisonDetail.getTextValueDifferences().size(), "There should be 0 text value differences");
        assertEquals(0, comparisonDetail.getAttributeDifferences().size(), "There should be 0 attribute differences");
        assertNull(comparisonDetail.getNodeDifferences().get(0).getControlValue(), "The control value should be null");
        assertEquals("<field3>c</field3>", comparisonDetail.getNodeDifferences().get(0).toXMLString().trim(), "The extra node should be '<field3>c</field3>'");
    }
}

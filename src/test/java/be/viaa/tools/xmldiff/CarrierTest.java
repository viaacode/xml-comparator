package be.viaa.tools.xmldiff;


import be.viaa.tools.xmldiff.model.ComparisonDetail;
import be.viaa.tools.xmldiff.model.differences.TextValueDifference;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;
import be.viaa.tools.xmldiff.fileutils.*;

import javax.xml.transform.Source;
import java.io.File;

/**
 * Created by dieter on 26/06/2018.
 */
public class CarrierTest {
    final static Logger logger = Logger.getLogger(CarrierTest.class);
    private DifferenceEngine engine;
    private FileReader fileReader;
    private DifferenceComparator comparator;
    private Source control;
    private Source test;

    @Before
    public void initialize() {
        // Wire all the things!
        engine = new DOMDifferenceEngine();
        fileReader = new FileReader();

        // Read the files
        File controlFile = fileReader.readFile("test-data/metadata/CarrierMetadataControl.xml");
        File testFile = fileReader.readFile("test-data/metadata/CarrierMetadataTest.xml");

        // Initialize the control and source
        control = SourceGenerator.fromFile(controlFile);
        test = SourceGenerator.fromFile(testFile);
    }

    @Test
    public void testIgnoreWhitespaces() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        Assert.assertEquals("There should be 5 node differences", 5, comparisonDetail.getNodeDifferences().size());
        Assert.assertEquals("There should be 32 text value differences", 32, comparisonDetail.getTextValueDifferences().size());

        for (TextValueDifference tvd : comparisonDetail.getTextValueDifferences()) {
            // All text value differences are '<VALUE> 0' vs '<VALUE> 1'
            Assert.assertEquals("Text values should only be 0's replaced with 1's", tvd.getControlValue().replace("0", "1"), tvd.getTestValue());
        }
        Assert.assertTrue("Node differences should contain 4 'multiselect' differences", comparisonDetail.getNodeDifferences().stream().filter(nd -> nd.toString().contains("<multiselect>")).count() == 4);


    }
}

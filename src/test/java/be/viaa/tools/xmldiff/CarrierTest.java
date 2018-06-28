package be.viaa.tools.xmldiff;


import be.viaa.tools.xmldiff.model.ComparisonDetail;
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
        File controlFile = fileReader.readFile("test-data/carrier/CarrierMetadataControl.xml");
        File testFile = fileReader.readFile("test-data/carrier/CarrierMetadataTest.xml");

        // Initialize the control and source
        control = SourceGenerator.fromFile(controlFile);
        test = SourceGenerator.fromFile(testFile);
    }

    @Test
    public void testIgnoreWhitespaces() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        Assert.assertEquals("There should be 4 node differences", 4, comparisonDetail.getNodeDifferences().size());
        Assert.assertEquals("There should be 33 text value differences", 33, comparisonDetail.getTextValueDifferences().size());
        Assert.assertTrue("Node differences should contain 4 'multiselect' differences", comparisonDetail.getNodeDifferences().stream().filter(nd -> nd.toString().contains("<multiselect>")).count() == 4);


    }

    @Test
    public void testEmptyTags() {
        final String CONTROL_XML =
                "<root>" +
                    "<data>value</data>" +
                    "<otherdata>value1</otherdata>" +
                "</root>";
        final String TEST_XML =
                "<root>" +
                    "<data></data>" +
                    "<otherdata>value2</otherdata>" +
                "</root>";

        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(
                SourceGenerator.fromString(CONTROL_XML),
                SourceGenerator.fromString(TEST_XML)
        );
        Assert.assertEquals("Should contain 2 text value differences", 2, comparisonDetail.getTextValueDifferences().size());
        Assert.assertEquals("Should contain 0 node differences", 0, comparisonDetail.getNodeDifferences().size());
    }

    @Test
    public void testSelfClosingTags() {
        final String CONTROL_XML =
                "<root>" +
                        "<data>value</data>" +
                        "<otherdata>value1</otherdata>" +
                        "</root>";
        final String TEST_XML =
                "<root>" +
                        "<data/>" +
                        "<otherdata>value2</otherdata>" +
                        "</root>";

        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(
                SourceGenerator.fromString(CONTROL_XML),
                SourceGenerator.fromString(TEST_XML)
        );
        Assert.assertEquals("Should contain 2 text value differences", 2, comparisonDetail.getTextValueDifferences().size());
        Assert.assertEquals("Should contain 0 node differences", 0, comparisonDetail.getNodeDifferences().size());
    }
}

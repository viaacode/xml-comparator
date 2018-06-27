package be.viaa.tools.xmldiff;

import be.viaa.tools.xmldiff.fileutils.FileReader;
import be.viaa.tools.xmldiff.model.ComparisonDetail;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlunit.diff.*;

import javax.xml.transform.*;
import java.io.File;

/**
 * Created by dieter on 26/06/2018.
 */
public class FragmentTest {
    final static Logger logger = Logger.getLogger(FragmentTest.class);
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
        File controlFile = fileReader.readFile("test-data/fragments/FragmentMetadataControl.xml");
        File testFile = fileReader.readFile("test-data/fragments/FragmentMetadataTest.xml");

        // Initialize the control and source
        control = SourceGenerator.fromFile(controlFile);
        test = SourceGenerator.fromFile(testFile);
    }

    @Test
    public void testIgnoreWhitespaces() {
        comparator = new DifferenceComparator(engine, true);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        Assert.assertEquals(comparisonDetail.getNodeDifferences().size(), 2);
        Assert.assertEquals(comparisonDetail.getTextValueDifferences().size(), 1);
    }

    @Test
    public void testIncludingWhitespaces() {
        comparator = new DifferenceComparator(engine, false);
        ComparisonDetail comparisonDetail = comparator.calculateDocumentDifferences(control, test);
        Assert.assertEquals(comparisonDetail.getNodeDifferences().size(), 4);
        Assert.assertEquals(comparisonDetail.getTextValueDifferences().size(), 2);
    }
}

package be.viaa.tools.xmldiff;

import be.viaa.tools.xmldiff.fileutils.FileReader;
import be.viaa.tools.xmldiff.generators.SourceGenerator;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;

import javax.xml.transform.Source;
import java.io.File;

/**
 * Created by VIAA on 02/07/2018.
 */
public class BaseTest {
    public DifferenceEngine engine;
    public FileReader fileReader;
    public DifferenceComparator comparator;
    public Source control;
    public Source test;

    public String controlFilePath;
    public String testFilePath;

    public void initialize(String controlFilePath, String testFilePath) {
        this.controlFilePath = controlFilePath;
        this.testFilePath = testFilePath;
        // Wire all the things!
        engine = new DOMDifferenceEngine();
        fileReader = new FileReader();

        // Read the files
        File controlFile = fileReader.readFile(controlFilePath);
        File testFile = fileReader.readFile(testFilePath);

        // Initialize the control and source
        control = SourceGenerator.fromFile(controlFile);
        test = SourceGenerator.fromFile(testFile);
    }
}

package be.viaa.tools.xmldiff.model.differences;

import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;

/**
 * Created by VIAA on 26/06/2018.
 */
public class TextValueDifference extends Difference {
    private String controlValue;
    private String testValue;
    private String nodeName;
    private String xPath;
    private Node controlNode;
    private Node testNode;

    public TextValueDifference(String controlValue, String testValue, String xPath, String nodeName, Node controlNode, Node testNode) {
        this.controlValue = controlValue;
        this.testValue = testValue;
        this.xPath = xPath;
        this.nodeName = nodeName;
        this.controlNode = controlNode;
        this.testNode = testNode;
    }

    public String getControlValue() {
        return controlValue;
    }

    public String getTestValue() {
        return testValue;
    }

    public String getxPath() {
        return xPath;
    }

    public String getNodeName() {
        return nodeName;
    }

    public Node getControlNode() {
        return controlNode;
    }

    public Node getTestNode() {
        return testNode;
    }

    @Override
    public String toString() {
        return String.format("Value at %s changed from '%s' to '%s'", getxPath(), getControlValue(), getTestValue());
    }

    public static class TextValueDifferenceBuilder {
        public static Difference fromComparison(Comparison comparison, boolean ignoreWhiteSpaces) {
            String controlValue = comparison.getControlDetails().getValue().toString().trim();
            String testValue = comparison.getTestDetails().getValue().toString().trim();
            if (ignoreWhiteSpaces && controlValue.isEmpty() && testValue.isEmpty()) return null;
            return new TextValueDifference(
                    comparison.getControlDetails().getValue().toString(),
                    comparison.getTestDetails().getValue().toString(),
                    comparison.getControlDetails().getXPath(),
                    comparison.getControlDetails().getTarget().getParentNode().getLocalName(),
                    comparison.getControlDetails().getTarget(),
                    comparison.getTestDetails().getTarget());
        }
    }
}

package be.viaa.tools.xmldiff.model.differences;

import org.xmlunit.diff.Comparison;

/**
 * Created by VIAA on 26/06/2018.
 */
public class TextValueDifference extends Difference {
    private String controlValue;
    private String testValue;
    private String xPath;

    public TextValueDifference(String controlValue, String testValue, String xPath) {
        this.controlValue = controlValue;
        this.testValue = testValue;
        this.xPath = xPath;
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

    @Override
    public String toString() {
        return String.format("Value at %s changed from '%s' to '%s'", xPath, controlValue, testValue);
    }

    public static class TextValueDifferenceBuilder {
        public static Difference fromComparison(Comparison comparison, boolean ignoreWhiteSpaces) {
            String controlValue = comparison.getControlDetails().getValue().toString().trim();
            String testValue = comparison.getTestDetails().getValue().toString().trim();
            if (ignoreWhiteSpaces && controlValue.isEmpty() && testValue.isEmpty()) return null;
            return new TextValueDifference(comparison.getControlDetails().getValue().toString(), comparison.getTestDetails().getValue().toString(), comparison.getControlDetails().getXPath());
        }
    }
}

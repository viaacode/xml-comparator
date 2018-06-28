package be.viaa.tools.xmldiff.model.differences;

/**
 * Created by dieter on 26/06/2018.
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

    @Override
    public String toString() {
        return String.format("Value at %s changed from '%s' to '%s'", xPath, controlValue, testValue);
    }
}

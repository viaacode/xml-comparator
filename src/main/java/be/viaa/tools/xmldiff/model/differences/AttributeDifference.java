package be.viaa.tools.xmldiff.model.differences;

import be.viaa.tools.xmldiff.model.OriginType;
import com.sun.org.apache.xerces.internal.dom.DeferredAttrNSImpl;
import com.sun.org.apache.xerces.internal.dom.DeferredTextImpl;
import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;

/**
 * Created by VIAA on 30/06/2018.
 */
public class AttributeDifference extends Difference {
    private DeferredAttrNSImpl controlValue;
    private DeferredAttrNSImpl testValue;
    private String xPath;

    public AttributeDifference(DeferredAttrNSImpl controlValue, DeferredAttrNSImpl testValue, OriginType origin, String xPath) {
        this.controlValue = controlValue;
        this.testValue = testValue;
        this.origin = origin;
        this.xPath = xPath;
    }

    public Node getControlValue() {
        return controlValue;
    }

    public Node getTestValue() {
        return testValue;
    }

    public static class AttributeDifferenceBuilder {
        public static Difference fromComparison(Comparison comparison, boolean ignoreWhiteSpaces) {
            boolean existsInControl = comparison.getControlDetails().getValue() != null;
            boolean existsInTest = comparison.getTestDetails().getValue() != null;
            Comparison.Detail currentNode = null;
            OriginType origin = null;
            // Check in which document the node exists, Control or Test
            if (existsInControl) {
                origin = OriginType.CONTROL;
            }
            if (existsInTest) {
                origin = OriginType.TEST;
            }

            String attributeName = (origin == OriginType.CONTROL ? comparison.getControlDetails().getValue() : comparison.getTestDetails().getValue()).toString();
            String xPath = (origin == OriginType.CONTROL ? comparison.getControlDetails().getXPath() : comparison.getTestDetails().getXPath());

            return new AttributeDifference(
                    (DeferredAttrNSImpl) comparison.getControlDetails().getTarget().getAttributes().getNamedItem(attributeName),
                    (DeferredAttrNSImpl) comparison.getTestDetails().getTarget().getAttributes().getNamedItem(attributeName),
                    origin,
                    xPath);
        }
    }
}

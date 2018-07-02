package be.viaa.tools.xmldiff.model.differences;

import be.viaa.tools.xmldiff.model.OriginType;
import com.sun.org.apache.xerces.internal.dom.DeferredTextImpl;
import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by VIAA on 26/06/2018.
 */
public class NodeDifference extends Difference {
    private Node controlValue;
    private Node testValue;

    public NodeDifference(Node controlValue, Node testValue, OriginType origin) {
        this.controlValue = controlValue;
        this.testValue = testValue;
        this.origin = origin;
    }

    public Node getControlValue() { return controlValue; }

    public Node getTestValue() {
        return testValue;
    }

    public OriginType getOrigin() {
        return origin;
    }

    public String toXMLString() {
        StringWriter writer = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.transform(new DOMSource(this.getOrigin() == OriginType.CONTROL ? this.getControlValue() : this.getTestValue()), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("New node in ").append(this.origin.toString()).append(":\n");
        sb.append("'").append(toXMLString()).append("'");
        sb.append("\n");
        return sb.toString();
    }

    public static class NodeDifferenceBuilder {
        public static Difference fromComparison(Comparison comparison, boolean ignoreWhiteSpaces) {
            boolean existsInControl = comparison.getControlDetails().getXPath() != null;
            boolean existsInTest = comparison.getTestDetails().getXPath() != null;
            Node currentNode = null;
            OriginType origin = null;
            // Check in which document the node exists, Control or Test
            if (existsInControl) {
                currentNode = comparison.getControlDetails().getTarget();
                origin = OriginType.CONTROL;
            }
            if (existsInTest) {
                currentNode = comparison.getTestDetails().getTarget();
                origin = OriginType.TEST;
            }
            // Ignore the node change if no node was found or if only the text changed from a bunch of whitespaces to another bunch of whitespaces
            if (currentNode == null || currentNode.getTextContent() == null || (ignoreWhiteSpaces && currentNode.getTextContent().trim().isEmpty()))
                return null;
            if (comparison.getControlDetails().getTarget() instanceof DeferredTextImpl)
                return new TextValueDifference(
                        origin == OriginType.CONTROL ? ((DeferredTextImpl) comparison.getControlDetails().getTarget()).getData() : null,
                        origin == OriginType.TEST ? ((DeferredTextImpl) comparison.getTestDetails().getTarget()).getData() : null,
                        origin == OriginType.CONTROL ? comparison.getControlDetails().getXPath() : comparison.getTestDetails().getXPath());
            return new NodeDifference(comparison.getControlDetails().getTarget(), comparison.getTestDetails().getTarget(), origin);
        }
    }
}

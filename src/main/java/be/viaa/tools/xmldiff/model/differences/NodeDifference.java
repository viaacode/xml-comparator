package be.viaa.tools.xmldiff.model.differences;

import be.viaa.tools.xmldiff.model.OriginType;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by dieter on 26/06/2018.
 */
public class NodeDifference {
    private Node node;
    private OriginType origin;

    public NodeDifference(Node node, OriginType origin) {
        this.node = node;
        this.origin = origin;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public OriginType getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("New node in ").append(this.origin.toString()).append(":\n");
        StringWriter writer = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            sb.append("'").append(writer.toString()).append("'");
            sb.append("\n");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

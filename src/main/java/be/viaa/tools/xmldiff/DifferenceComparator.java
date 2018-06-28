package be.viaa.tools.xmldiff;

import javax.xml.transform.Source;

import be.viaa.tools.xmldiff.model.ComparisonDetail;
import be.viaa.tools.xmldiff.model.OriginType;
import be.viaa.tools.xmldiff.model.differences.*;
import be.viaa.tools.xmldiff.model.differences.Difference;
import com.sun.org.apache.xerces.internal.dom.DeferredTextImpl;
import org.w3c.dom.Node;
import org.xmlunit.diff.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dieter on 26/06/2018.
 */
public class DifferenceComparator {
    private DifferenceEngine engine;
    private boolean ignoreWhiteSpaces = true;

    public DifferenceComparator(DifferenceEngine engine, boolean ignoreWhiteSpaces) {
        this.engine = engine;
        this.ignoreWhiteSpaces = ignoreWhiteSpaces;
    }

    public ComparisonDetail calculateDocumentDifferences(Source control, Source test) {
        final List<Comparison> differences = new ArrayList<Comparison>();
        // Add listener for all differences
        engine.addDifferenceListener((comparison, outcome) -> differences.add(comparison));
        // Compare the two documents
        engine.compare(control, test);

        // Create summary
        Map<ComparisonType, Integer> diffCounts = new HashMap<>();
        for (Comparison comp : differences) {
            diffCounts.merge(comp.getType(), 1, Integer::sum);
        }

        // Return an aggregate of all (relevant) differences
        List<Difference> allDifferences = extractAllDifferences(differences);
        return new ComparisonDetail(diffCounts, allDifferences);
    }

    private List<Difference> extractAllDifferences(List<Comparison> comparisons) {
        return comparisons.stream()
                .map(this::extractDifference)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Difference extractDifference(Comparison comparison) {
        switch (comparison.getType()) {
            case CHILD_LOOKUP:
                return transformNodeDifference(comparison);
            case TEXT_VALUE:
                return transformTextValueDifference(comparison);
//            case CHILD_NODELIST_LENGTH:
//                return transformNodeLengthDifference(comparison);
            default:
                return null;
        }
    }

    private Difference transformNodeDifference(Comparison comparison) {
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
        if (currentNode == null || currentNode.getTextContent() == null || (this.ignoreWhiteSpaces && currentNode.getTextContent().trim().isEmpty()))
            return null;
        if (comparison.getControlDetails().getTarget() instanceof DeferredTextImpl)
            return new TextValueDifference(
                    origin == OriginType.CONTROL ? ((DeferredTextImpl) comparison.getControlDetails().getTarget()).getData() : null,
                    origin == OriginType.TEST ? ((DeferredTextImpl) comparison.getTestDetails().getTarget()).getData() : null,
                    origin == OriginType.CONTROL ? comparison.getControlDetails().getXPath() : comparison.getTestDetails().getXPath());
        return new NodeDifference(comparison.getControlDetails().getTarget(), comparison.getTestDetails().getTarget(), origin);
    }

    private TextValueDifference transformTextValueDifference(Comparison comparison) {
        String controlValue = comparison.getControlDetails().getValue().toString().trim();
        String testValue = comparison.getTestDetails().getValue().toString().trim();
        if (this.ignoreWhiteSpaces && controlValue.isEmpty() && testValue.isEmpty()) return null;
        return new TextValueDifference(comparison.getControlDetails().getValue().toString(), comparison.getTestDetails().getValue().toString(), comparison.getControlDetails().getXPath());
    }

    private Difference transformNodeLengthDifference(Comparison comparison) {
        return null; //new NodeDifference(null, null, null);
    }
}

package be.viaa.tools.xmldiff;

import javax.xml.transform.Source;

import be.viaa.tools.xmldiff.model.ComparisonDetail;
import be.viaa.tools.xmldiff.model.OriginType;
import be.viaa.tools.xmldiff.model.differences.NodeDifference;
import be.viaa.tools.xmldiff.model.differences.TextValueDifference;
import org.w3c.dom.Node;
import org.xmlunit.diff.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return aggregateDifferences(
            calculateNodeDifferences(differences.stream().filter(d -> d.getType() == ComparisonType.CHILD_LOOKUP)),
            calculateTextValueDifferences(differences.stream().filter(d -> d.getType() == ComparisonType.TEXT_VALUE)),
            diffCounts
        );
    }

    private ComparisonDetail aggregateDifferences(
            List<NodeDifference> nd,
            List<TextValueDifference> tvd,
            Map<ComparisonType, Integer> summary) {
        return new ComparisonDetail(
                nd,
                tvd,
                summary
        );
    }

    public List<NodeDifference> calculateNodeDifferences(Stream<Comparison> comparisons) {
        return comparisons
                .map(this::findNodeDifference)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<TextValueDifference> calculateTextValueDifferences(Stream<Comparison> comparisons) {
        return comparisons
                .map(this::findTextValueDifference)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private NodeDifference findNodeDifference(Comparison comparison) {
        boolean existsInControl = comparison.getControlDetails().getXPath() != null;
        boolean existsInTest = comparison.getTestDetails().getXPath() != null;
        Node currentNode = null;
        OriginType origin = null;
        if (existsInControl) {
            currentNode = comparison.getControlDetails().getTarget();
            origin = OriginType.CONTROL;
        }
        if (existsInTest) {
            currentNode = comparison.getTestDetails().getTarget();
            origin = OriginType.TEST;
        }
        if (currentNode == null || currentNode.getTextContent() == null || (this.ignoreWhiteSpaces && currentNode.getTextContent().trim().isEmpty()))
            return null;
        return new NodeDifference(currentNode, origin);
    }

    private TextValueDifference findTextValueDifference(Comparison comparison) {
        String controlValue = comparison.getControlDetails().getValue().toString().trim();
        String testValue = comparison.getTestDetails().getValue().toString().trim();
        if (this.ignoreWhiteSpaces && controlValue.isEmpty() && testValue.isEmpty()) return null;
        return new TextValueDifference(comparison.getControlDetails().getValue().toString(), comparison.getTestDetails().getValue().toString(), comparison.getControlDetails().getXPath());
    }
}

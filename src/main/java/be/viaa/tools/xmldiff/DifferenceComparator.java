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
 * Created by VIAA on 26/06/2018.
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
                return NodeDifference.NodeDifferenceBuilder.fromComparison(comparison, this.ignoreWhiteSpaces);
            case TEXT_VALUE:
                return TextValueDifference.TextValueDifferenceBuilder.fromComparison(comparison, this.ignoreWhiteSpaces);
            default:
                return null;
        }
    }
}

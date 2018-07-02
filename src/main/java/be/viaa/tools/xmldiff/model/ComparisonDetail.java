package be.viaa.tools.xmldiff.model;

import be.viaa.tools.xmldiff.model.differences.AttributeDifference;
import be.viaa.tools.xmldiff.model.differences.Difference;
import be.viaa.tools.xmldiff.model.differences.NodeDifference;
import be.viaa.tools.xmldiff.model.differences.TextValueDifference;
import org.xmlunit.diff.ComparisonType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by VIAA on 26/06/2018.
 */
public class ComparisonDetail {
    private Map<ComparisonType, Integer> summary;
    private List<Difference> allDifferences;

    public ComparisonDetail(Map<ComparisonType, Integer> summary, List<Difference> allDifferences) {
        this.summary = summary;
        this.allDifferences = allDifferences;
    }

    public boolean areEqual() {
        return allDifferences.size() == 0;
    }

    public Map<ComparisonType, Integer> getSummary() {
        return summary;
    }

    public List<NodeDifference> getNodeDifferences() {
        return allDifferences.stream()
                .filter(d -> d instanceof NodeDifference)
                .map(d -> (NodeDifference) d)
                .collect(Collectors.toList());
    }

    public List<TextValueDifference> getTextValueDifferences() {
        return allDifferences.stream()
                .filter(d -> d instanceof TextValueDifference)
                .map(d -> (TextValueDifference) d)
                .collect(Collectors.toList());
    }

    public List<AttributeDifference> getAttributeDifferences() {
        return allDifferences.stream()
                .filter(d -> d instanceof AttributeDifference)
                .map(d -> (AttributeDifference) d)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\n==== Summary ====\n");
        for (Map.Entry entry : this.summary.entrySet()) {
            sb.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
        }
        sb.append("=================\n\n");
        sb.append("* Node differences *\n");
        for (NodeDifference nd : getNodeDifferences()) {
            sb.append("\t" + nd.toString());
        }
        sb.append("* Text value differences *\n");
        for (TextValueDifference tvd : getTextValueDifferences()) {
            sb.append("\t" + tvd.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}

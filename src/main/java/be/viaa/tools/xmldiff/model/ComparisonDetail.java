package be.viaa.tools.xmldiff.model;

import be.viaa.tools.xmldiff.model.differences.NodeDifference;
import be.viaa.tools.xmldiff.model.differences.TextValueDifference;
import org.xmlunit.diff.ComparisonType;
import java.util.List;
import java.util.Map;

/**
 * Created by dieter on 26/06/2018.
 */
public class ComparisonDetail {
    private Map<ComparisonType, Integer> summary;
    private List<NodeDifference> nodeDifferences;
    private List<TextValueDifference> textValueDifferences;

    public ComparisonDetail(List<NodeDifference> nodeDifferences, List<TextValueDifference> textValueDifferences, Map<ComparisonType, Integer> summary) {
        this.summary = summary;
        this.nodeDifferences = nodeDifferences;
        this.textValueDifferences = textValueDifferences;
    }

    public Map<ComparisonType, Integer> getSummary() {
        return summary;
    }

    public List<NodeDifference> getNodeDifferences() {
        return nodeDifferences;
    }

    public List<TextValueDifference> getTextValueDifferences() {
        return textValueDifferences;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\n==== Summary ====\n");
        for (Map.Entry entry : this.summary.entrySet()) {
            sb.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
        }
        sb.append("=================\n\n");
        sb.append("* Node differences *\n");
        for (NodeDifference nd : this.nodeDifferences) {
            sb.append(nd.toString());
        }
        sb.append("* Text value differences *\n");
        for (TextValueDifference tvd : this.textValueDifferences) {
            sb.append(tvd.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}

import java.util.List;

public record Classification(double score, int wordCount, List<String> susWords) {
    private String getText() {
        if(score > 0.6) {
            return "Definitely not spam";
        } else if(score > 0.5) {
            return "Probably not spam";
        } else if(score > 0.4) {
            return "Neutral";
        } else if(score > 0.3) {
            return "Possibly spam";
        } else if(score > 0.2) {
            return "Probably spam";
        } else {
            return "Definitely spam";
        }
    }

    private String sussyWords() {
        StringBuilder builder = new StringBuilder("<ul>");
        for(String s : susWords) {
            builder.append("<li>").append(s).append("</li>");
        }
        builder.append("</ul>");
        return builder.toString();
    }

    public String getHtml() {
        return String.format("""
                <h3>%s</h3>
                <h5>Score: %f</h5>
                <h5>Most suspicious words:</h5>
                %s
                """, getText(), score, sussyWords());
    }
}

public record Email(String body, boolean isSpam) {
    public static Email fromString(String source) {
        var data = source.split(",");
        var isSpam = data[1].equals("1");
        return new Email(data[0], isSpam);
    }
}
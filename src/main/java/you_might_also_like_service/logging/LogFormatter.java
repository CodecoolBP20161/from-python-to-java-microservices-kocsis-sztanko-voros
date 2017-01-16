package you_might_also_like_service.logging;

/**
 * Created by dorasztanko on 2017.01.15..
 */
public enum LogFormatter {
    FORMAT(">>>>> ");
    private final String customizedFormatter;

    LogFormatter(String formatter) {
        this.customizedFormatter = formatter;
    }
    public String getCustomizedFormatter() {
        return this.customizedFormatter;
    }
}


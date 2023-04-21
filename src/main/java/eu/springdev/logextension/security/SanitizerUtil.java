package eu.springdev.logextension.security;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class SanitizerUtil {

    private SanitizerUtil() {
        // do not instantiate the utility class
    }

    public static String toSafeString(String unsafe) {
        if (StringUtils.isBlank(unsafe)) {
            return unsafe;
        }

        return Jsoup.clean(unsafe, Safelist.none());
    }

    public static String toSafeToString(Object unsafe) {
        if (unsafe == null) {
            return "Null";
        }
        return toSafeString(unsafe.toString());
    }

    public static String toSafeStringWithoutLineBreaks(String unsafe) {
        if (StringUtils.isBlank(unsafe)) {
            return unsafe;
        }

        return Jsoup.clean(unsafe, Safelist.none())
                .replace('\n', '_')
                .replace('\r', '_')
                .replace('\t', '_');
    }
}

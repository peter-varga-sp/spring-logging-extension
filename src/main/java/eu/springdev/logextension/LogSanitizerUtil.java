package eu.springdev.logextension;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.codec.EncodingException;

public class LogSanitizerUtil {

	private static final String[] ELEMENTS_TO_BE_REPLACED = new String[] { "\n", "\r", "\t", "<", ">", "&", "\"", "'" };
	private static final String[] REPLACEMENT = new String[] { "_", "_", "_", "_", "_", "_", "_", "_" };

	public static String clean(String message) throws EncodingException {
		if (StringUtils.isBlank(message)) {
			return message;
		}

		return StringUtils.replaceEach(message, ELEMENTS_TO_BE_REPLACED, REPLACEMENT);
	}
}

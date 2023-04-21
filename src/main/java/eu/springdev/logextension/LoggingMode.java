package eu.springdev.logextension;

public enum LoggingMode {

	DEFAULT(true, false, false),
	ONLY_AT_RETURN(false, true),
	ONLY_AT_RETURN_SAFE(false, true, true);

	private final boolean logAtMethodEntering;
	private final boolean logArgumentsAtReturn;
	private final boolean skipLogInjectionCheck;

	private LoggingMode(boolean logAtMethodEntering, boolean logArgumentsAtReturn, boolean skipLogInjectionCheck) {
		this.logAtMethodEntering = logAtMethodEntering;
		this.logArgumentsAtReturn = logArgumentsAtReturn;
		this.skipLogInjectionCheck = skipLogInjectionCheck;
	}

	private LoggingMode(boolean skipLoggingAtEntering, boolean skipArgumentsAtReturn) {
		this(skipLoggingAtEntering, skipArgumentsAtReturn, false);
	}

	public boolean shouldLogAtMethodEntering() {
		return logAtMethodEntering;
	}

	public boolean shouldLogArgumentsAtReturn() {
		return logArgumentsAtReturn;
	}

	public boolean isSkipLogInjectionCheck() {
		return skipLogInjectionCheck;
	}

}

package eu.springdev.logextension;

public enum LoggingMode {

	DEFAULT(false, true, false),
	ONLY_AT_RETURN(true, false),
	ONLY_AT_RETURN_SAFE(true, false, true);

	private final boolean skipLoggingAtEntering;
	private final boolean skipArgumentsAtReturn;
	private final boolean skipLogInjectionCheck;

	private LoggingMode(boolean skipLoggingAtEntering, boolean skipArgumentsAtReturn, boolean skipLogInjectionCheck) {
		this.skipLoggingAtEntering = skipLoggingAtEntering;
		this.skipArgumentsAtReturn = skipArgumentsAtReturn;
		this.skipLogInjectionCheck = skipLogInjectionCheck;
	}

	private LoggingMode(boolean skipLoggingAtEntering, boolean skipArgumentsAtReturn) {
		this(skipLoggingAtEntering, skipArgumentsAtReturn, false);
	}

	public boolean isSkipLoggingAtEntering() {
		return skipLoggingAtEntering;
	}

	public boolean isSkipArgumentsAtReturn() {
		return skipArgumentsAtReturn;
	}

	public boolean isSkipLogInjectionCheck() {
		return skipLogInjectionCheck;
	}

}

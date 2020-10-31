package eu.springdev.logextension;

import org.springframework.boot.logging.LogLevel;

public class LoggableWrapper {

	private final Loggable loggable;
	private final LoggingMode loggingMode;

	public LoggableWrapper(Loggable loggable) {
		this.loggable = loggable;
		this.loggingMode = loggable.value().equals(LoggingMode.DEFAULT) ? loggable.loggingMode() : loggable.value();
	}

	public boolean shouldLogExecutionTime() {
		return loggable.withExecutionTime();
	}

	private boolean loggingModeDefined() {
		return !this.loggingMode.equals(LoggingMode.DEFAULT);
	}

	public boolean shouldSkipLoggingAtEntering() {
		if (LogLevel.OFF.equals(getLogLevel())) {
			return true;
		}

		return loggingModeDefined() ? this.loggingMode.isSkipLoggingAtEntering() : loggable.skipLoggingAtEntering();
	}

	public boolean shouldSkipArgumentsAtEntering() {
		return loggable.skipArgumentsAtEntering();
	}

	public boolean shouldSkipLoggingAtReturn() {
		return loggable.skipLoggingAtReturn();
	}

	public boolean shouldSkipArgumentsAtReturn() {
		if (LogLevel.OFF.equals(getLogLevel())) {
			return true;
		}

		return loggingModeDefined() ? this.loggingMode.isSkipArgumentsAtReturn() : loggable.skipArgumentsAtReturn();
	}

	public boolean shouldSkipResultAtReturn() {
		return loggable.skipResultAtReturn();
	}

	public boolean shouldLogArgumentCollectionSize() {
		return loggable.logArgumentCollectionSize();
	}

	public boolean shouldLogResultCollectionSize() {
		return loggable.logResultCollectionSize();
	}

	public LogLevel getLogLevel() {
		return loggable.logLevel();
	}

	public LogLevel getLogLevelForException() {
		return loggable.logLevelForException();
	}

	public boolean shouldSkipLogInjectionCheck() {
		return loggingModeDefined() ? this.loggingMode.isSkipLogInjectionCheck() : loggable.skipLogInjectionCheck();
	}

	public int getMaxLengthOfResultObject() {
		return loggable.maxLengthOfResultObject();
	}

}

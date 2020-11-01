package eu.springdev.logextension;

import org.springframework.boot.logging.LogLevel;

class LoggableWrapper {

	private final Loggable loggable;
	private final LoggingMode loggingMode;

	LoggableWrapper(Loggable loggable) {
		this.loggable = loggable;
		this.loggingMode = loggable.value().equals(LoggingMode.DEFAULT) ? loggable.loggingMode() : loggable.value();
	}

	boolean shouldLogExecutionTime() {
		return loggable.withExecutionTime();
	}

	private boolean loggingModeDefined() {
		return !this.loggingMode.equals(LoggingMode.DEFAULT);
	}

	boolean shouldLogAtEntering() {
		if (LogLevel.OFF.equals(getLogLevel())) {
			return false;
		}

		return loggingModeDefined() ? this.loggingMode.shouldLogAtMethodEntering() : loggable.logAtMethodEntering();
	}

	boolean shouldLogArgumentsAtEntering() {
		return loggable.logArgumentsAtEntering();
	}

	boolean shouldLogAtReturn() {
		return loggable.logAtMethodReturn();
	}

	boolean shouldLogArgumentsAtReturn() {
		if (LogLevel.OFF.equals(getLogLevel())) {
			return false;
		}

		return loggingModeDefined() ? this.loggingMode.shouldLogArgumentsAtReturn() : loggable.logArgumentsAtReturn();
	}

	boolean shouldLogResultAtReturn() {
		return loggable.logResultAtReturn();
	}

	boolean shouldLogArgumentCollectionSize() {
		return loggable.logArgumentCollectionSize();
	}

	boolean shouldLogResultCollectionSize() {
		return loggable.logResultCollectionSize();
	}

	LogLevel getLogLevel() {
		return loggable.logLevel();
	}

	LogLevel getLogLevelForException() {
		return loggable.logLevelForException();
	}

	boolean shouldSkipLogInjectionCheck() {
		return loggingModeDefined() ? this.loggingMode.isSkipLogInjectionCheck() : loggable.skipLogInjectionCheck();
	}

	int getMaxLengthOfResultObject() {
		return loggable.maxLengthOfResultObject();
	}

}

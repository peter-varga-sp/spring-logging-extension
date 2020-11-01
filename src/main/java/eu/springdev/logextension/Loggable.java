package eu.springdev.logextension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.logging.LogLevel;
import org.springframework.core.annotation.AliasFor;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
	boolean withExecutionTime() default true;

	boolean logAtMethodEntering() default true;

	boolean logArgumentsAtEntering() default true;

	boolean logAtMethodReturn() default true;

	boolean logArgumentsAtReturn() default false;

	boolean logResultAtReturn() default true;

	/**
	 * If true, logs the size of the Collection parameters, instead of it's content.
	 */
	boolean logArgumentCollectionSize() default true;

	boolean logResultCollectionSize() default true;

	LogLevel logLevel() default LogLevel.INFO;

	LogLevel logLevelForException() default LogLevel.WARN;

	/**
	 * Does not checks log message for log injection attack (a.k.a. forging). Default 'false', also the parameters will be checked before their values
	 * get logged.
	 */
	boolean skipLogInjectionCheck() default false;

	@AliasFor("loggingMode")
	LoggingMode value() default LoggingMode.DEFAULT;

	@AliasFor("value")
	LoggingMode loggingMode() default LoggingMode.DEFAULT;
	
	int maxLengthOfResultObject() default 0; 

}

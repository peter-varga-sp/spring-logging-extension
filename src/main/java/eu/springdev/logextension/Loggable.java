package eu.springdev.logextension;

import org.springframework.boot.logging.LogLevel;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    // specifies if execution time should be included in the log message
    boolean withExecutionTime() default true;

    boolean logAtMethodEntering() default true;

    boolean logArgumentsAtEntering() default true;

    boolean logAtMethodReturn() default true;

    boolean logArgumentsAtReturn() default false;

    boolean logResultAtReturn() default true;

    /**
     * If true, logs the size of the Collection parameters, instead of it's content. Default value is true
     */
    boolean logArgumentCollectionSize() default true;

    /**
     * If true, logs the size of the Collection return value, instead of it's content. Default value is true
     */
    boolean logResultCollectionSize() default true;

    LogLevel logLevel() default LogLevel.INFO;

    LogLevel logLevelForException() default LogLevel.WARN;

    /**
     * Does not check log message for log injection attack (a.k.a. forging). Default 'false', also the parameters will be checked before their values
     * get logged.
     */
    boolean skipLogInjectionCheck() default false;

    @AliasFor("loggingMode")
    LoggingMode value() default LoggingMode.DEFAULT;

    @AliasFor("value")
    LoggingMode loggingMode() default LoggingMode.DEFAULT;

    int maxLengthOfResultObject() default 0;

}

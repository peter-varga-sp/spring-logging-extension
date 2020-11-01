package eu.springdev.logextension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {

	private final AopHelperService aopHelperService;

	private final MethodParameterExtractor methodParameterExtractor = new MethodParameterExtractor();

	@Autowired
	public LoggerAspect(AopHelperService aopHelperService) {
		this.aopHelperService = aopHelperService;
	}

	@Pointcut(value = "@annotation(at.h3g.common.logextension.NotLoggable)")
	public void annotatedAsNotLoggable() {
		// defines pointcut
	}

	@Pointcut(value = "@annotation(at.h3g.common.logextension.Loggable)")
	public void methodAnnotatedAsLoggable() {
		// defines pointcut
	}

	@Pointcut("@within(Loggable)") // this should work for the annotation service pointcut
	public void inLoggableService() {
		// defines pointcut
	}

	@Pointcut("inLoggableService() &&! methodAnnotatedAsLoggable()")
	public void anyPublicMethodOfLoggableService() {
		// defines pointcut
	}

	@Around("!annotatedAsNotLoggable() && anyPublicMethodOfLoggableService()")
	public Object aroundPublicMethodOfLoggableClass(ProceedingJoinPoint joinPoint) throws Throwable {
		return logMethodExecution(joinPoint);
	}

	@Around("methodAnnotatedAsLoggable()")
	public Object aroundLoggableMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		return logMethodExecution(joinPoint);
	}

	private Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		LoggableWrapper loggable = new LoggableWrapper(aopHelperService.getLoggableAnnotation(joinPoint));

		Logger loggerOfDeclaringClass = getLoggerFor(joinPoint);
		String classAndMethodSignature = aopHelperService.getClassAndMethodSignature(joinPoint);

		logMethodEntering(joinPoint, loggable, loggerOfDeclaringClass, classAndMethodSignature);

		long methodExecutionStartTime = System.currentTimeMillis();
		try {
			Object returnValue = joinPoint.proceed();

			// calculate elapsed time as soon as possible
			long elapsedTimeInMs = System.currentTimeMillis() - methodExecutionStartTime;
			logMethodReturn(joinPoint, loggable, loggerOfDeclaringClass, classAndMethodSignature, elapsedTimeInMs, returnValue);
			return returnValue;

		} catch (Throwable exception) { // NOPMD
			logException(joinPoint, loggable, loggerOfDeclaringClass, classAndMethodSignature, exception);
			throw exception;
		}
	}

	private void logException(ProceedingJoinPoint joinPoint, LoggableWrapper loggable, Logger loggerOfDeclaringClass,
			String classAndMethodSignature, Throwable e) {
		String message = "{} occurred in {} with parameters {} exception message: {}";
		String methodParameters = methodParameterExtractor.getMethodParameters(joinPoint, loggable);
		Object[] messageArguments = { e.getClass().getName(), classAndMethodSignature, methodParameters,
				e.getMessage() };

		doLog(loggerOfDeclaringClass, loggable.getLogLevelForException(), message, messageArguments);
	}

	private void logMethodEntering(ProceedingJoinPoint joinPoint, LoggableWrapper loggable, Logger loggerOfDeclaringClass,
			String classAndMethodSignature) {

		if (!loggable.shouldLogAtEntering()) {
			return;
		}

		if (loggable.shouldLogArgumentsAtEntering() && joinPoint.getArgs().length > 0) {
			String parameters = methodParameterExtractor.getMethodParameters(joinPoint, loggable);
			doLog(loggerOfDeclaringClass, loggable.getLogLevel(), "Entering {} parameters: {}", classAndMethodSignature, parameters);
		} else {
			doLog(loggerOfDeclaringClass, loggable.getLogLevel(), "Entering {}", classAndMethodSignature);
		}

	}

	private Logger getLoggerFor(JoinPoint joinPoint) {
		return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
	}

	@SuppressWarnings("rawtypes")
	private void logMethodReturn(ProceedingJoinPoint joinPoint, LoggableWrapper loggable, Logger loggerOfDeclaringClass,
			String classAndMethodSignature, long elapsedTimeInMs, Object returnValue) {

		if (!loggable.shouldLogAtReturn()) {
			return;
		}

		String message = "Finished {}";
		List<Object> argumentsList = new ArrayList<>(4);
		argumentsList.add(classAndMethodSignature);
		
		if (joinPoint.getArgs().length > 0 && loggable.shouldLogArgumentsAtReturn()) {
			message = message + " parameters: {}";
			argumentsList.add(methodParameterExtractor.getMethodParameters(joinPoint, loggable));
		}

		if (loggable.shouldLogExecutionTime()) {
			message = message + " in {} ms";
			argumentsList.add(elapsedTimeInMs);
		}

		if (loggable.shouldLogResultAtReturn()) {
			if (logCollectionSize(loggable, returnValue)) {
				message = message + " with result " + returnValue.getClass().getSimpleName() + " size: {}";
				argumentsList.add(((Collection) returnValue).size());
			} else {
				message = message + " with result: {}";
				argumentsList.add(getResultAsString(returnValue, loggable));
			}
		}

		doLog(loggerOfDeclaringClass, loggable.getLogLevel(), message, argumentsList.toArray());
	}

	private boolean logCollectionSize(LoggableWrapper loggable, Object returnValue) {
		return returnValue != null && loggable.shouldLogResultCollectionSize() && returnValue instanceof Collection;
	}

	private Object getResultAsString(Object returnValue, LoggableWrapper loggable) {
		if (returnValue != null && loggable.getMaxLengthOfResultObject() > 0) {
			return StringUtils.left(returnValue.toString(), loggable.getMaxLengthOfResultObject());
		}

		return returnValue;
	}

	private void doLog(Logger loggerOfDeclaringClass, LogLevel logLevel, String message, Object... arguments) {
		if (logLevel.equals(LogLevel.INFO)) {
			loggerOfDeclaringClass.info(message, arguments);

		} else if (logLevel.equals(LogLevel.WARN)) {
			loggerOfDeclaringClass.warn(message, arguments);

		} else if (logLevel.equals(LogLevel.ERROR)) {
			loggerOfDeclaringClass.error(message, arguments);

		} else if (logLevel.equals(LogLevel.DEBUG)) {
			loggerOfDeclaringClass.debug(message, arguments);

		} else {
			loggerOfDeclaringClass.info(message, arguments);
		}
	}

}

package eu.springdev.logextension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

public class MethodParameterExtractor {

	String getMethodParameters(ProceedingJoinPoint joinPoint, LoggableWrapper loggable) {
		if (joinPoint.getArgs().length == 0) {
			return "";
		}

		return getArgumentValuesAsString(joinPoint, loggable);
	}

	private static String getArgumentValuesAsString(ProceedingJoinPoint joinPoint, LoggableWrapper loggable) {
		if (joinPoint.getArgs().length == 0) {
			return "";
		}

		Object[] methodArguments = joinPoint.getArgs();
		
		if (loggable.shouldLogArgumentCollectionSize()) {
			return getArgumentValuesWithCollectionSize(loggable, methodArguments);
		} else {
			String parametersAsString = StringUtils.join(methodArguments, ",");
			return sanitize(parametersAsString, loggable.shouldSkipLogInjectionCheck());
		}
	}

	@SuppressWarnings("rawtypes")
	private static String getArgumentValuesWithCollectionSize(LoggableWrapper loggable, Object[] methodArguments) {
		List<String> parameters = new ArrayList<>();

		for (int i = 0; i < methodArguments.length; i++) {
			Object argument = methodArguments[i];
			if (argument != null && argument instanceof Collection) {
				parameters.add(argument.getClass().getSimpleName() + " size:" + ((Collection) argument).size());
			} else {
				parameters.add(sanitize(argument, loggable.shouldSkipLogInjectionCheck()));
			}
		}

		return StringUtils.join(parameters, ", ");
	}

	private static String sanitize(Object parameter, boolean skipInjectionCheck) {
		if (parameter == null) {
			return null;
		}

		String parameterAsString = parameter.toString();
		if (StringUtils.isBlank(parameterAsString)) {
			return "";
		}

		if (skipInjectionCheck) {
			return parameterAsString;
		}

		return parameterAsString.replace('\n', '_').replace('\r', '_').replace('\t', '_');
	}

}

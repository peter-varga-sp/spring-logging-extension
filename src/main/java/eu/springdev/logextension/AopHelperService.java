package eu.springdev.logextension;

import java.lang.reflect.Method;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Service;

@Service
public class AopHelperService {

	private static final Logger logger = LoggerFactory.getLogger(AopHelperService.class);

	private final LRUMap<String, String> signatureCache = new LRUMap<>(1000);
	private final LRUMap<String, Loggable> methodAnnotationCache = new LRUMap<>(1000);

	public String getClassAndMethodSignature(JoinPoint joinPoint) {
		String fullMethodSignature = joinPoint.getSignature().toString();

		String result = signatureCache.get(fullMethodSignature);
		if (result != null) {
			return result;
		}

		int startIndexOfClassName = StringUtils.lastIndexOf(fullMethodSignature, ".") + 1;
		result = StringUtils.substring(fullMethodSignature, startIndexOfClassName);
		signatureCache.put(fullMethodSignature, result);

		logger.info("New cache element for {}", fullMethodSignature);
		return result;
	}

	public Loggable getLoggableAnnotation(JoinPoint joinPoint) {
		String fullMethodSignature = joinPoint.getSignature().toString();
		Loggable result = methodAnnotationCache.get(fullMethodSignature);

		if (result != null) {
			return result;
		}

		Loggable foundAnnotation = findAnnotationFor(joinPoint);
		methodAnnotationCache.put(fullMethodSignature, foundAnnotation);
		
		logger.info("New cache element for {}", fullMethodSignature);
		return foundAnnotation;
	}

	private Loggable findAnnotationFor(JoinPoint joinPoint) {
		Loggable foundAnnotation = null;

		if (joinPoint instanceof MethodInvocationProceedingJoinPoint) {
			foundAnnotation = getLoggableAnnotationForAnnotatedMethod(joinPoint);
		}

		if (foundAnnotation == null) {
			foundAnnotation = getLoggableAnnotationForAnnotatedClass(joinPoint);
		}

		return foundAnnotation;
	}

	@SuppressWarnings("unchecked")
	private static Loggable getLoggableAnnotationForAnnotatedClass(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		return (Loggable) signature.getDeclaringType().getAnnotation(Loggable.class);
	}

	private static Loggable getLoggableAnnotationForAnnotatedMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.getAnnotation(Loggable.class);
	}

}

package eu.springdev.logextension.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class JoinPointToAnnotationCache<A extends Annotation> extends AbstractJoinPointCache<A> {

    private static final Logger log = LoggerFactory.getLogger(JoinPointToAnnotationCache.class);

    private final Class<A> annotationClass;
    private final boolean checkClassForAnnotation;

    public JoinPointToAnnotationCache(Class<A> annotationClass, int maxSize) {
        this(annotationClass, false, maxSize);
    }

    public JoinPointToAnnotationCache(Class<A> annotationClass, boolean checkClassForAnnotation, int maxSize) {
        super(maxSize);
        this.annotationClass = annotationClass;
        this.checkClassForAnnotation = checkClassForAnnotation;
    }

    @Override
    protected A computeValue(JoinPoint joinPoint, String s) {
        return findAnnotationFor(joinPoint);
    }

    @Override
    protected void doIfComputed(JoinPoint joinPoint, String computedKey, A computedValue) {
        log.debug("New {} annotation cache element for {}", annotationClass.getSimpleName(), computedKey);
    }

    private A findAnnotationFor(JoinPoint joinPoint) {
        A foundAnnotation = null;

        if (joinPoint instanceof MethodInvocationProceedingJoinPoint) {
            foundAnnotation = findAnnotationOnMethod(joinPoint);
        }

        if (foundAnnotation == null && checkClassForAnnotation) {
            foundAnnotation = findAnnotationOnClass(joinPoint);
        }

        return foundAnnotation;
    }

    @SuppressWarnings("unchecked")
    private A findAnnotationOnClass(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        return (A) signature.getDeclaringType().getAnnotation(annotationClass);
    }

    private A findAnnotationOnMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(annotationClass);
    }
}

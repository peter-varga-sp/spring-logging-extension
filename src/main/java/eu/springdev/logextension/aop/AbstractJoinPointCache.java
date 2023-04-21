package eu.springdev.logextension.aop;

import org.aspectj.lang.JoinPoint;

public abstract class AbstractJoinPointCache<VALUE> extends AbstractMappingCache<JoinPoint, String, VALUE> {

    protected AbstractJoinPointCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected String computeKey(JoinPoint joinPoint) {
        return joinPoint.getSignature().toString();
    }
}

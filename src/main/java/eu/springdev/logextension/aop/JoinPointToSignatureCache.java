package eu.springdev.logextension.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinPointToSignatureCache extends AbstractJoinPointCache<String> {

    private static final Logger log = LoggerFactory.getLogger(JoinPointToSignatureCache.class);

    public JoinPointToSignatureCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected String computeValue(JoinPoint joinPoint, String computedKey) {
        if (StringUtils.isBlank(computedKey)) {
            return StringUtils.EMPTY;
        }

        String signature = computedKey;

        String[] splitClassAndMethodSignature = computedKey.split("\\.");
        if (splitClassAndMethodSignature.length >= 2) {
            signature = splitClassAndMethodSignature[splitClassAndMethodSignature.length - 2] + "." +
                    splitClassAndMethodSignature[splitClassAndMethodSignature.length - 1];
        }

        return signature;
    }

    @Override
    protected void doIfComputed(JoinPoint joinPoint, String computedKey, String computedValue) {
        log.debug("New cache element for method signature: {}", computedKey);
    }
}

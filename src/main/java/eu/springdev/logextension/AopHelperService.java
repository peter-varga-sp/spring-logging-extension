package eu.springdev.logextension;

import eu.springdev.logextension.aop.JoinPointToSignatureCache;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

@Service
public class AopHelperService {

    private final JoinPointToSignatureCache signatureCache = new JoinPointToSignatureCache(1000);

    public String getClassAndMethodSignature(JoinPoint joinPoint) {
        return signatureCache.get(joinPoint);
    }
}

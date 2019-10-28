package com.navercorp.pinpoint.plugin.jdk.http.interceptor;

import com.navercorp.pinpoint.bootstrap.context.Header;
import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;

import java.net.HttpURLConnection;

/**
 * An Implementation of Pinpoint interceptor that traces {@link HttpURLConnection#connect()} and injects pinpoint
 * headers to propagate trace context to a remote node via HTTP header.
 * <p>
 * <ol>
 *     <li>Initialize member fields</li>
 *     <ul>
 *         <li>{@link TraceContext}</li>
 *         <li>{@link MethodDescriptor}</li>
 *     </ul>
 *     <li>Implement {@link AroundInterceptor#before(Object, Object[])}</li>
 *     <ul>
 *         <li>Retrieve raw trace object</li>
 *         <li>Check sampling</li>
 *         <li>If false, set and add {@link Header#HTTP_SAMPLED} to {@code false} and return</li>
 *         <li>Begin trace block and retrieve {@code SpanEventRecorder}</li>
 *         <li>Obtain next trace id</li>
 *         <li>Record service type, and next span id</li>
 *         <li>Add Pinpoint headers</li>
 *
 *     </ul>
 *     <li>Implement {@link AroundInterceptor#after(Object, Object[], Object, Throwable)}</li>
 *     <ul>
 *         <li>Retrieve trace object</li>
 *         <li>Record API, exception</li>
 *         <li>Record HTTP client related data (destination id, http url)</li>
 *         <li>End trace block</li>
 *     </ul>
 * </ol>
 *
 * @see Header
 */
public class HttpURLConnectionConnectInterceptor implements AroundInterceptor {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }
        // Retrieve current raw trace object
        // Check sampling
        // If not sampling, add Pinpoint HTTP_SAMPLED header and set it to false, then return
        // Begin trace block and retrieve span event recorder
        // Record service type
        // Obtain next trace id and record next span id
        // Add Pinpoint headers
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args);
        }
        // Retrieve current trace object
        // Retrieve current span event recorder
        // Record API and exception
        // Record HTTP client related data (destination id, http url)
        // End trace block
    }
}

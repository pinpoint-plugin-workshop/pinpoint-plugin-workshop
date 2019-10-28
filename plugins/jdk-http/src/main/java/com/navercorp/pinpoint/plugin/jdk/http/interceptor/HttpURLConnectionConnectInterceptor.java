package com.navercorp.pinpoint.plugin.jdk.http.interceptor;

import com.navercorp.pinpoint.bootstrap.context.Header;
import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.context.TraceId;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.bootstrap.sampler.SamplingFlagUtils;
import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.plugin.jdk.http.JdkHttpConstants;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An Implementation of Pinpoint interceptor that traces {@link HttpURLConnection#connect()} and injects pinpoint
 * headers to propagate trace context to a remote node via HTTP header.
 * <p>
 * <ol>
 *     <li>Initialize member fields</li>
 *     <ul>
 *         <li>{@link PLogger Plugin Logger}</li>
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

    private final TraceContext traceContext;
    private final MethodDescriptor methodDescriptor;

    public HttpURLConnectionConnectInterceptor(TraceContext traceContext, MethodDescriptor methodDescriptor) {
        this.traceContext = traceContext;
        this.methodDescriptor = methodDescriptor;
    }

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }
        // Retrieve current raw trace object
        Trace trace = traceContext.currentRawTraceObject();
        if (trace == null) {
            return;
        }

        // Check sampling
        final boolean sampling = trace.canSampled();
        // If not sampling, add Pinpoint HTTP_SAMPLED header and set it to false, then return
        final HttpURLConnection request = (HttpURLConnection) target;
        if (!sampling) {
            if (request != null) {
                request.setRequestProperty(Header.HTTP_SAMPLED.toString(), SamplingFlagUtils.SAMPLING_RATE_FALSE);
            }
            return;
        }

        // Begin trace block and retrieve span event recorder
        final SpanEventRecorder recorder = trace.traceBlockBegin();

        // Record service type
        recorder.recordServiceType(JdkHttpConstants.SERVICE_TYPE);

        // Obtain next trace id and record next span id
        final TraceId nextId = trace.getTraceId().getNextTraceId();
        recorder.recordNextSpanId(nextId.getSpanId());

        // Add Pinpoint headers
        request.setRequestProperty(Header.HTTP_TRACE_ID.toString(), nextId.getTransactionId());
        request.setRequestProperty(Header.HTTP_PARENT_SPAN_ID.toString(), String.valueOf(nextId.getParentSpanId()));
        request.setRequestProperty(Header.HTTP_SPAN_ID.toString(), String.valueOf(nextId.getSpanId()));
        request.setRequestProperty(Header.HTTP_FLAGS.toString(), String.valueOf(nextId.getFlags()));

        request.setRequestProperty(Header.HTTP_PARENT_APPLICATION_NAME.toString(), traceContext.getApplicationName());
        request.setRequestProperty(Header.HTTP_PARENT_APPLICATION_TYPE.toString(), String.valueOf(traceContext.getServerTypeCode()));

        final String httpHost = getHttpHost(request.getURL());
        request.setRequestProperty(Header.HTTP_HOST.toString(), httpHost);
    }

    private String getHttpHost(URL url) {
        final String host = url.getHost();
        final int port = url.getPort();
        return host + ":" + port;
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args);
        }

        // Retrieve current trace object
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            // Retrieve current span event recorder
            final SpanEventRecorder recorder = trace.currentSpanEventRecorder();

            // Record API and exception
            recorder.recordApi(methodDescriptor);
            recorder.recordException(throwable);

            // Record HTTP client related data (destination id, http url)
            final HttpURLConnection request = (HttpURLConnection) target;
            final URL httpUrl = request.getURL();
            final String httpHost = getHttpHost(httpUrl);
            recorder.recordDestinationId(httpHost);
            recorder.recordAttribute(AnnotationKey.HTTP_URL, httpUrl.toString());
        } finally {
            // End trace block
            trace.traceBlockEnd();
        }
    }
}

/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.plugin.jdk.http;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplate;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplateAware;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;
import com.navercorp.pinpoint.plugin.jdk.http.interceptor.HttpURLConnectionConnectInterceptor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Implementation of {@code ProfilerPlugin} to allow bytecode instrumentation of JDK's {@code HttpURLConnection}.
 * <p>
 * The main purpose of this class is to define {@link TransformCallback} for JDK's
 * {@link sun.net.www.protocol.http.HttpURLConnection HttpURLConnection}, so that we can inject pinpoint interceptors
 * and other relevant code into the {@code HttpURLConnection} class.
 * <p>
 * Once defined, the {@code TransformCallback} is wrapped into a {@link ClassFileTransformer}, and is added to the
 * JVM's instrumentation pool via {@link Instrumentation#addTransformer(ClassFileTransformer)} by calling
 * {@link TransformTemplate#transform(String, Class)} during agent start-up.
 * <p>
 * When the application starts and loads {@code HttpURLConnection}, the JVM will look for the
 * {@code ClassFileTransformer} registered for {@code HttpURLConnection}, ultimately invoking the
 * {@code TransformCallback} defined in this class.
 */
public class JdkHttpPlugin implements ProfilerPlugin, TransformTemplateAware {

    private TransformTemplate transformTemplate;

    @Override
    public void setup(ProfilerPluginSetupContext context) {
        // TODO implement
        transformTemplate.transform("sun.net.www.protocol.http.HttpURLConnection", HttpURLConnectionTransformCallback.class);
    }

    public static class HttpURLConnectionTransformCallback implements TransformCallback {
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            InstrumentClass instrumentClass = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);

            InstrumentMethod connectMethod = instrumentClass.getDeclaredMethod("connect");
            connectMethod.addInterceptor(HttpURLConnectionConnectInterceptor.class);

            return instrumentClass.toBytecode();
        }
    }

    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
}

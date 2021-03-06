/*
 * Copyright 2019 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.collector.config;

import com.navercorp.pinpoint.common.util.Assert;
import com.navercorp.pinpoint.grpc.server.ServerOption;

import java.util.Objects;
import java.util.Properties;

/**
 * @author Taejin Koo
 */
public final class StatReceiverConfiguration implements DataReceiverGroupConfiguration {

    private static final String PREFIX = "collector.receiver.stat";
    private static final String GRPC_PREFIX = "collector.receiver.grpc.stat";

    private static final String GRPC_ENABLE = GRPC_PREFIX + ".enable";
    private static final String GRPC_BIND_IP = GRPC_PREFIX + ".ip";
    private static final String GRPC_BIND_PORT = GRPC_PREFIX + ".port";
    private static final String GRPC_SERVER_EXECUTOR_THREAD_SIZE = GRPC_PREFIX + ".server.executor.thread.size";
    private static final String GRPC_SERVER_EXECUTOR_QUEUE_SIZE = GRPC_PREFIX + ".server.executor.queue.size";
    private static final String GRPC_SERVER_EXECUTOR_MONITOR_ENABLE = GRPC_PREFIX + ".server.executor.monitor.enable";
    private static final String GRPC_WORKER_EXECUTOR_THREAD_SIZE = GRPC_PREFIX + ".worker.executor.thread.size";
    private static final String GRPC_WORKER_EXECUTOR_QUEUE_SIZE = GRPC_PREFIX + ".worker.executor.queue.size";
    private static final String GRPC_WORKER_EXECUTOR_MONITOR_ENABLE = GRPC_PREFIX + ".worker.executor.monitor.enable";

    private static final String GRPC_STREAM_SCHEDULER_THREAD_SIZE = GRPC_PREFIX + ".stream.scheduler.thread.size";
    private static final String GRPC_CALL_INIT_REQUEST_COUNT = GRPC_PREFIX + ".stream.call.init.request.count";
    private static final String GRPC_STREAM_SCHEDULER_PERIOD_MILLIS = GRPC_PREFIX + ".stream.scheduler.period.millis";
    private static final String GRPC_STREAM_SCHEDULER_RECOVERY_MESSAGE_COUNT = GRPC_PREFIX + ".stream.scheduler.recovery.message.count";


    private static final String TCP_ENABLE = PREFIX + ".tcp";
    private static final String TCP_BIND_IP = PREFIX + ".tcp.ip";
    private static final String TCP_BIND_PORT = PREFIX + ".tcp.port";
    private static final String UDP_ENABLE = PREFIX + ".udp";
    private static final String UDP_BIND_IP = PREFIX + ".udp.ip";
    private static final String UDP_BIND_PORT = PREFIX + ".udp.port";
    private static final String UDP_RECEIVE_BUFFER_SIZE = PREFIX + ".udp.receiveBufferSize";
    private static final String WORKER_THREAD_SIZE = PREFIX + ".worker.threadSize";
    private static final String WORKER_QUEUE_SIZE = PREFIX + ".worker.queueSize";
    private static final String WORKER_MONITOR_ENABLE = PREFIX + ".worker.monitor";

    private final boolean isTcpEnable;
    private final String tcpBindIp;
    private final int tcpBindPort;
    private final boolean isUdpEnable;
    private final String udpBindIp;
    private final int udpBindPort;
    private final int udpReceiveBufferSize;
    private final int workerThreadSize;
    private final int workerQueueSize;
    private final boolean workerMonitorEnable;

    private final boolean isGrpcEnable;
    private final String grpcBindIp;
    private final int grpcBindPort;
    private final int grpcServerExecutorThreadSize;
    private final int grpcServerExecutorQueueSize;
    private final boolean grpcServerExecutorMonitorEnable;
    private final int grpcWorkerExecutorThreadSize;
    private final int grpcWorkerExecutorQueueSize;
    private final boolean grpcWorkerExecutorMonitorEnable;
    private final int grpcStreamSchedulerThreadSize;
    private final int grpcStreamCallInitRequestCount;
    private final int grpcStreamSchedulerPeriodMillis;
    private final int grpcStreamSchedulerRecoveryMessageCount;
    private final ServerOption grpcServerOption;

    public StatReceiverConfiguration(Properties properties, DeprecatedConfiguration deprecatedConfiguration) {
        Objects.requireNonNull(properties, "properties must not be null");
        Objects.requireNonNull(deprecatedConfiguration, "deprecatedConfiguration must not be null");

        this.isTcpEnable = CollectorConfiguration.readBoolean(properties, TCP_ENABLE);
        this.tcpBindIp = CollectorConfiguration.readString(properties, TCP_BIND_IP, CollectorConfiguration.DEFAULT_LISTEN_IP);
        this.tcpBindPort = CollectorConfiguration.readInt(properties, TCP_BIND_PORT, -1);
        this.isUdpEnable = isUdpEnable(properties, deprecatedConfiguration, true);
        this.udpBindIp = getUdpBindIp(properties, deprecatedConfiguration, CollectorConfiguration.DEFAULT_LISTEN_IP);
        this.udpBindPort = getUdpBindPort(properties, deprecatedConfiguration, 9995);
        this.udpReceiveBufferSize = getUdpReceiveBufferSize(properties, deprecatedConfiguration, 1024 * 4096);
        this.workerThreadSize = getWorkerThreadSize(properties, deprecatedConfiguration, 128);
        Assert.isTrue(workerThreadSize > 0, "workerThreadSize must be greater than 0");
        this.workerQueueSize = getWorkerQueueSize(properties, deprecatedConfiguration, 1024);
        Assert.isTrue(workerQueueSize > 0, "workerQueueSize must be greater than 0");
        this.workerMonitorEnable = isWorkerThreadMonitorEnable(properties, deprecatedConfiguration, false);

        // gRPC
        this.isGrpcEnable = CollectorConfiguration.readBoolean(properties, GRPC_ENABLE);
        this.grpcBindIp = CollectorConfiguration.readString(properties, GRPC_BIND_IP, CollectorConfiguration.DEFAULT_LISTEN_IP);
        this.grpcBindPort = CollectorConfiguration.readInt(properties, GRPC_BIND_PORT, 9992);

        // Server executor
        this.grpcServerExecutorThreadSize = CollectorConfiguration.readInt(properties, GRPC_SERVER_EXECUTOR_THREAD_SIZE, 128);
        Assert.isTrue(grpcServerExecutorThreadSize > 0, "grpcServerExecutorThreadSize must be greater than 0");
        this.grpcServerExecutorQueueSize = CollectorConfiguration.readInt(properties, GRPC_SERVER_EXECUTOR_QUEUE_SIZE, 1024 * 5);
        Assert.isTrue(grpcServerExecutorQueueSize > 0, "grpcServerExecutorQueueSize must be greater than 0");
        this.grpcServerExecutorMonitorEnable = CollectorConfiguration.readBoolean(properties, GRPC_SERVER_EXECUTOR_MONITOR_ENABLE);

        // Work executor
        this.grpcWorkerExecutorThreadSize = CollectorConfiguration.readInt(properties, GRPC_WORKER_EXECUTOR_THREAD_SIZE, 128);
        Assert.isTrue(grpcWorkerExecutorThreadSize > 0, "grpcWorkerExecutorThreadSize must be greater than 0");
        this.grpcWorkerExecutorQueueSize = CollectorConfiguration.readInt(properties, GRPC_WORKER_EXECUTOR_QUEUE_SIZE, 1024 * 5);
        Assert.isTrue(grpcWorkerExecutorQueueSize > 0, "grpcWorkerExecutorQueueSize must be greater than 0");
        this.grpcWorkerExecutorMonitorEnable = CollectorConfiguration.readBoolean(properties, GRPC_WORKER_EXECUTOR_MONITOR_ENABLE);
        this.grpcStreamSchedulerThreadSize = CollectorConfiguration.readInt(properties, GRPC_STREAM_SCHEDULER_THREAD_SIZE, 1);
        Assert.isTrue(grpcStreamSchedulerThreadSize > 0, "grpcStreamSchedulerThreadSize must be greater than 0");
        this.grpcStreamSchedulerPeriodMillis = CollectorConfiguration.readInt(properties, GRPC_STREAM_SCHEDULER_PERIOD_MILLIS, 1000);
        this.grpcStreamCallInitRequestCount = CollectorConfiguration.readInt(properties, GRPC_CALL_INIT_REQUEST_COUNT, 64);
        this.grpcStreamSchedulerRecoveryMessageCount = CollectorConfiguration.readInt(properties, GRPC_STREAM_SCHEDULER_RECOVERY_MESSAGE_COUNT, 10);

        // Server option
        final ServerOption.Builder serverOptionBuilder = GrpcPropertiesServerOptionBuilder.newBuilder(properties, GRPC_PREFIX);
        this.grpcServerOption = serverOptionBuilder.build();

        validate();
    }

    private void validate() {
        Assert.isTrue(isTcpEnable || isUdpEnable, "statReceiver does not allow tcp and udp disable");

        if (isTcpEnable) {
            Objects.requireNonNull(tcpBindIp, "tcpBindIp must not be null");
            Assert.isTrue(tcpBindPort > 0, "tcpBindPort must be greater than 0");
        }

        if (isUdpEnable) {
            Objects.requireNonNull(udpBindIp, "udpBindIp must not be null");
            Assert.isTrue(udpBindPort > 0, "udpBindPort must be greater than 0");
            Assert.isTrue(udpReceiveBufferSize > 0, "udpReceiveBufferSize must be greater than 0");
        }
    }

    private boolean isUdpEnable(Properties properties, DeprecatedConfiguration deprecatedConfiguration, boolean defaultValue) {
        if (properties.containsKey(UDP_ENABLE)) {
            return CollectorConfiguration.readBoolean(properties, UDP_ENABLE);
        }

        return defaultValue;
    }

    private String getUdpBindIp(Properties properties, DeprecatedConfiguration deprecatedConfiguration, String defaultValue) {
        if (properties.containsKey(UDP_BIND_IP)) {
            return CollectorConfiguration.readString(properties, UDP_BIND_IP, null);
        }

        if (deprecatedConfiguration.isSetUdpStatListenIp()) {
            return deprecatedConfiguration.getUdpStatListenIp();
        }

        return defaultValue;
    }

    private int getUdpBindPort(Properties properties, DeprecatedConfiguration deprecatedConfiguration, int defaultValue) {
        if (properties.containsKey(UDP_BIND_PORT)) {
            return CollectorConfiguration.readInt(properties, UDP_BIND_PORT, -1);
        }

        if (deprecatedConfiguration.isSetUdpStatListenPort()) {
            return deprecatedConfiguration.getUdpStatListenPort();
        }

        return defaultValue;
    }


    private int getUdpReceiveBufferSize(Properties properties, DeprecatedConfiguration deprecatedConfiguration, int defaultValue) {
        if (properties.containsKey(UDP_RECEIVE_BUFFER_SIZE)) {
            return CollectorConfiguration.readInt(properties, UDP_RECEIVE_BUFFER_SIZE, -1);
        }

        if (deprecatedConfiguration.isSetUdpStatSocketReceiveBufferSize()) {
            return deprecatedConfiguration.getUdpStatSocketReceiveBufferSize();
        }

        return defaultValue;
    }

    private int getWorkerThreadSize(Properties properties, DeprecatedConfiguration deprecatedConfiguration, int defaultValue) {
        if (properties.containsKey(WORKER_THREAD_SIZE)) {
            return CollectorConfiguration.readInt(properties, WORKER_THREAD_SIZE, -1);
        }

        if (deprecatedConfiguration.isSetUdpStatWorkerThread()) {
            return deprecatedConfiguration.getUdpStatWorkerThread();
        }

        return defaultValue;
    }

    private int getWorkerQueueSize(Properties properties, DeprecatedConfiguration deprecatedConfiguration, int defaultValue) {
        if (properties.containsKey(WORKER_QUEUE_SIZE)) {
            return CollectorConfiguration.readInt(properties, WORKER_QUEUE_SIZE, -1);
        }

        if (deprecatedConfiguration.isSetUdpStatWorkerQueueSize()) {
            return deprecatedConfiguration.getUdpStatWorkerQueueSize();
        }

        return defaultValue;
    }

    private boolean isWorkerThreadMonitorEnable(Properties properties, DeprecatedConfiguration deprecatedConfiguration, boolean defaultValue) {
        if (properties.containsKey(WORKER_MONITOR_ENABLE)) {
            return CollectorConfiguration.readBoolean(properties, WORKER_MONITOR_ENABLE);
        }

        if (deprecatedConfiguration.isSetUdpStatWorkerMonitor()) {
            return deprecatedConfiguration.isUdpStatWorkerMonitor();
        }

        return defaultValue;
    }

    @Override
    public boolean isTcpEnable() {
        return isTcpEnable;
    }

    @Override
    public String getTcpBindIp() {
        return tcpBindIp;
    }

    @Override
    public int getTcpBindPort() {
        return tcpBindPort;
    }

    @Override
    public boolean isUdpEnable() {
        return isUdpEnable;
    }

    @Override
    public String getUdpBindIp() {
        return udpBindIp;
    }

    @Override
    public int getUdpBindPort() {
        return udpBindPort;
    }

    @Override
    public int getUdpReceiveBufferSize() {
        return udpReceiveBufferSize;
    }

    @Override
    public int getWorkerThreadSize() {
        return workerThreadSize;
    }

    @Override
    public int getWorkerQueueSize() {
        return workerQueueSize;
    }

    @Override
    public boolean isWorkerMonitorEnable() {
        return workerMonitorEnable;
    }

    @Override
    public boolean isGrpcEnable() {
        return isGrpcEnable;
    }

    @Override
    public String getGrpcBindIp() {
        return grpcBindIp;
    }

    @Override
    public int getGrpcBindPort() {
        return grpcBindPort;
    }

    public int getGrpcServerExecutorThreadSize() {
        return grpcServerExecutorThreadSize;
    }

    public int getGrpcServerExecutorQueueSize() {
        return grpcServerExecutorQueueSize;
    }

    public boolean isGrpcServerExecutorMonitorEnable() {
        return grpcServerExecutorMonitorEnable;
    }

    public int getGrpcWorkerExecutorThreadSize() {
        return grpcWorkerExecutorThreadSize;
    }

    public int getGrpcWorkerExecutorQueueSize() {
        return grpcWorkerExecutorQueueSize;
    }

    public boolean isGrpcWorkerExecutorMonitorEnable() {
        return grpcWorkerExecutorMonitorEnable;
    }

    public int getGrpcStreamSchedulerThreadSize() {
        return grpcStreamSchedulerThreadSize;
    }

    public int getGrpcStreamCallInitRequestCount() {
        return grpcStreamCallInitRequestCount;
    }

    public int getGrpcStreamSchedulerPeriodMillis() {
        return grpcStreamSchedulerPeriodMillis;
    }

    public int getGrpcStreamSchedulerRecoveryMessageCount() {
        return grpcStreamSchedulerRecoveryMessageCount;
    }

    public ServerOption getGrpcServerOption() {
        return grpcServerOption;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatReceiverConfiguration{");
        sb.append("isTcpEnable=").append(isTcpEnable);
        sb.append(", tcpBindIp='").append(tcpBindIp).append('\'');
        sb.append(", tcpBindPort=").append(tcpBindPort);
        sb.append(", isUdpEnable=").append(isUdpEnable);
        sb.append(", udpBindIp='").append(udpBindIp).append('\'');
        sb.append(", udpBindPort=").append(udpBindPort);
        sb.append(", udpReceiveBufferSize=").append(udpReceiveBufferSize);
        sb.append(", workerThreadSize=").append(workerThreadSize);
        sb.append(", workerQueueSize=").append(workerQueueSize);
        sb.append(", workerMonitorEnable=").append(workerMonitorEnable);
        sb.append(", isGrpcEnable=").append(isGrpcEnable);
        sb.append(", grpcBindIp='").append(grpcBindIp).append('\'');
        sb.append(", grpcBindPort=").append(grpcBindPort);
        sb.append(", grpcServerExecutorThreadSize=").append(grpcServerExecutorThreadSize);
        sb.append(", grpcServerExecutorQueueSize=").append(grpcServerExecutorQueueSize);
        sb.append(", grpcServerExecutorMonitorEnable=").append(grpcServerExecutorMonitorEnable);
        sb.append(", grpcWorkerExecutorThreadSize=").append(grpcWorkerExecutorThreadSize);
        sb.append(", grpcWorkerExecutorQueueSize=").append(grpcWorkerExecutorQueueSize);
        sb.append(", grpcWorkerExecutorMonitorEnable=").append(grpcWorkerExecutorMonitorEnable);
        sb.append(", grpcStreamSchedulerThreadSize=").append(grpcStreamSchedulerThreadSize);
        sb.append(", grpcStreamCallInitRequestCount=").append(grpcStreamCallInitRequestCount);
        sb.append(", grpcStreamSchedulerPeriodMillis=").append(grpcStreamSchedulerPeriodMillis);
        sb.append(", grpcStreamSchedulerRecoveryMessageCount=").append(grpcStreamSchedulerRecoveryMessageCount);
        sb.append(", grpcServerOption=").append(grpcServerOption);
        sb.append('}');
        return sb.toString();
    }
}
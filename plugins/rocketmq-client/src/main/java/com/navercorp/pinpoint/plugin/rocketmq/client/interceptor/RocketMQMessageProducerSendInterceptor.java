package com.navercorp.pinpoint.plugin.rocketmq.client.interceptor;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.context.TraceId;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.plugin.rocketmq.client.RocketMQConstants;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageDecoder;
import org.apache.rocketmq.common.protocol.header.SendMessageRequestHeader;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.maihaoche.com
 *
 * @Package com.navercorp.pinpoint.plugin.rocketmq.client.interceptor
 * @author: 文远（wenyuan@maihaoche.com）
 * @date: 2018-11-28 20:06
 * @Copyright: 2017-2020 www.maihaoche.com Inc. All rights reserved.
 * 注意：本内容仅限于卖好车内部传阅，禁止外泄以及用于其他的商业目
 */
public class RocketMQMessageProducerSendInterceptor implements AroundInterceptor {
    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();
    private final TraceContext traceContext;
    private final MethodDescriptor descriptor;
    public RocketMQMessageProducerSendInterceptor(TraceContext traceContext, MethodDescriptor descriptor) {
        this.traceContext = traceContext;
        this.descriptor = descriptor;
    }
    @Override
    public void before(Object target, Object[] args) {
        logger.info("rocketmq producer send interceptor begin...");
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }
        Trace trace = traceContext.currentRawTraceObject();
        if (trace == null) {
            return;
        }
        Message message = (Message) args[2];
        SendMessageRequestHeader requestHeader=(SendMessageRequestHeader) args[3];
        Map<String, String> header = message.getProperties();
        try {
            if (trace.canSampled()) {
                SpanEventRecorder recorder = trace.traceBlockBegin();
                recorder.recordServiceType(RocketMQConstants.ROCKETMQ_CLIENT);
                TraceId nextId = trace.getTraceId().getNextTraceId();
                recorder.recordNextSpanId(nextId.getSpanId());
                header.put("Pinpoint-TraceID", nextId.getTransactionId());
                header.put("Pinpoint-SpanID", nextId.getSpanId() + "");
                header.put("Pinpoint-pSpanID", nextId.getParentSpanId() + "");
                header.put("Pinpoint-Flags", nextId.getFlags() + "");
                header.put("Pinpoint-pAppName", traceContext.getApplicationName());
                header.put("Pinpoint-pAppType", traceContext.getServerTypeCode() + "");
            } else {
                header.put("Pinpoint-Sampled", 0 + "");
            }
            requestHeader.setProperties(MessageDecoder.messageProperties2String(header));
        } catch (Throwable t) {
            logger.warn("BEFORE. Cause:{}", t.getMessage(), t);
        }
    }
    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        logger.info("rocketmq producer send interceptor end...");
        if (isDebug) {
            logger.afterInterceptor(target, args);
        }
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }
        String brokerAddr = "UNKNOW";
        String brokerName = "UNKNOW";
        if (null != args[0]) {
            brokerAddr = (String) args[0];
        }
        if (null != args[1]) {
            brokerName = (String) args[1];
        }
        Message message = (Message) args[2];
        try {
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();
            recorder.recordApi(descriptor);
            recorder.recordAttribute(RocketMQConstants.ROCKETMQ_MESSAGE, new String(message.getBody()));
            if (throwable == null) {
                recorder.recordEndPoint(brokerAddr);
                recorder.recordAttribute(RocketMQConstants.ROCKETMQ_BROKER_URL, brokerAddr);
                // This annotation indicates the uri to which the call is made
                recorder.recordAttribute(AnnotationKey.MESSAGE_QUEUE_URI, brokerName);
                SendMessageRequestHeader requestHeader = (SendMessageRequestHeader) args[3];
                // DestinationId is used to render the virtual queue node.
                recorder.recordDestinationId(requestHeader.getTopic());
            } else {
                recorder.recordException(throwable);
            }
        } catch (Throwable t) {
            logger.warn("AFTER error. Cause:{}", t.getMessage(), t);
        } finally {
            trace.traceBlockEnd();
        }
    }
}

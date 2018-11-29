package com.navercorp.pinpoint.plugin.rocketmq.client;

/**
 * All rights Reserved, Designed By www.maihaoche.com
 *
 * @Package com.navercorp.pinpoint.plugin.rocketmq.client
 * @author: 文远（wenyuan@maihaoche.com）
 * @date: 2018-11-28 20:04
 * @Copyright: 2017-2020 www.maihaoche.com Inc. All rights reserved.
 * 注意：本内容仅限于卖好车内部传阅，禁止外泄以及用于其他的商业目
 */
import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.common.trace.AnnotationKeyMatchers;
import com.navercorp.pinpoint.common.trace.TraceMetadataProvider;
import com.navercorp.pinpoint.common.trace.TraceMetadataSetupContext;
/**
 */
public class RocketMQTraceMetadataProvider implements TraceMetadataProvider {
    @Override
    public void setup(TraceMetadataSetupContext context) {
        context.addServiceType(RocketMQConstants.ROCKETMQ_CLIENT, AnnotationKeyMatchers.exact(AnnotationKey.MESSAGE_QUEUE_URI));

        context.addAnnotationKey(RocketMQConstants.ROCKETMQ_BROKER_URL);
        context.addAnnotationKey(RocketMQConstants.ROCKETMQ_MESSAGE);
    }
}

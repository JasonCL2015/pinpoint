package com.navercorp.pinpoint.plugin.rocketmq.client;

import com.navercorp.pinpoint.common.trace.*;

import static com.navercorp.pinpoint.common.trace.ServiceTypeProperty.QUEUE;
import static com.navercorp.pinpoint.common.trace.ServiceTypeProperty.RECORD_STATISTICS;

/**
 * All rights Reserved, Designed By www.maihaoche.com
 *
 * @Package com.navercorp.pinpoint.plugin.rocketmq.client
 * @author: 文远（wenyuan@maihaoche.com）
 * @date: 2018-11-28 19:45
 * @Copyright: 2017-2020 www.maihaoche.com Inc. All rights reserved.
 * 注意：本内容仅限于卖好车内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RocketMQConstants {

    // rocketmq的服务类型
    public static final ServiceType ROCKETMQ_CLIENT = ServiceTypeFactory.of(8321, "ROCKETMQ_CLIENT", QUEUE,
            RECORD_STATISTICS);
    public static final AnnotationKey ROCKETMQ_BROKER_URL = AnnotationKeyFactory.of(401, "rocketmq.broker.address",
            AnnotationKeyProperty.VIEW_IN_RECORD_SET);
    public static final ServiceType ROCKETMQ_CLIENT_INTERNAL = ServiceTypeFactory.of(9902, "ROCKETMQ_CLIENT_INTERNAL",
            "ROCKET_MQ_CLIENT");
    public static final AnnotationKey ROCKETMQ_MESSAGE = AnnotationKeyFactory.of(402, "rocketmq.message",
            AnnotationKeyProperty.VIEW_IN_RECORD_SET);
    // rocketmq基础包
    String BASE_PACKAGE = "com.navercorp.pinpoint.plugin.rocketmq.client";
    // rocketmq的生产者检测代码
    String LISTEN_PRODUCER = "org.apache.rocketmq.client.impl.MQClientAPIImpl";
    // rocketmq的生产者监听方法
    String LISTEN_PRODUCER_METHOD = "sendMessage";
    // rocketmq的生产者切面基础包
    String AOP_LISTEN_PRODUCER_PACKAGE = BASE_PACKAGE + ".interceptor";
    // rocketmq的生产者切面实现
    String AOP_LISTEN_PRODUCER_METHOD = AOP_LISTEN_PRODUCER_PACKAGE + ".RocketMQMessageProducerSendInterceptor";
    // rocketmq的管道
    String LISTEN_CHANNEL = "org.apache.rocketmq.remoting.netty.NettyRemotingClient";
    String AOP_LISTEN_CHANNEL_METHOD = AOP_LISTEN_PRODUCER_PACKAGE + ".RocketMQChannelInterceptor";
    // rocketmq的消费者检测代码
    String LISTEN_CONSUMER_ORDERLYSERVICE = "org.apache.rocketmq.client.impl.consumer.ConsumeMessageOrderlyService";
    String LISTEN_CONSUMER_ConcurrentlyService = "org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService";
    String AOP_LISTEN_CONSUMER_METHOD = AOP_LISTEN_PRODUCER_PACKAGE + ".RocketMQMessageConsumeSendInterceptor";
}

package com.navercorp.pinpoint.plugin.rocketmq.client;

/**
 * All rights Reserved, Designed By www.maihaoche.com
 *
 * @Package com.navercorp.pinpoint.plugin
 * @author: 文远（wenyuan@maihaoche.com）
 * @date: 2018-11-28 20:03
 * @Copyright: 2017-2020 www.maihaoche.com Inc. All rights reserved.
 * 注意：本内容仅限于卖好车内部传阅，禁止外泄以及用于其他的商业目
 */

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplate;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplateAware;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;

import java.security.ProtectionDomain;

public class RocketMQPlugin implements ProfilerPlugin, TransformTemplateAware {
    private TransformTemplate transformTemplate;
    @Override
    public void setup(ProfilerPluginSetupContext context) {
        this.addProducerEditor();
        //this.addChannelEditor();
        this.addConsumerEditor();
    }
    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
    private void addProducerEditor() {
        transformTemplate.transform(RocketMQConstants.LISTEN_PRODUCER, new TransformCallback() {
            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className,
                                        Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
                    throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
                for (InstrumentMethod instrumentMethod : target.getDeclaredMethods()) {
                    if (RocketMQConstants.LISTEN_PRODUCER_METHOD.equals(instrumentMethod.getName())) {
                        instrumentMethod.addInterceptor(RocketMQConstants.AOP_LISTEN_PRODUCER_METHOD);
                    }
                }
                return target.toBytecode();
            }
        });
    }
    private void addChannelEditor() {
        transformTemplate.transform(RocketMQConstants.LISTEN_CHANNEL, new TransformCallback() {
            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className,
                                        Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
                    throws InstrumentException {
                InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
                for (InstrumentMethod instrumentMethod : target.getDeclaredMethods()) {
                    instrumentMethod.addInterceptor(RocketMQConstants.AOP_LISTEN_CHANNEL_METHOD);
                }
                return target.toBytecode();
            }
        });
    }
    private void addConsumerEditor() {
        String[] consumemessageservicefqcns = { RocketMQConstants.LISTEN_CONSUMER_ConcurrentlyService };
        for (String consumemessageservicefqcn : consumemessageservicefqcns) {
            transformTemplate.transform(consumemessageservicefqcn, new TransformCallback() {
                @Override
                public byte[] doInTransform(Instrumentor instrumentor, ClassLoader loader, String className,
                                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
                        throws InstrumentException {
                    InstrumentClass target = instrumentor.getInstrumentClass(loader, className, classfileBuffer);
                    for (InstrumentMethod method : target.getDeclaredMethods()) {
                        if ("submitConsumeRequest".equals(method.getName())) {
                            method.addInterceptor(RocketMQConstants.AOP_LISTEN_CONSUMER_METHOD);
                        }
                    }
                    return target.toBytecode();
                }
            });
        }
    }
}
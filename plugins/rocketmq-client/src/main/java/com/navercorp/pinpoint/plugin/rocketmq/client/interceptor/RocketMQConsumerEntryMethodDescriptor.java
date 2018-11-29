package com.navercorp.pinpoint.plugin.rocketmq.client.interceptor;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.common.trace.MethodType;

/**
 * All rights Reserved, Designed By www.maihaoche.com
 *
 * @Package com.navercorp.pinpoint.plugin.rocketmq.client.interceptor
 * @author: 文远（wenyuan@maihaoche.com）
 * @date: 2018-11-28 20:05
 * @Copyright: 2017-2020 www.maihaoche.com Inc. All rights reserved.
 * 注意：本内容仅限于卖好车内部传阅，禁止外泄以及用于其他的商业目
 */
public class RocketMQConsumerEntryMethodDescriptor implements MethodDescriptor {
    private int apiId = 0;
    private int type = MethodType.WEB_REQUEST;
    @Override
    public String getMethodName() {
        return "";
    }
    @Override
    public String getClassName() {
        return "";
    }
    @Override
    public String[] getParameterTypes() {
        return null;
    }
    @Override
    public String[] getParameterVariableName() {
        return null;
    }
    @Override
    public String getParameterDescriptor() {
        return "()";
    }
    @Override
    public int getLineNumber() {
        return -1;
    }
    @Override
    public String getFullName() {
        return RocketMQConsumerEntryMethodDescriptor.class.getName();
    }
    @Override
    public void setApiId(int apiId) {
        this.apiId = apiId;
    }
    @Override
    public int getApiId() {
        return this.apiId;
    }
    @Override
    public String getApiDescriptor() {
        return "ActiveMQ Consumer Invocation";
    }
    @Override
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
}

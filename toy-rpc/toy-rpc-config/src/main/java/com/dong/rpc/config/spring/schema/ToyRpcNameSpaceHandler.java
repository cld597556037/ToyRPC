package com.dong.rpc.config.spring.schema;

import com.dong.rpc.config.spring.ConsumerBean;
import com.dong.rpc.config.spring.DiscoveryBean;
import com.dong.rpc.config.spring.ProtocolBean;
import com.dong.rpc.config.spring.ProviderBean;
import com.dong.rpc.config.spring.RegistryBean;
import com.dong.rpc.config.spring.ServerBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ToyRpcNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new ToyRpcBeanDefinitionParser(RegistryBean.class));
        registerBeanDefinitionParser("discovery", new ToyRpcBeanDefinitionParser(DiscoveryBean.class));
        registerBeanDefinitionParser("protocol", new ToyRpcBeanDefinitionParser(ProtocolBean.class));
        registerBeanDefinitionParser("server", new ToyRpcBeanDefinitionParser(ServerBean.class));
        registerBeanDefinitionParser("provider", new ToyRpcBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("consumer", new ToyRpcBeanDefinitionParser(ConsumerBean.class));
    }
}

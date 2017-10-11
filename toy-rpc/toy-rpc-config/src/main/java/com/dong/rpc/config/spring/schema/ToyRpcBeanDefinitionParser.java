package com.dong.rpc.config.spring.schema;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Method;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ToyRpcBeanDefinitionParser implements BeanDefinitionParser {

    private Class<?> beanClass = null;

    public ToyRpcBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        String id = element.getAttribute("id");
        if (StringUtils.isBlank(id)) {
            throw new IllegalStateException("This bean do not set spring bean id " + id);
        }


        if (parserContext.getRegistry().containsBeanDefinition(id)) {
            throw new IllegalStateException("Duplicate spring bean id " + id);
        }
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        NamedNodeMap map = element.getAttributes();
        Node node = null;
        Method method = null;
        for (int i = 0; i < map.getLength(); i++) {
            node = map.item(i);
            String name = node.getNodeName().trim();
            String value = node.getNodeValue().trim();
            switch (name) {
                case "url":
                case "interface":
                case "port":
                    beanDefinition.getPropertyValues().addPropertyValue(name, value);
                    break;
                case "reference":
                    BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
                    if (!refBean.isSingleton()) {
                        throw new IllegalStateException("The exported service ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=" + value + " scope=singleton ...>");
                    } else {
                        Object reference = new RuntimeBeanReference(value);
                        beanDefinition.getPropertyValues().addPropertyValue(name, reference);
                    }
                    break;
            }
        }

        return beanDefinition;
    }



}

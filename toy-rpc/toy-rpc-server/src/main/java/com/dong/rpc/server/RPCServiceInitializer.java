package com.dong.rpc.server;

import com.dong.rpc.annotation.RPCService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author caolidong
 * @date 17/7/24.
 */
public class RPCServiceInitializer  implements ApplicationContextAware {

    private static final Logger LOGGER = Logger.getLogger(RPCServiceInitializer.class);

    private Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        //扫描带有RPCService注解的类
        Map<String, Object> serviceBeans = context.getBeansWithAnnotation(RPCService.class);
        serviceBeans.entrySet().stream().forEach(entry -> {
            Class<?> clazz = entry.getValue().getClass();
            RPCService rpcService = clazz.getAnnotation(RPCService.class);
            String interfaceName = null;
            if (rpcService.value() == Object.class) {
                //没有配置时，默认为第一个接口类
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces == null || interfaces.length == 0) {
                    LOGGER.error(String.format("%s 没有实现接口，无法正常提供RPC服务！", clazz.getName()));
                } else {
                    interfaceName = interfaces[0].getName();
                }

            } else {
                interfaceName = rpcService.value().getName();
            }
            if (!StringUtils.isEmpty(interfaceName)) {
                serviceMap.put(interfaceName, entry.getValue());
            }
        });

    }

    public Map<String, Object> getServiceMap() {
        return serviceMap;
    }
}

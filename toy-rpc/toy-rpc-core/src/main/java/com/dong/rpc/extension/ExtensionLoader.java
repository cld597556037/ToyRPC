package com.dong.rpc.extension;

import com.dong.rpc.exception.ToyException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author caolidong
 * @date 2017/9/23.
 */
public class ExtensionLoader<T> {

    private static Logger logger = Logger.getLogger(ExtensionLoader.class);

    private static ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoaders = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();

    private ConcurrentMap<String, Class<T>> classes = null;
    private ConcurrentMap<String, T> instances = null;

    private Class<T> type;

    private static final String PREFIX = "META-INF/services/";

    private static final String DEFAULT = "default";

    private ClassLoader classLoader;

    private boolean init = false;

    private static synchronized <T> ExtensionLoader<T> initExtensionLoader(Class<T> type) {
        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);

        if (loader == null) {
            loader = new ExtensionLoader<T>(type);

            extensionLoaders.putIfAbsent(type, loader);

            loader = (ExtensionLoader<T>) extensionLoaders.get(type);
        }

        return loader;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);

        if (loader == null) {
            loader = initExtensionLoader(type);
        }
        return loader;
    }

    private ExtensionLoader(Class<T> type) {
        this(type, Thread.currentThread().getContextClassLoader());
    }

    private ExtensionLoader(Class<T> type, ClassLoader classLoader) {
        this.type = type;
        this.classLoader = classLoader;
    }

    public T getExtension() throws ToyException {
        return getExtension(DEFAULT);
    }

    public synchronized T getExtension(String name) throws ToyException {
        if (!init) {
            init();
        }


        Spi spi = type.getAnnotation(Spi.class);

        Class<T> clazz = classes.get(name);
        if (clazz == null) {
            throw new ToyException("fail to get class of :" + name);
        }

        T instance = null;

        if (spi.scope() == Scope.SINGLETON && instances.containsKey(name)) {
            //单例已存在直接返回
            return instances.get(name);
        }


        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            throw new ToyException("fail to newInstance class of :" + name, e);
        }

        //单例
        if (spi.scope() == Scope.SINGLETON) {
            instances.putIfAbsent(name, instance);
        }

        return instance;
    }

    private void init() {
        if (init) {
            return;
        }

        classes = loadExtensionClasses();
        instances = new ConcurrentHashMap<String, T>();
        init = true;
    }


    private ConcurrentMap<String, Class<T>> loadExtensionClasses() {
        String path = PREFIX + type.getName();
        Enumeration<URL> urls = null;
        List<String> classNames = new ArrayList<String>();
        try {
            if (classLoader == null) {
                urls = ClassLoader.getSystemResources(path);
            } else {
                urls = classLoader.getResources(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (urls == null || !urls.hasMoreElements()) {
            return new ConcurrentHashMap();
        }

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            classNames.addAll(parseUrl(type, url));
        }

        return loadClass(classNames);
    }

    /**
     * 获取url文件中的class list
     *
     * @param type
     * @param url
     * @return
     * @throws ServiceConfigurationError
     */
    private List<String> parseUrl(Class<T> type, URL url) throws ServiceConfigurationError {
        InputStream inputStream = null;
        BufferedReader reader = null;
        List<String> classNames = new ArrayList<>();

        try {
            inputStream = url.openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;

            while ((line = reader.readLine()) != null) {
                classNames.add(line.trim());
            }
        } catch (IOException e) {
            logger.error("fail to parse url:" + url, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("close input stream exception", e);
            }
        }
        return classNames;
    }

    private ConcurrentMap<String, Class<T>> loadClass(List<String> classNames) {
        ConcurrentMap<String, Class<T>> map = new ConcurrentHashMap<String, Class<T>>();

        for (String className : classNames) {
            try {
                Class<T> clz;
                if (classLoader == null) {
                    clz = (Class<T>) Class.forName(className);
                } else {
                    clz = (Class<T>) Class.forName(className, true, classLoader);
                }

                String spiName = getSpiName(clz);
                if (map.containsKey(spiName)) {
                    logger.error("duplicate spiName:" + spiName);
                } else {
                    map.put(spiName, clz);
                }
            } catch (Exception e) {
                logger.error("fail to load class: " + className);
            }
        }
        return map;
    }

    private String getSpiName(Class<?> clz) {
        SpiMeta spiMeta = clz.getAnnotation(SpiMeta.class);
        String name = (spiMeta != null && !"".equals(spiMeta.name())) ? spiMeta.name() : clz.getSimpleName();
        return name;
    }

}

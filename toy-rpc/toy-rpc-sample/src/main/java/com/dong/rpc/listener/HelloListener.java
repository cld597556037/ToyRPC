package com.dong.rpc.listener;

import com.dong.rpc.client.Listener;
import org.apache.log4j.Logger;

/**
 * @author caolidong
 * @date 17/8/1.
 */
public class HelloListener implements Listener {

    private static Logger logger = Logger.getLogger(HelloListener.class);

    @Override
    public void onComplete() {
        logger.info("hello listener complete!");
    }

    @Override
    public void onException(Throwable throwable) {
        logger.info("hello listener exception:" + throwable);
    }

    @Override
    public void onTimeout() {
        logger.info("hello listener timeout!");
    }
}

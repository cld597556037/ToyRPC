package com.dong.rpc.task;

import com.dong.rpc.config.api.ProviderConfig;

/**
 * @author caolidong
 * @date 17/7/21.
 */
public class ProviderTask implements Runnable {

    private ProviderConfig providerConfig;


    public ProviderTask(ProviderConfig providerConfig) {
        this.providerConfig = providerConfig;
    }

    @Override
    public void run() {
        this.providerConfig.export();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            this.providerConfig.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.dong.rpc.task;


import com.dong.rpc.exception.HelloException;
import com.dong.rpc.service.hello.HelloService;

/**
 * @author caolidong
 * @date 17/6/29.
 */
public class ClientTask implements Runnable {

    private HelloService helloService;

    private int no;

    public ClientTask(HelloService helloService, int no) {
        this.helloService = helloService;
        this.no = no;
    }

    @Override
    public void run() {
        try {
            helloService.hello("dong" + no);
        } catch (HelloException e) {
            e.printStackTrace();
        }
    }
}

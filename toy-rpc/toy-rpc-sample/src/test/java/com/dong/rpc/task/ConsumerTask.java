package com.dong.rpc.task;


import com.dong.rpc.service.name.NameService;

/**
 * @author caolidong
 * @date 17/6/29.
 */
public class ConsumerTask implements Runnable {

    private NameService nameService;

    private int no;

    public ConsumerTask(NameService nameService, int no) {
        this.nameService = nameService;
        this.no = no;
    }

    @Override
    public void run() {
        nameService.getName();
    }
}

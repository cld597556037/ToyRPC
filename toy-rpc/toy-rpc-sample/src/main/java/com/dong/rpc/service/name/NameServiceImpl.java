package com.dong.rpc.service.name;

import com.dong.rpc.annotation.RPCService;

/**
 * @author caolidong
 * @date 17/8/21.
 */
@RPCService
public class NameServiceImpl implements NameService {

    @Override
    public String getName() {
        return "dong";
    }
}

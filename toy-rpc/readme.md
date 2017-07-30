    toy-rpc，just for fun！
    
    开发进程:
    1. RPC基础功能
        1.1 基于netty编解码
        1.2 基于kryo序列化
        1.3 单服务、单链接 client-server
    2. 项目结构调整，模块细分
    3. Client端Channel复用
        3.1 对象池
        3.2 多连接
    4 注册发现模块优化
        4.1 local用于测试
        4.2 Zookepper 服务治理
        4.3 多服务
        4.4 负载均衡 todo
    5. Annotation
        5.1 RPCService
        5.2 version todo
    6. 全局跟踪 traceid todo
    7. 集成ProtoBuf: Protostuff todo
    8. spring xsd todo
    9. 异步调用 method must return void
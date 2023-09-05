package com.honvay.flyai.application;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    private final int port;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void run() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))  // 在BossGroup中增加一个日志处理器 日志级别为INFO
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 因为是基于Http协议的所以采用Http的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块的方式写, 添加ChunkedWriteHandler(分块写入处理程序)
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                             * http 数据在传输过程中是分段的 http Object aggregator 就是可以将多个段聚合
                             * 这就是为什么 当浏览器发送大量数据时, 就会出现多次http请求
                             * 参数为 : 最大内容长度
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                             * 对应WebSocket 他的数据时以桢(frame) 形式传递
                             * 可以看到WebSocketFrame下面有6个子类
                             * 浏览器请求时: ws://localhost:7000/xxx 请求的url
                             * 核心功能是将http协议升级为ws协议 保持长链接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义Handler, 处理业务逻辑
                            pipeline.addLast(new WebSocketTextFrameHandler());
                        }
                    });
            System.out.println("netty server is starter......");
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new WebSocketServer(7000).run();
    }
}
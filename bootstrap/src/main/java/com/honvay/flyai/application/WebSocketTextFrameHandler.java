package com.honvay.flyai.application;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * TextWebSocketFrame 表示一个文本桢
 */
public class WebSocketTextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("[服务器] : 收到消息 -> " + msg.text());
        // 回复浏览器
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间: " + LocalDateTime.now() + "->"+ msg.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示唯一的值 LongText是唯一的
        System.out.println("handlerAdded 被调用:" + ctx.channel().id().asLongText());
        // shortText 可能会重复
        System.out.println("handlerAdded 被调用:" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // id 表示唯一的值 LongText是唯一的
        System.out.println("handlerRemoved 被调用:" + ctx.channel().id().asLongText());
        // shortText 可能会重复
        System.out.println("handlerRemoved 被调用:" + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }
}
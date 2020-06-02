package com.forezp.tcp;


import com.forezp.service.RabbitMQService;
import com.forezp.utils.EncodeUtil;
import com.forezp.utils.HttpUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TcpNetty{
	private static final Logger logger = LoggerFactory.getLogger(TcpNetty.class);

	@Value("${gps.server.ip}")
	private String gpsServerIp;

	@Value("${gps.server.port}")
	private int gpsServerPort;

	public Channel channel;

	public TcpNetty(){

	}

	public static void main(String[] args) {
		try {
			//System.out.println(HttpUtil.Get("http://localhost:8201/device/sendCommonCmd?deviceId=127958&cmd=S17,20200601,10,1&cmdName=ABC"));

			for(int t = 0;t < 20;t ++){
				final int t1 = t;
				new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println(""+t1);
						System.out.println(HttpUtil.Get("http://localhost:8201/device/sendCommonCmd?deviceId=127958&cmd=S17,20200601,10,1&cmdName=ABC"));
					}
				}).start();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送设置指令,加入同步锁，每次只能发送一个指令
	 * @param cmd
	 * @return  -2 链接断开
	 *          -1 发送失败
	 *          0 指令返回失败
	 *          1 指令放回成功
	 */
	public synchronized int sendCmd(String cmd){
		try {
			logger.info("发送数据：" + cmd);
			Thread.sleep(100);
			if(channel == null || !channel.isOpen() || !channel.isActive()){
				logger.info("通道异常,重连："+channel.isOpen()+","+channel.isActive());
				run();
				//return -2;
			}
			byte[] cmdBytes = EncodeUtil.HexString2Bytes(cmd,cmd.length()/2);
			ChannelFuture future = null;
			future = channel.writeAndFlush(Unpooled.copiedBuffer(cmdBytes)).sync();
			boolean sendResult = future.isSuccess();
			if(!sendResult){
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("",e);
			return -1;
		}
		return 1;
	}

	@PostConstruct
	public void run() {
		try {
			EventLoopGroup group = new NioEventLoopGroup();
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class);
			b.handler(new ChannelInitializer<Channel>() {
			    @Override
			    protected void initChannel(Channel ch) throws Exception {
			        ChannelPipeline pipeline = ch.pipeline();
			        pipeline.addLast("decoder",new BbDecoder());
			        pipeline.addLast("encoder",new BbEncoder());
			        pipeline.addLast("handler",new TcpClientHandler());
			    }
			});
			b.option(ChannelOption.SO_KEEPALIVE, true);

			channel = b.connect(gpsServerIp, gpsServerPort).sync().channel();

			//channel.closeFuture().sync();
			logger.info("NETTY 启动成功");
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("netty 启动失败",e);
		}
    }

	class TcpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msgObj) throws Exception {
		    byte[] msg = (byte[]) msgObj;
		    String result = EncodeUtil.getHexStr(msg);
		    logger.info("接收指令："+result);
			if(result.startsWith("1621444231")){
				logger.info("指令回应成功:"+EncodeUtil.toStringHex2(result));
			}else{
				logger.error("指令回应失败:"+EncodeUtil.toStringHex2(result));
			}
		}

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
			String result = datagramPacket.toString();
			System.out.println("接收指令1："+result);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			System.out.println("channel exception:"+cause);
			super.exceptionCaught(ctx, cause);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channelInactive:"+ctx);
			super.channelInactive(ctx);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			logger.info("NETTY 客户端 连接:"+ctx);
			super.channelInactive(ctx);
		}
		
		
	}

}

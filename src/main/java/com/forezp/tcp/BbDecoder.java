package com.forezp.tcp;

import com.forezp.utils.EncodeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @Title：ZWB GPS客户端协议
 * @Description：<类描述>
 *
 * @author ccq
 * @version 1.0
 */
public class BbDecoder extends MessageToMessageDecoder<ByteBuf> {
	protected static final Log log = LogFactory.getLog(BbDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> out) throws Exception {
		byte[] msgPackage = null;
		Channel channel =context.channel();
		try {
			ByteBuf wrongBuffer = null;

			byte[] srcByte = new byte[buffer.readableBytes()];
			buffer.getBytes(buffer.readerIndex(), srcByte);
			log.info(channel+"原始消息---->"+ EncodeUtil.getHexStr(srcByte));


			while(buffer.readableBytes()>0){
				//前缀 16 21 44 42
				if(buffer.getByte(buffer.readerIndex())!=0x16){
					//读取出前面不匹配的数据，丢弃
					if(wrongBuffer==null){
						wrongBuffer = Unpooled.buffer();
					}
					byte wrongByte =buffer.readByte();
					wrongBuffer.writeByte(wrongByte);
					//如果前缀有太多的不匹配的数据，跳出来
					if(wrongBuffer.readableBytes()>500){
						break;
					}
				}else{
					break;
				}
			}
			if(wrongBuffer!=null){
				if(log.isErrorEnabled()){
					byte[] wrongByte = new byte[wrongBuffer.readableBytes()];
					wrongBuffer.readBytes(wrongByte);
					log.error("######################"+channel+"  wrong byte :"+EncodeUtil.getHexStr(wrongByte));
				}
				wrongBuffer.clear();
				buffer.clear();
				channel.close();//并且关闭会话
				return;
			}

			if(buffer.readableBytes()<=1){
				return;
			}
			//前缀 16 21
			if(!(buffer.getByte(buffer.readerIndex()) == 0x16 && buffer.getByte(buffer.readerIndex() + 1) == 0x21)){
				return;
			}

			buffer.markReaderIndex();
			/**
			 * 获取到当前消息包的长度
			 */
			int msgPackLen = 0;
			for (int index = 1; index < buffer.readableBytes(); index++) {
				//后缀  18 26
				if(buffer.getByte(buffer.readerIndex()+index)==0x18 && buffer.getByte(buffer.readerIndex()+index+1)==0x26){
					msgPackLen = index+2;
					if(msgPackLen==2){//如果是两个相连的7E7E，那么立即读掉一个
						buffer.readByte();index =1;msgPackLen = 0;
						continue;
					}else{
						break;
					}
				}
			}
			if(msgPackLen == 0){
				buffer.resetReaderIndex();
				return;
			}
			//读取到整个消息包
			msgPackage = new byte[msgPackLen];
			buffer.readBytes(msgPackage);

			if(msgPackage.length>=5){//转义
				ByteBuf msgBuffer = Unpooled.buffer();
				for (int index = 0; index < msgPackage.length; index++) {
					if(index+1<msgPackage.length){
						msgBuffer.writeByte(msgPackage[index]);
					}else{
						msgBuffer.writeByte(msgPackage[index]);
					}
				}
				msgPackage = new byte[msgBuffer.readableBytes()];
				msgBuffer.readBytes(msgPackage);

				String hexStr = EncodeUtil.getHexStr(msgPackage);
				log.info(channel+"  转义后 rec msg :"+hexStr);

				out.add(msgPackage);

			}else{
				if(log.isErrorEnabled()){
					log.error("%%%%%%%%%%%%%%%%%%%%%"+channel+" rec msg 消息包过短:"+EncodeUtil.getHexStr(msgPackage));
				}
				return;
			}
		}catch (Exception e) {
			log.error("",e);
		}finally{
		}
		return;
	}

	public static void main(String[] args) {
		String hex = "7E02000038014762859136007700000000000C000301E625D4072B254B0015027B010E13082217393301040000AB2F030200007104020F0A05FE029F00FF0600000A2208B9A37E";
		System.out.println(hex.contains("14762859136"));
	}


}

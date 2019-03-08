package com.forezp.tcp;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * @Title：<类标题>
 * @Description：<类描述>
 *
 * @author ccq
 * @version 1.0
 */
public class BbEncoder extends MessageToMessageEncoder<byte[]> {
	protected static final Log log = LogFactory.getLog(BbEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext context, byte[] bytes, List<Object> list) throws Exception {
		context.writeAndFlush(bytes);
	}
}

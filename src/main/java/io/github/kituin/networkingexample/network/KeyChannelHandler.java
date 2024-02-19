package io.github.kituin.networkingexample.network;

import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.slf4j.Logger;

public class KeyChannelHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final KeyChannelHandler INSTANCE = new KeyChannelHandler ();
    public static KeyChannelHandler getInstance() {
        return INSTANCE;
    }
    // 这里是我们的客户端接收后处理部分
    public void clientHandle(final KeyPacket data, final PlayPayloadContext context) {
        // 这里的逻辑可以自己编写
        // 由于示例只发给服务端,所以不需要客户端接收
        LOGGER.info("客户端接收数据:PlayerName:{},按键:{}",
                data.playerName(),
                data.key());
    }
    // 这里是我们的服务端接收后处理部分
    public void serverHandle(final KeyPacket data, final PlayPayloadContext context) {
        // 这里的逻辑可以自己编写
        LOGGER.info("服务端接收数据:PlayerName:{},按键:{}",
                data.playerName(),
                data.key());
    }
}

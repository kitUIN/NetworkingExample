package io.github.kituin.networkingexample.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkUtil {

    // 发送到服务端
    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.SERVER.noArg().send(payload);
    }

    // 发送到客户端
    public static void sendToPlayer(CustomPacketPayload payload, ServerPlayer player) {
        player.connection.send(payload);
    }
}

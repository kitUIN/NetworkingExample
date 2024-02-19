package io.github.kituin.networkingexample.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static io.github.kituin.networkingexample.NetworkingExample.MOD_ID;


public record KeyPacket(String playerName, int key) implements CustomPacketPayload {
    // 这个是网络通道的ID,MOD_ID即为你的modid,key_packet是我自己定义的通道名称
    public static final ResourceLocation ID = new ResourceLocation(MOD_ID, "key_packet");
    // 这里需要实现从FriendlyByteBuf读取,如果没有你的类型的实现则需要自己写
    public KeyPacket(final FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readInt());
    }
    // 这里需要实现写入FriendlyByteBuf,如果没有你的类型的实现则需要自己写
    @Override
    public void write(final FriendlyByteBuf buffer) {
        buffer.writeUtf(playerName());
        buffer.writeInt(key());
    }
    // 获取通道ID
    @Override
    public ResourceLocation id() {
        return ID;
    }
}
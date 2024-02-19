package io.github.kituin.networkingexample;

import com.mojang.logging.LogUtils;
import io.github.kituin.networkingexample.network.KeyChannelHandler;
import io.github.kituin.networkingexample.network.KeyPacket;
import io.github.kituin.networkingexample.network.NetworkUtil;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NetworkingExample.MOD_ID)
public class NetworkingExample
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "networkingexample";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "networkingexample" namespace

    public NetworkingExample(IEventBus modEventBus)
    {
        NeoForge.EVENT_BUS.register(this);

        // 添加监听器,用于注册通道
        modEventBus.addListener(this::register);
    }

    public void register(final RegisterPayloadHandlerEvent event) {
        // 首先我们需要获取自己模组的数据包注册器
        final IPayloadRegistrar registrar = event.registrar(MOD_ID);
        registrar.play(KeyPacket.ID, KeyPacket::new, handler -> handler
                // 由于不需要客户端接收,所以我们不注册客户端接收处理器
                .client(KeyChannelHandler.getInstance()::clientHandle)
                // 注册服务端接收处理器
                .server(KeyChannelHandler.getInstance()::serverHandle));
        // 如果该通道不是必须,可以设置为可选,这样连接到未注册该通道的服务端与客户端仍将能够连接。
        // registrar.optional();
        // 通道可以设置版本号,服务端会与客户端检测版本号,若不一致则无法连接
        // registrar.versioned("1.0.2");
        LOGGER.info("注册自定义数据包");
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            NeoForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
        }

        public static void onKeyInput(InputEvent.Key event) {
            // 监听客户端按下按键
            NetworkUtil.sendToServer(new KeyPacket(Minecraft.getInstance().getUser().getName(),event.getKey()));
        }
    }
}

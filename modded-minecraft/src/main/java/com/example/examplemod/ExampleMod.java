package com.example.examplemod;


import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class ExampleMod {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get()
                                .getModEventBus()
                                .addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get()
                                .getModEventBus()
                                .addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get()
                                .getModEventBus()
                                .addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.safeCallWhenOn(Dist.CLIENT,
                () -> ClientSetup::adjustClient
        );

    }


    private void setup(final FMLCommonSetupEvent event) {

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                                       map(m -> m.messageSupplier()
                                                 .get()).
                                       collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");

        ClassLoader cl = ClassLoader.getSystemClassLoader();

        // TODO add a guard here, try to load the class?
        // Switch classloaders to the system classloader, rather than the transformer classloader Forge uses for mod loading
//        try {
//            Class<?> clazz = cl.loadClass("com.example.examplemod.Listener");
//            clazz.getDeclaredConstructor().newInstance();
//        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
        new Listener().start();
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        System.out.println("QUARKCRAFT - Client connected: " + player);
        player.displayClientMessage(Component.literal("Hello from the Quarkiverse!"), true);

        PlayerWrapper playerWrapper = new PlayerWrapper(player);

        // To find the class, we need to use the system classloader rather than the TransformingClassLoader Forge uses for mod loading
//        try {
//            ClassLoader cl = ClassLoader.getSystemClassLoader();
//            Class<?> clazz = cl.loadClass("com.example.examplemod.Endpoint");
//            // The signature needs to be an Object because Player would be in a different classloader
//            Method m = clazz.getMethod("setPlayer", Object.class);
//            // Rather inelegant static communication, but it does the job
//            m.invoke(null, playerWrapper);
//
//        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
        Endpoint.setPlayer(playerWrapper);

    }

}

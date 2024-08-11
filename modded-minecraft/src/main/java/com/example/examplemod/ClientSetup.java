package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;

public class ClientSetup {
    public static Object adjustClient() {
        // Keep the game running when tabbing away
        Minecraft.getInstance().options.pauseOnLostFocus = false;

        try {
            // Turn off music, which otherwise gets very annoying during demos and development
            Minecraft.getInstance().options.setSoundCategoryVolume(SoundSource.MUSIC, 0.0f);
        } catch (Exception e) {
            // This sometimes causes an exception, and we don't want to crash client or server, so be defensive
            e.printStackTrace();
        }

        return "";

    }
}

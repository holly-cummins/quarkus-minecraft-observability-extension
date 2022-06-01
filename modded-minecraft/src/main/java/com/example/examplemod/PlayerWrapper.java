package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * We need a wrapper here because we need something with a simple enough
 * signature that everything in it is in a JVM library, rather than loaded
 * by one of the fragmented classloaders. That allows us to find and
 * invoke the method by reflection.
 */
public class PlayerWrapper {
    private final Player player;

    PlayerWrapper(Player player) {
        this.player = player;
    }


    public void event(String message) {

        Vec3 pos = getPositionInFrontOfPlayer(3);

        player.displayClientMessage(new TextComponent(message), true);

        Level world = player.getCommandSenderWorld();

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(world);
        lightning.setPos(pos);
        lightning.setVisualOnly(true);
        world.addFreshEntity(lightning);

        Chicken chicken = EntityType.CHICKEN.create(world);
        chicken.setPos(pos);
        world.addFreshEntity(chicken);


    }

    public void say(String message) {
        // Use the chat interface for logs since it wraps more nicely
        TextComponent msg = new TextComponent(message);
        player.sendMessage(msg, player.getUUID());
    }


    public void explode(String message) {
        player.displayClientMessage(new TextComponent(message), true);
        Level level = player.getCommandSenderWorld();
        Chicken chicken = EntityType.CHICKEN.create(level);
        chicken.setPos(getPositionInFrontOfPlayer(6));
        level.addFreshEntity(chicken);

        Vec3 blockPos = chicken.getPosition(45);
        List<BlockPos> affectedPositions = new ArrayList();
        affectedPositions.add(new BlockPos(chicken.getX(), chicken.getY(),
                chicken.getZ()));
        Explosion explosion = new Explosion(level, chicken, chicken.getX(), chicken.getY(),
                chicken.getZ(), 6F, affectedPositions);

        explosion.explode();
    }


    @NotNull
    private Vec3 getPositionInFrontOfPlayer(int distance) {
        double x = player.getX() + distance * player.getLookAngle().x;
        double y = player.getY() + distance * player.getLookAngle().y;
        double z = player.getZ() + distance * player.getLookAngle().z;
        Vec3 pos = new Vec3(x, y, z);
        return pos;
    }


}

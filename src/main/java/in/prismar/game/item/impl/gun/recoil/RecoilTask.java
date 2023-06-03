package in.prismar.game.item.impl.gun.recoil;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import lombok.Setter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RecoilTask extends TimerTask {

    private static final Set<ClientboundPlayerPositionPacket.RelativeArgument> RELATIVE_FLAGS = new HashSet<>(Arrays.asList(
            ClientboundPlayerPositionPacket.RelativeArgument.X,
            ClientboundPlayerPositionPacket.RelativeArgument.Y,
            ClientboundPlayerPositionPacket.RelativeArgument.Z,
            ClientboundPlayerPositionPacket.RelativeArgument.X_ROT,
            ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT));

    private final Gun gun;
    private final Player player;

    private final GunPlayer gunPlayer;

    private final long repeat;

    private final long tickDuration;

    private final long iterations;

    @Setter
    private int tickCounter;

    public RecoilTask(Gun gun, Player player, long repeat) {
        this.gun = gun;
        this.player = player;
        this.gunPlayer = GunPlayer.of(player);
        this.repeat = repeat;
        this.tickDuration = 50;
        this.iterations = tickDuration / repeat;
    }

    @Override
    public void run() {
        if(tickCounter >= iterations || player.isDead() || !gunPlayer.isAiming() || !player.isOnline()) {
            cancel();
            gunPlayer.setRecoilTask(null);
            return;
        }
        ClientboundPlayerPositionPacket packet = new ClientboundPlayerPositionPacket(0, 0, 0, 0, -(float)(gun.getRecoil() / iterations), RELATIVE_FLAGS, 0, false);
        sendPacket(player, packet);
        tickCounter++;
    }


    protected void sendPacket(Player player, Packet<?> packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }
}

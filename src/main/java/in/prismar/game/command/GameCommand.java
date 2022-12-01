package in.prismar.game.command;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.library.common.tuple.Tuple;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.location.LocationUtil;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.nms.NmsUtil;
import org.bukkit.entity.*;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class GameCommand extends SpigotCommand<Player> {


    @Inject
    private Game game;

    public GameCommand() {
        super("game");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "game");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(game, player.getUniqueId().toString(),
                LocationUtil.getCenterOfBlock(player.getLocation().getBlock().getLocation().clone().add(0, 0.2, 0)));

        Tuple<String, String> tuple = NmsUtil.getTextureAndSignature(player);
        npc.setSkin(tuple.getFirst(), tuple.getSecond());
        npc.addCustomClickAction(NPC.Interact.ClickType.RIGHT_CLICK, (npc1, clicker) -> {
            
        });
        npc.setSleeping(true);
        npc.update();
        npc.forceUpdate();
        return true;
    }


}

package in.prismar.game.item;

import in.prismar.game.Game;
import in.prismar.game.item.holder.CustomItemHolder;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class CustomItemUpdater implements Runnable {

    private Game game;
    private CustomItemRegistry registry;

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            List<CustomItemHolder> holders = registry.scan(player);
            for(CustomItemHolder holder : holders) {
                holder.getItem().getEventBus().publish(player, game, holder, holder);
            }
        }
    }
}

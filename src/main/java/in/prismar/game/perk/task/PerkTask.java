package in.prismar.game.perk.task;

import in.prismar.game.perk.Perk;
import in.prismar.game.perk.PerkService;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class PerkTask implements Runnable{

    private final PerkService service;

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(service.hasPerkAndAllowedToUse(player, Perk.FASTHANDS)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 1, false, false));
            } else if(service.hasPerkAndAllowedToUse(player, Perk.FORTIFY)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1, false, false));
            }
        }
    }
}

package in.prismar.game.animation;

import in.prismar.game.Game;
import in.prismar.game.animation.repository.AnimationRepository;
import in.prismar.game.animation.repository.GsonAnimationRepository;
import in.prismar.game.animation.service.AnimationService;
import in.prismar.game.animation.service.LocalAnimationService;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service
public class AnimationFacade {

    public static final String LOCATION_A_KEY = "animationLocationA";
    public static final String LOCATION_B_KEY = "animationLocationB";
    public static final ItemStack WAND_ITEM = new ItemBuilder(Material.BLAZE_ROD).setName("§aAnimation Wand").glow().build();

    private final Game game;
    private final AnimationService service;
    private final AnimationRepository repository;

    public AnimationFacade(Game game) {
        this.game = game;
        this.repository = new GsonAnimationRepository(game);
        this.service = new LocalAnimationService(this);
    }
}

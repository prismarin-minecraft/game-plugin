package in.prismar.game.ffa.powerup;

import in.prismar.game.Game;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.ffa.powerup.impl.AmmoPowerUp;
import in.prismar.game.ffa.powerup.impl.DoubleJumpPowerUp;
import in.prismar.game.ffa.powerup.impl.HealPowerUp;
import in.prismar.game.ffa.powerup.impl.SpeedPowerUp;
import in.prismar.library.common.registry.LocalMapRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class PowerUpRegistry extends LocalMapRegistry<String, PowerUp> {

    @Inject
    private FFAFacade facade;

    public PowerUpRegistry(Game game) {
        super(false, false);

        register("heal", new HealPowerUp());
        register("speed", new SpeedPowerUp());
        register("doublejump", new DoubleJumpPowerUp());
        register("ammo", new AmmoPowerUp(game));
    }
}

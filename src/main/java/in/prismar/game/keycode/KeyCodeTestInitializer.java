package in.prismar.game.keycode;

import in.prismar.game.Game;
import in.prismar.game.animation.model.Animation;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.scheduler.Scheduler;
import org.bukkit.Location;
import org.bukkit.SoundCategory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class KeyCodeTestInitializer {

    public KeyCodeTestInitializer(Game game) {
        AtomicBoolean doorOpen = new AtomicBoolean(false);
        new KeyCode(game, "lab.entrance", (player, code) ->  {
            if (code.equals("05071") && !doorOpen.get()) {
                player.closeInventory();
                Animation animation = game.getAnimationFacade().getRepository().findById("lab.entrance");
                if(animation != null) {
                    game.getAnimationFacade().getService().play(animation, false);
                    Location location = animation.getCuboid().getCenter();
                    location.getWorld().playSound(location, "door.warning", SoundCategory.BLOCKS, 0.6f, 1f);
                    Scheduler.runDelayed(24, () -> {
                        location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
                    });
                    Scheduler.runDelayed(20 * 15, () -> {
                        game.getAnimationFacade().getService().play(animation, true);
                        Scheduler.runDelayed(24, () -> {
                            location.getWorld().playSound(location, "door.open", SoundCategory.BLOCKS, 0.75f, 1f);
                            doorOpen.set(false);
                        });
                    });
                    doorOpen.set(true);
                    return true;
                }
            }
            return false;
        });
    }
}

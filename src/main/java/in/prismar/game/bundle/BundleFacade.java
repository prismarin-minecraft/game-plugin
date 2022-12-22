package in.prismar.game.bundle;

import in.prismar.api.bundle.BundleProvider;
import in.prismar.game.Game;
import in.prismar.game.bundle.repository.BundleRepository;
import in.prismar.game.bundle.repository.FileBundleRepository;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class BundleFacade implements BundleProvider {

    private final BundleRepository repository;

    public BundleFacade(Game game) {
        this.repository = new FileBundleRepository(game.getDefaultDirectory());
    }

    @Override
    public void give(Player player, String bundle) {

    }
}

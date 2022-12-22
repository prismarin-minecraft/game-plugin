package in.prismar.game.bundle;

import in.prismar.game.Game;
import in.prismar.game.bundle.repository.BundleRepository;
import in.prismar.game.bundle.repository.FileBundleRepository;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class BundleFacade  {

    private final BundleRepository repository;

    public BundleFacade(Game game) {
        this.repository = new FileBundleRepository(game.getDefaultDirectory());
    }

}

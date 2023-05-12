package in.prismar.game.web.route;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class PostWebRoute<T> extends AbstractWebRoute<T> {
    public PostWebRoute(String path) {
        super(path);
    }
}

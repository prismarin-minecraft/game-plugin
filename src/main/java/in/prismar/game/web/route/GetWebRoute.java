package in.prismar.game.web.route;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public abstract class GetWebRoute<T> extends AbstractWebRoute<T> {
    public GetWebRoute(String path) {
        super(path);
    }
}

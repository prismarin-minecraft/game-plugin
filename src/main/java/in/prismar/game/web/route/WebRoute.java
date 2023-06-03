package in.prismar.game.web.route;

import io.javalin.http.Context;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface WebRoute<T> {

    String getPath();

    T onRoute(Context context);

    default String[] isSecured() {
        return new String[]{};
    }
}

package in.prismar.game.database;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class RedisContext {

    private RedissonClient client;

    public RedisContext() {
        connect();
    }

    public void connect() {
        ConfigStore store = PrismarinApi.getProvider(ConfigStore.class);
        Config config = new Config();

        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress(store.getProperty("redis", "address"))
                .setPassword(store.getProperty("redis", "password"));
        client = Redisson.create(config);
    }

    public void disconnect() {
        if(!client.isShutdown()) {
            client.shutdown();
        }
    }
}

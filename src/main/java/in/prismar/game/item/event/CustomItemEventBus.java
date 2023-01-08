package in.prismar.game.item.event;

import in.prismar.game.Game;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.holder.CustomItemHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CustomItemEventBus {

    private final CustomItem item;
    private Map<Class<?>, List<Method>> subscribers;

    public CustomItemEventBus(CustomItem item) {
        this.item = item;
        this.subscribers = new HashMap<>();
        scan();
    }

    public void publish(Player player, Game game, CustomItemHolder holder,  Object event) {
        if(subscribers.containsKey(event.getClass())) {
            List<Method> methods = subscribers.get(event.getClass());
            for(Method method : methods) {
                try {
                    method.invoke(item, player, game, holder, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void scan() {
        Class<?> clazz = item.getClass();
        for(Method method : clazz.getMethods()) {
            if(method.isAnnotationPresent(CustomItemEvent.class)) {
                Parameter parameter = method.getParameters()[3];
                Class<?> type = parameter.getType();
                if(!subscribers.containsKey(type)) {
                    subscribers.put(type, new ArrayList<>());
                }
                subscribers.get(type).add(method);
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public class BackroomItemEventArgs<T> {

        private Player player;
        private Game game;
        private T event;
    }
}

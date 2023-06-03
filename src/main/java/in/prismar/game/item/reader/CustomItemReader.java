package in.prismar.game.item.reader;

import in.prismar.game.Game;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.reader.impl.ConfigGunReaderSource;
import in.prismar.game.item.reader.impl.ConfigMeleeReaderSource;
import in.prismar.game.item.reader.impl.FileGunReaderSource;
import in.prismar.game.item.reader.impl.FileMeleeReaderSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CustomItemReader {

    private List<CustomItemReaderSource> sources;

    public CustomItemReader(Game game) {
        this.sources = new ArrayList<>();

        //this.sources.add(new FileGunReaderSource());
        //this.sources.add(new FileMeleeReaderSource());
        this.sources.add(new ConfigGunReaderSource(game));
        this.sources.add(new ConfigMeleeReaderSource(game));
    }

    public void load() {
        for(CustomItemReaderSource source : sources) {
            source.load();
        }
    }

    public void apply(CustomItemRegistry registry) {
        try {
            for(CustomItemReaderSource source : sources) {
                for(CustomItem item : source.read(registry)) {
                    registry.register(item);
                }
            }
        } catch (Exception exception) {
            System.out.println("There was an error while trying to read from custom item source: " + exception.getMessage());
        }

    }


}

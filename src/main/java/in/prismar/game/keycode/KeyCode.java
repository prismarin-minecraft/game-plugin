package in.prismar.game.keycode;

import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class KeyCode extends Frame {

    private static final int[] CODES_SLOTS = {
            21, 22, 23,
            30, 31, 32,
            39, 40, 41,
                49
    };

    private static final int[] TERMINAL_SLOTS = {2, 3, 4, 5, 6};
    private final KeyCodeCallback callback;

    private int[] code = new int[]{-1, -1, -1, -1, -1};

    public KeyCode(KeyCodeCallback callback) {
        super("§f七七七七七七七七从", 6);
        this.callback = callback;

        for (int i = 0; i < CODES_SLOTS.length; i++) {
            int slot = CODES_SLOTS[i];
            int number = slot == 49 ? 0 : i+1;
            addButton(CODES_SLOTS[i], new ItemBuilder(Material.MAP).setCustomModelData(105).setName("§f" + number).allFlags().build(), (ClickFrameButtonEvent) (player, event) -> {
                if(fillNextNumber(number)) {
                    playPressSound(player);
                    update();
                }
            });
        }

        addButton(48, new ItemBuilder(Material.MAP).setCustomModelData(105).setName("§cClear").allFlags().build(), (ClickFrameButtonEvent) (player, event) -> {
            if(clear()) {
                playPressSound(player);
                update();
            }
        });

        addButton(50, new ItemBuilder(Material.MAP).setCustomModelData(105).setName("§aSubmit").allFlags().build(), (ClickFrameButtonEvent) (player, event) -> {
            if(isFull()) {
                StringBuilder rawNumber = new StringBuilder();
                for (int code : this.code) {
                    rawNumber.append(code);
                }
                if(callback.onSubmit(player, rawNumber.toString())) {
                    playSuccessSound(player);
                } else {
                    clear();
                    update();
                    playFailSound(player);
                }
            }
        });

        for(int slot : TERMINAL_SLOTS) {
            addButton(slot, new ItemBuilder(Material.MAP).setCustomModelData(105).setName(" ").allFlags().build());
        }

        build();
    }

    public void update() {
        for (int i = 0; i < TERMINAL_SLOTS.length; i++) {
            int slot = TERMINAL_SLOTS[i];
            int number = code[i];
            if(number != -1) {
                updateButton(slot, new ItemBuilder(Material.REDSTONE).setCustomModelData(number == 0 ? 10 : number).setName("§f" + number).allFlags().build());
            } else {
                updateButton(slot, new ItemBuilder(Material.MAP).setCustomModelData(105).setName(" ").allFlags().build());
            }
        }
    }

    public boolean fillNextNumber(int number) {
        for (int i = 0; i < code.length; i++) {
            int current = code[i];
            if(current == -1) {
                code[i] = number;
                return true;
            }
        }

        return false;
    }

    public boolean clear() {
        long amountMinusNumbers = Arrays.stream(this.code).filter(number -> number == -1).count();
        if(amountMinusNumbers == code.length) {
            return false;
        }
        this.code = new int[]{-1, -1, -1, -1, -1};
        return true;
    }

    public boolean isFull() {
        return code[code.length-1] != -1;
    }

    private void playSuccessSound(Player player) {
        player.playSound(player.getLocation(), "keypad.success", 1f, 1f);
    }

    private void playFailSound(Player player) {
        player.playSound(player.getLocation(), "keypad.fail", 1f, 1f);
    }

    private void playPressSound(Player player) {
        player.playSound(player.getLocation(), "keypad.press", 1f, 1f);
    }

    private void playOpenSound(Player player) {
        player.playSound(player.getLocation(), "keypad.open", 1f, 1f);
    }

    @Override
    public Frame openInventory(Player player) {
        playOpenSound(player);
        return super.openInventory(player);
    }

    public interface KeyCodeCallback {
        boolean onSubmit(Player player, String code);
    }
}

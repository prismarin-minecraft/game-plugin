package in.prismar.game.battleroyale.frame;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleQueueEntry;
import in.prismar.library.spigot.chat.ChatInput;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BattleRoyaleQueueFrame extends Frame {

    private static final ItemStack INVITE_ITEM = new ItemBuilder(Material.BOOK).setCustomModelData(4).setName("§aInvite").allFlags().build();
    private static final ItemStack LEAVE_ITEM = new ItemBuilder(Material.OAK_DOOR).setName("§cLeave").allFlags().build();

    public BattleRoyaleQueueFrame(BattleRoyaleService service, BattleRoyaleGame game, BattleRoyaleQueueEntry entry) {
        super("§aBR Queue", 3);

        int amount = game.getProperties().getTeamSize() - entry.getPlayers().size();

        int slot = 10;
        for(Player player : entry.getPlayers().values()) {
            addButton(slot, new SkullBuilder(player.getName()).setName("§6" + player.getName()).allFlags().build());
            slot++;
        }

        for (int i = 0; i < amount; i++) {
            addButton(slot, INVITE_ITEM, (ClickFrameButtonEvent) (player, event) -> {
                player.closeInventory();
                player.sendMessage(PrismarinConstants.PREFIX + "§7Please type the §6player's name §7you want in the chat§8:");
                player.sendMessage(PrismarinConstants.PREFIX + "§7Type §8'§ccancel§8' §7to cancel the chat input");
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                ChatInput input = new ChatInput(player, message -> {
                    message = message.replace(" ", "");
                    player.performCommand("broyale queue invite " + message);
                    return true;
                });
                input.setCancelMessage(PrismarinConstants.PREFIX + "§cYou canceled the chat input");
            });
            slot++;
        }

        addButton(26, LEAVE_ITEM, (ClickFrameButtonEvent) (player, event) -> {
            player.performCommand("broyale queue leave");
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
        });
    }
}

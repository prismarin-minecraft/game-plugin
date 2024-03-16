package in.prismar.game.item.impl.backpack;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BackpackItem extends CustomItem {

    private final int rows;
    public BackpackItem(String id, Material material, String displayName, int rows) {
        super(id, material, displayName);
        this.rows = rows;
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        String id = PersistentItemDataUtil.getString(game, event.getItem(), "storage_id");
        if(id.isBlank()) {
            id = UUID.randomUUID().toString();
            PersistentItemDataUtil.setString(game, event.getItem(), "storage_id", id);
        }
        game.getStorageService().openStorageInventory(player, id, getDisplayName(), rows);
        player.playSound(player.getLocation(), "misc.backpack", 0.35f, 1f);


    }

    @CustomItemEvent
    public void onClick(Player player, Game game, CustomItemHolder holder, InventoryClickEvent event) {
        if(player.getOpenInventory() != null) {
            if(player.getOpenInventory().getTitle().contains("Backpack") || player.getOpenInventory().getTitle().contains("Vault")) {
                ItemStack stack;
                if(event.getClick() == ClickType.NUMBER_KEY) {
                    stack = player.getInventory().getItem(event.getHotbarButton());
                } else {
                    stack = event.getCurrentItem();
                }
                CustomItem customItem = game.getItemRegistry().getItemByStack(stack);
                if(customItem instanceof BackpackItem) {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
    
}

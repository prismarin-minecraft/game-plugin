package in.prismar.game.quarry.frame;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.quarry.QuarryService;
import in.prismar.game.quarry.model.Quarry;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class QuarryOutputFrame extends Frame {

    private final Quarry quarry;

    public QuarryOutputFrame(QuarryService service, Quarry quarry) {
        super(quarry.getDisplayName(), 3);
        this.quarry = quarry;

        fill();

        addButton(13, new ItemStack(Material.AIR), (ClickFrameButtonEvent) (player, event) -> {
            if(quarry.getOutputAmount() <= 0) {
                return;
            }
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.35f, 1f);

            int amount = quarry.getOutputAmount() >= 64 ? 64 : quarry.getOutputAmount();
            ItemStack output = quarry.getOutput().getItem().clone();
            output.setAmount(amount);
            ItemUtil.giveItem(player, output);

            quarry.setOutputAmount(quarry.getOutputAmount() - amount);
            service.save(quarry);
            update();
        });

        build();
    }

    public void update() {
        updateButton(13, quarry.getOutputAmount() >= 1 ? new ItemBuilder(quarry.getOutput().getItem()).setAmount(quarry.getOutputAmount()).addLore(
                "§c",
                PrismarinConstants.ARROW_RIGHT + " §7Amount§8: §e" + quarry.getOutputAmount(),
                "§c",
                "§eLeft click §7to takeout"
        ).build() :
                new ItemStack(Material.AIR));
    }
}

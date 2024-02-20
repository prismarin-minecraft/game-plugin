package in.prismar.game.quarry.frame;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.missions.MissionWrapper;
import in.prismar.game.quarry.QuarryService;
import in.prismar.game.quarry.model.Quarry;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.event.FrameClickEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class QuarryInputFrame extends Frame {

    private final Quarry quarry;

    public QuarryInputFrame(QuarryService service, Quarry quarry) {
        super(quarry.getDisplayName(), 3);
        this.quarry = quarry;

        fill();

        addButton(11, new ItemStack(Material.AIR));

        final String arrow = PrismarinConstants.ARROW_RIGHT + " §7";

        addButton(15, new ItemBuilder(Material.PAPER).setName("§eInformation")
                .addLore("§c",
                        arrow + "Produce per second§8: §e" + quarry.getProducePerSecond(),
                        arrow + "Fuel type§8: §8Diesel Fuel",
                        arrow + "Fuel duration per barrel§8: §e" + TimeUtil.convertToTwoDigits(service.getNextFuelConsumption()),
                        "")
                .build());

        getEventBus().subscribe(FrameClickEvent.class, event -> {
            if (event.getEvent().getClickedInventory() != null && event.getEvent().getCurrentItem() != null) {
                if (event.getEvent().getClickedInventory().getType() == InventoryType.PLAYER) {
                    event.getEvent().setCancelled(true);
                    event.getEvent().setResult(Event.Result.DENY);
                    Player player = event.getPlayer();
                    ItemStack stack = event.getEvent().getCurrentItem();
                    if(stack.getType() != Material.FEATHER) {
                        return;
                    }
                    if(!stack.hasItemMeta()) {
                        return;
                    }
                    if(!stack.getItemMeta().hasDisplayName()) {
                        return;
                    }
                    if(!stack.getItemMeta().getDisplayName().equals("§8Diesel Fuel")) {
                        return;
                    }
                    if(quarry.getInputAmount() <= 0) {
                        service.announceQuarry(quarry);
                    }

                    service.fillQuarry(quarry, stack.getAmount());
                    event.getEvent().setCurrentItem(new ItemStack(Material.AIR));
                    player.updateInventory();
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1f);
                    MissionWrapper.progress(player, "activate_quarry", 1, 1);
                    update();
                }
            }
        });

        build();


        update();
    }

    public void update() {
        final String arrow = PrismarinConstants.ARROW_RIGHT + " §7";
        updateButton(11, quarry.getInputAmount() >= 1 ? new ItemBuilder(Material.FEATHER).setCustomModelData(10).setName("§8Diesel Fuel")
                .setAmount(quarry.getInputAmount()).addLore("§c", arrow + "Amount§8: §e" + quarry.getInputAmount(), "§c ").build()
                :
                new ItemBuilder(Material.IRON_BARS).setName("§7No fuel").addLore("§c",
                        arrow + "§7To insert fuel into this quarry",
                        arrow + "§7click on §8Diesel Fuel §7in your inventory",
                        "§c",
                        "§cYou can not takeout the fuel after you inserted it"
                ).build()
        );
    }
}

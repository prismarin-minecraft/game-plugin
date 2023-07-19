package in.prismar.game.battleroyale.arena.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.template.Pager;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ListDropLocationsSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleArenaService service;

    public ListDropLocationsSubCommand(BattleRoyaleArenaService service) {
        super("listDropLocations");
        setAliases("listDropLocation");
        setDescription("List all drop locations");
        setUsage("<id>");
        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis arena does not exists");
                return true;
            }
            BattleRoyaleArena arena = service.getRepository().findById(id);
            Pager pager = new Pager(arena.getDisplayName(), 6);
            pager.fill();
            for(Location location : arena.getDrops()) {
                pager.addItem(new ItemBuilder(Material.PAPER).setName("§a" + location.getBlockX() + " §8| §a" + location.getBlockY() + " §8| §a" + location.getBlockZ())
                                .addLore("§c", "§aLeft click §7to teleport", "§aRight click §7to delete")
                        .build(), (ClickFrameButtonEvent) (player1, event) -> {
                    if(event.isRightClick()) {
                        arena.getDrops().remove(location);
                        service.getRepository().save(arena);
                        player.performCommand("bra listDropLocations " + arena.getId());
                        return;
                    }
                    player.teleport(location);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1f);
                });
            }
            pager.build();
            pager.openInventory(player, Sound.BLOCK_CHEST_CLOSE, 0.45f);
            return true;
        }
        return false;
    }
}

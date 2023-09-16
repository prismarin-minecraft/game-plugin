package in.prismar.game.item.impl.tea;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.node.ConfigNode;
import in.prismar.api.configuration.node.ConfigNodeProvider;
import in.prismar.api.item.TeaType;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.common.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class TeaItem extends CustomItem {

    private final UserProvider<User> userProvider;
    private final ScoreboardProvider scoreboardProvider;

    protected final TeaType type;

    protected final int tier;


    public TeaItem(TeaType type, String displayName, int customModelData, int tier) {
        super(type.name().toLowerCase() + "tea" + tier, Material.POTION, displayName);
        this.tier = tier;
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.scoreboardProvider = PrismarinApi.getProvider(ScoreboardProvider.class);
        this.type = type;
        setCustomModelData(customModelData);


        allFlags();
    }

    public void buildLore(Game game) {
        final String arrow = PrismarinConstants.ARROW_RIGHT + " §7";
        final long duration = game.getConfigNodeFile().getLong("Tea." + type.name() + ".Tier_"+tier+".Duration", 10000);
        final String multiplier = tier == 1 ? "§a2x" : tier == 2 ? "§a2x §8(§a+1 50% Chance§8)" : "§a3x";
        addLore("§c ",
                arrow + "Duration§8: §a" + TimeUtil.convertToTwoDigits(duration / 1000),
                arrow + "Multiplier§8: " + multiplier,
                "§c "
        );
    }

    @CustomItemEvent
    public void onConsumeEvent(Player player, Game game, CustomItemHolder holder, PlayerItemConsumeEvent event) {
        if(holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND && holder.getHoldingType() != CustomItemHoldingType.LEFT_HAND) {
            event.setCancelled(true);
            return;
        }
        User user = userProvider.getUserByUUID(player.getUniqueId());
        final String id = type.name().toLowerCase() + "tea";
        if(!user.isTimestampAvailable(id)) {
            event.setCancelled(true);

            player.sendMessage(PrismarinConstants.PREFIX + "§cYou have already consumed this type of tea");
            return;
        }
        event.setCancelled(true);
        player.getEquipment().setItem(event.getHand(), new ItemStack(Material.AIR));
        final long duration = game.getConfigNodeFile().getLong("Tea." + type.name() + ".Tier_"+tier+".Duration", 10000);
        user.setTimestamp(id, System.currentTimeMillis() + duration);
        user.getSeasonData().getAttachments().put(id, tier);
        userProvider.saveAsync(user, true);
        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_DRINK, 0.7F, 1f);
        scoreboardProvider.recreateSidebar(player);
    }


}

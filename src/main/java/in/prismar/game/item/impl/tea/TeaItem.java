package in.prismar.game.item.impl.tea;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.SeasonData;
import in.prismar.api.user.data.UserData;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class TeaItem extends CustomItem {

    private final UserProvider<User> userProvider;

    protected final TeaType type;

    protected final int tier;


    public TeaItem(TeaType type, int tier) {
        super(type.name().toLowerCase() + "tea" + tier, Material.POTION, type.getDisplayName());
        this.tier = tier;
        setCustomModelData(type.getCustomModelData());
        allFlags();

        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.type = type;
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
        final long duration = game.getConfigNodeFile().getLong("Tea." + type.name() + ".Tier_"+tier+".Duration", 10000);
        user.setTimestamp(id, System.currentTimeMillis() + duration);
        userProvider.saveAsync(user, true);

        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_DRINK, 0.7F, 1f);
    }
}

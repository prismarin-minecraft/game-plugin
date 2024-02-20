package in.prismar.game.missions.impl;

import in.prismar.api.PrismarinApi;
import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Kill100ZombiesMission extends AbstractMission {

    private final CustomItemRegistry itemRegistry;

    public Kill100ZombiesMission(CustomItemRegistry itemRegistry) {
        super("kill100zombies", MissionType.WEEKLY, 1, Material.ZOMBIE_HEAD, "§cZombie slayer V2");
        this.itemRegistry = itemRegistry;
    }

    @Override
    public String getDescription(int stage) {
        return "§cKill 100 Zombies";
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§a+10.000$",
                "§2Gas Mask"
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);
        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.getSeasonData().setBalance(user.getSeasonData().getBalance() + 10000);

        ItemUtil.giveItem(player, itemRegistry.createItem("GasMask"));
    }

    @Override
    public long getMaxProgress(int stage) {
        return 100;
    }
}

package in.prismar.game.missions;

import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillZaku10Mission extends AbstractMission {

    private final CustomItemRegistry customItemRegistry;

    public KillZaku10Mission(CustomItemRegistry customItemRegistry) {
        super("killzaku10", MissionType.WEEKLY, 1, Material.REDSTONE, "§cRailgun?????");
        this.customItemRegistry = customItemRegistry;
    }

    @Override
    public String getDescription(int stage) {
        return "§cKill Zaku 10x";
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§cRailgun",
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        ItemUtil.giveItem(player, customItemRegistry.getItemById("railgun").build());


    }

    @Override
    public long getMaxProgress(int stage) {
        return 10;
    }
}

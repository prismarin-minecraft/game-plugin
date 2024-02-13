package in.prismar.game.missions;

import in.prismar.api.mission.AbstractMission;
import in.prismar.api.mission.MissionType;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KillAnderson10Mission extends AbstractMission {

    private final CustomItemRegistry customItemRegistry;

    public KillAnderson10Mission(CustomItemRegistry customItemRegistry) {
        super("killanderson10", MissionType.SEASONAL, 1, Material.WITHER_SKELETON_SKULL, "§cEndgame grinder");
        this.customItemRegistry = customItemRegistry;
    }

    @Override
    public String getDescription(int stage) {
        return "§cKill Anderson 10x";
    }

    @Override
    public String[] getRewards() {
        return new String[]{
                "§eJuggernaut Helmet",
                "§eJuggernaut Chestplate",
                "§eJuggernaut Leggings",
                "§eJuggernaut Boots"
        };
    }

    @Override
    public void onReceiveReward(Player player) {
        ItemUtil.giveItem(player, customItemRegistry.getItemById("JuggernautHelmet").build());
        ItemUtil.giveItem(player, customItemRegistry.getItemById("JuggernautChestplate").build());
        ItemUtil.giveItem(player, customItemRegistry.getItemById("JuggernautLeggings").build());
        ItemUtil.giveItem(player, customItemRegistry.getItemById("JuggernautBoots").build());


    }

    @Override
    public long getMaxProgress(int stage) {
        return 10;
    }
}

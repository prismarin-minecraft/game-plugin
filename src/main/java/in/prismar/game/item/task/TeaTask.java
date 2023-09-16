package in.prismar.game.item.task;

import in.prismar.api.item.TeaType;
import in.prismar.api.user.User;
import in.prismar.game.item.CustomItemTeaService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeaTask implements Runnable{

    private final CustomItemTeaService teaService;

    public TeaTask(CustomItemTeaService teaService) {
        this.teaService = teaService;
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            User user = teaService.getUserProvider().getUserByUUID(player.getUniqueId());
            for(TeaType type : TeaType.values()) {
                final String id = type.name().toLowerCase() + "tea";
                if(user.getSeasonData().getTimestamps().containsKey(id)) {
                    if(user.isTimestampAvailable(id)) {
                        user.getSeasonData().getTimestamps().remove(id);
                        Bukkit.getScheduler().runTask(teaService.getGame(), () -> {
                            teaService.getScoreboardProvider().recreateSidebar(player);
                        });
                    }
                }
            }
        }
    }
}

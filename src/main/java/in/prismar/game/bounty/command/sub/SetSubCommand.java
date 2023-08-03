package in.prismar.game.bounty.command.sub;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.bounty.BountyService;
import in.prismar.game.bounty.model.Bounty;
import in.prismar.game.bounty.model.BountySupplier;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Consumer;

public class SetSubCommand extends HelpSubCommand<Player> {

    private final BountyService service;

    public SetSubCommand(BountyService service) {
        super("set");
        this.service = service;

        setUsage("<player> <money>");
        setDescription("Set a bounty");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            Player target = arguments.getOnlinePlayer(1);
            /*if(player.getName().equals(target.getName())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou can't put a bounty on yourself");
                return true;
            }*/
            double money = arguments.getDouble(2);
            if(money < service.getMinMoney()) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou need to have a minimum of " + NumberFormatter.formatDoubleToThousands(service.getMinMoney()) + "$");
                return true;
            }
            User user = service.getUserProvider().getUserByUUID(player.getUniqueId());
            if(user.getSeasonData().getBalance() < money) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou don't have enough money");
                return true;
            }
            final String uuid = target.getUniqueId().toString();
            Bounty bounty = service.getRepository().findById(uuid);

            user.getSeasonData().setBalance(user.getSeasonData().getBalance() - money);
            service.getUserProvider().saveAsync(user, true);
            final String arrow = PrismarinConstants.ARROW_RIGHT + " §7";
            if(bounty != null) {
                Optional<BountySupplier> optional = bounty.getSupplier(player.getUniqueId().toString());
                if(optional.isPresent()) {
                    service.increase(target, optional.get(), money);
                } else {
                    service.increase(target, new BountySupplier(player, money), money);
                }
                Bukkit.broadcastMessage(PrismarinConstants.BORDER);
                Bukkit.broadcastMessage("§c");
                Bukkit.broadcastMessage(arrow + "The bounty of §c" + target.getName() + " §7has been increased");
                Bukkit.broadcastMessage(arrow + "by §c" + player.getName() + " §7for §6" + NumberFormatter.formatDoubleToThousands(money) + "$");
                Bukkit.broadcastMessage(arrow + "The current sum is §6" + NumberFormatter.formatDoubleToThousands(bounty.getMoney()) + "$");
                Bukkit.broadcastMessage(arrow + "The ranking of this bounty is §c#" + service.getRank(bounty));
                Bukkit.broadcastMessage("§c");
                Bukkit.broadcastMessage(PrismarinConstants.BORDER);
            } else {
                service.create(target, new BountySupplier(player, money)).thenAccept(bounty1 -> {
                    Bukkit.broadcastMessage(PrismarinConstants.BORDER);
                    Bukkit.broadcastMessage("§c");
                    Bukkit.broadcastMessage(arrow + "Bounty has been set on §c" + target.getName());
                    Bukkit.broadcastMessage(arrow + "by §c" + player.getName() + " §7for §6" + NumberFormatter.formatDoubleToThousands(money) + "$");
                    Bukkit.broadcastMessage(arrow + "The ranking of this bounty is §c#" + service.getRank(bounty1));
                    Bukkit.broadcastMessage("§c");
                    Bukkit.broadcastMessage(PrismarinConstants.BORDER);
                });
            }
            return true;
        }
        return false;
    }
}

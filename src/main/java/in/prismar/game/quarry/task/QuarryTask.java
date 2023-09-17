package in.prismar.game.quarry.task;

import in.prismar.game.quarry.QuarryService;
import in.prismar.game.quarry.model.Quarry;
import in.prismar.library.common.math.MathUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

@AllArgsConstructor
public class QuarryTask implements Runnable {

    private final QuarryService service;


    @Override
    public void run() {
        for(Quarry quarry : service.getRepository().findAll()) {
            if(quarry.getInputAmount() >= 1) {
                if(quarry.getCurrentFuelConsumptionSeconds() <= 0) {
                    quarry.setInputAmount(quarry.getInputAmount() - 1);
                    quarry.setCurrentFuelConsumptionSeconds(service.getNextFuelConsumption());
                } else {
                    quarry.setOutputAmount(quarry.getOutputAmount() + quarry.getProducePerSecond());
                    quarry.setCurrentFuelConsumptionSeconds(quarry.getCurrentFuelConsumptionSeconds() - 1);
                }
                quarry.getInputFrame().update();
                quarry.getOutputFrame().update();
                Bukkit.getScheduler().runTask(service.getGame(), () -> {
                   quarry.getInputLocation().getWorld().playSound(quarry.getInputLocation(), Sound.BLOCK_METAL_BREAK, 5f, MathUtil.random(0.35f, 1f));
                   quarry.getInputLocation().getWorld().playSound(quarry.getOutputLocation(), Sound.BLOCK_PISTON_CONTRACT, 5f, MathUtil.random(0.35f, 1f));
                });
                service.save(quarry);
            }
        }
    }
}

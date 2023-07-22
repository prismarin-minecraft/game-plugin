package in.prismar.game.advancedgui;

import in.prismar.library.common.tuple.Tuple;
import me.leoko.advancedgui.utils.Layout;
import me.leoko.advancedgui.utils.LayoutExtension;
import me.leoko.advancedgui.utils.components.*;
import me.leoko.advancedgui.utils.events.LayoutLoadEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BountyLayoutExtension implements LayoutExtension {
    public static final String LAYOUT_NAME = "Test";

    @Override
    @EventHandler
    public void onLayoutLoad(LayoutLoadEvent event) {
        Layout layout = event.getLayout();
        if (layout.getName().equals(LAYOUT_NAME)) {
            GroupComponent component = layout.getTemplateComponentTree();
            GroupComponent base = component.locate("gzTMp8kK", GroupComponent.class);


            ListComponent<Tuple<String, Integer>> list = new ListComponent<>(UUID.randomUUID().toString(), null, false, event.getLayout().getDefaultInteraction(), interaction -> {
                List<Tuple<String, Integer>> data = new ArrayList<>();
                Random random = new Random();
                boolean maga = true;
                data.add(new Tuple<>("Boss", Integer.MAX_VALUE));
                for (int i = 0; i < 20; i++) {
                    data.add(new Tuple<>(maga ? "ReaperMaga" : "tsuuukiii", random.nextInt(100000)));
                    maga = !maga;
                }
                data.sort((o1, o2) -> Integer.compare(o2.getSecond(), o1.getSecond()));
                return data;
            }, (interaction, index, item) -> {
                GroupComponent groupComponent = base.clone(interaction);
                String name = item.getFirst();

                int balance = item.getSecond();
                TextComponent nameComponent = groupComponent.locate("I9fiuuq5", TextComponent.class);
                nameComponent.setText(name);

                TextComponent moneyComponent = groupComponent.locate("MxD7uo2S", TextComponent.class);
                moneyComponent.setText(balance + " $");
                groupComponent.setHidden(false);



                RectComponent background = groupComponent.locate("L9vAftlb", RectComponent.class);


                int next = (index + 1) * 50;

                RemoteImageComponent imageComponent = new RemoteImageComponent(UUID.randomUUID().toString(), null, false, interaction,
                        "https://minotar.net/helm/"+name+"/32.png", null, nameComponent.getX() - 35, (nameComponent.getY() + next) - 20, 27, 27, false);
                groupComponent.getComponents().add(0, imageComponent);

                background.setY(background.getY() + next);
                nameComponent.setY(nameComponent.getY() + next);
                moneyComponent.setY(moneyComponent.getY() + next);


                return groupComponent;
            }, 5, 1);
            component.locate("1UEIyjsS", DummyComponent.class).setComponent(list);

            component.locate("MROoX9uP", HoverComponent.class).setClickAction((interaction, player, primaryTrigger) -> {
                ListComponent<Tuple<String, Integer>> listComponent = list.locateOn(interaction);
                listComponent.nextPage();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
            });
            component.locate("TFy9fGcZ", HoverComponent.class).setClickAction((interaction, player, primaryTrigger) -> {
                ListComponent<Tuple<String, Integer>> listComponent = list.locateOn(interaction);
                if(listComponent.getPage() <= 0) {
                    return;
                }
                listComponent.previousPage();
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
            });
        }
    }
}
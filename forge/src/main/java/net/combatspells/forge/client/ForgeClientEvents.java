package net.combatspells.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.combatspells.CombatSpells;
import net.combatspells.client.gui.HudRenderHelper;

@Mod.EventBusSubscriber(modid = CombatSpells.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEvents {
    @SubscribeEvent
    public static void onRenderHud(RenderGuiEvent.Post event){
        HudRenderHelper.render(event.getPoseStack(), event.getPartialTick());
    }
}

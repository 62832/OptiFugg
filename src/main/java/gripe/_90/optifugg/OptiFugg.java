package gripe._90.optifugg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(OptiFugg.MODID)
public class OptiFugg {
    static final String MODID = "optifugg";

    private static final Logger LOGGER = LoggerFactory.getLogger("OptiFugg");
    private static boolean logged = false;

    public OptiFugg() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OptiFuggConfig.CONFIG_SPEC);

        MinecraftForge.EVENT_BUS.addListener((ScreenEvent.Opening event) -> {
            if (logged || !(event.getScreen() instanceof TitleScreen)) return;

            if (hasOptifine()) {
                if (OptiFuggConfig.CONFIG.crashOnStartup.get()) {
                    throw new RuntimeException("OptiFine is forbidden by one or more mods on this client. Please remove OptiFine before proceeding.");
                } else {
                    LOGGER.warn("OptiFine installation detected. " + (OptiFuggConfig.CONFIG.allowToProceed.get()
                            ? "This will likely cause issues with any heavily-modded games."
                            : "Please remove OptiFine before proceeding."));

                    if (OptiFuggConfig.CONFIG.showScreen.get()) {
                        event.setNewScreen(new OptiFuggWarningScreen());
                    }
                }
            } else {
                LOGGER.info("No OptiFine installation detected.");
            }

            logged = true;
        });
    }

    private static boolean hasOptifine() {
        try {
            Class.forName("optifine.OptiFineTransformationService");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

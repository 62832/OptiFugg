package gripe._90.optifugg;

import java.util.Objects;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import gripe._90.optifugg.mixin.WarningScreenAccessor;

@Mod.EventBusSubscriber(modid = OptiFugg.MODID, value = Dist.CLIENT)
class OptiFuggWarningScreen extends WarningScreen {
    private static final MutableComponent HEADER = Component.translatable("header.optifugg").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD);
    private static final Component MESSAGE = Component.translatable("message.optifugg");
    private static final MutableComponent NARRATED_TEXT = HEADER.copy().append("\n").append(MESSAGE);

    private static final Component OPEN_MODS_FOLDER = Component.translatable("label.optifugg.mods_folder");
    private static final Component OPEN_ALTERNATIVES_LINK = Component.translatable("label.optifugg.alternatives");

    private static boolean logged = false;

    protected OptiFuggWarningScreen() {
        super(HEADER, MESSAGE, NARRATED_TEXT);
    }

    @SubscribeEvent
    public static void onEvent(ScreenEvent.Opening event) {
        if (logged || !(event.getScreen() instanceof TitleScreen)) return;

        if (OptiFugg.hasOptifine()) {
            if (OptiFuggConfig.CONFIG.crashOnStartup.get()) {
                throw new RuntimeException("OptiFine is forbidden by one or more mods on this client. Please remove OptiFine before proceeding.");
            } else {
                OptiFugg.LOGGER.warn("OptiFine installation detected. " + (OptiFuggConfig.CONFIG.allowToProceed.get()
                        ? "This will likely cause issues with any heavily-modded games."
                        : "Please remove OptiFine before proceeding."));

                if (OptiFuggConfig.CONFIG.showScreen.get()) {
                    event.setNewScreen(new OptiFuggWarningScreen());
                }
            }
        } else {
            OptiFugg.LOGGER.info("No OptiFine installation detected.");
        }

        logged = true;
    }

    @Override
    protected void init() {
        ((WarningScreenAccessor) this).setMessage(MultiLineLabel.create(font, MESSAGE, width - 50));
        var lineHeight = (((WarningScreenAccessor) this).getMessage().getLineCount() + 1) * font.lineHeight * 2 - 20;

        if (OptiFuggConfig.CONFIG.allowToProceed.get()) {
            stopShowing = new Checkbox(width / 2 - 155 + 80, 76 + lineHeight, 150, 20, Component.translatable("multiplayerWarning.check"), false);
            addRenderableWidget(stopShowing);
        }

        initButtons(lineHeight);
    }

    @Override
    protected void initButtons(int lineHeight) {
        addRenderableWidget(new Button(width / 2 - 155, 100 + lineHeight, 150, 20, OPEN_MODS_FOLDER, btn -> Util.getPlatform().openUri(FMLPaths.MODSDIR.get().toUri())));
        addRenderableWidget(new Button(width / 2 - 155 + 160, 100 + lineHeight, 150, 20, OPEN_ALTERNATIVES_LINK, btn -> Util.getPlatform().openUri("https://optifugg.90.gripe")));

        if (OptiFuggConfig.CONFIG.allowToProceed.get()) {
            addRenderableWidget(new Button(width / 2 - 75, 130 + lineHeight, 150, 20, CommonComponents.GUI_PROCEED, btn ->  {
                if (Objects.requireNonNull(stopShowing).selected()) {
                    OptiFuggConfig.CONFIG.showScreen.set(false);
                }

                Objects.requireNonNull(minecraft).setScreen(new TitleScreen(false));
            }));
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}

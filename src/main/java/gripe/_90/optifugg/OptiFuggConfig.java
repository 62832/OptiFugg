package gripe._90.optifugg;

import net.minecraftforge.common.ForgeConfigSpec;

class OptiFuggConfig {
    static final ForgeConfigSpec CONFIG_SPEC;
    static final OptiFuggConfig CONFIG;

    public final ForgeConfigSpec.BooleanValue showScreen;
    public final ForgeConfigSpec.BooleanValue allowToProceed;
    public final ForgeConfigSpec.BooleanValue crashOnStartup;

    private OptiFuggConfig(ForgeConfigSpec.Builder builder) {
        showScreen = builder.comment("Show OptiFugg warning screen on startup.")
                .define("showScreen", true);
        allowToProceed = builder.comment("Allow the user to proceed past warning screen even if OptiFine is installed.")
                .define("allowToProceed", true);
        crashOnStartup = builder.comment("Forcefully crash the game on startup instead of showing a warning screen.")
                .define("crashOnStartup", false);
    }

    static {
        var configured = new ForgeConfigSpec.Builder().configure(OptiFuggConfig::new);
        CONFIG_SPEC = configured.getValue();
        CONFIG = configured.getKey();
    }
}

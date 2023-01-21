package gripe._90.optifugg;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(OptiFugg.MODID)
public class OptiFugg {
    static final String MODID = "optifugg";
    static final Logger LOGGER = LoggerFactory.getLogger("OptiFugg");

    public OptiFugg() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OptiFuggConfig.CONFIG_SPEC);
    }

    static boolean hasOptifine() {
        try {
            Class.forName("optifine.OptiFineTransformationService");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

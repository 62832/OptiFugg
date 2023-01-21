package gripe._90.optifugg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;

@Mixin(WarningScreen.class)
public interface WarningScreenAccessor {
    @Accessor
    MultiLineLabel getMessage();

    @Accessor
    void setMessage(MultiLineLabel message);
}

package net.yiran.sjc;

import li.cil.scannable.client.gui.ConfigurableBlockScannerModuleContainerScreen;
import li.cil.scannable.client.gui.ConfigurableEntityScannerModuleContainerScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CompatPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(ScannableJEICompat.MODID, "compat");
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(ConfigurableBlockScannerModuleContainerScreen.class, BlockHandler.INSTANCE);
        registration.addGhostIngredientHandler(ConfigurableEntityScannerModuleContainerScreen.class, EntityHandler.INSTANCE);
    }

}

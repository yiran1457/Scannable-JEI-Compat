package net.yiran.sjc;

import li.cil.scannable.client.gui.AbstractConfigurableScannerModuleContainerScreen;
import li.cil.scannable.client.gui.ConfigurableBlockScannerModuleContainerScreen;
import li.cil.scannable.common.container.BlockModuleContainerMenu;
import li.cil.scannable.common.network.Network;
import li.cil.scannable.common.network.message.SetConfiguredModuleItemAtMessage;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockHandler implements IGhostIngredientHandler<ConfigurableBlockScannerModuleContainerScreen> {
    public static BlockHandler INSTANCE = new BlockHandler();

    @Override
    public <I> @NotNull List<Target<I>> getTargetsTyped(@NotNull ConfigurableBlockScannerModuleContainerScreen screen, @NotNull ITypedIngredient<I> typedIngredient, boolean doStart) {
        Optional<ItemStack> itemStackOptional = typedIngredient.getItemStack();
        if (itemStackOptional.isEmpty()) {
            return List.of();
        }

        ItemStack ingredientStack = itemStackOptional.get();
        if (ingredientStack.isEmpty() || Block.byItem(ingredientStack.getItem()) == Blocks.AIR) {
            return List.of();
        }

        List<Target<I>> targets = new ArrayList<>();
        for (int slot = 0; slot < 5; slot++) {
            int x = screen.getGuiLeft() + AbstractConfigurableScannerModuleContainerScreen.SLOTS_ORIGIN_X + slot * AbstractConfigurableScannerModuleContainerScreen.SLOT_SIZE;
            int y = screen.getGuiTop() + AbstractConfigurableScannerModuleContainerScreen.SLOTS_ORIGIN_Y;

            targets.add(new BlockTarget<>(screen, slot, x, y));
        }
        return targets;
    }

    @Override
    public void onComplete() {

    }

    private static class BlockTarget<I> implements IGhostIngredientHandler.Target<I> {
        private final ConfigurableBlockScannerModuleContainerScreen screen;
        private final int slotIndex;
        private final Rect2i area;

        private BlockTarget(ConfigurableBlockScannerModuleContainerScreen screen, int slotIndex, int x, int y) {
            this.screen = screen;
            this.slotIndex = slotIndex;
            this.area = new Rect2i(x, y, 16, 16);
        }

        @Override
        public @NotNull Rect2i getArea() {
            return area;
        }

        @Override
        public void accept(@NotNull I ingredient) {
            if (!(ingredient instanceof ItemStack stack) || stack.isEmpty()) {
                return;
            }
            Block block = Block.byItem(stack.getItem());
            if (block == Blocks.AIR) {
                return;
            }
            ResourceLocation blockKey = ForgeRegistries.BLOCKS.getKey(block);
            if (blockKey == null) {
                return;
            }
            BlockModuleContainerMenu menu = screen.getMenu();
            Network.sendToServer(new SetConfiguredModuleItemAtMessage(menu.containerId, slotIndex, blockKey));
        }
    }

}

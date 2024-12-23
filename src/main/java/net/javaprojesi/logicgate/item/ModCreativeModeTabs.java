package net.javaprojesi.logicgate.item;

import net.javaprojesi.logicgate.block.ModBlocks;
import net.javaprojesi.logicgate.logicGateMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    // Yeni yaratıcı mod sekmeleri için DeferredRegister tanımlanması
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
    DeferredRegister.create(Registries.CREATIVE_MODE_TAB, logicGateMod.MOD_ID);

    // Mantık kapıları için bir yaratıcı mod sekmesi tanımlanması
    public static final RegistryObject<CreativeModeTab> LOGIC_GATE_BLOCKS_TAB = CREATIVE_MODE_TABS.register("logic_gate_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.OR_GATE_BLOCK.get()))
                    .title(Component.translatable("creativetab.logicgateid.logic_gate_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // Sekmede yer alacak bloklar sırasıyla ekleniyor.
                        output.accept(ModBlocks.OR_GATE_BLOCK.get());// OR kapısı bloğu.
                        output.accept(ModBlocks.AND_GATE_BLOCK.get());// AND kapısı bloğu.
                        output.accept(ModBlocks.NAND_GATE_BLOCK.get());// NAND kapısı bloğu.
                        output.accept(ModBlocks.NOR_GATE_BLOCK.get());// NOR kapısı bloğu.
                        output.accept(ModBlocks.XOR_GATE_BLOCK.get());// XOR kapısı bloğu.
                        output.accept(ModBlocks.XNOR_GATE_BLOCK.get());// XNOR kapısı bloğu.

                    }).build());
    // Kayıt işlemini tamamlamak için kullanılacak metot.
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

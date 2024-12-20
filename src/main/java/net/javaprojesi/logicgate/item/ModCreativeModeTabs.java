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
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
    DeferredRegister.create(Registries.CREATIVE_MODE_TAB, logicGateMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ALEXANDRITE_ITEMS_TAB = CREATIVE_MODE_TABS.register("alexanderite_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Moditems.ALEXANDRITE.get()))
                    .title(Component.translatable("creativetab.logicgateid.alexandrite_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(Moditems.ALEXANDRITE.get());
                        output.accept(Moditems.RAW_ALEXANDRITE.get());

                    }).build());


    public static final RegistryObject<CreativeModeTab> ALEXANDRITE_BLOCKS_TAB = CREATIVE_MODE_TABS.register("alexanderite_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.ALEXANDRITE_BLOCK.get()))
                    .withTabsBefore(ALEXANDRITE_ITEMS_TAB.getId())
                    .title(Component.translatable("creativetab.logicgateid.alexandrite_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.ALEXANDRITE_BLOCK.get());
                        output.accept(ModBlocks.RAW_ALEXANDRITE_BLOCK.get());

                        output.accept(ModBlocks.ALEXANDRITE_ORE.get());
                        output.accept(ModBlocks.ALEXANDRITE_DEEPSLATE_ORE.get());


                    }).build());



    public static final RegistryObject<CreativeModeTab> LOGIC_GATE_BLOCKS_TAB = CREATIVE_MODE_TABS.register("logic_gate_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.OR_GATE_BLOCK.get()))
                    .withTabsBefore(ALEXANDRITE_BLOCKS_TAB.getId())
                    .title(Component.translatable("creativetab.logicgateid.logic_gate_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModBlocks.OR_GATE_BLOCK.get());
                        output.accept(ModBlocks.AND_GATE_BLOCK.get());
                        output.accept(ModBlocks.NAND_GATE_BLOCK.get());
                        output.accept(ModBlocks.NOR_GATE_BLOCK.get());
                        output.accept(ModBlocks.XOR_GATE_BLOCK.get());
                        output.accept(ModBlocks.XNOR_GATE_BLOCK.get());

                    }).build());





    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

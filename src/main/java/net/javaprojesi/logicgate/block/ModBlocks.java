package net.javaprojesi.logicgate.block;
//eşya oluşturma temeli
import net.javaprojesi.logicgate.item.Moditems;
import net.javaprojesi.logicgate.logicGateMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;



public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, logicGateMod.MOD_ID );
            //burada logicGateMod.MOD_ID dediğimiz, logicGateMod içinde @Mod() parantez içindeki kısmı içermelidir


    public static final RegistryObject<Block> XNOR_GATE_BLOCK = registerBlock("xnor_gate_block",
            ()-> new XnorGateBlock());

    public static final RegistryObject<Block> XOR_GATE_BLOCK = registerBlock("xor_gate_block",
            ()-> new XorGateBlock());

    public static final RegistryObject<Block> NOR_GATE_BLOCK = registerBlock("nor_gate_block",
            ()-> new NorGateBlock());

    public static final RegistryObject<Block> NAND_GATE_BLOCK = registerBlock("nand_gate_block",
            ()-> new NandGateBlock());

    public static final RegistryObject<Block> OR_GATE_BLOCK = registerBlock("or_gate_block",
            ()-> new OrGateBlock());

    public static final RegistryObject<Block> AND_GATE_BLOCK = registerBlock("and_gate_block",
            () -> new AndGateBlock());



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        Moditems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
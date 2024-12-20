package net.javaprojesi.logicgate.item;

import net.javaprojesi.logicgate.logicGateMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.WritableBookItem;

public class Moditems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, logicGateMod.MOD_ID);
//eşyaların register olarak kaydedilmesi gerekiyor minecraft için

    //buranın altındaki registryobject<Item> kısımlarında öncelikle eşyanın ismi sonradan da oyuna register ile kaydetme gerçekleştiriliyor
    public static final RegistryObject<Item> ALEXANDRITE = ITEMS.register("alexandrite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_ALEXANDRITE = ITEMS.register("raw_alexandrite", () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

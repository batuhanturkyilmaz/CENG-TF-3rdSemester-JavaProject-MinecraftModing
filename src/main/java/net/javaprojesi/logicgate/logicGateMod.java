package net.javaprojesi.logicgate;
import com.mojang.logging.LogUtils;
import net.javaprojesi.logicgate.block.ModBlocks;
import net.javaprojesi.logicgate.item.ModCreativeModeTabs;
import net.javaprojesi.logicgate.item.Moditems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// @Mod(DEGER) bunun içindeki DEGER META-INF/mods.toml dosyasının içindeki değerle eşleşmelidir
@Mod(logicGateMod.MOD_ID)
public class logicGateMod// Main sınıfı
{
    // referans değeri olacak MOD_ID tanımlanır. Burada tanımlanan MOD_ID her yerde kullanılacaktır,
    public static final String MOD_ID = "logicgateid";
    // SLF4J (Simple Logging Facade for Java) kullanarak bir logger (günlük kaydı aracı) doğrudan referans alıyoruz.
    // Bu logger, modun içinde meydana gelen önemli olayları, hataları veya bilgi mesajlarını kaydetmek için kullanılır.
    public static final Logger LOGGER = LogUtils.getLogger();

    public logicGateMod() //yapıcı metot
    {
        // `modEventBus`, Forge'un sunduğu `IEventBus` arayüzünün bir örneğidir.
        // Modlara özel olayların kaydedilmesi için kullanılır.
        // Bu mekanizma, Forge'un olay sistemiyle modunuzun entegre olmasını sağlar.
        // `FMLJavaModLoadingContext.get().getModEventBus()`
        // metodu, mevcut mod için olayların işlendiği Mod Event Bus alır.
        // Bu "Mod Olay Otobüsü", genellikle bloklar, eşyalar ve özel olay dinleyicilerinin
        // kaydedilmesi gibi işlemler için kullanılır.
        // Böylece, modun öğeleri ve davranışları Forge altyapısına düzgün bir şekilde entegre olur.
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Mod yüklemesi için commonSetup metodunu kaydediyoruz
        modEventBus.addListener(this::commonSetup);

        // İlgilendiğimiz sunucu ve diğer oyun olayları için kendimizi kaydediyoruz
        MinecraftForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);//Oyunda eklenecek tab menüsü buradan kaydediliyor.
        Moditems.register(modEventBus);//Moditems'ın kaydedilmesi
        ModBlocks.register(modEventBus);//ModBlocks'un kaydedilmesi

        //Öğeyi yaratıcı sekmesine (creative tab) kaydediyoruz
        modEventBus.addListener(this::addCreative);

        // Modun ForgeConfigSpec'ini kaydediyoruz, böylece Forge bizim için yapılandırma dosyasını oluşturup yükleyebilir
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //ortak kurulum kodları
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        // Konfigürasyondaki öğeler tek tek loglanır
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }



    // Örnek blok öğesini inşa blokları sekmesine ekliyoruz
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // Eğer sekme inşa blokları sekmesi ise, buranın altına ekleyeceğimiz blokları tanımlıyoruz
        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(ModBlocks.AND_GATE_BLOCK);  // AND kapı bloğunu ekliyoruz
            event.accept(ModBlocks.OR_GATE_BLOCK);   // OR kapı bloğunu ekliyoruz
            event.accept(ModBlocks.NAND_GATE_BLOCK); // NAND kapı bloğunu ekliyoruz
            event.accept(ModBlocks.NOR_GATE_BLOCK);  // NOR kapı bloğunu ekliyoruz
            event.accept(ModBlocks.XOR_GATE_BLOCK);  // XOR kapı bloğunu ekliyoruz
            event.accept(ModBlocks.XNOR_GATE_BLOCK); // XNOR kapı bloğunu ekliyoruz
        }
    }






    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }




    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

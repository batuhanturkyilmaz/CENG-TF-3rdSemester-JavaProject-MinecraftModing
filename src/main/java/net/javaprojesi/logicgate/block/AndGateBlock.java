package net.javaprojesi.logicgate.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AndGateBlock extends Block {
    // Giriş ve çıkış durumları
    public static final BooleanProperty INPUT1 = BooleanProperty.create("input1");
    public static final BooleanProperty INPUT2 = BooleanProperty.create("input2");
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");

    public AndGateBlock() {
        super(BlockBehaviour.Properties.of().strength(1.0f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(INPUT1, false)
                .setValue(INPUT2, false)
                .setValue(OUTPUT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INPUT1, INPUT2, OUTPUT);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide) {
            // Sol ve sağ girişlerden sinyal al
            boolean input1 = isReceivingSignal(level, pos.relative(Direction.WEST));
            boolean input2 = isReceivingSignal(level, pos.relative(Direction.EAST));
            boolean output = input1 && input2; // AND mantığı

            // Eğer durum değiştiyse, blok durumunu güncelle
            if (state.getValue(OUTPUT) != output) {
                level.setBlock(pos, state.setValue(INPUT1, input1).setValue(INPUT2, input2).setValue(OUTPUT, output), 3);
                level.updateNeighborsAt(pos.relative(Direction.NORTH), this); // Kuzeye sinyal gönder
            }
        }
    }

    private boolean isReceivingSignal(Level level, BlockPos pos) {
        // Belirli bir konumdaki redstone sinyalini kontrol et
        return level.getSignal(pos, Direction.DOWN) > 0 || level.hasSignal(pos, Direction.UP);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true; // Blok redstone kaynağı olabilir
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Sadece kuzeye doğru çıkış sinyali gönder
        return direction == Direction.SOUTH && state.getValue(OUTPUT) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction);
    }

    // DeferredRegister tanımlama
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "logicgateid");

    // Block kaydı
    public static final RegistryObject<Block> AND_GATE_BLOCK = BLOCKS.register("and_gate_block", AndGateBlock::new);

}

package net.javaprojesi.logicgate.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

// XOR Kapısı Bloğu
public class XorGateBlock extends Block {
    // Giriş ve çıkış durumları
    public static final BooleanProperty INPUT1 = BooleanProperty.create("input1"); // Birinci giriş
    public static final BooleanProperty INPUT2 = BooleanProperty.create("input2"); // İkinci giriş
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output"); // Çıkış
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL); // Blok yönü

    // Yapıcı metot: Bloğun varsayılan durumlarını ayarlıyoruz
    public XorGateBlock() {
        super(BlockBehaviour.Properties.of().strength(1.0f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(INPUT1, false)
                .setValue(INPUT2, false)
                .setValue(OUTPUT, false) // Varsayılan çıkış XOR mantığına göre false
                .setValue(FACING, Direction.NORTH)); // Varsayılan yön kuzeydir
    }

    // Bloğun şekli (slab yüksekliği gibi) belirleniyor
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return Shapes.box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0); // Yükseklik yarım blok
    }

    // Çarpışma şekli, aynı şekilde slab yüksekliğinde belirleniyor
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return Shapes.box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0); // Yükseklik yarım blok
    }

    // Blok, oyuncunun baktığı yöne göre yerleştirilir
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer(); // Oyuncu bilgisi alınır
        return this.defaultBlockState()
                .setValue(FACING, player != null ? player.getDirection() : Direction.NORTH); // Oyuncunun yönüne göre blok yerleştirilir
    }

    // Blok durumlarının tanımlandığı metot
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INPUT1, INPUT2, OUTPUT, FACING); // Durumda olacak tüm özellikler eklenir
    }

    // Komşu bloklardan gelen değişikliklere tepki verir
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING); // Blok yönünü al

            // Sol ve sağ girişlerden sinyalleri al
            boolean input1 = isReceivingSignal(level, pos.relative(facing.getCounterClockWise())); // Sol taraftan sinyal al
            boolean input2 = isReceivingSignal(level, pos.relative(facing.getClockWise()));       // Sağ taraftan sinyal al
            boolean output = input1 ^ input2; // XOR mantığı

            // Çıkış durumu değişmişse, blok durumu güncellenir
            if (state.getValue(OUTPUT) != output) {
                level.setBlock(pos, state.setValue(INPUT1, input1).setValue(INPUT2, input2).setValue(OUTPUT, output), 3);
                level.updateNeighborsAt(pos.relative(facing), this); // Komşu bloklara sinyal gönder
            }
        }
    }

    // Belirli bir konumda redstone sinyalinin olup olmadığını kontrol et
    private boolean isReceivingSignal(Level level, BlockPos pos) {
        return level.getSignal(pos, Direction.DOWN) > 0 || level.hasSignal(pos, Direction.UP); // Down ve Up yönlerinden sinyal alınıp alınmadığı kontrol edilir
    }

    // Bu blok bir sinyal kaynağı olabilir
    @Override
    public boolean isSignalSource(BlockState state) {
        return true; // Blok, redstone kaynağı olabilir
    }

    // Çıkış sinyalini yönlendirir
    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        // Çıkış sinyali, yön ve çıkış durumuna göre belirlenir
        return direction == state.getValue(FACING) && state.getValue(OUTPUT) ? 15 : 0; // Eğer doğru yöndeyse ve çıkış true ise, maksimum sinyal gücü (15) gönderilir
    }

    // Direkt sinyal iletimi
    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction); // Direkt sinyal, normal sinyalle aynıdır
    }
}

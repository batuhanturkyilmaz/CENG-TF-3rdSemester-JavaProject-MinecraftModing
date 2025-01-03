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

//and kapısı sınıfı
public class AndGateBlock extends Block {
    // Giriş ve çıkış durumları
    public static final BooleanProperty INPUT1 = BooleanProperty.create("input1"); //yanlardan girilecek olan input1 ve input 2 değişkenleri true/false durumu için atandı
    public static final BooleanProperty INPUT2 = BooleanProperty.create("input2");

    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");//çıkış boolean değişkeni
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL); // blok yönü değişkeni

    public AndGateBlock() { //yapıcı metot
        super(BlockBehaviour.Properties.of().strength(1.0f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(INPUT1, false) //blok durumları default olarak false tanımlanıyor ve yön değeri giriliyor
                .setValue(INPUT2, false)
                .setValue(OUTPUT, false)
                .setValue(FACING, Direction.NORTH));
    }

    // Blok şekli slab yüksekliğinde olacak şekilde tanımlanıyor.
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        // Bloğun üst kısmı slab yüksekliğinde olacak şekilde ayarlanıyor
        return Shapes.box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    }
    // Çarpışma kutusu slab yüksekliğinde tanımlanıyor.
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        // Çarpışma kutusu slab yüksekliğine göre ayarlanıyor
        return Shapes.box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    }

    // Blok, oyuncunun baktığı yöne göre yerleştirr
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Bloğu oyuncunun baktığı yöne göre yerleştirmesi için kullanılan fonksiyon
        Player player = context.getPlayer(); // Oyuncu bilgisi alınan değişken
        return this.defaultBlockState()
                .setValue(FACING, player != null ? player.getDirection() : Direction.NORTH);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INPUT1, INPUT2, OUTPUT, FACING);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);

            // Sol ve sağ girişlerden sinyal al
            boolean input1 = isReceivingSignal(level, pos.relative(facing.getCounterClockWise())); // Sol taraf
            boolean input2 = isReceivingSignal(level, pos.relative(facing.getClockWise()));       // Sağ taraf
            boolean output = input1 && input2; // AND mantığı

            // Eğer durum değiştiyse, blok durumunu güncelle
            if (state.getValue(OUTPUT) != output) {
                level.setBlock(pos, state.setValue(INPUT1, input1).setValue(INPUT2, input2).setValue(OUTPUT, output), 3);
                level.updateNeighborsAt(pos.relative(facing), this); // Karşı tarafa sinyal gönder
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
        // Yön, bloğun 'facing' özelliğine göre belirlenir ve çıkış sinyali gönderilir
        return direction == state.getValue(FACING) && state.getValue(OUTPUT) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction);
    }
}

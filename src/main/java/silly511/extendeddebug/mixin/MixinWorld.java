package silly511.extendeddebug.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(World.class)
public abstract class MixinWorld implements IBlockAccess {
	@Inject(method = "neighborChanged(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
	private void onNeighborChanged(BlockPos pos, final Block blockIn, BlockPos fromPos, CallbackInfo ci) {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.getConnection() == null || minecraft.world == null) {
			return;
		}

		long time = minecraft.world.getTotalWorldTime();

		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		buf.writeVarLong(time);
		buf.writeBlockPos(pos);
		// Fun fact: MC|DebugNeighborsUpdate is 23 characters, which is longer than the 20 char maximum.
		// This means that if this packet were to actually go over the network, it'd be invalid.
		SPacketCustomPayload packet = new SPacketCustomPayload("MC|DebugNeighborsUpdate", buf);

		minecraft.addScheduledTask(() -> packet.processPacket(minecraft.getConnection()));
	}
}

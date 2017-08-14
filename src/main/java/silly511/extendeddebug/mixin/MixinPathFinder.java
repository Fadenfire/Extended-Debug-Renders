package silly511.extendeddebug.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import silly511.extendeddebug.ducks.WritablePath;

@Mixin(PathFinder.class)
public abstract class MixinPathFinder {
	@Inject(method = "findPath(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/entity/EntityLiving;DDDF)Lnet/minecraft/pathfinding/Path;", at = @At("RETURN"))
	private void findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, double x, double y, double z, float maxDistance, CallbackInfoReturnable<Path> cir) {
		if (Minecraft.getMinecraft().getConnection() == null) {
			return;
		}

		Path path = cir.getReturnValue();
		if (path == null) {
			return;
		}

		((WritablePath) path).setTarget(new PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z))); // TODO: this isn't accurate
		PacketBuffer buf = makePacket(entitylivingIn.getEntityId(), path);

		SPacketCustomPayload packet = new SPacketCustomPayload("MC|DebugPath", buf);

		Minecraft.getMinecraft().addScheduledTask(() -> packet.processPacket(Minecraft.getMinecraft().getConnection()));
	}

	private PacketBuffer makePacket(int eid, Path path) {
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());

		buf.writeInt(eid);
		buf.writeFloat(.4F);  // TODO dunno what this value should be
		((WritablePath) path).write(buf);

		return buf;
	}
}

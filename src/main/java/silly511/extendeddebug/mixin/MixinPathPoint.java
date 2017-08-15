package silly511.extendeddebug.mixin;

import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import silly511.extendeddebug.ducks.WritablePathPoint;

@Mixin(PathPoint.class)
public abstract class MixinPathPoint implements WritablePathPoint {
	@Shadow public @Final int x;
	@Shadow public @Final int y;
	@Shadow public @Final int z;
	@Shadow private @Final int hash;
	@Shadow public int index = -1;
	@Shadow public float totalPathDistance;
	@Shadow public float distanceToNext;
	@Shadow public float distanceToTarget;
	@Shadow public PathPoint previous;
	@Shadow public boolean visited;
	@Shadow public float distanceFromOrigin;
	@Shadow public float cost;
	@Shadow public float costMalus;
	@Shadow public PathNodeType nodeType = PathNodeType.BLOCKED;

	@Override
	public void write(PacketBuffer buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeFloat(this.distanceFromOrigin);
		buf.writeFloat(this.cost);
		buf.writeFloat(this.costMalus);
		buf.writeBoolean(this.visited);
		buf.writeInt(this.nodeType.ordinal());
		buf.writeFloat(this.distanceToTarget);
	}
}

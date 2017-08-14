package silly511.extendeddebug.mixin;

import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import silly511.extendeddebug.ducks.WritablePath;
import silly511.extendeddebug.ducks.WritablePathPoint;

@Mixin(Path.class)
public abstract class MixinPath implements WritablePath {
	@Shadow private @Final PathPoint[] points;
	@Shadow private PathPoint[] openSet = new PathPoint[0];
	@Shadow private PathPoint[] closedSet = new PathPoint[0];
	@Shadow private PathPoint target;
	@Shadow private int currentPathIndex;
	@Shadow private int pathLength;

	@Override
	public void setTarget(PathPoint target) {
		this.target = target;
	}

	@Override
	public void write(PacketBuffer buf) {
		buf.writeInt(0); // TODO Current path index
		target = points[0];
		((WritablePathPoint) target).write(buf);
		buf.writeInt(points.length); // NOTE: may differ from pathLength
		for (int i = 0; i < points.length; i++) {
			((WritablePathPoint) points[i]).write(buf);
		}
		buf.writeInt(openSet.length);
		for (int i = 0; i < openSet.length; i++) {
			((WritablePathPoint) openSet[i]).write(buf);
		}
		buf.writeInt(closedSet.length);
		for (int i = 0; i < closedSet.length; i++) {
			((WritablePathPoint) closedSet[i]).write(buf);
		}
	}
}

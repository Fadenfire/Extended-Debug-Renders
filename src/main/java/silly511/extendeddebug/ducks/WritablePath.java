package silly511.extendeddebug.ducks;

import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.PathPoint;

public interface WritablePath {
	public abstract void setTarget(PathPoint target);
	public abstract void write(PacketBuffer buf);
}

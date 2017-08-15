package silly511.extendeddebug.ducks;

import net.minecraft.network.PacketBuffer;

public interface WritablePathPoint {
	public abstract void write(PacketBuffer buf);
}

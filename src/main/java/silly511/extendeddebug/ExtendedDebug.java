package silly511.extendeddebug;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

@Mod(name = "Extended Debug Renders", modid = "extended_debug_renders", version = "1.0")
@EventBusSubscriber
public class ExtendedDebug {
	
	public static KeyBinding pathfinding = new KeyBinding("key.extended_debug_renders.pathfinding", Keyboard.KEY_NONE, "key.categories.extended_debug_renders");
	public static KeyBinding water = new KeyBinding("key.extended_debug_renders.water", Keyboard.KEY_NONE, "key.categories.extended_debug_renders");
	public static KeyBinding heightMap = new KeyBinding("key.extended_debug_renders.heightMap", Keyboard.KEY_NONE, "key.categories.extended_debug_renders");
	public static KeyBinding collisionBox = new KeyBinding("key.extended_debug_renders.collisionBox", Keyboard.KEY_NONE, "key.categories.extended_debug_renders");
	public static KeyBinding neighborsUpdate = new KeyBinding("key.extended_debug_renders.neighborsUpdate", Keyboard.KEY_NONE, "key.categories.extended_debug_renders");
	public static KeyBinding solidFace = new KeyBinding("key.extended_debug_renders.solidFace", Keyboard.KEY_NONE, "key.categories.extended_debug_renders");
	
	public static KeyBinding[] all = new KeyBinding[] {pathfinding, water, heightMap, collisionBox, neighborsUpdate, solidFace};
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		for (KeyBinding bind : all)
			ClientRegistry.registerKeyBinding(bind);
	}
	
	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		
		for (int i = 0; i < all.length; i++) {
			KeyBinding bind = all[i];
			
			if (bind.isPressed()) {
				try {
					Field f = DebugRenderer.class.getDeclaredFields()[8 + i];
					f.setAccessible(true);
					boolean value = !f.getBoolean(mc.debugRenderer);
					
					f.setBoolean(mc.debugRenderer, value);
					
					mc.ingameGUI.getChatGUI().printChatMessage(createDebugText(bind.getKeyDescription(), value));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static ITextComponent createDebugText(String name, boolean state) {
		ITextComponent tag = new TextComponentTranslation("debug.prefix");
		ITextComponent message = new TextComponentTranslation(state ? "extended_debug_renders.debugEnabled" : "extended_debug_renders.debugDisabled", new TextComponentTranslation(name));
		
		tag.setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true));
		
		return new TextComponentString("").appendSibling(tag).appendText(" ").appendSibling(message);
	}

}

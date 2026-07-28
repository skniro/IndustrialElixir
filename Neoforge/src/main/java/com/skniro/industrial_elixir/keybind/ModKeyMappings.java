package com.skniro.industrial_elixir.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import com.skniro.industrial_elixir.IndustrialElixir;
import com.skniro.industrial_elixir.api.Helper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final KeyMapping.Category INDUSTRIAL_ELIXIR =
            KeyMapping.Category.register(Helper.id( "industrial_elixir.keybinds"));

    public static final KeyMapping Mode_Switch_Keybind =
            new KeyMapping("key.industrial_elixir.mode_switch_key", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, INDUSTRIAL_ELIXIR);
    public static final KeyMapping ALT_Keybind =
            new KeyMapping("key.industrial_elixir.alt_key", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, INDUSTRIAL_ELIXIR);
    public static final KeyMapping Boost_Keybind =
            new KeyMapping("key.industrial_elixir.boost_key", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL, INDUSTRIAL_ELIXIR);

    public static void registerKeys() {
        IndustrialElixir.LOGGER.info("Registering Keys for " + IndustrialElixir.MOD_ID);
    }
}
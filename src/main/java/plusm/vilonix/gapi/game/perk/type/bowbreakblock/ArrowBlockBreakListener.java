package plusm.vilonix.gapi.game.perk.type.bowbreakblock;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;

import java.util.HashSet;

public class ArrowBlockBreakListener {

    private static final SoundAPI SOUND_API = VilonixNetwork.getSoundAPI();
    private static final HashSet<String> playerArrowPerk = new HashSet<>();

    public ArrowBlockBreakListener(Player player) {
        playerArrowPerk.add(player.getName());
    }

    public static HashSet<String> getPlayerArrowPerk() {
        return playerArrowPerk;
    }

    public static Sound PlaySound(Block block) {
        Sound sound = SOUND_API.getSound(SoundType.DIG_STONE);
        if (block.getType() == Material.LEAVES)
            sound = SOUND_API.getSound(SoundType.DIG_GRASS);
        if (block.getType() == Material.LEAVES_2)
            sound = SOUND_API.getSound(SoundType.DIG_GRASS);
        if (block.getType() == Material.GRASS)
            sound = SOUND_API.getSound(SoundType.DIG_GRASS);
        if (block.getType() == Material.DIRT)
            sound = SOUND_API.getSound(SoundType.DIG_GRASS);

        if (block.getType() == Material.GRAVEL)
            sound = SOUND_API.getSound(SoundType.DIG_GRAVEL);

        if (block.getType() == Material.SAND)
            sound = SOUND_API.getSound(SoundType.DIG_SAND);

        if (block.getType() == Material.SNOW)
            sound = SOUND_API.getSound(SoundType.DIG_SNOW);

        if (block.getType() == Material.WOOL)
            sound = SOUND_API.getSound(SoundType.DIG_WOOL);

        if (block.getType() == Material.LOG)
            sound = SOUND_API.getSound(SoundType.DIG_WOOD);
        if (block.getType() == Material.LOG_2)
            sound = SOUND_API.getSound(SoundType.DIG_WOOD);
        if (block.getType() == Material.WOOD)
            sound = SOUND_API.getSound(SoundType.DIG_WOOD);
        if (block.getType() == Material.BED || block.getType() == Material.BED_BLOCK)
            sound = SOUND_API.getSound(SoundType.BAT_DEATH);
        if (block.getType() == Material.STAINED_GLASS_PANE)
            sound = SOUND_API.getSound(SoundType.DIG_GLASS);
        if (block.getType() == Material.STAINED_GLASS)
            sound = SOUND_API.getSound(SoundType.DIG_GLASS);
        if (block.getType() == Material.GLASS)
            sound = SOUND_API.getSound(SoundType.DIG_GLASS);

        return sound;
    }
}
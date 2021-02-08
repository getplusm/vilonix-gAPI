package plusm.vilonix.gapi.game.spectator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.inventory.type.DInventory;
import plusm.vilonix.api.sound.SoundAPI;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.gapi.utils.inventory.ItemUtil;

public class SpectatorSettings {
    private static SoundAPI soundAPI = VilonixNetwork.getSoundAPI();

    private DInventory dInventory;
    private SPlayer sPlayer;

    SpectatorSettings(SPlayer sPlayer) {
        dInventory = VilonixNetwork.getInventoryAPI().createInventory(sPlayer.getPlayer(),
                "Настройки наблюдателя", 4);
        this.sPlayer = sPlayer;
        updateInventory();
    }

    public void openInventory() {
        dInventory.openInventory(sPlayer.getPlayer());
    }

    private void setItemSpeed(int slot, Material material, String name, int speedItem) {
        int speed = speedItem + 1;
        if (sPlayer.getSpeedSpec() == speed) {
            dInventory.setItem(slot,
                    new DItem(ItemUtil.createItemStack(material, "§a" + name, Enchantment.PROTECTION_FALL)));
        } else {
            dInventory.setItem(slot,
                    new DItem(ItemUtil.createItemStack(material, "§c" + name), (player, clickType, slot1) -> {
                        if (speed == 0) {
                            PlayerUtil.removePotionEffect(player, PotionEffectType.SPEED);
                        } else {
                            PlayerUtil.addPotionEffect(player, PotionEffectType.SPEED, speed - 1);
                        }
                        sPlayer.setSpeedSpec(speed);
                        updateInventory();
                        soundAPI.play(player, SoundType.CLICK);
                    }));
        }
    }

    public void updateInventory() {
        dInventory.clearInventory();

        setItemSpeed(11, Material.LEATHER_BOOTS, "Без скорости", -1);
        setItemSpeed(12, Material.CHAINMAIL_BOOTS, "Скорость" + " 1", 0);
        setItemSpeed(13, Material.IRON_BOOTS, "Скорость" + " 2", 1);
        setItemSpeed(14, Material.GOLD_BOOTS, "Скорость" + " 3", 2);
        setItemSpeed(15, Material.DIAMOND_BOOTS, "Скорость" + " 4", 3);

        if (sPlayer.getAlwaysFly() == 1) {
            dInventory.setItem(21,
                    new DItem(ItemUtil.createItemStack(Material.SULPHUR, "§cВыключить постоянный полет"),
                            (player, clickType, slot) -> {
                                sPlayer.setAlwaysFly(0);
                                updateInventory();
                                soundAPI.play(player, SoundType.POP);
                            }));
        } else {
            dInventory.setItem(21,
                    new DItem(ItemUtil.createItemStack(Material.SUGAR, "§aВключить постоянный полет"),
                            (player, clickType, slot) -> {
                                sPlayer.setAlwaysFly(1);
                                updateInventory();
                                soundAPI.play(player, SoundType.POP);
                            }));
        }

        if (sPlayer.getVision() == 1) {
            dInventory.setItem(22, new DItem(
                    ItemUtil.createItemStack(Material.ENDER_PEARL, "§cВыключить ночное видение"),
                    (player, clickType, slot) -> {
                        sPlayer.setVision(0);
                        updateInventory();
                        soundAPI.play(player, SoundType.POP);
                    }));
        } else {
            dInventory.setItem(22, new DItem(
                    ItemUtil.createItemStack(Material.EYE_OF_ENDER, "§aВключить ночное видение"),
                    (player, clickType, slot) -> {
                        sPlayer.setVision(1);
                        updateInventory();
                        soundAPI.play(player, SoundType.POP);
                    }));
        }

        if (sPlayer.getHideSpectators() == 0) {
            dInventory.setItem(23,
                    new DItem(ItemUtil.createItemStack(Material.REDSTONE, "§cСкрыть наблюдателей"),
                            (player, clickType, slot) -> {
                                sPlayer.setHideSpectators(1);
                                updateInventory();
                                soundAPI.play(player, SoundType.POP);
                            }));
        } else {
            dInventory.setItem(23, new DItem(
                    ItemUtil.createItemStack(Material.GLOWSTONE_DUST, "§aПоказать наблюдателей"),
                    (player, clickType, slot) -> {
                        sPlayer.setHideSpectators(0);
                        updateInventory();
                        soundAPI.play(player, SoundType.POP);
                    }));
        }
    }
}

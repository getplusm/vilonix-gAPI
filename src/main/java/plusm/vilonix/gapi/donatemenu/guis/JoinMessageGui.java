package plusm.vilonix.gapi.donatemenu.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.mAPI;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.constans.JoinMessage;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.InventoryUtil;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.donatemenu.DonateMenuData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JoinMessageGui extends DonateMenuGui {

    public JoinMessageGui(Player player, DonateMenuData donateMenuData) {
        super(player, donateMenuData,
                "����������� ���. > ���������� � �����");
    }

    private static ItemStack getItem(Player p, JoinMessage joinMessage, boolean available, boolean active, String displayName) {
        List<String> dependLore = new ArrayList<>();

        if (!available && joinMessage.can()) {
            if (joinMessage.isFeliks()) {
                dependLore.add("");
                dependLore.add("�7����� ��������:");
                if (joinMessage == JoinMessage.DEFAULT_MESSAGE)
                    dependLore.add(
                            "�8\u2022 �� ���������� " + Objects.requireNonNull(mAPI.getLuckPerms().getGroupManager().getGroup("PRIMO")).getDisplayName()); // ��
                else
                    dependLore.add("�8\u2022 ��������� ��� " + Objects.requireNonNull(mAPI.getLuckPerms().getGroupManager().getGroup("FELIKS")).getDisplayName()); // ���������

            }
            if (joinMessage.getPriceRub() > 0) {
                dependLore.add("");
                dependLore.add("�7����: �6" + StringUtil.getNumberFormat(joinMessage.getPriceRub()) + " ����������"); // ���� () ����������
            }
        }
        dependLore.add("");
        if (active) {
            dependLore.add("�a��������� �������"); // ��������� �������
        } else if (available) {
            dependLore.add("�e\u25b8 ��������, ����� �������"); // ��������, ����� �������
        } else {
            dependLore.add("�c������ ��������� ��� �� ��������"); // ������ ��������� ��� �� ��������
            dependLore.add("");
            dependLore.add("�e��������, ����� ������");
        }
        String message = joinMessage == JoinMessage.DEFAULT_MESSAGE
                ? "�e>�a>�b> " + displayName + " �6����� �� ������! �b<�a<�e<"
                : joinMessage.getMessage(Objects.requireNonNull(VilonixNetwork.getGamerManager().getGamer(p)));
        return ItemUtil.getBuilder(available || active ? new ItemStack(Material.PAPER) : NO_PERMS)
                .setName((available ? "�a" : "�c") + "�7��������� " + joinMessage.getId())
                .setLore("", "�7�����:", message)
                .addLore(dependLore)
                .glowing(active)
                .build();
    }

    @Override
    protected void setItems(BukkitGamer gamer) {
        setBack(40, donateMenuData.get(MainDonateMenuGui.class));
        inventory.setItem(0, 41, new DItem(ItemUtil.getBuilder(Material.PAPER)
                .setName("�b������ ���������� � �����")
                .setLore("", "�7���������� ��������, � ��� �����",
                        "�7������� - �a������7 ����������", "�7��� ����� �� ������!")
                .build()));
        inventory.setItem(0, 39, new DItem(ItemUtil.getBuilder(Material.BARRIER)
                .setName("�b������� ��������� � �����")
                .setLore("", "�7������ ����������� � �����",
                        "�7����� �� ������", "", "�e��������, ����� �������")
                .build(), (player1, clickType, i) -> {
            gamer.setDefaultJoinMessage(null);
            gamer.playSound(SoundType.SELECTED);
            player1.closeInventory();
        }));

        List<JoinMessage> messages = JoinMessage.getMessages();

        int slot = 0;
        int page = 0;
        for (JoinMessage joinMessage : messages) {
            boolean avalaible = gamer.getAvailableJoinMessages().contains(joinMessage);
            boolean active = gamer.getJoinMessage() == joinMessage;

            inventory.setItem(page, slot++,
                    new DItem(
                            ItemUtil.getBuilder(
                                    getItem(player, joinMessage, avalaible, active, gamer.getDisplayName())).build(),
                            (clicker, clickType, clickedSlot) -> {
                                if (avalaible && active) {
                                    SOUND_API.play(clicker, SoundType.NO);
                                    return;
                                }

                                if (avalaible) {
                                    gamer.setDefaultJoinMessage(joinMessage);
                                    SOUND_API.play(clicker, SoundType.CHEST_OPEN);
                                    gamer.sendMessage("�6Vilonix �8> �f��������� ��� ������� �a" + StringUtil.getNumberFormat(joinMessage.getId()) + " �f������������");
                                    clicker.closeInventory();
                                    donateMenuData.updateClass(this, clicker);
                                    return;
                                }

                                if (joinMessage.getPriceRub() <= 0) {
                                    SOUND_API.play(clicker, SoundType.NO);
                                    return;
                                }
                                try {
                                    if (gamer.changeMoney(PurchaseType.VILONIX, -joinMessage.getPriceRub())) {
                                        gamer.addJoinMessage(joinMessage);
                                        SOUND_API.play(clicker, SoundType.LEVEL_UP);
                                        donateMenuData.updateClass(this, clicker);
                                        gamer.sendMessage("�6Vilonix �8> �f�� ������� ������ �a" + StringUtil.getNumberFormat(joinMessage.getId()) + " �f���������");
                                        clicker.closeInventory();
                                        return;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    gamer.sendMessage("�c������ � ���������� DataGamerBalancer.class");
                                    gamer.sendMessage("�f��������� ������� ������� ������.");
                                }

                                SOUND_API.play(clicker, SoundType.NO);
                            }));

            if (slot == 18)
                ++slot;
            if (slot == 20)
                slot += 0;
            if (slot >= 26) {
                slot = 0;
                ++page;
            }
        }
        INVENTORY_API.pageButton(InventoryUtil.getPagesCount(messages.size(), 21), inventory, InventoryUtil.getSlotByXY(1, 3), InventoryUtil.getSlotByXY(9, 3));
    }
}

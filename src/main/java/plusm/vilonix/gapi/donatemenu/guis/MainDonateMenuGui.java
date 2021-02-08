package plusm.vilonix.gapi.donatemenu.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.inventory.DItem;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.sound.SoundType;
import plusm.vilonix.api.util.Head;
import plusm.vilonix.api.util.InventoryUtil;
import plusm.vilonix.api.util.ItemUtil;
import plusm.vilonix.gapi.donatemenu.DonateMenuData;
import plusm.vilonix.gapi.guis.CustomItems;

public final class MainDonateMenuGui extends DonateMenuGui {

    public MainDonateMenuGui(Player player, DonateMenuData donateMenuData) {
        super(player, donateMenuData, "����������� ����������", 3);
    }

    @Override
    protected void setItems(BukkitGamer gamer) {
        if (VilonixNetwork.isHub() || VilonixNetwork.isLobby()) {
            inventory.setItem(40, new DItem(CustomItems.getBack(), (player, clickType, slot) -> {
                SOUND_API.play(player, SoundType.PICKUP);
                player.chat("/profile");
            }));
        }

        inventory.setItem(InventoryUtil.getSlotByXY(3, 2), new DItem(ItemUtil.getBuilder(Head.BOOKS) // TODO FM
                .setName("�b������� ���������")
                .setLore("�7������ ���� ����� �� �������,",
                        "�7��������� ��� ������� ���������",
                        "",
                        "�7�������� ��� �a�lANGEL �7� ����",
                        "",
                        "�7��� �������� �������� ���� �����������",
                        "�7������� �c/fm �7��� �c/fastmessage",
                        "",
                        "�e\u25b8 ��������, ����� ������� ����"
                ).removeFlags().build(),
                (player, clickType, slot) -> {
                    FastMessageGui gui = donateMenuData.get(FastMessageGui.class);
                    if (gui != null) {
                        gui.open();
                    }
                }));

        inventory.setItem(InventoryUtil.getSlotByXY(5, 2), new DItem(ItemUtil.getBuilder(Head.PREFIX) // TODO PREFIX
                .setName("�c����� ��������")
                .setLore("�7����� ����� ��������",
                        "",
                        "�7�������� ������ ��� �a�lFELIKS",
                        "",
                        "�7������� �������: " + gamer.getChatName(),
                        "",
                        "�c������ ������� ����� �������������� �����!"
                        //"�e\u25b8 ��������, ����� ������� ����"
                ).removeFlags().build(),
                (player, clickType, slot) -> {
                    SOUND_API.play(player, SoundType.NO);
                    /*PrefixGui gui = donateMenuData.get(PrefixGui.class);
                    if (gui != null) {
                        gui.open();
                    }*/
                }));

        inventory.setItem(InventoryUtil.getSlotByXY(4, 2), new DItem(ItemUtil.getBuilder(Material.PAPER) // TODO JM
                .setName("�b��������� ��� �����")
                .setLore("�7���������� ��������� ���������",
                        "�7������ ����� �� ������",
                        "",
                        "�7�������� ��� �7�lPRIMO �7� ����!",
                        "",
                        "�e\u25b8 ��������, ����� ������� ����"
                ).removeFlags().build(),
                (player, clickType, slot) -> {
                    SOUND_API.play(player, SoundType.NO);
                    JoinMessageGui gui = donateMenuData.get(JoinMessageGui.class);
                    if (gui != null) {
                        gui.open();
                    }
                }));
        inventory.setItem(InventoryUtil.getSlotByXY(6, 2), new DItem(ItemUtil.getBuilder(Head.BLUE_ORB) // TODO Donate
                .setName("�b�������� �������")
                .setLore("�7������ ������ ����������?",
                        "�7�� ����� ������� ��� ��������!",
                        "",
                        "�7���� ����� ����� ������ �",
                        "�7�� ������� ���������!",
                        "",
                        "�e\u25b8 ��������, ����� ������� ����"
                ).removeFlags().build(),
                (player, clickType, slot) -> {
                    SOUND_API.play(player, SoundType.NO);
                    player.chat("/deluxemenu open donate");
                }));

		/*inventory.setItem(InventoryUtil.getSlotByXY(5, 4), new DItem(ItemUtil.getBuilder(Head.PREFIX) // TODO GLOW
				.setName("�b" + lang.getMessage("DONATE_MENU_GLOWING_NAME"))
				.setLore(lang.getList("DONATE_MENU_GLOWING_LORE", gamer.getChatName())).removeFlags().build(),
				(player, clickType, slot) -> {
					PrefixGui gui = donateMenuData.get(PrefixGui.class);
					if (gui != null) {
						gui.open();
					}
				}));*/

    }
}

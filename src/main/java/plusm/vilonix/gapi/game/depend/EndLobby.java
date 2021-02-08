package plusm.vilonix.gapi.game.depend;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import plusm.vilonix.api.VilonixNetwork;
import plusm.vilonix.api.entity.npc.type.HumanNPC;
import plusm.vilonix.api.event.game.EndGameEvent;
import plusm.vilonix.api.game.GameSettings;
import plusm.vilonix.api.player.BukkitGamer;
import plusm.vilonix.api.player.GamerManager;
import plusm.vilonix.api.player.constans.KeyType;
import plusm.vilonix.api.player.constans.PurchaseType;
import plusm.vilonix.api.scoreboard.ScoreBoardAPI;
import plusm.vilonix.api.skin.Skin;
import plusm.vilonix.api.util.LocationUtil;
import plusm.vilonix.api.util.StringUtil;
import plusm.vilonix.gapi.game.GameManager;
import plusm.vilonix.gapi.game.boards.EndBoard;
import plusm.vilonix.gapi.module.stats.Stats;
import plusm.vilonix.gapi.utils.DListener;
import plusm.vilonix.gapi.utils.bukkit.BukkitUtil;
import plusm.vilonix.gapi.utils.core.PlayerUtil;
import plusm.vilonix.libraries.LibAPI;
import plusm.vilonix.libraries.interfaces.entity.DEntityCreeper;

@Getter
public class EndLobby extends DListener {

    private static final GamerManager GAMER_MANAGER = VilonixNetwork.getGamerManager();
    //private static final ParticleAPI PARTICLE_API = eNetwork.getParticleAPI();
    private static final ScoreBoardAPI SCORE_BOARD_API = VilonixNetwork.getScoreBoardAPI();
    private final EndGameEvent endGameEvent;
    //private WrapperPlayServerMap mapPacket;
    // private byte[] bytes;

    /*@SuppressWarnings("deprecation")
	private boolean loadImage(Spigot spigot) {
        File imgFile = new File(CoreUtil.getServerDirectory(), "winimage.png");
        if (!imgFile.exists()) {
            spigot.sendMessage("§c[mServerAPI]: Картинка для окончания игры не найдена. Плагин отключается!");
            return false;
        }

        BufferedImage img;
        try {
            img = ImageIO.read(imgFile);
        } catch (IOException e) {
            return false;
        }

        if (img.getHeight() != 128 || img.getWidth() != 128) {
            spigot.sendMessage("§c[mServerAPI]:§6 Картинка найдена, но её размер больше или меньше 128 пикселей (128x128) winimage.png");
            return false;
        }

        bytes = MapPalette.imageToBytes(img);

        mapPacket = new WrapperPlayServerMap();
        mapPacket.setScale((byte) 3);
        mapPacket.setColumns(128);
        mapPacket.setRows(128);
        mapPacket.setX(0);
        mapPacket.setZ(0);
        mapPacket.setTrackingPosition(false);
        mapPacket.setData(bytes);
        mapPacket.setItemDamage(0);
        return true;
        
    }*/

    public EndLobby(EndGameEvent endGameEvent) {
        this.endGameEvent = endGameEvent;
        this.endLobby();
    }

	/*private void createHolo(Player player, Location location, List<String> text) {
		Hologram holo = eNetwork.getHologramAPI().createHologram(location.clone().subtract(0, 0.4, 0));
		holo.addTextLine(text);
		holo.showTo(player);
	}*/

    private void endLobby() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!GAMER_MANAGER.containsGamer(player)) {
                continue;
            }
            BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
            if (gamer == null) {
                continue;
            }

			/*if (loadImage(eNetwork.getGamerManager().getSpigot()) == true) { //карта победителя 
		        mapPacket.sendPacket(player);
	        player.getInventory().setItemInOffHand(plusm.enetwork.api.util.ItemUtil.getBuilder(Material.MAP)
	                .setName(lang.getMessage("WIN_TEAM_BOARD", endGameEvent.getTeamWin()))
	                .removeFlags()
	                .build());
			}*/

            /*PlayerTag playerTag = SCORE_BOARD_API.createTag(SCORE_BOARD_API.getPriorityScoreboardTag(gamer.getGroup()) + gamer.getName());
            playerTag.setPrefix(gamer.getPrefix());
            playerTag.addPlayerToTeam(player);
            playerTag.sendToAll();*/


            //Location locationHolo = LocationUtil.stringToLocation("endlobby;1.5;70.0;-4.5", false);
            //Location locationTopHolo = LocationUtil.stringToLocation("endlobby;1.5;68.0;8.5", false);
            //Location locPos1 = LocationUtil.stringToLocation("endlobby;1.5;67.0;11.5;-0.0;-180.0", true);
            //Location locPos2 = LocationUtil.stringToLocation("endlobby;4.5;67.0;9.5;-0.0;-180.0", true);
            //Location locPos3 = LocationUtil.stringToLocation("endlobby;-1.5;67.0;9.5;-0.0;-180.0", true);


            Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(StringUtil.stringToCenter(endGameEvent.getWinMsg()));

            Stats st = GameManager.getInstance().getStats();
            Player[] killers = new Player[3];
            if (Stats.getStatsPlayers().size() == 2)
                killers = new Player[2];
            int i = 0;
            for (Integer id : st.getPlayersTop(endGameEvent.getTopValue()).keySet()) {
                BukkitGamer killer = (BukkitGamer) VilonixNetwork.getGamerManager().getOrCreate(id);
                if (i == 3)
                    break;
                assert killer != null;
                killers[i] = killer.getPlayer();
                i++;
            }


            Location topLoc = LocationUtil.stringToLocation("endlobby;1.5;68.0;8.5;-1.2;-179.9", true);
            HumanNPC npc = VilonixNetwork.getEntityAPI().createNPC(topLoc, Skin.createSkin(gamer.getSkin().getValue(), gamer.getSkin().getSignature()));
            DEntityCreeper creep = LibAPI.getManager().createDEntity(DEntityCreeper.class, topLoc);
            creep.setPowered(true);
            npc.setPublic(true);
            npc.setHeadLook(false);
            npc.setOwner(player);
		
		
        /*new BukkitRunnable() {
        	 
            int counter = 0;
            double angle = 0.0D;
            double step = ((2 * Math.PI) / 20.0D); // 1 circle in 5 seconds
            double speed = 0.3D; // determines radius
 
            @Override
            public void run() {
                npc.onTeleport(npc.getLocation().clone().add(new Vector(Math.cos(angle) * speed, 0.0D, Math.sin(angle) * speed)));
                npc.setLook((float)(0 % step), (float)(0 % step));
                angle += step;
                PARTICLE_API.shootRandomFirework(npc.getLocation());
                npc.animation(AnimationNpcType.SWING_MAIN_HAND);
                if (++counter > 1000)
                    this.cancel();
                
            }
        }.runTaskTimer(DartaAPI.getInstance(), 10L, 5L);*/

            if (GameSettings.teamMode && endGameEvent.getTeamWin() != null) {
                EndBoard.createBoard(gamer, "Победила команда:", endGameEvent.getTeamWin());
            } else {
                if (!endGameEvent.getWinners().isEmpty() && endGameEvent.getWinners().get(0) != null) {
                    EndBoard.createBoard(gamer, "Победил игрок:",
                            endGameEvent.getWinners().get(0).getDisplayName());
                }
            }
        }

		/*Hologram hologram = eNetwork.getHologramAPI().createHologram(locationTopHolo.clone().subtract(0, 1.0, 0));
		hologram.addTextLine(endGameEvent.getHoloTop());
		hologram.setPublic(true);

		for (int a = 1; a <= killers.length; a++) {
			Location location = locPos1;
			if (a == 2)
				location = locPos2;
			if (a == 3)
				location = locPos3;
			BukkitGamer gamer = GAMER_MANAGER.getGamer(killers[a - 1]);

			if (gamer != null) {
				String name = gamer.getPrefix() + killers[a - 1].getName();
				int kills = st.getPlayerStats(killers[a - 1], endGameEvent.getTopValue());
				String killsString = StringUtil.getCorrectWord(kills, endGameEvent.getTopValueSuffix().split(";")[0],
						endGameEvent.getTopValueSuffix().split(";")[1], endGameEvent.getTopValueSuffix().split(";")[2],
						"");

				CustomStand stand = eNetwork.getEntityAPI().createStand(location.clone());
				stand.setArms(true);
				stand.setSmall(true);
				stand.setBasePlate(false);
				stand.setPublic(true);
				stand.getEntityEquip().setItem(EquipType.HEAD, gamer.getHead());
				setEquipment(stand, a);

				Hologram hologramStand = eNetwork.getHologramAPI()
						.createHologram(location.clone().add(0.0D, 1.1D, 0.0D));
				hologramStand.addTextLine("§e" + a + " место");
				hologramStand.addTextLine(name);
				hologramStand.addTextLine(kills + " " + killsString);
				hologramStand.setPublic(true);
			}
		}*/

        BukkitUtil.runTaskAsync(() -> {
            // оффлайн игрокам(тиммейтам) выдаю их призы
            if (GameSettings.teamMode) {
                for (Player player : endGameEvent.getWinners()) {
                    if (player.isOnline()) {
                        continue;
                    }
                    BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
                    assert gamer != null;
                    gamer.changeKeys(getKeyType(gamer), 1);
                    int money = (int) (gamer.getMoneyLocal() * gamer.getMultiple());
                    if (money >= 1)
                        gamer.changeMoney(PurchaseType.MONEY, money);
                    int exp = gamer.getExpLocal();
                    if (exp >= 1)
                        gamer.addExp(exp);
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                Location locationSpawn = LocationUtil.stringToLocation("endlobby;1.5;68.0;-9.5;-2.7000182;-0.29683468", true);
                BukkitUtil.runTask(() -> player.teleport(locationSpawn));
                if (!GAMER_MANAGER.containsGamer(player))
                    continue;
                BukkitGamer gamer = GAMER_MANAGER.getGamer(player);
                //List<String> holoText = new ArrayList<>();
                //holoText.add("§e" + lang.getMessage("HOLO_REWARD_STATS"));
                //holoText.add("§f" + lang.getMessage("HOLO_REWARD_MULTIPLAY") + ": §ex" + gamer.getMultiple());
                //for (String patch : endGameEvent.getHoloInfo()) {
                //	String stats = endGameEvent.getHoloPlayerInfo().get(player);
                //	if (stats == null)
                //		continue;
                //	for (String getStats : stats.split(";")) {
                //		patch = patch.replaceAll(getStats.split(":")[0], getStats.split(":")[1]);
                //	}
                //	holoText.add(patch);
                //}
                int money = (int) (gamer.getMoneyLocal() * gamer.getMultiple());
                //holoText.add(lang.getMessage("HOLO_REWARD_MONEY") + ": §6" + money);
                //createHolo(player, locationHolo, holoText);

                player.sendMessage("");
                player.sendMessage(StringUtil.stringToCenter("§eВы заработали:"));
                player.sendMessage(StringUtil.stringToCenter(
                        "§6" + money + " " + StringUtil.getCorrectWord(money, "MONEY_2")));

                int exp = gamer.getExpLocal();
                if (endGameEvent.getWinners().contains(player)) {
                    KeyType keyType = getKeyType(gamer);

                    if (keyType != null) {
                        gamer.changeKeys(keyType, 1);
                        player.sendMessage(StringUtil.stringToCenter("§d1 " + keyType.getName().substring(2)));
                    }
                }
                player.sendMessage(StringUtil.stringToCenter("§a" + exp + " XP"));

                if (money >= 1) {
                    gamer.changeMoney(PurchaseType.MONEY, money);
                }
                if (exp >= 1) {
                    gamer.addExp(exp);
                }
            }

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        });
    }

    private KeyType getKeyType(BukkitGamer gamer) {
        KeyType keyType = null;
        try {
            Stats st = GameManager.getInstance().getStats();
            Integer wins = st.getMainStats(gamer.getPlayerID()).get("Wins");
            if (wins != null) {
                wins++;
                if (wins % 30 == 0) {
                    keyType = KeyType.SURVIVAL_KEY;
                } else if (wins % 10 == 0) {
                    keyType = KeyType.MONEY_KEY;
                } else if (wins % 5 == 0) {
                    keyType = KeyType.TITLE_KEY;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyType;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getName().contains("endlobby"))
            if (player.getLocation().getBlockY() <= 0)
                PlayerUtil.redirectToHub(player);

    }

	/*private void setEquipment(CustomStand armorStand, int top) {
		Color color;
		ItemStack itemInHand;
		switch (top) {
		case 1:
			color = Color.fromRGB(23, 164, 79);
			itemInHand = new ItemStack(Material.EMERALD);
			armorStand.setLeftArmPose(new CraftVector(-20.0F, 0.0F, -120.0F));
			armorStand.setRightArmPose(new CraftVector(-40.0F, 50.0F, 90.0F));
			armorStand.setRightLegPose(new CraftVector(-10.0F, 70.0F, 40.0F));
			armorStand.setLeftLegPose(new CraftVector(-10.0F, -60.0F, -40.0F));
			armorStand.setHeadPose(new CraftVector(15.0F, 0.0F, 0.0F));
			break;
		case 2:
			color = Color.fromRGB(46, 210, 185);
			itemInHand = new ItemStack(Material.DIAMOND);
			armorStand.setLeftArmPose(new CraftVector(-20.0F, 0.0F, -140.0F));
			armorStand.setRightArmPose(new CraftVector(-50.0F, 20.0F, 10.0F));
			armorStand.setRightLegPose(new CraftVector(-5.0F, -10.0F, 10.0F));
			armorStand.setLeftLegPose(new CraftVector(-10.0F, -10.0F, -6.0F));
			armorStand.setHeadPose(new CraftVector(5.0F, 0.0F, 5.0F));
			break;
		case 3:
			color = Color.fromRGB(179, 132, 16);
			itemInHand = new ItemStack(Material.GOLD_INGOT);
			armorStand.setLeftArmPose(new CraftVector(50.0F, 15.0F, -7.0F));
			armorStand.setRightArmPose(new CraftVector(-50.0F, 10.0F, 5.0F));
			armorStand.setRightLegPose(new CraftVector(-20.0F, 0.0F, 5.0F));
			armorStand.setLeftLegPose(new CraftVector(20.0F, 0.0F, -5.0F));
			armorStand.setHeadPose(new CraftVector(0.0F, 0.0F, 2.0F));
			break;
		default:
			color = Color.fromRGB(0, 0, 0);
			itemInHand = new ItemStack(Material.BARRIER);
		}

		EntityEquip entityEquip = armorStand.getEntityEquip();
		entityEquip.setItem(EquipType.CHEST, ItemUtil.getColorLeatherArmor(Material.LEATHER_CHESTPLATE, color));
		entityEquip.setItem(EquipType.LEGS, ItemUtil.getColorLeatherArmor(Material.LEATHER_LEGGINGS, color));
		entityEquip.setItem(EquipType.FEET, ItemUtil.getColorLeatherArmor(Material.LEATHER_BOOTS, color));
		entityEquip.setItem(EquipType.MAINHAND, itemInHand);
	}*/
}
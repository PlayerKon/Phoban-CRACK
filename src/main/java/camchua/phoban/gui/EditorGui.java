// Decompiled with: CFR 0.152
// Class Version: 8
package camchua.phoban.gui;

import camchua.phoban.PhoBan;
import camchua.phoban.game.Game;
import camchua.phoban.gui.RewardGui;
import camchua.phoban.manager.FileManager;
import camchua.phoban.nbtapi.NBTItem;
import camchua.phoban.utils.ItemBuilder;
import camchua.phoban.utils.Messages;
import camchua.phoban.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EditorGui
        implements Listener {
   private static HashMap<Player, String> viewers = new HashMap();
   private static HashMap<Player, String> editor = new HashMap();

   public static void open(Player p, String name) {
      FileConfiguration gui = FileManager.getFileConfig(FileManager.Files.GUI);
      File configFile = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
      if (!configFile.exists()) {
         try {
            configFile.createNewFile();
         }
         catch (Exception exception) {
            // empty catch block
         }
      }
      YamlConfiguration room = YamlConfiguration.loadConfiguration(configFile);
      Inventory inv = Bukkit.createInventory(null, gui.getInt("EditorGui.Rows") * 9, gui.getString("EditorGui.Title").replace("&", "§").replace("<name>", name));
      HashMap<String, List<String>> replace = new HashMap<String, List<String>>();
      replace.put("<max_players>", Arrays.asList(String.valueOf(room.getInt("Player", 0))));
      replace.put("<prefix>", Arrays.asList(room.getString("Prefix", "").replace("&", "§")));
      replace.put("<time>", Arrays.asList(String.valueOf(room.getInt("Time", 0))));
      for (int i = 1; i <= 10; ++i) {
         ArrayList<String> lores = new ArrayList<String>();
         if (room.contains("Mob" + i)) {
            for (String s : room.getConfigurationSection("Mob" + i).getKeys(false)) {
               String type = room.getString("Mob" + i + "." + s + ".Type", "null");
               int amount = room.getInt("Mob" + i + "." + s + ".Amount", 0);
               String format = gui.getString("EditorGui.MobLoreFormat").replace("&", "§").replace("<mobs>", type).replace("<amount>", amount + "");
               lores.add(format);
            }
         }
         replace.put("<mob" + i + ">", lores);
      }
      replace.put("<boss_type>", Arrays.asList(room.getString("Boss.Type", "null")));
      replace.put("<boss_amount>", Arrays.asList(String.valueOf(room.getInt("Boss.Amount", 0))));
      Location spawn = (Location)room.get("Spawn");
      replace.put("<spawn>", Arrays.asList(spawn == null ? "" : spawn.getBlockX() + "," + spawn.getBlockY() + "," + spawn.getBlockZ() + "," + spawn.getWorld().getName()));
      replace.put("<type>", Arrays.asList(((FileConfiguration)room).getString("Type"), ""));
      block4: for (String content : gui.getConfigurationSection("EditorGui.Content").getKeys(false)) {
         ItemStack item = ItemBuilder.build(FileManager.Files.GUI, "EditorGui.Content." + content, replace);
         if (gui.contains("EditorGui.Content." + content + ".ClickType")) {
            String mob;
            String clicktype = gui.getString("EditorGui.Content." + content + ".ClickType");
            if (clicktype.contains("EditMob") && EditorGui.isEdited(name, mob = clicktype.replace("Edit", ""))) {
               item = ItemBuilder.build(FileManager.Files.GUI, "EditorGui.Content." + content + ".Edited", replace);
            }
            if (clicktype.equalsIgnoreCase("EditBoss")) {
               ItemMeta meta = item.getItemMeta();
               meta.addEnchant(Enchantment.DURABILITY, 10, true);
               meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
               item.setItemMeta(meta);
            }
         }
         NBTItem nbt = new NBTItem(item);
         if (gui.contains("EditorGui.Content." + content + ".ClickType")) {
            nbt.setString("EditorGui_ClickType", gui.getString("EditorGui.Content." + content + ".ClickType"));
         }
         Iterator iterator = gui.getIntegerList("EditorGui.Content." + content + ".Slot").iterator();
         while (iterator.hasNext()) {
            int slot = (Integer)iterator.next();
            if (slot >= gui.getInt("EditorGui.Rows") * 9) continue;
            if (slot <= -1) {
               for (int i = 0; i < gui.getInt("EditorGui.Rows") * 9; ++i) {
                  inv.setItem(i, nbt.getItem().clone());
               }
               continue block4;
            }
            inv.setItem(slot, nbt.getItem().clone());
         }
      }
      p.openInventory(inv);
      if (viewers.containsKey(p)) {
         viewers.remove(p);
      }
      viewers.put(p, name);
   }

   @EventHandler
   public void onClick(InventoryClickEvent e) {
      final Player p = (Player)((Object)e.getWhoClicked());
      if (viewers.containsKey(p)) {
         e.setCancelled(true);
         final String room = viewers.get(p);
         ItemStack click = e.getCurrentItem();
         if (click == null) {
            return;
         }
         if (click.getType().equals(Material.AIR)) {
            return;
         }
         NBTItem nbt = new NBTItem(click);
         if (!nbt.hasKey("EditorGui_ClickType").booleanValue()) {
            return;
         }
         String clicktype = nbt.getString("EditorGui_ClickType");
         for (int i = 1; i <= 10; ++i) {
            if (!clicktype.equalsIgnoreCase("editmob" + i)) continue;
            p.closeInventory();
            p.getInventory().addItem(new ItemStack[]{this.superultrablazerod(room, "Mob" + i)});
            p.sendMessage(Messages.get("EditMob_Step1"));
            break;
         }
         switch (clicktype.toLowerCase()) {
            case "editplayer": {
               if (editor.containsKey(p)) {
                  editor.remove(p);
               }
               editor.put(p, room + ":editplayer");
               p.closeInventory();
               p.sendMessage(Messages.get("EditPlayer"));
               return;
            }
            case "editprefix": {
               if (editor.containsKey(p)) {
                  editor.remove(p);
               }
               editor.put(p, room + ":editprefix");
               p.closeInventory();
               p.sendMessage(Messages.get("EditPrefix"));
               return;
            }
            case "edittime": {
               if (editor.containsKey(p)) {
                  editor.remove(p);
               }
               editor.put(p, room + ":edittime");
               p.closeInventory();
               p.sendMessage(Messages.get("EditTime"));
               return;
            }
            case "editreward": {
               p.closeInventory();
               new BukkitRunnable(){

                  public void run() {
                     RewardGui.open(p, room);
                  }
               }.runTaskLater(PhoBan.inst(), 1L);
               return;
            }
            case "editboss": {
               p.closeInventory();
               p.getInventory().addItem(new ItemStack[]{this.superultrablazerod(room, "Boss")});
               p.sendMessage(Messages.get("EditBoss_Step1"));
               return;
            }
            case "editspawn": {
               p.closeInventory();
               Location loc = p.getLocation();
               File file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + room + ".yml");
               YamlConfiguration rooms = YamlConfiguration.loadConfiguration(file);
               rooms.set("Spawn", loc);
               FileManager.saveFileConfig((FileConfiguration)rooms, file);
               try {
                  Game game = Game.getGame(room);
                  game.setSpawn(loc);
               }
               catch (Exception game) {
                  // empty catch block
               }
               EditorGui.open(p, room);
               return;
            }
            case "edittype": {
               if (editor.containsKey(p)) {
                  editor.remove(p);
               }
               editor.put(p, room + ":edittype");
               p.closeInventory();
               p.sendMessage(Messages.get("EditType"));
               return;
            }
            case "deleteroom": {
               File file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + room + ".yml");
               file.delete();
               Game.deleteRoom(room);
               p.closeInventory();
               p.sendMessage(Messages.get("DeleteRoom"));
               return;
            }
            case "confirm": {
               File file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + room + ".yml");
               YamlConfiguration rooms = YamlConfiguration.loadConfiguration(file);
               if (!Game.canJoin(rooms)) {
                  p.sendMessage(Messages.get("RoomNotConfig"));
                  return;
               }
               Game.load(room, rooms, file);
               p.closeInventory();
               p.sendMessage(Messages.get("ConfigDone"));
               String type = ((FileConfiguration)rooms).getString("Type");
               String result = EditorGui.containsPhobanType(type);
               if (result.equals("deo-co-con-cac-gi-o-day-het")) {
                  FileConfiguration phoban = FileManager.getFileConfig(FileManager.Files.PHOBAN);
                  FileConfiguration gui = FileManager.getFileConfig(FileManager.Files.GUI);
                  phoban.set(type + "Room.Type", type);
                  phoban.set(type + "Room.Priority", 100);
                  phoban.set(type + "Room.ID", gui.getString("ChooseTypeGui.TypeFormat.ID"));
                  phoban.set(type + "Room.Name", gui.getString("ChooseTypeGui.TypeFormat.Name"));
                  phoban.set(type + "Room.Lore", gui.getStringList("ChooseTypeGui.TypeFormat.Lore"));
                  FileManager.saveFileConfig(phoban, FileManager.Files.PHOBAN);
               }
               return;
            }
         }
         return;
      }
   }

   @EventHandler
   public void onClose(InventoryCloseEvent e) {
      Player p = (Player)((Object)e.getPlayer());
      if (viewers.containsKey(p)) {
         viewers.remove(p);
      }
   }

   @EventHandler
   public void onChat(AsyncPlayerChatEvent e) {
      final Player p = e.getPlayer();
      if (editor.containsKey(p)) {
         YamlConfiguration room;
         String id;
         File file;
         YamlConfiguration room2;
         File file2;
         e.setCancelled(true);
         String type = editor.get(p).split(":")[1];
         final String name = editor.get(p).split(":")[0];
         String mess = ChatColor.stripColor(e.getMessage());
         if (mess.equalsIgnoreCase("cancel") && !type.toLowerCase().contains("editmob") && !type.toLowerCase().contains("editboss")) {
            editor.remove(p);
            new BukkitRunnable(){

               public void run() {
                  EditorGui.open(p, name);
               }
            }.runTaskLater(PhoBan.inst(), 0L);
            return;
         }
         Game game = Game.getGame(name);
         for (int i = 1; i <= 10; ++i) {
            if (type.equalsIgnoreCase("editmob" + i + "_control")) {
               String id2 = editor.get(p).split(":")[2];
               switch (mess.toLowerCase()) {
                  case "add": {
                     editor.remove(p);
                     editor.put(p, name + ":editmob" + i + "_type:" + id2);
                     p.sendMessage(Messages.get("EditMob_Step3"));
                     break;
                  }
                  case "remove": {
                     file2 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                     room2 = YamlConfiguration.loadConfiguration(file2);
                     room2.set("Mob" + i, null);
                     FileManager.saveFileConfig((FileConfiguration)room2, file2);
                     p.sendMessage(Messages.get("RemoveMob"));
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     break;
                  }
                  case "exit": {
                     file2 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                     room2 = YamlConfiguration.loadConfiguration(file2);
                     room2.set("Mob" + i + "." + id2, null);
                     FileManager.saveFileConfig((FileConfiguration)room2, file2);
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     break;
                  }
                  default: {
                     p.sendMessage(Messages.get("EditMob_Step2"));
                     break;
                  }
               }
               break;
            }
            if (type.equalsIgnoreCase("editmob" + i + "_type")) {
               String id3 = editor.get(p).split(":")[2];
               file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               YamlConfiguration room3 = YamlConfiguration.loadConfiguration(file);
               room3.set("Mob" + i + "." + id3 + ".Type", mess);
               FileManager.saveFileConfig((FileConfiguration)room3, file);
               editor.remove(p);
               editor.put(p, name + ":editmob" + i + "_amount:" + id3);
               p.sendMessage(Messages.get("EditMob_Step4"));
               break;
            }
            if (type.equalsIgnoreCase("editmob" + i + "_amount")) {
               try {
                  int amount = Integer.parseInt(mess);
                  id = editor.get(p).split(":")[2];
                  File file3 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                  room = YamlConfiguration.loadConfiguration(file3);
                  room.set("Mob" + i + "." + id + ".Amount", amount);
                  FileManager.saveFileConfig((FileConfiguration)room, file3);
                  editor.remove(p);
                  editor.put(p, name + ":editmob" + i + "_time:" + id);
                  p.sendMessage(Messages.get("EditMob_Step5"));
               }
               catch (Exception ex) {
                  if (ex.getMessage().contains("For input string:")) {
                     p.sendMessage(Messages.get("NotInt"));
                     break;
                  }
                  p.sendMessage(Messages.get("Error").replace("<error>", ex.getMessage()));
                  ex.printStackTrace();
               }
               break;
            }
            if (!type.equalsIgnoreCase("editmob" + i + "_time")) continue;
            try {
               int time = Integer.parseInt(mess);
               id = editor.get(p).split(":")[2];
               final File file4 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               room = YamlConfiguration.loadConfiguration(file4);
               room.set("Mob" + i + "." + id + ".Time", time);
               FileManager.saveFileConfig((FileConfiguration)room, file4);
               if (game != null) {
                  game.setConfig(room);
               }
               editor.remove(p);
               YamlConfiguration finalRoom = room;
               new BukkitRunnable(){

                  public void run() {
                     Game.load(name, finalRoom, file4);
                     EditorGui.open(p, name);
                  }
               }.runTaskLater(PhoBan.inst(), 0L);
            }
            catch (Exception ex) {
               if (ex.getMessage() == null) {
                  new BukkitRunnable(){

                     public void run() {
                        EditorGui.open(p, name);
                     }
                  }.runTaskLater(PhoBan.inst(), 0L);
                  return;
               }
               if (ex.getMessage().contains("For input string:")) {
                  p.sendMessage(Messages.get("NotInt"));
                  break;
               }
               p.sendMessage(Messages.get("Error").replace("<error>", ex.getMessage()));
               ex.printStackTrace();
            }
            break;
         }
         block28 : switch (type.toLowerCase()) {
            case "editplayer": {
               try {
                  int player = (int)Long.parseLong(mess);
                  File file5 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                  room = YamlConfiguration.loadConfiguration(file5);
                  room.set("Player", player);
                  FileManager.saveFileConfig((FileConfiguration)room, file5);
                  try {
                     game.setMaxPlayer(player);
                  }
                  catch (Exception room4) {
                     // empty catch block
                  }
                  editor.remove(p);
                  new BukkitRunnable(){

                     public void run() {
                        EditorGui.open(p, name);
                     }
                  }.runTaskLater(PhoBan.inst(), 0L);
               }
               catch (Exception ex) {
                  if (ex.getMessage() == null) {
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     return;
                  }
                  if (ex.getMessage().contains("For input string:")) {
                     p.sendMessage(Messages.get("NotInt"));
                     break;
                  }
                  p.sendMessage(Messages.get("Error").replace("<error>", ex.getMessage()));
                  ex.printStackTrace();
               }
               break;
            }
            case "editprefix": {
               file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               YamlConfiguration room5 = YamlConfiguration.loadConfiguration(file);
               room5.set("Prefix", mess);
               FileManager.saveFileConfig((FileConfiguration)room5, file);
               editor.remove(p);
               new BukkitRunnable(){

                  public void run() {
                     EditorGui.open(p, name);
                  }
               }.runTaskLater(PhoBan.inst(), 0L);
               break;
            }
            case "edittime": {
               try {
                  int time = Integer.parseInt(mess);
                  File file6 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                  room = YamlConfiguration.loadConfiguration(file6);
                  room.set("Time", time);
                  FileManager.saveFileConfig((FileConfiguration)room, file6);
                  try {
                     game.setMaxTime(time);
                  }
                  catch (Exception room4) {
                     // empty catch block
                  }
                  editor.remove(p);
                  new BukkitRunnable(){

                     public void run() {
                        EditorGui.open(p, name);
                     }
                  }.runTaskLater(PhoBan.inst(), 0L);
               }
               catch (Exception ex) {
                  if (ex.getMessage() == null) {
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     return;
                  }
                  if (ex.getMessage().contains("For input string:")) {
                     p.sendMessage(Messages.get("NotInt"));
                     break;
                  }
                  p.sendMessage(Messages.get("Error").replace("<error>", ex.getMessage()));
                  ex.printStackTrace();
               }
               break;
            }
            case "editboss_control": {
               id = editor.get(p).split(":")[2];
               switch (mess.toLowerCase()) {
                  case "add": {
                     editor.remove(p);
                     editor.put(p, name + ":editboss_type:" + id);
                     p.sendMessage(Messages.get("EditBoss_Step3"));
                     break block28;
                  }
                  case "remove": {
                     File file7 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                     YamlConfiguration room6 = YamlConfiguration.loadConfiguration(file7);
                     room6.set("Boss", null);
                     FileManager.saveFileConfig((FileConfiguration)room6, file7);
                     p.sendMessage("RemoveMob");
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     break block28;
                  }
                  case "exit": {
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     break block28;
                  }
               }
               p.sendMessage(Messages.get("EditBoss_Step2"));
               break;
            }
            case "editboss_type": {
               id = editor.get(p).split(":")[2];
               File file8 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               room = YamlConfiguration.loadConfiguration(file8);
               room.set("Boss.Type", mess);
               FileManager.saveFileConfig((FileConfiguration)room, file8);
               editor.remove(p);
               editor.put(p, name + ":editboss_amount:" + id);
               p.sendMessage(Messages.get("EditBoss_Step4"));
               break;
            }
            case "editboss_amount": {
               try {
                  id = editor.get(p).split(":")[2];
                  int amount = Integer.parseInt(mess);
                  file2 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                  room2 = YamlConfiguration.loadConfiguration(file2);
                  room2.set("Boss.Amount", amount);
                  FileManager.saveFileConfig((FileConfiguration)room2, file2);
                  editor.remove(p);
                  editor.put(p, name + ":editboss_time:" + id);
                  p.sendMessage(Messages.get("EditMob_Step5"));
               }
               catch (Exception ex) {
                  if (ex.getMessage() == null) {
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     return;
                  }
                  if (ex.getMessage().contains("For input string:")) {
                     p.sendMessage(Messages.get("NotInt"));
                     break;
                  }
                  p.sendMessage(Messages.get("Error").replace("<error>", ex.getMessage()));
                  ex.printStackTrace();
               }
               break;
            }
            case "editboss_time": {
               try {
                  int time = Integer.parseInt(mess);
                  String id4 = editor.get(p).split(":")[2];
                  file2 = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                  room2 = YamlConfiguration.loadConfiguration(file2);
                  room2.set("Boss.Time", time);
                  FileManager.saveFileConfig((FileConfiguration)room2, file2);
                  if (game != null) {
                     game.setConfig(room2);
                  }
                  editor.remove(p);
                  new BukkitRunnable(){

                     public void run() {
                        EditorGui.open(p, name);
                     }
                  }.runTaskLater(PhoBan.inst(), 0L);
               }
               catch (Exception ex) {
                  if (ex.getMessage() == null) {
                     new BukkitRunnable(){

                        public void run() {
                           EditorGui.open(p, name);
                        }
                     }.runTaskLater(PhoBan.inst(), 0L);
                     return;
                  }
                  if (ex.getMessage().contains("For input string:")) {
                     p.sendMessage(Messages.get("NotInt"));
                     break;
                  }
                  p.sendMessage(Messages.get("Error").replace("<error>", ex.getMessage()));
                  ex.printStackTrace();
               }
               break;
            }
            case "edittype": {
               file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               YamlConfiguration room7 = YamlConfiguration.loadConfiguration(file);
               room7.set("Type", mess);
               FileManager.saveFileConfig((FileConfiguration)room7, file);
               editor.remove(p);
               new BukkitRunnable(){

                  public void run() {
                     EditorGui.open(p, name);
                  }
               }.runTaskLater(PhoBan.inst(), 0L);
               break;
            }
         }
      }
   }

   private ItemStack superultrablazerod(String room, String mob) {
      ItemStack item = new ItemStack(Utils.matchMaterial("blaze_rod"), 1);
      NBTItem nbt = new NBTItem(item);
      nbt.setString("Room", room);
      nbt.setString("Mob", mob);
      return nbt.getItem().clone();
   }

   @EventHandler
   public void onInteract(PlayerInteractEvent e) {
      Player p = e.getPlayer();
      if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
         return;
      }
      ItemStack item = e.getItem();
      if (item == null) {
         return;
      }
      if (item.getType().equals(Material.AIR)) {
         return;
      }
      NBTItem nbt = new NBTItem(item);
      if (!nbt.hasKey("Room").booleanValue()) {
         return;
      }
      if (!nbt.hasKey("Mob").booleanValue()) {
         return;
      }
      e.setCancelled(true);
      String room = nbt.getString("Room");
      String mob = nbt.getString("Mob");
      Location loc = e.getClickedBlock().getLocation();
      File file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + room + ".yml");
      YamlConfiguration rooms = YamlConfiguration.loadConfiguration(file);
      String id = UUID.randomUUID().toString();
      if (mob.equalsIgnoreCase("boss")) {
         rooms.set("Boss.Location", loc);
      } else {
         rooms.set(mob + "." + id + ".Location", loc);
      }
      FileManager.saveFileConfig((FileConfiguration)rooms, file);
      if (editor.containsKey(p)) {
         editor.remove(p);
      }
      editor.put(p, room + ":edit" + mob + "_control:" + id);
      p.getInventory().setItemInMainHand(null);
      if (mob.equalsIgnoreCase("boss")) {
         p.sendMessage(Messages.get("EditBoss_Step2"));
      } else {
         p.sendMessage(Messages.get("EditMob_Step2"));
      }
   }

   private static boolean isEdited(String room, String mob) {
      File file = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + room + ".yml");
      YamlConfiguration rooms = YamlConfiguration.loadConfiguration(file);
      if (rooms.contains(mob)) {
         for (String s : rooms.getConfigurationSection(mob).getKeys(false)) {
            if (!rooms.contains(mob + "." + s + ".Type") || !rooms.contains(mob + "." + s + ".Amount") || !rooms.contains(mob + "." + s + ".Location")) continue;
            return true;
         }
      }
      return false;
   }

   private static String containsPhobanType(String type) {
      FileConfiguration phoban = FileManager.getFileConfig(FileManager.Files.PHOBAN);
      for (String key : phoban.getKeys(false)) {
         if (!phoban.contains(key + ".Type") || !phoban.getString(key + ".Type").equalsIgnoreCase(type)) continue;
         return phoban.getString(key + ".Type");
      }
      return "deo-co-con-cac-gi-o-day-het";
   }
}

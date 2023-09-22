package camchua.phoban.gui;

import camchua.phoban.PhoBan;
import camchua.phoban.game.Game;
import camchua.phoban.manager.FileManager;
import camchua.phoban.nbtapi.NBTItem;
import camchua.phoban.utils.ItemBuilder;
import camchua.phoban.utils.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class RewardGui implements Listener {
   private static HashMap<Player, String> viewers = new HashMap();
   private static HashMap<Player, String> editor = new HashMap();

   public static void open(Player p, String name) {
      Game game = Game.getGame(name);
      FileConfiguration gui = FileManager.getFileConfig(FileManager.Files.GUI);
      File configFile = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
      if (!configFile.exists()) {
         try {
            configFile.createNewFile();
         } catch (Exception var15) {
         }
      }

      FileConfiguration room = game == null ? YamlConfiguration.loadConfiguration(configFile) : game.getConfig();
      Inventory inv = Bukkit.createInventory((InventoryHolder)null, gui.getInt("RewardGui.Rows") * 9, gui.getString("RewardGui.Title").replace("&", "§").replace("<name>", name));
      HashMap<String, List<String>> replace = new HashMap();
      replace.put("<reward_amount>", Arrays.asList(String.valueOf(((FileConfiguration)room).getInt("RewardAmount", 0))));
      Iterator var8 = gui.getConfigurationSection("RewardGui.Content").getKeys(false).iterator();

      while(true) {
         String s;
         ItemStack item;
         while(var8.hasNext()) {
            s = (String)var8.next();
            item = ItemBuilder.build(FileManager.Files.GUI, "RewardGui.Content." + s, replace);
            NBTItem nbt = new NBTItem(item);
            if (gui.contains("RewardGui.Content." + s + ".ClickType")) {
               nbt.setString("RewardGui_ClickType", gui.getString("RewardGui.Content." + s + ".ClickType"));
            }

            Iterator var12 = gui.getIntegerList("RewardGui.Content." + s + ".Slot").iterator();

            while(var12.hasNext()) {
               int slot = (Integer)var12.next();
               if (slot < gui.getInt("RewardGui.Rows") * 9) {
                  if (slot <= -1) {
                     for(int i = 0; i < gui.getInt("RewardGui.Rows") * 9; ++i) {
                        inv.setItem(i, nbt.getItem().clone());
                     }
                     break;
                  }

                  inv.setItem(slot, nbt.getItem().clone());
               }
            }
         }

         var8 = gui.getIntegerList("RewardGui.RewardSlot").iterator();

         while(var8.hasNext()) {
            int reward_slot = (Integer)var8.next();
            inv.setItem(reward_slot, new ItemStack(Material.AIR));
         }

         if (((FileConfiguration)room).contains("Reward")) {
            var8 = ((FileConfiguration)room).getConfigurationSection("Reward").getKeys(false).iterator();

            while(var8.hasNext()) {
               s = (String)var8.next();
               item = ((FileConfiguration)room).getItemStack("Reward." + s + ".Item").clone();
               ItemMeta meta = item.getItemMeta();
               List<String> lores = meta.hasLore() ? meta.getLore() : new ArrayList();
               ((List)lores).add("§r");
               Iterator var19 = gui.getStringList("RewardGui.Format").iterator();

               while(var19.hasNext()) {
                  String format = (String)var19.next();
                  ((List)lores).add(format.replace("&", "§").replace("<chance>", ((FileConfiguration)room).getInt(name + ".Reward." + s + ".Chance") + ""));
               }

               meta.setLore((List)lores);
               item.setItemMeta(meta);
               NBTItem nbt = new NBTItem(item);
               nbt.setString("RewardGui_ID", s);
               inv.addItem(new ItemStack[]{nbt.getItem().clone()});
            }
         }

         p.openInventory(inv);
         if (viewers.containsKey(p)) {
            viewers.remove(p);
         }

         viewers.put(p, name);
         return;
      }
   }

   @EventHandler
   public void onClick(InventoryClickEvent e) {
      final Player p = (Player)e.getWhoClicked();
      if (viewers.containsKey(p)) {
         final String name = (String)viewers.get(p);
         Game game = Game.getGame(name);
         FileConfiguration gui = FileManager.getFileConfig(FileManager.Files.GUI);
         if (e.getClickedInventory() == p.getOpenInventory().getBottomInventory()) {
            if (e.isShiftClick()) {
               e.setCancelled(true);
            }

            return;
         }

         ItemStack click;
         String clicktype;
         String id;
         if (gui.getIntegerList("RewardGui.RewardSlot").contains(e.getSlot())) {
            click = e.getCurrentItem() == null ? null : e.getCurrentItem().clone();
            ItemStack cursor = e.getCursor() == null ? null : e.getCursor().clone();
            if ((click == null || click.getType().equals(Material.AIR)) && !cursor.getType().equals(Material.AIR)) {
               clicktype = UUID.randomUUID().toString();
               File configFile = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               if (!configFile.exists()) {
                  try {
                     configFile.createNewFile();
                  } catch (Exception var12) {
                  }
               }

               FileConfiguration room = game == null ? YamlConfiguration.loadConfiguration(configFile) : game.getConfig();
               ((FileConfiguration)room).set("Reward." + clicktype + ".Item", cursor);
               ((FileConfiguration)room).set("Reward." + clicktype + ".Chance", 100);
               FileManager.saveFileConfig((FileConfiguration)room, (File)configFile);
               if (editor.containsKey(p)) {
                  editor.remove(p);
               }

               editor.put(p, name + ":editchance:" + clicktype);
               (new BukkitRunnable() {
                  public void run() {
                     p.closeInventory();
                     p.sendMessage(Messages.get("EditChance"));
                  }
               }).runTaskLater(PhoBan.inst(), 1L);
               return;
            }

            if (!click.getType().equals(Material.AIR) && cursor.getType().equals(Material.AIR)) {
               e.setCancelled(true);
               NBTItem nbt = new NBTItem(click.clone());
               id = nbt.getString("RewardGui_ID");
               if (e.isLeftClick()) {
                  if (editor.containsKey(p)) {
                     editor.remove(p);
                  }

                  editor.put(p, name + ":editchance:" + id);
                  (new BukkitRunnable() {
                     public void run() {
                        p.closeInventory();
                        p.sendMessage(Messages.get("EditChance"));
                     }
                  }).runTaskLater(PhoBan.inst(), 1L);
               }

               if (e.isRightClick()) {
                  File configFile = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
                  if (!configFile.exists()) {
                     try {
                        configFile.createNewFile();
                     } catch (Exception var13) {
                     }
                  }

                  FileConfiguration room = game == null ? YamlConfiguration.loadConfiguration(configFile) : game.getConfig();
                  ((FileConfiguration)room).set("Reward." + id, (Object)null);
                  FileManager.saveFileConfig((FileConfiguration)room, (File)configFile);
                  p.closeInventory();
                  (new BukkitRunnable() {
                     public void run() {
                        RewardGui.open(p, name);
                     }
                  }).runTaskLater(PhoBan.inst(), 1L);
               }

               return;
            }

            e.setCancelled(true);
            return;
         }

         e.setCancelled(true);
         click = e.getCurrentItem();
         if (click == null) {
            return;
         }

         if (click.getType().equals(Material.AIR)) {
            return;
         }

         NBTItem nbt = new NBTItem(click);
         if (!nbt.hasKey("RewardGui_ClickType")) {
            return;
         }

         clicktype = nbt.getString("RewardGui_ClickType");
         id = clicktype.toLowerCase();
         byte var10 = -1;
         switch(id.hashCode()) {
         case 951117504:
            if (id.equals("confirm")) {
               var10 = 1;
            }
            break;
         case 2136424519:
            if (id.equals("rewardamount")) {
               var10 = 0;
            }
         }

         switch(var10) {
         case 0:
            if (editor.containsKey(p)) {
               editor.remove(p);
            }

            editor.put(p, name + ":editrewardamount");
            p.closeInventory();
            p.sendMessage(Messages.get("EditRewardAmount"));
            return;
         case 1:
            EditorGui.open(p, name);
            return;
         }
      }

   }

   @EventHandler
   public void onClose(InventoryCloseEvent e) {
      Player p = (Player)e.getPlayer();
      if (viewers.containsKey(p)) {
         viewers.remove(p);
      }

   }

   @EventHandler
   public void onChat(AsyncPlayerChatEvent e) {
      final Player p = e.getPlayer();
      if (editor.containsKey(p)) {
         e.setCancelled(true);
         String mess = ChatColor.stripColor(e.getMessage());
         final String name = ((String)editor.get(p)).split(":")[0];
         String type = ((String)editor.get(p)).split(":")[1];
         Game game = Game.getGame(name);
         String var7 = type.toLowerCase();
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -1908780610:
            if (var7.equals("editchance")) {
               var8 = 0;
            }
            break;
         case 1324347217:
            if (var7.equals("editrewardamount")) {
               var8 = 1;
            }
         }

         switch(var8) {
         case 0:
            String id = ((String)editor.get(p)).split(":")[2];

            try {
               int chance = Integer.parseInt(mess);
               File configFile = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               if (!configFile.exists()) {
                  try {
                     configFile.createNewFile();
                  } catch (Exception var14) {
                  }
               }

               FileConfiguration room = game == null ? YamlConfiguration.loadConfiguration(configFile) : game.getConfig();
               ((FileConfiguration)room).set(name + ".Reward." + id + ".Chance", chance);
               FileManager.saveFileConfig((FileConfiguration)room, (File)configFile);
               editor.remove(p);
               (new BukkitRunnable() {
                  public void run() {
                     RewardGui.open(p, name);
                  }
               }).runTaskLater(PhoBan.inst(), 0L);
            } catch (Exception var16) {
               if (var16.getMessage() == null) {
                  (new BukkitRunnable() {
                     public void run() {
                        EditorGui.open(p, name);
                     }
                  }).runTaskLater(PhoBan.inst(), 0L);
                  return;
               }

               if (var16.getMessage().contains("For input string:")) {
                  p.sendMessage(Messages.get("NotInt"));
               } else {
                  p.sendMessage(Messages.get("Error").replace("<error>", var16.getMessage()));
                  var16.printStackTrace();
               }
            }
            break;
         case 1:
            try {
               int amount = Integer.parseInt(mess);
               File configFile = new File(PhoBan.inst().getDataFolder(), "room" + File.separator + name + ".yml");
               if (!configFile.exists()) {
                  try {
                     configFile.createNewFile();
                  } catch (Exception var13) {
                  }
               }

               FileConfiguration room = game == null ? YamlConfiguration.loadConfiguration(configFile) : game.getConfig();
               ((FileConfiguration)room).set("RewardAmount", amount);
               FileManager.saveFileConfig((FileConfiguration)room, (File)configFile);
               editor.remove(p);
               (new BukkitRunnable() {
                  public void run() {
                     RewardGui.open(p, name);
                  }
               }).runTaskLater(PhoBan.inst(), 0L);
            } catch (Exception var15) {
               if (var15.getMessage() == null) {
                  (new BukkitRunnable() {
                     public void run() {
                        EditorGui.open(p, name);
                     }
                  }).runTaskLater(PhoBan.inst(), 0L);
                  return;
               }

               if (var15.getMessage().contains("For input string:")) {
                  p.sendMessage(Messages.get("NotInt"));
               } else {
                  p.sendMessage(Messages.get("Error").replace("<error>", var15.getMessage()));
                  var15.printStackTrace();
               }
            }
         }
      }

   }
}

// Decompiled with: CFR 0.152
// Class Version: 8
package camchua.phoban.gui;

import camchua.phoban.PhoBan;
import camchua.phoban.game.Game;
import camchua.phoban.gui.PhoBanGui;
import camchua.phoban.manager.FileManager;
import camchua.phoban.nbtapi.NBTItem;
import camchua.phoban.utils.ItemBuilder;
import camchua.phoban.utils.Messages;
import camchua.phoban.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChooseTypeGui
        implements Listener {
   public static HashMap<Player, ChooseTypeGui> viewers = new HashMap();
   public ArrayList<Inventory> pages = new ArrayList();
   public int curpage = 0;

   public ChooseTypeGui(Player p, int pg) {
      if (p == null || pg < 0) {
         return;
      }
      this.curpage = pg;
      Inventory page = this.gui();
      int firstempty = Utils.firstEmpty(page.getSize() / 9);
      ArrayList<String> type = new ArrayList<String>();
      ArrayList<String> sortedType = new ArrayList<String>();
      for (String name : Game.listGame()) {
         Game game = Game.getGame(name);
         if (type.contains(game.getType())) continue;
         type.add(game.getType());
      }
      String lastType = null;
      int lastTypeValue = Integer.MAX_VALUE;
      for (Object t : new ArrayList(type)) {
         for (Object t2 : new ArrayList(type)) {
            String key = this.parseType((String) t2);
            int priority = FileManager.getFileConfig(FileManager.Files.PHOBAN).getInt(key + ".Priority", 100);
            if (priority >= lastTypeValue) continue;
            lastTypeValue = priority;
            lastType = (String) t2;
         }
         if (lastType == null) continue;
         sortedType.add(lastType);
         type.remove(lastType);
         lastType = null;
         lastTypeValue = Integer.MAX_VALUE;
      }
      for (String t : sortedType) {
         String status = !Game.hasTurn(p, t) ? FileManager.getFileConfig(FileManager.Files.GUI).getString("ChooseTypeGui.Format.NoTurn").replace("&", "§") : FileManager.getFileConfig(FileManager.Files.GUI).getString("ChooseTypeGui.Format.Join").replace("&", "§");
         HashMap<String, List<String>> replace = new HashMap<String, List<String>>();
         replace.put("<type>", Arrays.asList(t));
         replace.put("<status>", Arrays.asList(status));
         ItemStack item = null;
         String key = this.parseType(t);
         item = key.equals("deo-co-con-cac-gi-o-day-het") ? ItemBuilder.build(FileManager.Files.GUI, "ChooseTypeGui.TypeFormat", replace) : ItemBuilder.build(FileManager.Files.PHOBAN, key, replace);
         NBTItem nbt = new NBTItem(item.clone());
         nbt.setString("ChooseTypeGui_ClickType", "ChooseType");
         nbt.setString("ChooseTypeGui_Type", t);
         if (page.firstEmpty() == firstempty) {
            page.addItem(new ItemStack[]{nbt.getItem().clone()});
            this.pages.add(page);
            page = this.gui();
            continue;
         }
         page.addItem(new ItemStack[]{nbt.getItem().clone()});
      }
      this.pages.add(page);
      if (viewers.containsKey(p)) {
         viewers.remove(p);
         p.openInventory(this.pages.get(this.curpage));
         viewers.put(p, this);
      } else {
         p.openInventory(this.pages.get(this.curpage));
         viewers.put(p, this);
      }
   }

   private Inventory gui() {
      FileConfiguration gui = FileManager.getFileConfig(FileManager.Files.GUI);
      int rows = gui.getInt("ChooseTypeGui.Rows");
      if (rows < 3) {
         rows = 3;
      }
      Inventory inv = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', gui.getString("PhoBanGui.Title")));
      ItemStack blank = ItemBuilder.build(FileManager.Files.GUI, "ChooseTypeGui.Blank", new HashMap<String, List<String>>());
      Iterator iterator = gui.getIntegerList("ChooseTypeGui.Blank.Slot").iterator();
      while (iterator.hasNext()) {
         int slot = (Integer)iterator.next();
         if (slot >= gui.getInt("ChooseTypeGui.Rows") * 9) continue;
         if (slot <= -1) {
            for (int i = 0; i < gui.getInt("ChooseTypeGui.Rows") * 9; ++i) {
               inv.setItem(i, blank.clone());
            }
            break;
         }
         inv.setItem(slot, blank.clone());
      }
      ItemStack nextpage = ItemBuilder.build(FileManager.Files.GUI, "ChooseTypeGui.NextPage", new HashMap<String, List<String>>());
      NBTItem nbt = new NBTItem(nextpage.clone());
      nbt.setString("ChooseTypeGui_ClickType", "NextPage");
      Iterator i = gui.getIntegerList("ChooseTypeGui.NextPage.Slot").iterator();
      while (i.hasNext()) {
         int slot = (Integer)i.next();
         if (slot >= gui.getInt("ChooseTypeGui.Rows") * 9) continue;
         if (slot <= -1) {
            for (int i2 = 0; i2 < gui.getInt("ChooseTypeGui.Rows") * 9; ++i2) {
               inv.setItem(i2, nbt.getItem().clone());
            }
            break;
         }
         inv.setItem(slot, nbt.getItem().clone());
      }
      ItemStack previouspage = ItemBuilder.build(FileManager.Files.GUI, "ChooseTypeGui.PreviousPage", new HashMap<String, List<String>>());
      nbt = new NBTItem(previouspage.clone());
      nbt.setString("ChooseTypeGui_ClickType", "PreviousPage");
      Iterator iterator2 = gui.getIntegerList("ChooseTypeGui.PreviousPage.Slot").iterator();
      while (iterator2.hasNext()) {
         int slot = (Integer)iterator2.next();
         if (slot >= gui.getInt("ChooseTypeGui.Rows") * 9) continue;
         if (slot <= -1) {
            for (int i3 = 0; i3 < gui.getInt("ChooseTypeGui.Rows") * 9; ++i3) {
               inv.setItem(i3, nbt.getItem().clone());
            }
            break;
         }
         inv.setItem(slot, nbt.getItem().clone());
      }
      iterator2 = gui.getIntegerList("ChooseTypeGui.TypeSlot").iterator();
      while (iterator2.hasNext()) {
         int room_slot = (Integer)iterator2.next();
         inv.setItem(room_slot, new ItemStack(Material.AIR));
      }
      return inv;
   }

   @EventHandler
   public void onClick(InventoryClickEvent e) {
      final Player p = (Player)((Object)e.getWhoClicked());
      if (viewers.containsKey(p)) {
         e.setCancelled(true);
         ItemStack click = e.getCurrentItem();
         if (click == null) {
            return;
         }
         if (click.getType().equals(Material.AIR)) {
            return;
         }
         NBTItem nbt = new NBTItem(click);
         if (!nbt.hasKey("ChooseTypeGui_ClickType").booleanValue()) {
            return;
         }
         String clicktype = nbt.getString("ChooseTypeGui_ClickType");
         switch (clicktype.toLowerCase()) {
            case "nextpage": {
               ChooseTypeGui inv = viewers.get(p);
               if (inv.curpage >= inv.pages.size() - 1) {
                  return;
               }
               new ChooseTypeGui(p, inv.curpage + 1);
               return;
            }
            case "previouspage": {
               ChooseTypeGui inv = viewers.get(p);
               if (inv.curpage > 0) {
                  new ChooseTypeGui(p, inv.curpage - 1);
               }
               return;
            }
            case "choosetype": {
               final String type = nbt.getString("ChooseTypeGui_Type");
               if (!Game.hasTurn(p, type)) {
                  p.sendMessage(Messages.get("NoTurn"));
                  return;
               }
               p.closeInventory();
               new BukkitRunnable(){

                  public void run() {
                     new PhoBanGui(p, 0, type);
                  }
               }.runTaskLater(PhoBan.inst(), 1L);
               return;
            }
         }
      }
   }

   @EventHandler
   public void onClose(InventoryCloseEvent e) {
      Player p = (Player)((Object)e.getPlayer());
      if (viewers.containsKey(p)) {
         viewers.remove(p);
      }
   }

   public String parseType(String type) {
      FileConfiguration phoban = FileManager.getFileConfig(FileManager.Files.PHOBAN);
      for (String str : phoban.getKeys(false)) {
         if (!phoban.contains(str + ".Type") || !phoban.getString(str + ".Type").equalsIgnoreCase(type)) continue;
         return str;
      }
      return "deo-co-con-cac-gi-o-day-het";
   }
}

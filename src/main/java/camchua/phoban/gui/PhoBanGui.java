package camchua.phoban.gui;

import camchua.phoban.game.Game;
import camchua.phoban.game.GameStatus;
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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PhoBanGui implements Listener {
   public static HashMap<Player, PhoBanGui> viewers = new HashMap();
   public ArrayList<Inventory> pages = new ArrayList();
   public int curpage = 0;
   public String type;

   public PhoBanGui(Player p, int pg, String type) {
      if (p != null && pg >= 0) {
         this.type = type;
         this.curpage = pg;
         Inventory page = this.gui();
         int firstempty = Utils.firstEmpty(page.getSize() / 9);
         Iterator var6 = Game.listGame().iterator();

         while(true) {
            String name;
            Game g;
            String t;
            do {
               if (!var6.hasNext()) {
                  this.pages.add(page);
                  if (viewers.containsKey(p)) {
                     viewers.remove(p);
                     p.openInventory((Inventory)this.pages.get(this.curpage));
                     viewers.put(p, this);
                  } else {
                     p.openInventory((Inventory)this.pages.get(this.curpage));
                     viewers.put(p, this);
                  }

                  return;
               }

               name = (String)var6.next();
               g = Game.getGame(name);
               t = g.getType();
            } while(!t.equalsIgnoreCase(type));

            FileConfiguration room = g.getConfig();
            HashMap<String, List<String>> replace = new HashMap();
            replace.put("<prefix>", Arrays.asList(room.getString("Prefix", "").replace("&", "§")));
            replace.put("<current>", Arrays.asList(g.getPlayers().size() + ""));
            replace.put("<max>", Arrays.asList(room.getInt("Player") + ""));
            List<String> lores = new ArrayList();
            Iterator var13 = g.getPlayers().iterator();

            while(var13.hasNext()) {
               Player player = (Player)var13.next();
               lores.add(FileManager.getFileConfig(FileManager.Files.GUI).getString("PhoBanGui.PlayerFormat").replace("&", "§").replace("<player>", player.getName()));
            }

            replace.put("<players>", lores);
            replace.put("<time>", Arrays.asList(timeFormat(g.getTimeLeft())));
            replace.put("<status>", Arrays.asList(FileManager.getFileConfig(FileManager.Files.GUI).getString("PhoBanGui.StatusFormat." + g.getStatus().toString()).replace("&", "§")));
            ItemStack item = g.getStatus().equals(GameStatus.WAITING) ? ItemBuilder.build(FileManager.Files.GUI, "PhoBanGui.WaitingRoom", replace) : (g.getStatus().equals(GameStatus.STARTING) ? ItemBuilder.build(FileManager.Files.GUI, "PhoBanGui.StartingRoom", replace) : ItemBuilder.build(FileManager.Files.GUI, "PhoBanGui.PlayingRoom", replace));
            NBTItem nbt = new NBTItem(item.clone());
            nbt.setString("PhoBanGui_ClickType", "JoinRoom");
            nbt.setString("PhoBanGui_Room", name);
            if (page.firstEmpty() == firstempty) {
               page.addItem(new ItemStack[]{nbt.getItem().clone()});
               this.pages.add(page);
               page = this.gui();
            } else {
               page.addItem(new ItemStack[]{nbt.getItem().clone()});
            }
         }
      }
   }

   private Inventory gui() {
      FileConfiguration gui = FileManager.getFileConfig(FileManager.Files.GUI);
      int rows = gui.getInt("PhoBanGui.Rows");
      if (rows < 3) {
         rows = 3;
      }

      Inventory inv = Bukkit.createInventory((InventoryHolder)null, rows * 9, ChatColor.translateAlternateColorCodes('&', gui.getString("PhoBanGui.Title")));
      ItemStack blank = ItemBuilder.build(FileManager.Files.GUI, "PhoBanGui.Blank", new HashMap());
      Iterator var5 = gui.getIntegerList("PhoBanGui.Blank.Slot").iterator();

      label90:
      while(var5.hasNext()) {
         int slot = (Integer)var5.next();
         if (slot < gui.getInt("PhoBanGui.Rows") * 9) {
            if (slot <= -1) {
               int i = 0;

               while(true) {
                  if (i >= gui.getInt("PhoBanGui.Rows") * 9) {
                     break label90;
                  }

                  inv.setItem(i, blank.clone());
                  ++i;
               }
            }

            inv.setItem(slot, blank.clone());
         }
      }

      ItemStack nextpage = ItemBuilder.build(FileManager.Files.GUI, "PhoBanGui.NextPage", new HashMap());
      NBTItem nbt = new NBTItem(nextpage.clone());
      nbt.setString("PhoBanGui_ClickType", "NextPage");
      Iterator var13 = gui.getIntegerList("PhoBanGui.NextPage.Slot").iterator();

      int i;
      label75:
      while(var13.hasNext()) {
         int slot = (Integer)var13.next();
         if (slot < gui.getInt("PhoBanGui.Rows") * 9) {
            if (slot <= -1) {
               i = 0;

               while(true) {
                  if (i >= gui.getInt("PhoBanGui.Rows") * 9) {
                     break label75;
                  }

                  inv.setItem(i, nbt.getItem().clone());
                  ++i;
               }
            }

            inv.setItem(slot, nbt.getItem().clone());
         }
      }

      ItemStack previouspage = ItemBuilder.build(FileManager.Files.GUI, "PhoBanGui.PreviousPage", new HashMap());
      nbt = new NBTItem(previouspage.clone());
      nbt.setString("PhoBanGui_ClickType", "PreviousPage");
      Iterator var15 = gui.getIntegerList("PhoBanGui.PreviousPage.Slot").iterator();

      label60:
      while(var15.hasNext()) {
         i = (Integer)var15.next();
         if (i < gui.getInt("PhoBanGui.Rows") * 9) {
            if (i <= -1) {
               i = 0;

               while(true) {
                  if (i >= gui.getInt("PhoBanGui.Rows") * 9) {
                     break label60;
                  }

                  inv.setItem(i, nbt.getItem().clone());
                  ++i;
               }
            }

            inv.setItem(i, nbt.getItem().clone());
         }
      }

      var15 = gui.getIntegerList("PhoBanGui.RoomSlot").iterator();

      while(var15.hasNext()) {
         i = (Integer)var15.next();
         inv.setItem(i, new ItemStack(Material.AIR));
      }

      return inv;
   }

   public static String timeFormat(int time) {
      int minute = time / 60;
      int second = time % 60;
      FileConfiguration gui = FileManager.getFileConfig(FileManager.Files.GUI);
      StringBuilder sb = new StringBuilder();
      if (minute > 0) {
         sb.append(gui.getString("PhoBanGui.TimeFormat.Minute").replace("&", "§").replace("<minute>", minute + ""));
         sb.append(" ");
      }

      if (second > 0) {
         sb.append(gui.getString("PhoBanGui.TimeFormat.Second").replace("&", "§").replace("<second>", second + ""));
      }

      return sb.toString();
   }

   @EventHandler
   public void onClick(InventoryClickEvent e) {
      Player p = (Player)e.getWhoClicked();
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
         if (!nbt.hasKey("PhoBanGui_ClickType")) {
            return;
         }

         String clicktype = nbt.getString("PhoBanGui_ClickType");
         String var6 = clicktype.toLowerCase();
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -1400850171:
            if (var6.equals("joinroom")) {
               var7 = 2;
            }
            break;
         case -1116485114:
            if (var6.equals("previouspage")) {
               var7 = 1;
            }
            break;
         case 1425226754:
            if (var6.equals("nextpage")) {
               var7 = 0;
            }
         }

         PhoBanGui inv;
         switch(var7) {
         case 0:
            inv = (PhoBanGui)viewers.get(p);
            if (inv.curpage >= inv.pages.size() - 1) {
               return;
            }

            new PhoBanGui(p, inv.curpage + 1, inv.type);
            return;
         case 1:
            inv = (PhoBanGui)viewers.get(p);
            if (inv.curpage > 0) {
               new PhoBanGui(p, inv.curpage - 1, inv.type);
            }

            return;
         case 2:
            String name = nbt.getString("PhoBanGui_Room");
            Game game = Game.getGame(name);
            if (game.getStatus().equals(GameStatus.WAITING)) {
               if (game.isFull()) {
                  p.sendMessage(Messages.get("RoomFull"));
                  return;
               }

               if (!Game.canJoin(game.getConfig())) {
                  p.sendMessage(Messages.get("JoinRoomNotConfig"));
                  return;
               }

               game.join(p);
               if (game.isLeader(p)) {
                  p.sendMessage(Messages.get("LeaderStart"));
               }

               return;
            }

            if (!game.getStatus().equals(GameStatus.STARTING) && !game.getStatus().equals(GameStatus.PLAYING)) {
               return;
            }

            p.sendMessage(Messages.get("RoomStarted"));
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
}

package camchua.phoban.utils;

import camchua.phoban.manager.FileManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
   private Material material;
   private int amount;
   private String name;
   private List<String> lores;
   private byte data;
   private String skullowner;
   private boolean glow;

   public static ItemStack build(FileManager.Files file, String path, HashMap<String, List<String>> replace) {
      FileConfiguration config = FileManager.getFileConfig(file);
      ItemBuilder builder = new ItemBuilder();
      builder.material(Utils.matchMaterial(config.getString(path + ".ID")));
      if (config.contains(path + ".Amount")) {
         builder.amount(config.getInt(path + ".Amount"));
      }

      if (config.contains(path + ".Data")) {
         builder.data((byte)config.getInt(path + ".Data"));
      }

      Iterator var6;
      String lore;
      if (config.contains(path + ".Name")) {
         String name = ChatColor.translateAlternateColorCodes('&', config.getString(path + ".Name"));
         var6 = replace.keySet().iterator();

         while(var6.hasNext()) {
            lore = (String)var6.next();

            String value;
            for(Iterator var8 = ((List)replace.get(lore)).iterator(); var8.hasNext(); name = name.replace(lore, value == null ? "" : value)) {
               value = (String)var8.next();
            }
         }

         builder.name(name);
      }

      if (config.contains(path + ".Lore")) {
         List<String> lores = new ArrayList();
         var6 = config.getStringList(path + ".Lore").iterator();

         while(true) {
            label79:
            while(var6.hasNext()) {
               lore = (String)var6.next();
               String newLore = ChatColor.translateAlternateColorCodes('&', lore);

               for(int i = 1; i <= 10; ++i) {
                  if (lore.contains("<mob" + i + ">")) {
                     Iterator var10 = ((List)replace.get(lore)).iterator();

                     while(var10.hasNext()) {
                        String value = (String)var10.next();
                        newLore = lore.replace(lore, value);
                        lores.add(newLore);
                     }
                     continue label79;
                  }
               }

               Iterator var16;
               String old;
               if (lore.contains("<players>")) {
                  var16 = ((List)replace.get(lore)).iterator();

                  while(var16.hasNext()) {
                     old = (String)var16.next();
                     newLore = lore.replace(lore, old);
                     lores.add(newLore);
                  }
               } else {
                  var16 = replace.keySet().iterator();

                  while(var16.hasNext()) {
                     old = (String)var16.next();

                     String value;
                     for(Iterator var18 = ((List)replace.get(old)).iterator(); var18.hasNext(); newLore = newLore.replace(old, value == null ? "" : value)) {
                        value = (String)var18.next();
                     }
                  }

                  lores.add(newLore);
               }
            }

            builder.lore(lores);
            break;
         }
      }

      return builder.build();
   }

   public ItemBuilder() {
      this.material = Material.STONE;
      this.amount = 1;
      this.data = 0;
      this.glow = false;
   }

   public ItemBuilder material(Material m) {
      this.material = m;
      return this;
   }

   public ItemBuilder amount(int a) {
      this.amount = a;
      return this;
   }

   public ItemBuilder name(String n) {
      this.name = n;
      return this;
   }

   public ItemBuilder lore(List<String> l) {
      this.lores = l;
      return this;
   }

   public ItemBuilder data(byte d) {
      this.data = d;
      return this;
   }

   public ItemBuilder skull(String s) {
      this.skullowner = s;
      return this;
   }

   public ItemBuilder glow(boolean g) {
      this.glow = g;
      return this;
   }

   public ItemStack build() {
      ItemStack item = new ItemStack(this.material, this.amount, (short)this.data);
      ItemMeta meta = item.getItemMeta();
      SkullMeta smeta = null;
      if (this.data > 0) {
         item.setDurability((short)this.data);
      }

      if (this.name != null) {
         meta.setDisplayName(this.name);
      }

      if (this.lores != null) {
         meta.setLore(this.lores);
      }

      if (this.glow) {
         meta.addEnchant(Enchantment.DURABILITY, 11, true);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
      }

      item.setItemMeta(meta);
      if (this.skullowner != null) {
         smeta = (SkullMeta)item.getItemMeta();
         smeta.setOwner(this.skullowner);
         item.setItemMeta(smeta);
      }

      return item;
   }
}

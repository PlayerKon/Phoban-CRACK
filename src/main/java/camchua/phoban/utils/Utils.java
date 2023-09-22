package camchua.phoban.utils;

import camchua.phoban.PhoBan;
import camchua.phoban.manager.FileManager;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Utils {
   private static boolean LEGACY = true;

   public static void checkVersion() {
      String version = Bukkit.getBukkitVersion().split("-")[0];
      int versionMajor = Integer.parseInt(version.split("\\.")[0]);
      int versionMinor = Integer.parseInt(version.split("\\.")[1]);
      int versionMicro = Integer.parseInt(version.split("\\.")[2]);
      if (versionMinor >= 13) {
         LEGACY = false;
      }

   }

   public static boolean isLegacy() {
      return LEGACY;
   }

   public static Material matchMaterial(String mat) {
      return LEGACY ? Material.matchMaterial(mat) : Material.matchMaterial(mat, LEGACY);
   }

   public static int firstEmpty(int rows) {
      if (rows < 3) {
         rows = 3;
      }

      switch(rows) {
      case 3:
         return 16;
      case 4:
         return 25;
      case 5:
         return 34;
      case 6:
         return 43;
      default:
         return 43;
      }
   }

   public static boolean checkStage(HashMap<String, Integer> require, HashMap<String, Integer> current) {
      Iterator var2 = require.keySet().iterator();

      String key;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         key = (String)var2.next();
         if (!current.containsKey(key)) {
            return false;
         }
      } while((Integer)current.get(key) >= (Integer)require.get(key));

      return false;
   }

   public static boolean isSuckBlock(Location loc) {
      Location loc1 = loc.clone();
      Location loc2 = loc.clone().add(0.0D, 1.0D, 0.0D);
      return !loc1.getBlock().getType().equals(Material.AIR) && !loc2.getBlock().getType().equals(Material.AIR);
   }

   public static void scanSection(FileConfiguration configScan, FileConfiguration newConfig, String key, String arenaName) {
      if (configScan.contains(key)) {
         Iterator var4 = configScan.getConfigurationSection(key).getKeys(false).iterator();

         while(var4.hasNext()) {
            String k = (String)var4.next();
            if (configScan.isConfigurationSection(key + "." + k)) {
               scanSection(configScan, newConfig, key + "." + k, arenaName);
            } else {
               newConfig.set((key + "." + k).replaceFirst(arenaName + ".", ""), configScan.get(key + "." + k));
            }
         }

      }
   }

   public static boolean isJail(Player p) {
      if (!Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
         return false;
      } else {
         try {
            Essentials ess = (Essentials)Bukkit.getPluginManager().getPlugin("Essentials");
            return ess.getUser(p).isJailed();
         } catch (Exception var3) {
            IEssentials ess = (IEssentials)Bukkit.getPluginManager().getPlugin("Essentials");
            return ess.getUser(p).isJailed();
         }
      }
   }

   public static int getRespawnTurn(Player p) {
      for(int i = 100; i > 0; --i) {
         if (PhoBan.inst().hasPerm(p, "phoban.respawn." + i)) {
            return i;
         }
      }

      return FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.Respawn.Amount");
   }
}

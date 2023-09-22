package camchua.phoban.manager;

import camchua.phoban.utils.Utils;
import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {
   private static HashMap<FileManager.Files, File> file = new HashMap();
   private static HashMap<FileManager.Files, FileConfiguration> configuration = new HashMap();

   public static void setup(Plugin plugin) {
      boolean legacy = Utils.isLegacy();
      FileManager.Files[] var2 = FileManager.Files.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         FileManager.Files f = var2[var4];
         String location = legacy ? f.getLegacyLocation() : f.getLocation();
         File fl = new File(plugin.getDataFolder(), location);
         if (!fl.exists()) {
            fl.getParentFile().mkdirs();
            plugin.saveResource(location, false);
         }

         YamlConfiguration config = new YamlConfiguration();

         try {
            config.load(fl);
         } catch (Exception var10) {
         }

         file.put(f, fl);
         configuration.put(f, config);
      }

   }

   public static FileConfiguration getFileConfig(FileManager.Files f) {
      return (FileConfiguration)configuration.get(f);
   }

   public static void saveFileConfig(FileConfiguration data, FileManager.Files f) {
      try {
         data.save((File)file.get(f));
      } catch (Exception var3) {
      }

   }

   public static void saveFileConfig(FileConfiguration data, File f) {
      try {
         data.save(f);
      } catch (Exception var3) {
      }

   }

   public static enum Files {
      CONFIG("config.yml", "config.yml"),
      MESSAGE("message.yml", "message.yml"),
      GUI("gui-1.13.yml", "gui.yml"),
      DATA("data.yml", "data.yml"),
      PHOBAN("phoban.yml", "phoban.yml"),
      SPAWN("spawn.yml", "spawn.yml");

      private String location;
      private String legacyLocation;

      private Files(String l, String legacyLocation) {
         this.location = l;
         this.legacyLocation = legacyLocation;
      }

      public String getLocation() {
         return this.location;
      }

      public String getLegacyLocation() {
         return this.legacyLocation;
      }

      // $FF: synthetic method
      private static FileManager.Files[] $values() {
         return new FileManager.Files[]{CONFIG, MESSAGE, GUI, DATA, PHOBAN, SPAWN};
      }
   }
}

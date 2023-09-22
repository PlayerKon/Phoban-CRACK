package camchua.phoban.nbtapi.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public enum MinecraftVersion {
   UNKNOWN(Integer.MAX_VALUE),
   MC1_7_R4(174),
   MC1_8_R3(183),
   MC1_9_R1(191),
   MC1_9_R2(192),
   MC1_10_R1(1101),
   MC1_11_R1(1111),
   MC1_12_R1(1121),
   MC1_13_R1(1131),
   MC1_13_R2(1132),
   MC1_14_R1(1141),
   MC1_15_R1(1151),
   MC1_16_R1(1161),
   MC1_16_R2(1162),
   MC1_16_R3(1163),
   MC1_17_R1(1171),
   MC1_18_R1(1181, true),
   MC1_18_R2(1182, true),
   MC1_19_R1(1191, true);

   private static MinecraftVersion version;
   private static Boolean hasGsonSupport;
   private static Boolean isForgePresent;
   private static boolean bStatsDisabled = false;
   private static boolean disablePackageWarning = false;
   private static boolean updateCheckDisabled = false;
   private static Logger logger = Logger.getLogger("NBTAPI");
   protected static final String VERSION = "2.11.0-SNAPSHOT";
   private final int versionId;
   private final boolean mojangMapping;

   private MinecraftVersion(int versionId) {
      this(versionId, false);
   }

   private MinecraftVersion(int versionId, boolean mojangMapping) {
      this.versionId = versionId;
      this.mojangMapping = mojangMapping;
   }

   public int getVersionId() {
      return this.versionId;
   }

   public boolean isMojangMapping() {
      return this.mojangMapping;
   }

   public String getPackageName() {
      return this == UNKNOWN ? Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] : this.name().replace("MC", "v");
   }

   public static boolean isAtLeastVersion(MinecraftVersion version) {
      return getVersion().getVersionId() >= version.getVersionId();
   }

   public static boolean isNewerThan(MinecraftVersion version) {
      return getVersion().getVersionId() > version.getVersionId();
   }

   public static MinecraftVersion getVersion() {
      if (version != null) {
         return version;
      } else {
         String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
         logger.info("[NBTAPI] Found Spigot: " + ver + "! Trying to find NMS support");

         try {
            version = valueOf(ver.replace("v", "MC"));
         } catch (IllegalArgumentException var2) {
            version = UNKNOWN;
         }

         if (version != UNKNOWN) {
            logger.info("[NBTAPI] NMS support '" + version.name() + "' loaded!");
         } else {
            logger.warning("[NBTAPI] This Server-Version(" + ver + ") is not supported by this NBT-API Version(" + "2.11.0-SNAPSHOT" + ") located at " + MinecraftVersion.class.getName() + ". The NBT-API will try to work as good as it can! Some functions may not work!");
         }

         init();
         return version;
      }
   }

   private static void init() {
      try {
         if (hasGsonSupport() && !bStatsDisabled) {
            new ApiMetricsLite();
         }
      } catch (Exception var1) {
         logger.log(Level.WARNING, "[NBTAPI] Error enabling Metrics!", var1);
      }

      if (hasGsonSupport() && !updateCheckDisabled) {
         (new Thread(() -> {
            try {
               VersionChecker.checkForUpdates();
            } catch (Exception var1) {
               logger.log(Level.WARNING, "[NBTAPI] Error while checking for updates! Error: " + var1.getMessage());
            }

         })).start();
      }

      String defaultPackage = new String(new byte[]{100, 101, 46, 116, 114, 55, 122, 119, 46, 99, 104, 97, 110, 103, 101, 109, 101, 46, 110, 98, 116, 97, 112, 105, 46, 117, 116, 105, 108, 115});
      if (!disablePackageWarning && MinecraftVersion.class.getPackage().getName().equals(defaultPackage)) {
         logger.warning("#########################################- NBTAPI -#########################################");
         logger.warning("The NBT-API package has not been moved! This *will* cause problems with other plugins containing");
         logger.warning("a different version of the api! Please read the guide on the plugin page on how to get the");
         logger.warning("Maven Shade plugin to relocate the api to your personal location! If you are not the developer,");
         logger.warning("please check your plugins and contact their developer, so he can fix this issue.");
         logger.warning("#########################################- NBTAPI -#########################################");
      }

   }

   public static boolean hasGsonSupport() {
      if (hasGsonSupport != null) {
         return hasGsonSupport;
      } else {
         try {
            logger.info("[NBTAPI] Found Gson: " + Class.forName("com.google.gson.Gson"));
            hasGsonSupport = true;
         } catch (Exception var1) {
            logger.info("[NBTAPI] Gson not found! This will not allow the usage of some methods!");
            hasGsonSupport = false;
         }

         return hasGsonSupport;
      }
   }

   public static boolean isForgePresent() {
      if (isForgePresent != null) {
         return isForgePresent;
      } else {
         try {
            logger.info("[NBTAPI] Found Forge: " + (getVersion() == MC1_7_R4 ? Class.forName("cpw.mods.fml.common.Loader") : Class.forName("net.minecraftforge.fml.common.Loader")));
            isForgePresent = true;
         } catch (Exception var1) {
            isForgePresent = false;
         }

         return isForgePresent;
      }
   }

   public static void disableBStats() {
      bStatsDisabled = true;
   }

   public static void disableUpdateCheck() {
      updateCheckDisabled = true;
   }

   public static void disablePackageWarning() {
      disablePackageWarning = true;
   }

   public static Logger getLogger() {
      return logger;
   }

   public static void replaceLogger(Logger logger) {
      if (logger == null) {
         throw new NullPointerException("Logger can not be null!");
      } else {
         MinecraftVersion.logger = logger;
      }
   }

   // $FF: synthetic method
   private static MinecraftVersion[] $values() {
      return new MinecraftVersion[]{UNKNOWN, MC1_7_R4, MC1_8_R3, MC1_9_R1, MC1_9_R2, MC1_10_R1, MC1_11_R1, MC1_12_R1, MC1_13_R1, MC1_13_R2, MC1_14_R1, MC1_15_R1, MC1_16_R1, MC1_16_R2, MC1_16_R3, MC1_17_R1, MC1_18_R1, MC1_18_R2, MC1_19_R1};
   }
}

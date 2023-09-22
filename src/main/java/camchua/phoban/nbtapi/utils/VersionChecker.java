package camchua.phoban.nbtapi.utils;

import camchua.phoban.nbtapi.NBTItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class VersionChecker {
   private static final String USER_AGENT = "nbt-api Version check";
   private static final String REQUEST_URL = "https://api.spiget.org/v2/resources/7939/versions?size=100";
   public static boolean hideOk = false;

   protected static void checkForUpdates() throws Exception {
      URL url = new URL("https://api.spiget.org/v2/resources/7939/versions?size=100");
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.addRequestProperty("User-Agent", "nbt-api Version check");
      InputStream inputStream = connection.getInputStream();
      InputStreamReader reader = new InputStreamReader(inputStream);
      JsonElement element = (new JsonParser()).parse(reader);
      if (element.isJsonArray()) {
         JsonArray updates = (JsonArray)element;
         JsonObject latest = (JsonObject)updates.get(updates.size() - 1);
         int versionDifference = getVersionDifference(latest.get("name").getAsString());
         if (versionDifference == -1) {
            MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] The NBT-API located at '" + NBTItem.class.getPackage() + "' seems to be outdated!");
            MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Current Version: '2.11.0-SNAPSHOT' Newest Version: " + latest.get("name").getAsString() + "'");
            MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Please update the NBTAPI or the plugin that contains the api(nag the mod author when the newest release has an old version, not the NBTAPI dev)!");
         } else if (versionDifference == 0) {
            if (!hideOk) {
               MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] The NBT-API seems to be up-to-date!");
            }
         } else if (versionDifference == 1) {
            MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] The NBT-API at '" + NBTItem.class.getPackage() + "' seems to be a future Version, not yet released on Spigot/CurseForge! This is not an error!");
            MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] Current Version: '2.11.0-SNAPSHOT' Newest Version: " + latest.get("name").getAsString() + "'");
         }
      } else {
         MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error when looking for Updates! Got non Json Array: '" + element.toString() + "'");
      }

   }

   private static int getVersionDifference(String version) {
      String current = "2.11.0-SNAPSHOT";
      if (current.equals(version)) {
         return 0;
      } else {
         String pattern = "\\.";
         if (current.split(pattern).length == 3 && version.split(pattern).length == 3) {
            int curMaj = Integer.parseInt(current.split(pattern)[0]);
            int curMin = Integer.parseInt(current.split(pattern)[1]);
            String curPatch = current.split(pattern)[2];
            int relMaj = Integer.parseInt(version.split(pattern)[0]);
            int relMin = Integer.parseInt(version.split(pattern)[1]);
            String relPatch = version.split(pattern)[2];
            if (curMaj < relMaj) {
               return -1;
            } else if (curMaj > relMaj) {
               return 1;
            } else if (curMin < relMin) {
               return -1;
            } else if (curMin > relMin) {
               return 1;
            } else {
               int curPatchN = Integer.parseInt(curPatch.split("-")[0]);
               int relPatchN = Integer.parseInt(relPatch.split("-")[0]);
               if (curPatchN < relPatchN) {
                  return -1;
               } else if (curPatchN > relPatchN) {
                  return 1;
               } else if (!relPatch.contains("-") && curPatch.contains("-")) {
                  return -1;
               } else {
                  return relPatch.contains("-") && curPatch.contains("-") ? 0 : 1;
               }
            }
         } else {
            return -1;
         }
      }
   }
}

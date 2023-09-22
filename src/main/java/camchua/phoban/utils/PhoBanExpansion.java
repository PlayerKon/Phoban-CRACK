package camchua.phoban.utils;

import camchua.phoban.game.Game;
import camchua.phoban.game.PlayerData;
import camchua.phoban.gui.PhoBanGui;
import camchua.phoban.manager.FileManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PhoBanExpansion extends PlaceholderExpansion {
   public String getIdentifier() {
      return "phoban";
   }

   public String getPlugin() {
      return "PhoBan";
   }

   public String getAuthor() {
      return "CamChua_VN";
   }

   public String getVersion() {
      return "1.0";
   }

   public String onRequest(OfflinePlayer p, String identifier) {
      try {
         String[] args = identifier.split("_");
         Player player = p.getPlayer();
         String var5 = args[0].toLowerCase();
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -984284210:
            if (var5.equals("maxplayers")) {
               var6 = 2;
            }
            break;
         case -980110702:
            if (var5.equals("prefix")) {
               var6 = 1;
            }
            break;
         case 3560141:
            if (var5.equals("time")) {
               var6 = 0;
            }
            break;
         case 1487715104:
            if (var5.equals("minplayers")) {
               var6 = 3;
            }
         }

         PlayerData data;
         Game gameTarget;
         FileConfiguration roomTarget;
         switch(var6) {
         case 0:
            if (!PlayerData.data().containsKey(player)) {
               return "Not in game";
            }

            data = (PlayerData)PlayerData.data().get(player);
            gameTarget = data.getGame();
            roomTarget = gameTarget.getConfig();
            return PhoBanGui.timeFormat(gameTarget.getTimeLeft());
         case 1:
            if (!PlayerData.data().containsKey(player)) {
               return "Not in game";
            }

            data = (PlayerData)PlayerData.data().get(player);
            gameTarget = data.getGame();
            roomTarget = gameTarget.getConfig();
            return roomTarget.getString("Prefix", "").replace("&", "§");
         case 2:
            if (!PlayerData.data().containsKey(player)) {
               return "Not in game";
            }

            data = (PlayerData)PlayerData.data().get(player);
            gameTarget = data.getGame();
            roomTarget = gameTarget.getConfig();
            return String.valueOf(roomTarget.getInt("Player"));
         case 3:
            if (!PlayerData.data().containsKey(player)) {
               return "Not in game";
            }

            data = (PlayerData)PlayerData.data().get(player);
            gameTarget = data.getGame();
            roomTarget = gameTarget.getConfig();
            return String.valueOf(gameTarget.getPlayers().size());
         default:
            String name = args[0];
            gameTarget = Game.getGame(name);
            if (gameTarget == null) {
               return "Game not found";
            } else if (args.length <= 1) {
               return "args.length <= 1";
            } else {
               roomTarget = gameTarget.getConfig();
               String var10 = args[1].toLowerCase();
               byte var11 = -1;
               switch(var10.hashCode()) {
               case -984284210:
                  if (var10.equals("maxplayers")) {
                     var11 = 2;
                  }
                  break;
               case -980110702:
                  if (var10.equals("prefix")) {
                     var11 = 1;
                  }
                  break;
               case -892481550:
                  if (var10.equals("status")) {
                     var11 = 4;
                  }
                  break;
               case 3560141:
                  if (var10.equals("time")) {
                     var11 = 0;
                  }
                  break;
               case 1487715104:
                  if (var10.equals("minplayers")) {
                     var11 = 3;
                  }
               }

               switch(var11) {
               case 0:
                  return PhoBanGui.timeFormat(gameTarget.getTimeLeft());
               case 1:
                  return roomTarget.getString("Prefix", "").replace("&", "§");
               case 2:
                  return String.valueOf(roomTarget.getInt("Player"));
               case 3:
                  return String.valueOf(gameTarget.getPlayers().size());
               case 4:
                  return FileManager.getFileConfig(FileManager.Files.GUI).getString("PhoBanGui.StatusFormat." + gameTarget.getStatus().toString()).replace("&", "§");
               default:
                  return "No placeholder";
               }
            }
         }
      } catch (Exception var12) {
         return "PhoBan placeholder error: " + var12.getMessage() + " | identifier: " + identifier;
      }
   }
}

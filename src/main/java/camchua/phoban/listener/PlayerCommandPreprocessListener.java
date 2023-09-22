package camchua.phoban.listener;

import camchua.phoban.game.PlayerData;
import camchua.phoban.manager.FileManager;
import camchua.phoban.utils.Messages;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {
   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onCommand(PlayerCommandPreprocessEvent e) {
      String cmd = e.getMessage().replaceFirst("/", "");
      StringBuilder sb = new StringBuilder();

      for(int i = 1; i < cmd.split(" ").length; ++i) {
         sb.append(cmd.split(" ")[i]).append(" ");
      }

      Iterator var6 = FileManager.getFileConfig(FileManager.Files.CONFIG).getStringList("Settings.CommandAliases").iterator();

      while(var6.hasNext()) {
         String c = (String)var6.next();
         if (cmd.split(" ")[0].toLowerCase().equals(c.toLowerCase())) {
            e.setMessage("/phoban " + sb.toString());
            break;
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onCommand2(PlayerCommandPreprocessEvent e) {
      Player p = e.getPlayer();
      if (PlayerData.data().containsKey(p)) {
         String cmd = e.getMessage().replaceFirst("/", "").toLowerCase();

         try {
            if (cmd.split(" ")[1].equalsIgnoreCase("start")) {
               return;
            }

            if (cmd.split(" ")[1].equalsIgnoreCase("leave")) {
               return;
            }
         } catch (Exception var9) {
         }

         boolean block = true;
         Iterator var5 = FileManager.getFileConfig(FileManager.Files.CONFIG).getStringList("Settings.CommandWhitelist").iterator();

         while(var5.hasNext()) {
            String cw = (String)var5.next();
            Pattern pat = Pattern.compile(cw.toLowerCase());
            Matcher mat = pat.matcher(cmd);
            if (mat.matches()) {
               block = false;
            }
         }

         if (block) {
            e.setCancelled(true);
            p.sendMessage(Messages.get("NoCommand"));
         }
      }

   }
}

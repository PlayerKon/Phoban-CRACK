package camchua.phoban.listener;

import camchua.phoban.game.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
   @EventHandler
   public void onQuit(PlayerQuitEvent e) {
      Player p = e.getPlayer();
      if (PlayerData.data().containsKey(p)) {
         ((PlayerData)PlayerData.data().get(p)).getGame().leave(p, true);
      }
   }
}

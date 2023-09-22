package camchua.phoban.game;

import camchua.phoban.PhoBan;
import camchua.phoban.manager.FileManager;
import camchua.phoban.mythicmobs.BukkitAPIHelper;
import camchua.phoban.utils.Messages;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class GameListener implements Listener {
   private HashMap<Player, Long> protect = new HashMap();
   private HashMap<Player, Long> lastDeath = new HashMap();

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onListener(EntityDeathEvent e) {
      if (e.getEntity() instanceof Player) {
         Player p = (Player)e.getEntity();
         if (PlayerData.data().containsKey(p)) {
            if (!this.lastDeath.containsKey(p)) {
               this.lastDeath.put(p, System.currentTimeMillis());
            } else {
               long last = (Long)this.lastDeath.get(p);
               if (System.currentTimeMillis() - last < 1000L) {
                  return;
               }
            }

            this.lastDeath.replace(p, System.currentTimeMillis());
            if (FileManager.getFileConfig(FileManager.Files.CONFIG).getBoolean("Settings.Respawn.Enable")) {
               try {
                  Bukkit.getScheduler().scheduleSyncDelayedTask(PhoBan.inst(), () -> {
                     PlayerData data = (PlayerData)PlayerData.data().get(p);
                     if (data != null) {
                        Game game = data.getGame();
                        data.setLastDeath(p.getLocation().clone());
                        p.spigot().respawn();
                        if (!data.canRespawn()) {
                           p.sendMessage(Messages.get("NoMoreRespawn"));
                           game.leave(p, false);
                        } else {
                           data.minusRespawn();
                           data.setRespawning(true);
                           data.setRespawnCountdown(FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.Respawn.Countdown", 3));
                           p.setGameMode(GameMode.SPECTATOR);
                           p.teleport(data.getLastDeath());
                           if (this.protect.containsKey(p)) {
                              this.protect.remove(p);
                           }

                           this.protect.put(p, System.currentTimeMillis());
                        }
                     }
                  }, 1L);
               } catch (Exception var10) {
                  var10.printStackTrace();
                  System.out.println("§cError when respawn player " + p.getName());
               }
            } else {
               Bukkit.getScheduler().scheduleSyncDelayedTask(PhoBan.inst(), () -> {
                  p.spigot().respawn();
                  ((PlayerData)PlayerData.data().get(p)).getGame().leave(p, false);
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
               }, 1L);
            }

         }
      } else {
         BukkitAPIHelper mm = PhoBan.inst().getBukkitAPIHelper();
         if (mm.isMythicMob(e.getEntity())) {
            if (EntityData.data().containsKey(e.getEntity())) {
               EntityData data = (EntityData)EntityData.data().get(e.getEntity());
               Game game = data.getGame();
               game.addProgress(mm.getMythicMobInternalName(e.getEntity()), 1);
               int max = game.getProgressMax();
               int current = game.getProgressCurrent();
               String name = mm.getMythicMobDisplayNameGet((Entity)e.getEntity());
               Iterator var8 = game.getPlayers().iterator();

               while(var8.hasNext()) {
                  Player player = (Player)var8.next();
                  player.sendMessage(Messages.get("MobsLeft").replace("<name>", name).replace("<max>", max + "").replace("<current>", current + ""));
               }

               EntityData.data().remove(e.getEntity());
            }
         }
      }
   }

   private boolean noProtect(Player p) {
      if (!this.protect.containsKey(p)) {
         return true;
      } else {
         long current = System.currentTimeMillis();
         int respawnProtect = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.RespawnProtect");
         long deathTime = (Long)this.protect.get(p);
         return (current - deathTime) / 1000L > (long)(respawnProtect + FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.Respawn.Countdown", 3));
      }
   }

   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
      if (e.getEntity() instanceof Player) {
         Player p = (Player)e.getEntity();
         if (!this.noProtect(p)) {
            e.setCancelled(true);
         }

      }
   }
}

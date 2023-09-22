package camchua.phoban.game;

import camchua.phoban.manager.FileManager;
import camchua.phoban.utils.Messages;
import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameTask implements Runnable {
   private Game game;
   private int countdown;

   public GameTask(Game g) {
      this.game = g;
      this.countdown = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.StartCountdown");
   }

   public void run() {
      if (this.game.getStatus().equals(GameStatus.WAITING)) {
         this.countdown = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.StartCountdown");
         if (this.game.isFull()) {
            this.game.starting();
         }
      }

      Iterator var1;
      Player p;
      if (this.game.getStatus().equals(GameStatus.STARTING)) {
         if (this.game.getPlayers().size() > 0) {
            if (Messages.has("StartCountdown." + this.countdown)) {
               var1 = this.game.getPlayers().iterator();

               while(var1.hasNext()) {
                  p = (Player)var1.next();
                  p.sendMessage(Messages.get("StartCountdown." + this.countdown));
               }
            }

            --this.countdown;
            if (this.countdown <= 0) {
               this.game.start();
               this.countdown = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.StageCountdown");
            }
         } else {
            this.game.setStatus(GameStatus.WAITING);
            this.countdown = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.StartCountdown");
         }
      }

      if (this.game.getStatus().equals(GameStatus.PLAYING)) {
         if (Messages.has("TimeRemaining." + this.game.getTimeLeft())) {
            var1 = this.game.getPlayers().iterator();

            while(var1.hasNext()) {
               p = (Player)var1.next();
               p.sendMessage(Messages.get("TimeRemaining." + this.game.getTimeLeft()));
            }
         }

         var1 = this.game.getPlayers().iterator();

         while(var1.hasNext()) {
            p = (Player)var1.next();
            PlayerData data = (PlayerData)PlayerData.data().get(p);
            if (data != null && data.isRespawning()) {
               if (data.getRespawnCountdown() <= 0) {
                  data.setRespawning(false);
                  p.setGameMode(GameMode.SURVIVAL);
                  p.sendMessage(Messages.get("Respawn").replace("<amount>", String.valueOf(data.remainRespawn())));
                  if (FileManager.getFileConfig(FileManager.Files.CONFIG).getBoolean("Settings.Checkpoint")) {
                     if (data.hasDeath()) {
                        p.teleport(data.getLastDeath());
                     } else {
                        p.teleport(this.game.mobLocation());
                     }
                  } else {
                     p.teleport(this.game.mobLocation());
                  }
               } else {
                  p.setGameMode(GameMode.SPECTATOR);
                  String title = Messages.get("Respawning.Title").replace("<time>", String.valueOf(data.getRespawnCountdown()));
                  String subtitle = Messages.get("Respawning.Subtitle").replace("<time>", String.valueOf(data.getRespawnCountdown()));
                  p.sendTitle(title, subtitle);
                  data.respawnCountdown();
               }
            }
         }

         if (!this.game.quit_countdown) {
            this.game.time();
         }

         if (this.game.getTimeLeft() <= 0) {
            var1 = this.game.getPlayers().iterator();

            while(var1.hasNext()) {
               p = (Player)var1.next();
               p.sendMessage(Messages.get("TimeOut"));
            }

            this.game.forceStop();
            this.game.restore();
            this.game.resetTime();
            return;
         }

         if (this.game.getPlayers().size() > 0) {
            if (this.game.stage_countdown) {
               if (Messages.has("StageCountdown." + this.countdown)) {
                  var1 = this.game.getPlayers().iterator();

                  while(var1.hasNext()) {
                     p = (Player)var1.next();
                     p.sendMessage(Messages.get("StageCountdown." + this.countdown));
                  }
               }

               --this.countdown;
               if (this.countdown <= 0) {
                  this.game.nextStage();
                  boolean neww = this.game.newStage();
                  if (neww) {
                     this.countdown = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.StageCountdown");
                  } else {
                     this.countdown = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.QuitCountdown");
                  }
               }
            } else if (this.game.quit_countdown) {
               if (Messages.has("QuitCountdown." + this.countdown)) {
                  var1 = this.game.getPlayers().iterator();

                  while(var1.hasNext()) {
                     p = (Player)var1.next();
                     p.sendMessage(Messages.get("QuitCountdown." + this.countdown));
                  }
               }

               --this.countdown;
               if (this.countdown <= 0) {
                  this.game.leaveAllAfterComplete();
                  this.countdown = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.StartCountdown");
               }
            } else {
               if (Messages.has("StageTime." + this.game.getStageTime())) {
                  var1 = this.game.getPlayers().iterator();

                  while(var1.hasNext()) {
                     p = (Player)var1.next();
                     p.sendMessage(Messages.get("StageTime." + this.game.getStageTime()));
                  }
               }

               if (this.game.getStageTime() <= 0 && this.game.getStageTime() != -1) {
                  this.game.clearCurrentStage();
                  this.game.newStage();
               } else {
                  this.game.checkStage();
               }
            }
         } else {
            this.game.setStatus(GameStatus.WAITING);
            this.game.restore();
            this.game.resetTime();
         }
      }

   }

   public void setCountdown(int countdown) {
      this.countdown = countdown;
   }
}

// Decompiled with: Procyon 0.6.0
// Class Version: 8
package camchua.phoban.game;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import camchua.phoban.utils.Random;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import camchua.phoban.mythicmobs.BukkitAPIHelper;
import java.util.concurrent.ThreadLocalRandom;
import camchua.phoban.utils.Messages;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import camchua.phoban.manager.FileManager;
import org.bukkit.OfflinePlayer;
import java.lang.reflect.Field;
import java.util.Iterator;
import camchua.phoban.utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import camchua.phoban.PhoBan;
import java.util.Collection;
import java.util.ArrayList;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.HashMap;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.Location;
import java.util.LinkedHashMap;

public class Game
{
   public static final int maxStage = 10;
   private static LinkedHashMap<String, Game> game;
   private String name;
   private GameStatus status;
   private GameTask task;
   private int maxtime;
   private Location spawn;
   private int max_players;
   private int time;
   private int stageTime;
   private List<Player> players;
   private LinkedHashMap<String, HashMap<String, Integer>> stage1;
   private LinkedHashMap<String, HashMap<String, Integer>> stage2;
   private LinkedHashMap<String, HashMap<String, Integer>> stage3;
   private LinkedHashMap<String, HashMap<String, Integer>> stage4;
   private LinkedHashMap<String, HashMap<String, Integer>> stage5;
   private LinkedHashMap<String, HashMap<String, Integer>> stage6;
   private LinkedHashMap<String, HashMap<String, Integer>> stage7;
   private LinkedHashMap<String, HashMap<String, Integer>> stage8;
   private LinkedHashMap<String, HashMap<String, Integer>> stage9;
   private LinkedHashMap<String, HashMap<String, Integer>> stage10;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage1;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage2;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage3;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage4;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage5;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage6;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage7;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage8;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage9;
   private LinkedHashMap<String, HashMap<String, Integer>> timeStage10;
   private HashMap<String, Integer> boss;
   private HashMap<String, Integer> timeBoss;
   private List<HashMap<String, HashMap<String, Integer>>> stage;
   private List<HashMap<String, HashMap<String, Integer>>> timeStage;
   private int current_stage;
   private int stage_count;
   private HashMap<String, Integer> current_progress;
   private FileConfiguration room;
   private File configFile;
   private String type;
   private int realStage;
   public boolean stage_countdown;
   public boolean quit_countdown;

   public static LinkedHashMap<String, Game> game() {
      return Game.game;
   }

   public static Game getGame(final String name) {
      if (!Game.game.containsKey(name)) {
         return null;
      }
      return Game.game.get(name);
   }

   public static List<String> listGame() {
      return new ArrayList<String>(Game.game.keySet());
   }

   public static List<String> listGameWithoutCompleteSetup() {
      final File folder = new File(PhoBan.inst().getDataFolder(), "room" + File.separator);
      if (!folder.exists()) {
         folder.mkdirs();
      }
      final List<String> listGame = new ArrayList<String>();
      for (final File file : folder.listFiles()) {
         if (file.getName().contains(".yml")) {
            final String name = file.getName().replace(".yml", "");
            listGame.add(name);
         }
      }
      return listGame;
   }

   public static void convertData() {
      final File oldFile = new File(PhoBan.inst().getDataFolder(), "room.yml");
      if (!oldFile.exists()) {
         return;
      }
      final FileConfiguration room = YamlConfiguration.loadConfiguration(oldFile);
      for (final String key : room.getKeys(false)) {
         final File newFile = new File(PhoBan.inst().getDataFolder(), File.separator + "room" + File.separator + key + ".yml");
         if (!newFile.exists()) {
            try {
               newFile.createNewFile();
            }
            catch (final Exception ex) {}
         }
         final FileConfiguration config = new YamlConfiguration();
         Utils.scanSection(room, config, key, key);
         try {
            config.save(newFile);
         }
         catch (final Exception ex2) {}
      }
      oldFile.delete();
   }

   public static List<String> listType() {
      final List<String> listType = new ArrayList<String>();
      for (final Game g : game().values()) {
         if (!listType.contains(g.getType())) {
            listType.add(g.getType());
         }
      }
      return listType;
   }

   public static void load() {
      final File folder = new File(PhoBan.inst().getDataFolder(), "room" + File.separator);
      if (!folder.exists()) {
         folder.mkdirs();
      }
      for (final File file : folder.listFiles()) {
         if (file.getName().contains(".yml")) {
            final String name = file.getName().replace(".yml", "");
            final FileConfiguration room = YamlConfiguration.loadConfiguration(file);
            if (canJoin(room)) {
               load(name, room, file);
            }
         }
      }
   }

   public static void load(final String name, final FileConfiguration room, final File configFile) {
      if (Game.game.containsKey(name)) {
         Game.game.remove(name);
      }
      final int time = room.getInt("Time") * 60;
      final Location spawn = (Location)room.get("Spawn");
      final int max_players = room.getInt("Player");
      final String boss_type = room.getString("Boss.Type");
      final int boss_amount = room.getInt("Boss.Amount");
      final int timeStageBoss = room.getInt("Boss.Time") * 60;
      final HashMap<String, Integer> boss = new HashMap<String, Integer>();
      boss.put(boss_type, boss_amount);
      final HashMap<String, Integer> timeBoss = new HashMap<String, Integer>();
      timeBoss.put(boss_type, timeStageBoss);
      final String roomType = room.getString("Type");
      final Game g = new Game(name, time, spawn, max_players, boss, timeBoss, room, roomType, configFile);
      for (int i = 1; i <= 10; ++i) {
         if (room.contains("Mob" + i)) {
            final LinkedHashMap<String, HashMap<String, Integer>> stageHash = new LinkedHashMap<String, HashMap<String, Integer>>();
            final LinkedHashMap<String, HashMap<String, Integer>> timeStageHash = new LinkedHashMap<String, HashMap<String, Integer>>();
            for (final String key : room.getConfigurationSection("Mob" + i).getKeys(false)) {
               final String type = room.getString("Mob" + i + "." + key + ".Type");
               final int amount = room.getInt("Mob" + i + "." + key + ".Amount");
               int timeStage = room.getInt("Mob" + i + "." + key + ".Time") * 60;
               if (timeStage == 0) {
                  timeStage = -1;
               }
               final HashMap<String, Integer> a = new HashMap<String, Integer>();
               a.put(type, amount);
               final HashMap<String, Integer> b = new HashMap<String, Integer>();
               b.put(type, timeStage);
               stageHash.put(key, a);
               timeStageHash.put(key, b);
            }
            try {
               final Field stageField = g.getClass().getDeclaredField("stage" + i);
               stageField.setAccessible(true);
               stageField.set(g, stageHash);
               stageField.setAccessible(false);
               final Field timeStageField = g.getClass().getDeclaredField("timeStage" + i);
               timeStageField.setAccessible(true);
               timeStageField.set(g, timeStageHash);
               timeStageField.setAccessible(false);
            }
            catch (final Exception ex) {
               ex.printStackTrace();
            }
         }
      }
      g.init();
      Game.game.put(name, g);
   }

   public static void deleteRoom(final String room) {
      if (Game.game.containsKey(room)) {
         Game.game.remove(room);
      }
   }

   public static boolean canJoin(final FileConfiguration room) {
      int roomContains = 0;
      for (int i = 1; i <= 10; ++i) {
         if (room.contains("Mob" + i)) {
            ++roomContains;
         }
      }
      return room.contains("Prefix") && room.contains("Player") && room.contains("Time") && room.contains("Boss") && room.contains("Reward") && roomContains >= 3 && room.contains("RewardAmount") && room.contains("Spawn") && room.contains("Type");
   }

   public static int getTurn(final OfflinePlayer p, final String type) {
      final FileConfiguration data = FileManager.getFileConfig(FileManager.Files.DATA);
      if (!data.contains(p.getName() + ".Turn." + type)) {
         return 1;
      }
      return data.getInt(p.getName() + ".Turn." + type);
   }

   public static void giveTurn(final OfflinePlayer p, final String type, final int amount) {
      final FileConfiguration data = FileManager.getFileConfig(FileManager.Files.DATA);
      final int turn = getTurn(p, type) + amount;
      data.set(p.getName() + ".Turn." + type, turn);
      FileManager.saveFileConfig(data, FileManager.Files.DATA);
   }

   public static void giveTurn(final OfflinePlayer[] listPlayer, final List<String> types, final int amount) {
      final FileConfiguration data = FileManager.getFileConfig(FileManager.Files.DATA);
      for (final OfflinePlayer player : listPlayer) {
         for (final String type : types) {
            final int turn = getTurn(player, type) + amount;
            data.set(player.getName() + ".Turn." + type, turn);
         }
      }
      FileManager.saveFileConfig(data, FileManager.Files.DATA);
   }

   public static void giveTurnChangeDay(final OfflinePlayer[] listPlayer, final List<String> types, final int amount) {
      final FileConfiguration data = FileManager.getFileConfig(FileManager.Files.DATA);
      for (final OfflinePlayer player : listPlayer) {
         for (final String type : types) {
            final int currentTurn = getTurn(player, type);
            if (currentTurn > 0) {
               continue;
            }
            final int turn = currentTurn + amount;
            data.set(player.getName() + ".Turn." + type, turn);
         }
      }
      FileManager.saveFileConfig(data, FileManager.Files.DATA);
   }

   public static void takeTurn(final OfflinePlayer p, final String type, final int amount) {
      final FileConfiguration data = FileManager.getFileConfig(FileManager.Files.DATA);
      final int turn = getTurn(p, type) - amount;
      data.set(p.getName() + ".Turn." + type, turn);
      FileManager.saveFileConfig(data, FileManager.Files.DATA);
   }

   public static boolean hasTurn(final OfflinePlayer p, final String type) {
      return getTurn(p, type) > 0;
   }

   public static void setGlobalSpawn(final Location loc) {
      final FileConfiguration config = FileManager.getFileConfig(FileManager.Files.SPAWN);
      config.set("s", loc.clone());
      FileManager.saveFileConfig(config, FileManager.Files.SPAWN);
   }

   public static Location getGlobalSpawn() {
      final FileConfiguration config = FileManager.getFileConfig(FileManager.Files.SPAWN);
      return (Location)config.get("s");
   }

   public Game(final String name, final int time, final Location spawn, final int max_players, final HashMap<String, Integer> boss, final HashMap<String, Integer> timeBoss, final FileConfiguration room, final String type, final File configFile) {
      this.stage = new ArrayList<HashMap<String, HashMap<String, Integer>>>();
      this.timeStage = new ArrayList<HashMap<String, HashMap<String, Integer>>>();
      this.realStage = 1;
      this.stage_countdown = false;
      this.quit_countdown = false;
      this.name = name;
      this.status = GameStatus.WAITING;
      this.maxtime = time;
      this.spawn = spawn;
      this.max_players = max_players;
      this.time = time;
      this.stageTime = 0;
      this.players = new ArrayList<Player>();
      this.current_stage = 0;
      this.boss = boss;
      this.timeBoss = timeBoss;
      this.stage = new ArrayList<HashMap<String, HashMap<String, Integer>>>();
      this.current_stage = 1;
      this.stage_count = -1;
      this.current_progress = new HashMap<String, Integer>();
      this.room = room;
      this.configFile = configFile;
      this.type = type;
   }

   public String getName() {
      return this.name;
   }

   public GameStatus getStatus() {
      return this.status;
   }

   public void setStatus(final GameStatus status) {
      this.status = status;
   }

   public List<Player> getPlayers() {
      return this.players;
   }

   public int getTimeLeft() {
      return this.time;
   }

   public void time() {
      --this.time;
      if (this.stageTime > 0) {
         --this.stageTime;
      }
   }

   public void resetTime() {
      this.time = this.maxtime;
   }

   public int getStageTime() {
      return this.stageTime;
   }

   public void clearCurrentStage() {
      this.clearMobs();
   }

   public void init() {
      this.initStage();
      this.task = new GameTask(this);
      Bukkit.getScheduler().scheduleSyncRepeatingTask(PhoBan.inst(), this.task, 20L, 20L);
   }

   private void initStage() {
      for (int i = 1; i <= 10; ++i) {
         try {
            final Field stageField = this.getClass().getDeclaredField("stage" + i);
            final LinkedHashMap<String, HashMap<String, Integer>> stageHash = (LinkedHashMap<String, HashMap<String, Integer>>)stageField.get(this);
            if (stageHash != null) {
               for (final String key : stageHash.keySet()) {
                  final HashMap<String, HashMap<String, Integer>> h = new HashMap<String, HashMap<String, Integer>>();
                  h.put(key, stageHash.get(key));
                  this.stage.add(h);
               }
            }
         }
         catch (final Exception ex) {
            ex.printStackTrace();
         }
      }
      HashMap<String, HashMap<String, Integer>> h2 = new HashMap<String, HashMap<String, Integer>>();
      h2.put("Boss", this.boss);
      this.stage.add(h2);
      for (int i = 1; i <= 10; ++i) {
         try {
            final Field timeStageField = this.getClass().getDeclaredField("timeStage" + i);
            final LinkedHashMap<String, HashMap<String, Integer>> timeStageHash = (LinkedHashMap<String, HashMap<String, Integer>>)timeStageField.get(this);
            if (timeStageHash != null) {
               for (final String key : timeStageHash.keySet()) {
                  final HashMap<String, HashMap<String, Integer>> h = new HashMap<String, HashMap<String, Integer>>();
                  h.put(key, timeStageHash.get(key));
                  this.timeStage.add(h);
               }
            }
         }
         catch (final Exception ex) {
            ex.printStackTrace();
         }
      }
      h2 = new HashMap<String, HashMap<String, Integer>>();
      h2.put("Boss", this.timeBoss);
      this.timeStage.add(h2);
   }

   public void addProgress(final String key, final int value) {
      if (!this.current_progress.containsKey(key)) {
         this.current_progress.put(key, value);
      }
      else {
         this.current_progress.replace(key, this.current_progress.get(key) + value);
      }
   }

   public HashMap<String, Integer> getStage(final int s) {
      final Iterator iterator = this.stage.get(s).keySet().iterator();
      if (iterator.hasNext()) {
         final String key = (String)iterator.next();
         return (HashMap)this.stage.get(s).get(key);
      }
      return new HashMap<String, Integer>();
   }

   public HashMap<String, Integer> getTimeStage(final int s) {
      final Iterator iterator = this.timeStage.get(s).keySet().iterator();
      if (iterator.hasNext()) {
         final String key = (String)iterator.next();
         return (HashMap)this.timeStage.get(s).get(key);
      }
      return new HashMap<String, Integer>();
   }

   public String getStageKey(final int s) {
      final Iterator iterator = this.stage.get(s).keySet().iterator();
      if (iterator.hasNext()) {
         final String key = (String)iterator.next();
         return key;
      }
      return "";
   }

   public int keyToStage(final String key) {
      for (int i = 1; i <= 10; ++i) {
         try {
            final Field stageField = this.getClass().getDeclaredField("stage" + i);
            final HashMap<String, HashMap<String, Integer>> stageHash = (HashMap<String, HashMap<String, Integer>>)stageField.get(this);
            if (stageHash != null) {
               for (final String k : stageHash.keySet()) {
                  if (k.equals(key)) {
                     return i;
                  }
               }
            }
         }
         catch (final Exception ex) {
            ex.printStackTrace();
         }
      }
      return 11;
   }

   public boolean hasStage(final int s) {
      if (s > 10) {
         return true;
      }
      try {
         final Field stageField = this.getClass().getDeclaredField("stage" + s);
         final HashMap<String, HashMap<String, Integer>> stageHash = (HashMap<String, HashMap<String, Integer>>)stageField.get(this);
         return stageHash != null;
      }
      catch (final Exception ex) {
         ex.printStackTrace();
         return false;
      }
   }

   public int keyToTurn(final String key) {
      final int stage = this.keyToStage(key);
      int turn = 0;
      for (int i = 1; i <= 10; ++i) {
         turn = 0;
         try {
            final Field stageField = this.getClass().getDeclaredField("stage" + i);
            final HashMap<String, HashMap<String, Integer>> stageHash = (HashMap<String, HashMap<String, Integer>>)stageField.get(this);
            if (stageHash != null) {
               for (final String k : stageHash.keySet()) {
                  if (k.equals(key)) {
                     return turn + 1;
                  }
                  ++turn;
               }
            }
         }
         catch (final Exception ex) {
            ex.printStackTrace();
         }
      }
      return turn;
   }

   public void checkStage() {
      if (Utils.checkStage(this.getStage(this.stage_count), this.current_progress)) {
         this.newStage();
      }
   }

   public int getProgressLeft() {
      final Iterator<String> iterator = this.current_progress.keySet().iterator();
      if (!iterator.hasNext()) {
         return 0;
      }
      final String key = iterator.next();
      if (!this.current_progress.containsKey(key)) {
         return 0;
      }
      return this.getProgressMax() - this.current_progress.get(key);
   }

   public int getProgressCurrent() {
      final Iterator<String> iterator = this.current_progress.keySet().iterator();
      if (!iterator.hasNext()) {
         return 0;
      }
      final String key = iterator.next();
      if (!this.current_progress.containsKey(key)) {
         return 0;
      }
      return this.current_progress.get(key);
   }

   public int getProgressMax() {
      final Iterator<String> iterator = this.getStage(this.stage_count).keySet().iterator();
      if (!iterator.hasNext()) {
         return 0;
      }
      final String key = iterator.next();
      if (!this.getStage(this.stage_count).containsKey(key)) {
         return 0;
      }
      return this.getStage(this.stage_count).get(key);
   }

   public boolean newStage() {
      if (this.stage_count + 1 >= this.stage.size()) {
         this.complete();
         return false;
      }
      final String nextStageKey = this.getStageKey(this.stage_count + 1);
      final int nextStage = this.keyToStage(nextStageKey);
      while (!this.hasStage(this.current_stage)) {
         ++this.current_stage;
      }
      if (nextStage > this.current_stage) {
         this.stage_countdown = true;
         ++this.realStage;
         return true;
      }
      this.stageTime = 0;
      this.stage_countdown = false;
      ++this.stage_count;
      this.current_progress = new HashMap<String, Integer>();
      final int radius = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.SpawnRadius");
      final HashMap<String, Integer> currentStage = this.getStage(this.stage_count);
      for (final String key : currentStage.keySet()) {
         final int amount = currentStage.get(key);
         final int timeStage = this.getTimeStage(this.stage_count).get(key);
         if (timeStage > 0) {
            this.stageTime = timeStage;
         }
         else {
            this.stageTime = -1;
         }
         String mob = "Boss";
         for (int i = 1; i <= 10; ++i) {
            if (this.current_stage == i) {
               mob = "Mob" + i;
            }
         }
         final String keyy = mob.equals("Boss") ? "" : ("." + this.getStageKey(this.stage_count));
         final Location loc = (Location)this.room.get(mob + keyy + ".Location");
         loc.add(0.0, 1.0, 0.0);
         if (FileManager.getFileConfig(FileManager.Files.CONFIG).getBoolean("Settings.TeleportNewStage")) {
            for (final Player player : this.players) {
               player.teleport(loc);
            }
         }
         loc.subtract(0.0, 1.0, 0.0);
         final BukkitAPIHelper mm = PhoBan.inst().getBukkitAPIHelper();
         for (final Player player2 : this.players) {
            final String displayName = mm.getMythicMobDisplayNameGet(key);
            final String title = Messages.get("StageInfo." + (mob.equals("Boss") ? "Boss" : "Mob") + ".Title").replace("&", "§").replace("<stage>", this.realStage + "").replace("<turn>", this.keyToTurn(this.getStageKey(this.stage_count)) + "").replace("<mob>", key).replace("<amount>", amount + "").replace("<name>", displayName);
            final String subtitle = Messages.get("StageInfo." + (mob.equals("Boss") ? "Boss" : "Mob") + ".Subtitle").replace("&", "§").replace("<stage>", this.realStage + "").replace("<turn>", this.keyToTurn(this.getStageKey(this.stage_count)) + "").replace("<mob>", key).replace("<amount>", amount + "").replace("<name>", displayName);
            player2.sendTitle(title, subtitle);
         }
         try {
            for (int j = 1; j <= amount; ++j) {
               double origin;
               double bound;
               Location spawn;
               for (origin = -radius, bound = radius + 0.1, spawn = loc.clone().add(ThreadLocalRandom.current().nextDouble(origin, bound), 1.0, ThreadLocalRandom.current().nextDouble(origin, bound)); Utils.isSuckBlock(spawn); spawn = loc.clone().add(ThreadLocalRandom.current().nextDouble(origin, bound), 1.0, ThreadLocalRandom.current().nextDouble(origin, bound))) {}
               if (!spawn.getChunk().isLoaded()) {
                  spawn.getChunk().load();
               }
               final Entity entity = mm.spawnMythicMob(key, spawn);
               EntityData.data().put(entity, new EntityData(entity, this));
            }
         }
         catch (final Exception e) {
            e.printStackTrace();
         }
      }
      return true;
   }

   public void nextStage() {
      ++this.current_stage;
   }

   public void start() {
      this.status = GameStatus.PLAYING;
      this.current_stage = 1;
      this.stage_count = -1;
      this.realStage = 1;
      this.stage_countdown = false;
      this.quit_countdown = false;
      this.newStage();
   }

   public void forceStop() {
      this.leaveAllAfterComplete();
   }

   public void join(final Player p) {
      if (PlayerData.data().containsKey(p)) {
         PlayerData.data().remove(p);
      }
      final String type = this.room.getString("Type");
      takeTurn(p, type, 1);
      final Location loc = p.getLocation();
      p.teleport(this.spawn);
      p.setGameMode(GameMode.SURVIVAL);
      PlayerData.data().put(p, new PlayerData(p, this, loc));
      this.players.add(p);
      p.sendMessage(Messages.get("LeaveOnJoin"));
      final String msg = Messages.get("PlayerJoin").replace("<player>", p.getName()).replace("<joined>", String.valueOf(this.players.size())).replace("<max>", String.valueOf(this.max_players));
      this.players.forEach(player -> player.sendMessage(msg));
   }

   public void leave(final Player p, final boolean message) {
      if (!PlayerData.data().containsKey(p)) {
         return;
      }
      this.players.remove(p);
      final Location location = PlayerData.data().get(p).getLocation();
      PlayerData.data().remove(p);
      new BukkitRunnable() {
         public void run() {
            p.teleport(location);
         }
      }.runTaskLater(PhoBan.inst(), 1L);
      p.setGameMode(GameMode.SURVIVAL);
      if (message) {
         final String msg = Messages.get("PlayerQuit").replace("<player>", p.getName()).replace("<joined>", String.valueOf(this.players.size())).replace("<max>", String.valueOf(this.max_players));
         this.players.forEach(player -> player.sendMessage(msg));
      }
   }

   private void clearMobs() {
      final List<EntityData> edata = new ArrayList<EntityData>(EntityData.data().values());
      for (final EntityData e : edata) {
         if (!e.getGame().equals(this)) {
            continue;
         }
         EntityData.data().remove(e.getEntity());
         e.getEntity().remove();
      }
   }

   public void restore() {
      this.players = new ArrayList<Player>();
      this.current_stage = 1;
      this.stage_count = -1;
      this.clearMobs();
      this.status = GameStatus.WAITING;
      this.time = this.maxtime;
   }

   public void complete() {
      final List<Player> playerss = new ArrayList<Player>(this.players);
      Bukkit.broadcastMessage(Messages.get("BroadcastComplete").replace("&", "§").replace("<player>", this.players.get(0).getName()).replace("<prefix>", this.room.getString("Prefix").replace("&", "§")));
      for (final Player p : playerss) {
         p.sendMessage(Messages.get("Complete"));
         this.reward(p);
      }
      this.quit_countdown = true;
      this.task.setCountdown(FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.QuitCountdown"));
   }

   public void leaveAllAfterComplete() {
      final List<Player> playerss = new ArrayList<Player>(this.players);
      for (final Player p : playerss) {
         this.leave(p, false);
      }
      this.restore();
   }

   public void reward(final Player p) {
      final Random random = new Random();
      for (final String key : this.room.getConfigurationSection("Reward").getKeys(false)) {
         final ItemStack item = this.room.getItemStack("Reward." + key + ".Item");
         final int chance = this.room.getInt("Reward." + key + ".Chance");
         random.addChance(item, chance);
      }
      final List<ItemStack> a = new ArrayList<ItemStack>();
      for (int i = 1; i <= this.room.getInt("RewardAmount"); ++i) {
         ItemStack item;
         for (item = (ItemStack)random.getRandomElement(); a.contains(item); item = (ItemStack)random.getRandomElement()) {}
         a.add(item);
         p.getInventory().addItem(new ItemStack[] { item });
      }
      for (final String cmd : FileManager.getFileConfig(FileManager.Files.CONFIG).getStringList("Settings.RewardCommand")) {
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("<player>", p.getName()));
      }
   }

   public void starting() {
      this.status = GameStatus.STARTING;
   }

   public boolean isFull() {
      return this.players.size() >= this.max_players;
   }

   public void setMaxPlayer(final int max) {
      this.max_players = max;
   }

   public void setMaxTime(final int time) {
      this.maxtime = time;
   }

   public void setSpawn(final Location spawn) {
      this.spawn = spawn;
   }

   public void spectator(final Player p) {
      p.setGameMode(GameMode.SPECTATOR);
   }

   public boolean isLeader(final Player p) {
      return this.players.size() > 0 && this.players.get(0).getName().equals(p.getName());
   }

   public Location mobLocation() {
      String mob = "Boss";
      for (int i = 1; i <= 10; ++i) {
         if (this.current_stage == i) {
            mob = "Mob" + i;
         }
      }
      final String keyy = mob.equals("Boss") ? "" : ("." + this.getStageKey(this.stage_count));
      final Location loc = (Location)this.room.get(mob + keyy + ".Location");
      return loc;
   }

   public FileConfiguration getConfig() {
      return this.room;
   }

   public void setConfig(final FileConfiguration config) {
      this.room = config;
   }

   public File getConfigFile() {
      return this.configFile;
   }

   public String getType() {
      return this.type;
   }

   static {
      Game.game = new LinkedHashMap<String, Game>();
   }
}

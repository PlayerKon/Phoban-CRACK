// Decompiled with: CFR 0.152
// Class Version: 8
package camchua.phoban;

//import camchua.phoban.LicenseKey;
import camchua.phoban.game.Game;
import camchua.phoban.game.GameListener;
import camchua.phoban.game.GameStatus;
import camchua.phoban.game.PlayerData;
import camchua.phoban.gui.ChooseTypeGui;
import camchua.phoban.gui.EditorGui;
import camchua.phoban.gui.PhoBanGui;
import camchua.phoban.gui.RewardGui;
import camchua.phoban.listener.PlayerCommandPreprocessListener;
import camchua.phoban.listener.PlayerQuitListener;
import camchua.phoban.manager.FileManager;
import camchua.phoban.mythicmobs.BukkitAPIHelper;
import camchua.phoban.utils.Messages;
import camchua.phoban.utils.PhoBanExpansion;
import camchua.phoban.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PhoBan
        extends JavaPlugin {
   private static PhoBan plugin;
   private static boolean premium;
   private static boolean run;
   private static String perm;
   private static LuckPerms luckperms;
   private boolean o = false;
   private List<String> n = new ArrayList<String>();
   private BukkitAPIHelper bukkitAPIHelper;

   public static PhoBan inst() {
      return plugin;
   }

   public void onEnable() {
      this.disableWarnASW();
      plugin = this;
      Utils.checkVersion();
      FileManager.setup(this);
      this.bukkitAPIHelper = new BukkitAPIHelper();
      Game.convertData();
      Game.load();
      Bukkit.getConsoleSender().sendMessage("§e[CRACK] CẢM ƠN DIHOA STORE VÌ MỘT PLUGIN HAY!");
      Bukkit.getConsoleSender().sendMessage("§e[DI HOA STORE] NHƯNG VẪN ĐỊT MẸ MÀY VÌ BÁN MẮC VÃI LỒN!");
      Bukkit.getConsoleSender().sendMessage("§e[DI HOA STORE] PHOBAN CRACK~~~~~~~~~~~");
      Bukkit.getConsoleSender().sendMessage("§e[DI HOA STORE] DISCORD : lernhtmlol");
      Bukkit.getConsoleSender().sendMessage("§e[DI HOA STORE] Product Information");
      Bukkit.getConsoleSender().sendMessage("§f| Product: §6Nova Skyblock");
      Bukkit.getConsoleSender().sendMessage("§f| Author: §aCamChua_VN");
      Bukkit.getConsoleSender().sendMessage("§f| Product Version: §a1.5");
      Bukkit.getConsoleSender().sendMessage("§f| Minecraft Verions: §a1.10 - 1.19.3");
      Bukkit.getConsoleSender().sendMessage("§f| Support: §aBukkit, Spigot, PaperMC, Purpur");
      Bukkit.getConsoleSender().sendMessage("§fOur Website: §ewww.dihoastore.net");
      premium = true;
      run = true;
      this.n.add("key error");
      Bukkit.getConsoleSender().sendMessage("§e[Nova Skyblock] §aThe plugin has been activated and it is working on your server");
      Bukkit.getPluginManager().registerEvents(new EditorGui(), this);
      Bukkit.getPluginManager().registerEvents(new RewardGui(), this);
      Bukkit.getPluginManager().registerEvents(new PhoBanGui(null, -1, ""), this);
      Bukkit.getPluginManager().registerEvents(new ChooseTypeGui(null, -1), this);
      Bukkit.getPluginManager().registerEvents(new GameListener(), this);
      Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(), this);
      Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
      if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
         new PhoBanExpansion().register();
      }
      if (Bukkit.getPluginManager().isPluginEnabled("PermissionsEx")) {
         perm = "pex";
      } else if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
         RegisteredServiceProvider provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
         if (provider != null) {
            luckperms = (LuckPerms)provider.getProvider();
         }
         perm = "lp";
      }
      Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){

         @Override
         public void run() {
            boolean checkHour;
            Calendar c = Calendar.getInstance();
            boolean everyDay = FileManager.getFileConfig(FileManager.Files.CONFIG).getString("Settings.AutoReset").equalsIgnoreCase("day");
            boolean bl = checkHour = everyDay && c.getTime().getHours() == 0 || !everyDay;
            if (checkHour && c.getTime().getMinutes() == 0 && c.getTime().getSeconds() == 0) {
               int defaultTurn = FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.DefaultTurn");
               List<String> listType = Game.listType();
               OfflinePlayer[] listPlayer = Bukkit.getOfflinePlayers();
               Game.giveTurnChangeDay(listPlayer, listType, defaultTurn);
            }
         }
      }, 20L, 20L);
      this.o = run;
      if (this.n.isEmpty() && "a".equals("a") && Integer.parseInt("1") == 1 && !this.o) {
         this.n = null;
      }
      if (this.n == null) {
         PluginManager pm = Bukkit.getServer().getPluginManager();
         pm.disablePlugin(this);
      }
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   public void onDisable() {
      try {
         for (String key : Game.game().keySet()) {
            Game.game().get(key).forceStop();
         }
      }
      catch (Exception exception) {
         // empty catch block
      }
         Bukkit.getConsoleSender().sendMessage("§e[Nova Skyblock] §cThis plugin has been disabled");
      }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      if (!this.o && !premium) {
         return true;
      }
      if (args.length == 0) {
         if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.get("NotPlayer"));
            return true;
         }
         if (Utils.isJail((Player)((Object)sender))) {
            return true;
         }
         new ChooseTypeGui((Player)((Object)sender), 0);
         return true;
      }
      if (args.length >= 1) {
         switch (args[0].toLowerCase()) {
            case "create": {
               if (!sender.hasPermission("phoban.admin")) {
                  sender.sendMessage(Messages.get("NoPermissions"));
                  return true;
               }
               if (!(sender instanceof Player)) {
                  sender.sendMessage(Messages.get("NotPlayer"));
                  return true;
               }
               if (args.length == 1) {
                  return true;
               }
               Player p = (Player)((Object)sender);
               if (Game.game().containsKey(args[1])) {
                  p.sendMessage(Messages.get("RoomExist"));
                  return true;
               }
               String name = args[1];
               EditorGui.open(p, name);
               File file = new File(this.getDataFolder(), "room" + File.separator + name + ".yml");
               try {
                  file.createNewFile();
               }
               catch (Exception exception) {
                  // empty catch block
               }
               return true;
            }
            case "edit": {
               if (!sender.hasPermission("phoban.admin")) {
                  sender.sendMessage(Messages.get("NoPermissions"));
                  return true;
               }
               if (!(sender instanceof Player)) {
                  sender.sendMessage(Messages.get("NotPlayer"));
                  return true;
               }
               if (args.length == 1) {
                  return true;
               }
               Player p = (Player)((Object)sender);
               if (!Game.listGameWithoutCompleteSetup().contains(args[1])) {
                  p.sendMessage(Messages.get("RoomNotExist"));
                  return true;
               }
               EditorGui.open(p, args[1]);
               return true;
            }
            case "add": {
               if (!sender.hasPermission("phoban.admin")) {
                  sender.sendMessage(Messages.get("NoPermissions"));
                  return true;
               }
               if (args.length == 1) {
                  return true;
               }
               if (args.length == 2) {
                  if (args[1].equalsIgnoreCase("all")) {
                     List<String> listType = Game.listType();
                     OfflinePlayer[] listPlayer = Bukkit.getOfflinePlayers();
                     Game.giveTurn(listPlayer, listType, FileManager.getFileConfig(FileManager.Files.CONFIG).getInt("Settings.DefaultTurn"));
                     sender.sendMessage(Messages.get("GiveTurnAll"));
                  }
                  return true;
               }
               if (args.length == 3) {
                  return true;
               }
               String player = args[1];
               String type = args[2];
               int amount = Integer.parseInt(args[3]);
               if (!Bukkit.getOfflinePlayer(player).isOnline()) {
                  sender.sendMessage(Messages.get("NotOnline"));
                  return true;
               }
               Game.giveTurn(Bukkit.getPlayer(player), type, amount);
               sender.sendMessage(Messages.get("GiveTurn").replace("<player>", player).replace("<amount>", amount + ""));
               return true;
            }
            case "reload": {
               if (!sender.hasPermission("phoban.admin")) {
                  sender.sendMessage(Messages.get("NoPermissions"));
                  return true;
               }
               sender.sendMessage("§aReloading...");
               try {
                  FileManager.setup(this);
                  sender.sendMessage("§aReload complete.");
               }
               catch (Exception ex) {
                  ex.printStackTrace();
                  sender.sendMessage("§cReload failed. Check console");
               }
               return true;
            }
            case "start": {
               if (!(sender instanceof Player)) {
                  sender.sendMessage(Messages.get("NotPlayer"));
                  return true;
               }
               Player p = (Player)((Object)sender);
               PlayerData data = PlayerData.data().get(p);
               if (data == null) {
                  return true;
               }
               if (!data.getGame().getPlayers().get(0).getName().equals(p.getName())) {
                  return true;
               }
               if (!data.getGame().getStatus().equals((Object)GameStatus.WAITING)) {
                  return true;
               }
               data.getGame().starting();
               return true;
            }
            case "help": {
               for (String mess : FileManager.getFileConfig(FileManager.Files.MESSAGE).getStringList("Help")) {
                  sender.sendMessage(mess.replace("&", "§"));
               }
               return true;
            }
            case "leave": {
               if (!(sender instanceof Player)) {
                  sender.sendMessage(Messages.get("NotPlayer"));
                  return true;
               }
               Player p = (Player)((Object)sender);
               if (!PlayerData.data().containsKey(p)) {
                  return true;
               }
               PlayerData.data().get(p).getGame().leave(p, true);
               return true;
            }
            case "list": {
               if (!sender.hasPermission("phoban.admin")) {
                  sender.sendMessage(Messages.get("NoPermissions"));
                  return true;
               }
               StringBuilder sb = new StringBuilder();
               for (String name : Game.listGameWithoutCompleteSetup()) {
                  sb.append(name).append(" ");
               }
               sender.sendMessage(Messages.get("ListRoom").replace("<rooms>", sb.toString()));
               return true;
            }
            case "join": {
               if (args.length == 1) {
                  return true;
               }
               if (!(sender instanceof Player)) {
                  sender.sendMessage(Messages.get("NotPlayer"));
                  return true;
               }
               Player p = (Player)((Object)sender);
               String name = args[1];
               Game game = Game.getGame(name);
               if (game == null) {
                  p.sendMessage(Messages.get("RoomNotExist"));
                  return true;
               }
               if (Utils.isJail(p)) {
                  return true;
               }
               if (game.getStatus().equals((Object)GameStatus.WAITING)) {
                  if (game.isFull()) {
                     p.sendMessage(Messages.get("RoomFull"));
                     return true;
                  }
                  if (!Game.canJoin(game.getConfig())) {
                     p.sendMessage(Messages.get("JoinRoomNotConfig"));
                     return true;
                  }
                  if (!Game.hasTurn(p, game.getType())) {
                     p.sendMessage(Messages.get("NoTurn"));
                     return true;
                  }
                  game.join(p);
                  if (game.isLeader(p)) {
                     p.sendMessage(Messages.get("LeaderStart"));
                  }
                  return true;
               }
               if (game.getStatus().equals((Object)GameStatus.STARTING) || game.getStatus().equals((Object)GameStatus.PLAYING)) {
                  p.sendMessage(Messages.get("RoomStarted"));
                  return true;
               }
               return true;
            }
         }
      }
      return false;
   }

   public boolean hasPerm(Player p, String permission) {
      switch (perm.toLowerCase()) {
         case "pex": {
            PermissionUser user = PermissionsEx.getUser(p);
            return user.getPermissions("world").contains(permission);
         }
         case "lp": {
            User user = luckperms.getUserManager().getUser(p.getName());
            return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
         }
      }
      return false;
   }

   public BukkitAPIHelper getBukkitAPIHelper() {
      return this.bukkitAPIHelper;
   }

   public void disableWarnASW() {
      File aswf = new File(this.getDataFolder().getParentFile(), File.separator + "AutoSaveWorld" + File.separator + "config.yml");
      if (aswf.exists()) {
         YamlConfiguration asw = new YamlConfiguration();
         try {
            asw.load(aswf);
         }
         catch (Exception exception) {
            // empty catch block
         }
         if (asw.getBoolean("networkwatcher.mainthreadnetaccess.warn")) {
            asw.set("networkwatcher.mainthreadnetaccess.warn", false);
            try {
               ((FileConfiguration)asw).save(aswf);
            }
            catch (Exception exception) {
               // empty catch block
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "asw reload");
         }
      }
   }

   static {
      premium = false;
      run = false;
      perm = "";
   }
}

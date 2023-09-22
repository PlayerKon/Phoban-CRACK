package camchua.phoban.mythicmobs;

import camchua.phoban.PhoBan;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class BukkitAPIHelper {
   private List<String> mmPackageAPI = Arrays.asList("io.lumine.mythic.bukkit", "io.lumine.xikage.mythicmobs.api.bukkit", "io.lumine.xikage.mythicmobs.api.bukkit");
   private Object apiClass;

   public BukkitAPIHelper() {
      this.scanPackage();
   }

   private void scanPackage() {
      Iterator var1 = this.mmPackageAPI.iterator();

      while(var1.hasNext()) {
         String packagee = (String)var1.next();

         try {
            String className = packagee + ".BukkitAPIHelper";
            this.apiClass = Class.forName(className).newInstance();
            PhoBan.inst().getLogger().info("MythicMobs API Class: " + className);
            break;
         } catch (Exception var4) {
         }
      }

   }

   public Entity spawnMythicMob(String key, Location loc) {
      try {
         return (Entity)this.apiClass.getClass().getMethod("spawnMythicMob", String.class, Location.class).invoke(this.apiClass, key, loc);
      } catch (InvocationTargetException var4) {
         var4.getCause().printStackTrace();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return null;
   }

   public boolean isMythicMob(Entity entity) {
      try {
         return (Boolean)this.apiClass.getClass().getMethod("isMythicMob", Entity.class).invoke(this.apiClass, entity);
      } catch (InvocationTargetException var3) {
         var3.getCause().printStackTrace();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return false;
   }

   public String getMythicMobDisplayNameGet(Entity entity) {
      try {
         Object activeInstance = this.apiClass.getClass().getMethod("getMythicMobInstance", Entity.class).invoke(this.apiClass, entity);
         Object mmInstance = activeInstance.getClass().getMethod("getType").invoke(activeInstance);
         Object displayNamePlaceholder = mmInstance.getClass().getMethod("getDisplayName").invoke(mmInstance);
         Object displayName = displayNamePlaceholder.getClass().getMethod("get").invoke(displayNamePlaceholder);
         return displayName == null ? "" : (String)displayName;
      } catch (InvocationTargetException var6) {
         var6.getCause().printStackTrace();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return null;
   }

   public String getMythicMobDisplayNameGet(String key) {
      try {
         Object mmInstance = this.apiClass.getClass().getMethod("getMythicMob", String.class).invoke(this.apiClass, key);
         Object displayNamePlaceholder = mmInstance.getClass().getMethod("getDisplayName").invoke(mmInstance);
         Object displayName = displayNamePlaceholder.getClass().getMethod("get").invoke(displayNamePlaceholder);
         return displayName == null ? "" : (String)displayName;
      } catch (InvocationTargetException var5) {
         var5.getCause().printStackTrace();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }

   public String getMythicMobInternalName(Entity entity) {
      try {
         Object activeInstance = this.apiClass.getClass().getMethod("getMythicMobInstance", Entity.class).invoke(this.apiClass, entity);
         Object mmInstance = activeInstance.getClass().getMethod("getType").invoke(activeInstance);
         Object internalName = mmInstance.getClass().getMethod("getInternalName").invoke(mmInstance);
         return (String)internalName;
      } catch (InvocationTargetException var5) {
         var5.getCause().printStackTrace();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }
}

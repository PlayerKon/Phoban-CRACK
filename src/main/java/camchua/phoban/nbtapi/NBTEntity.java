package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.MinecraftVersion;
import camchua.phoban.nbtapi.utils.annotations.AvailableSince;
import camchua.phoban.nbtapi.utils.annotations.CheckUtil;
import camchua.phoban.nbtapi.utils.annotations.FAUtil;
import java.lang.invoke.SerializedLambda;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class NBTEntity extends NBTCompound {
   private final Entity ent;

   public NBTEntity(Entity entity) {
      super(null, null);
      if (entity == null)
         throw new NullPointerException("Entity can't be null!");
      this.ent = entity;
   }

   public Object getCompound() {
      if (!Bukkit.isPrimaryThread())
         throw new NbtApiException("Entity NBT needs to be accessed sync!");
      return NBTReflectionUtil.getEntityNBTTagCompound(NBTReflectionUtil.getNMSEntity(this.ent));
   }

   protected void setCompound(Object compound) {
      if (!Bukkit.isPrimaryThread())
         throw new NbtApiException("Entity NBT needs to be accessed sync!");
      NBTReflectionUtil.setEntityNBTTag(compound, NBTReflectionUtil.getNMSEntity(this.ent));
   }

   @AvailableSince(version = MinecraftVersion.MC1_14_R1)
   public NBTCompound getPersistentDataContainer() {
      FAUtil.check(this::getPersistentDataContainer, CheckUtil::isAvaliable);
      return new NBTPersistentDataContainer(this.ent.getPersistentDataContainer());
   }
}

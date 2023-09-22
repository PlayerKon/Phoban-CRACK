package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.MinecraftVersion;
import camchua.phoban.nbtapi.utils.annotations.AvailableSince;
import camchua.phoban.nbtapi.utils.annotations.CheckUtil;
import camchua.phoban.nbtapi.utils.annotations.FAUtil;
import java.lang.invoke.SerializedLambda;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;

public class NBTTileEntity extends NBTCompound {
   private final BlockState tile;

   public NBTTileEntity(BlockState tile) {
      super(null, null);
      if (tile == null || (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_8_R3) && !tile.isPlaced()))
         throw new NullPointerException("Tile can't be null/not placed!");
      this.tile = tile;
   }

   public Object getCompound() {
      if (!Bukkit.isPrimaryThread())
         throw new NbtApiException("BlockEntity NBT needs to be accessed sync!");
      return NBTReflectionUtil.getTileEntityNBTTagCompound(this.tile);
   }

   protected void setCompound(Object compound) {
      if (!Bukkit.isPrimaryThread())
         throw new NbtApiException("BlockEntity NBT needs to be accessed sync!");
      NBTReflectionUtil.setTileEntityNBTTagCompound(this.tile, compound);
   }

   @AvailableSince(version = MinecraftVersion.MC1_14_R1)
   public NBTCompound getPersistentDataContainer() {
      FAUtil.check(this::getPersistentDataContainer, CheckUtil::isAvaliable);
      if (hasTag("PublicBukkitValues"))
         return getCompound("PublicBukkitValues");
      NBTContainer container = new NBTContainer();
      container.addCompound("PublicBukkitValues").setString("__nbtapi", "Marker to make the PersistentDataContainer have content");
      mergeCompound(container);
      return getCompound("PublicBukkitValues");
   }
}

package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.MinecraftVersion;
import camchua.phoban.nbtapi.utils.annotations.AvailableSince;
import camchua.phoban.nbtapi.utils.annotations.CheckUtil;
import camchua.phoban.nbtapi.utils.annotations.FAUtil;
import java.lang.invoke.SerializedLambda;
import org.bukkit.Chunk;

public class NBTChunk {
   private final Chunk chunk;

   public NBTChunk(Chunk chunk) {
      this.chunk = chunk;
   }

   @AvailableSince(version = MinecraftVersion.MC1_16_R3)
   public NBTCompound getPersistentDataContainer() {
      FAUtil.check(this::getPersistentDataContainer, CheckUtil::isAvaliable);
      return new NBTPersistentDataContainer(this.chunk.getPersistentDataContainer());
   }
}

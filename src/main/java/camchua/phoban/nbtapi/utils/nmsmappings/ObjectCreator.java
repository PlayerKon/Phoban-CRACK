package camchua.phoban.nbtapi.utils.nmsmappings;

import camchua.phoban.nbtapi.NbtApiException;
import camchua.phoban.nbtapi.utils.MinecraftVersion;
import java.lang.reflect.Constructor;
import java.util.logging.Level;

public enum ObjectCreator {
   NMS_NBTTAGCOMPOUND((MinecraftVersion)null, (MinecraftVersion)null, ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), new Class[0]),
   NMS_BLOCKPOSITION((MinecraftVersion)null, (MinecraftVersion)null, ClassWrapper.NMS_BLOCKPOSITION.getClazz(), new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}),
   NMS_COMPOUNDFROMITEM(MinecraftVersion.MC1_11_R1, (MinecraftVersion)null, ClassWrapper.NMS_ITEMSTACK.getClazz(), new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()});

   private Constructor<?> construct;
   private Class<?> targetClass;

   private ObjectCreator(MinecraftVersion from, MinecraftVersion to, Class<?> clazz, Class<?>... args) {
      if (clazz != null) {
         if (from == null || MinecraftVersion.getVersion().getVersionId() >= from.getVersionId()) {
            if (to == null || MinecraftVersion.getVersion().getVersionId() <= to.getVersionId()) {
               try {
                  this.targetClass = clazz;
                  this.construct = clazz.getDeclaredConstructor(args);
                  this.construct.setAccessible(true);
               } catch (Exception var8) {
                  MinecraftVersion.getLogger().log(Level.SEVERE, "Unable to find the constructor for the class '" + clazz.getName() + "'", var8);
               }

            }
         }
      }
   }

   public Object getInstance(Object... args) {
      try {
         return this.construct.newInstance(args);
      } catch (Exception var3) {
         throw new NbtApiException("Exception while creating a new instance of '" + this.targetClass + "'", var3);
      }
   }

   // $FF: synthetic method
   private static ObjectCreator[] $values() {
      return new ObjectCreator[]{NMS_NBTTAGCOMPOUND, NMS_BLOCKPOSITION, NMS_COMPOUNDFROMITEM};
   }
}

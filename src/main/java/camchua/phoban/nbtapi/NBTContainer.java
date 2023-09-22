package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.nmsmappings.ClassWrapper;
import camchua.phoban.nbtapi.utils.nmsmappings.ObjectCreator;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;
import java.io.InputStream;

public class NBTContainer extends NBTCompound {
   private Object nbt;

   public NBTContainer() {
      super((NBTCompound)null, (String)null);
      this.nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
   }

   public NBTContainer(Object nbt) {
      super((NBTCompound)null, (String)null);
      if (nbt == null) {
         throw new NullPointerException("The NBT-Object can't be null!");
      } else if (!ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().isAssignableFrom(nbt.getClass())) {
         throw new NbtApiException("The object '" + nbt.getClass() + "' is not a valid NBT-Object!");
      } else {
         this.nbt = nbt;
      }
   }

   public NBTContainer(InputStream inputsteam) {
      super((NBTCompound)null, (String)null);
      this.nbt = NBTReflectionUtil.readNBT(inputsteam);
   }

   public NBTContainer(String nbtString) {
      super((NBTCompound)null, (String)null);
      if (nbtString == null) {
         throw new NullPointerException("The String can't be null!");
      } else {
         try {
            this.nbt = ReflectionMethod.PARSE_NBT.run((Object)null, nbtString);
         } catch (Exception var3) {
            throw new NbtApiException("Unable to parse Malformed Json!", var3);
         }
      }
   }

   public Object getCompound() {
      return this.nbt;
   }

   public void setCompound(Object tag) {
      this.nbt = tag;
   }
}

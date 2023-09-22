package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.MinecraftVersion;
import camchua.phoban.nbtapi.utils.nmsmappings.ClassWrapper;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTCompoundList extends NBTList<NBTListCompound> {
   protected NBTCompoundList(NBTCompound owner, String name, NBTType type, Object list) {
      super(owner, name, type, list);
   }

   public NBTListCompound addCompound() {
      return (NBTListCompound)this.addCompound((NBTCompound)null);
   }

   public NBTCompound addCompound(NBTCompound comp) {
      try {
         Object compound = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
         if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
            ReflectionMethod.LIST_ADD.run(this.listObject, this.size(), compound);
         } else {
            ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, compound);
         }

         this.getParent().saveCompound();
         NBTListCompound listcomp = new NBTListCompound(this, compound);
         if (comp != null) {
            listcomp.mergeCompound(comp);
         }

         return listcomp;
      } catch (Exception var4) {
         throw new NbtApiException(var4);
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean add(NBTListCompound empty) {
      return this.addCompound(empty) != null;
   }

   public void add(int index, NBTListCompound element) {
      if (element != null) {
         throw new NbtApiException("You need to pass null! ListCompounds from other lists won't work.");
      } else {
         try {
            Object compound = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
            if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
               ReflectionMethod.LIST_ADD.run(this.listObject, index, compound);
            } else {
               ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, compound);
            }

            super.getParent().saveCompound();
         } catch (Exception var4) {
            throw new NbtApiException(var4);
         }
      }
   }

   public NBTListCompound get(int index) {
      try {
         Object compound = ReflectionMethod.LIST_GET_COMPOUND.run(this.listObject, index);
         return new NBTListCompound(this, compound);
      } catch (Exception var3) {
         throw new NbtApiException(var3);
      }
   }

   public NBTListCompound set(int index, NBTListCompound element) {
      throw new NbtApiException("This method doesn't work in the ListCompound context.");
   }

   protected Object asTag(NBTListCompound object) {
      return null;
   }
}

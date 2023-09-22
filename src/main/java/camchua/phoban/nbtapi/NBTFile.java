package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.nmsmappings.ObjectCreator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NBTFile extends NBTCompound {
   private final File file;
   private Object nbt;

   public NBTFile(File file) throws IOException {
      super((NBTCompound)null, (String)null);
      if (file == null) {
         throw new NullPointerException("File can't be null!");
      } else {
         this.file = file;
         if (file.exists()) {
            this.nbt = NBTReflectionUtil.readNBT(Files.newInputStream(file.toPath()));
         } else {
            this.nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance();
            this.save();
         }

      }
   }

   public void save() throws IOException {
      try {
         this.getWriteLock().lock();
         saveTo(this.file, this);
      } finally {
         this.getWriteLock().unlock();
      }

   }

   public File getFile() {
      return this.file;
   }

   public Object getCompound() {
      return this.nbt;
   }

   protected void setCompound(Object compound) {
      this.nbt = compound;
   }

   public static NBTCompound readFrom(File file) throws IOException {
      return !file.exists() ? new NBTContainer() : new NBTContainer(NBTReflectionUtil.readNBT(Files.newInputStream(file.toPath())));
   }

   public static void saveTo(File file, NBTCompound nbt) throws IOException {
      if (!file.exists()) {
         file.getParentFile().mkdirs();
         if (!file.createNewFile()) {
            throw new IOException("Unable to create file at " + file.getAbsolutePath());
         }
      }

      NBTReflectionUtil.writeNBT(nbt.getCompound(), Files.newOutputStream(file.toPath()));
   }
}

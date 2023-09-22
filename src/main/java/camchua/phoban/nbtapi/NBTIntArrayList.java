package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.nmsmappings.ClassWrapper;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NBTIntArrayList extends NBTList<int[]> {
   private final NBTContainer tmpContainer = new NBTContainer();

   protected NBTIntArrayList(NBTCompound owner, String name, NBTType type, Object list) {
      super(owner, name, type, list);
   }

   protected Object asTag(int[] object) {
      try {
         Constructor<?> con = ClassWrapper.NMS_NBTTAGINTARRAY.getClazz().getDeclaredConstructor(int[].class);
         con.setAccessible(true);
         return con.newInstance(object);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException var3) {
         throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", var3);
      }
   }

   public int[] get(int index) {
      try {
         Object obj = ReflectionMethod.LIST_GET.run(this.listObject, index);
         ReflectionMethod.COMPOUND_SET.run(this.tmpContainer.getCompound(), "tmp", obj);
         int[] val = this.tmpContainer.getIntArray("tmp");
         this.tmpContainer.removeKey("tmp");
         return val;
      } catch (NumberFormatException var4) {
         return null;
      } catch (Exception var5) {
         throw new NbtApiException(var5);
      }
   }
}

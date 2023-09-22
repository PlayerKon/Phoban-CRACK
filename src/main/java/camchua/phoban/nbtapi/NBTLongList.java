package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.nmsmappings.ClassWrapper;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NBTLongList extends NBTList<Long> {
   protected NBTLongList(NBTCompound owner, String name, NBTType type, Object list) {
      super(owner, name, type, list);
   }

   protected Object asTag(Long object) {
      try {
         Constructor<?> con = ClassWrapper.NMS_NBTTAGLONG.getClazz().getDeclaredConstructor(Long.TYPE);
         con.setAccessible(true);
         return con.newInstance(object);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException var3) {
         throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", var3);
      }
   }

   public Long get(int index) {
      try {
         Object obj = ReflectionMethod.LIST_GET.run(this.listObject, index);
         return Long.valueOf(obj.toString().replace("L", ""));
      } catch (NumberFormatException var3) {
         return 0L;
      } catch (Exception var4) {
         throw new NbtApiException(var4);
      }
   }
}

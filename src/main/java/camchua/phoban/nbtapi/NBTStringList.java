package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.nmsmappings.ClassWrapper;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NBTStringList extends NBTList<String> {
   protected NBTStringList(NBTCompound owner, String name, NBTType type, Object list) {
      super(owner, name, type, list);
   }

   public String get(int index) {
      try {
         return (String)ReflectionMethod.LIST_GET_STRING.run(this.listObject, index);
      } catch (Exception var3) {
         throw new NbtApiException(var3);
      }
   }

   protected Object asTag(String object) {
      try {
         Constructor<?> con = ClassWrapper.NMS_NBTTAGSTRING.getClazz().getDeclaredConstructor(String.class);
         con.setAccessible(true);
         return con.newInstance(object);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException var3) {
         throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", var3);
      }
   }
}

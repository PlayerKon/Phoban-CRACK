package camchua.phoban.nbtapi.utils;

import camchua.phoban.nbtapi.NbtApiException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionUtil {
   private static Field field_modifiers;

   public static Field makeNonFinal(Field field) throws IllegalArgumentException, IllegalAccessException {
      int mods = field.getModifiers();
      if (Modifier.isFinal(mods)) {
         field_modifiers.set(field, mods & -17);
      }

      return field;
   }

   public static void setFinal(Object obj, Field field, Object newValue) throws IllegalArgumentException, IllegalAccessException {
      field.setAccessible(true);
      field = makeNonFinal(field);
      field.set(obj, newValue);
   }

   static {
      try {
         field_modifiers = Field.class.getDeclaredField("modifiers");
         field_modifiers.setAccessible(true);
      } catch (NoSuchFieldException var8) {
         try {
            Method fieldGetter = Class.class.getDeclaredMethod("getDeclaredFields0", Boolean.TYPE);
            fieldGetter.setAccessible(true);
            Field[] fields = (Field[])fieldGetter.invoke(Field.class, false);
            Field[] var3 = fields;
            int var4 = fields.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Field f = var3[var5];
               if (f.getName().equals("modifiers")) {
                  field_modifiers = f;
                  field_modifiers.setAccessible(true);
                  break;
               }
            }
         } catch (Exception var7) {
            throw new NbtApiException(var7);
         }
      }

      if (field_modifiers == null) {
         throw new NbtApiException("Unable to init the modifiers Field.");
      }
   }
}

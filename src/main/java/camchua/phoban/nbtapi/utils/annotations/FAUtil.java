package camchua.phoban.nbtapi.utils.annotations;

import camchua.phoban.nbtapi.utils.annotations.ref.MethodRefrence;
import camchua.phoban.nbtapi.utils.annotations.ref.MethodRefrence1;
import camchua.phoban.nbtapi.utils.annotations.ref.MethodRefrence2;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.function.Function;

public class FAUtil {
   public static <T> T getAnnotation(MethodRefrence method, Class<T> annotation) {
      return (T)getInternalMethod(method).getAnnotation((Class)annotation);
   }

   public static <T, Z> T getAnnotation(MethodRefrence1<Z> method, Class<T> annotation) {
      return (T)getInternalMethod(method).getAnnotation((Class)annotation);
   }

   public static <T, Z, X> T getAnnotation(MethodRefrence2<Z, X> method, Class<T> annotation) {
      return (T)getInternalMethod(method).getAnnotation((Class)annotation);
   }

   public static Method getMethod(MethodRefrence method) {
      return getInternalMethod(method);
   }

   public static <Z> Method getMethod(MethodRefrence1<Z> method) {
      return getInternalMethod(method);
   }

   public static <T, Z> Method getMethod(MethodRefrence2<T, Z> method) {
      return getInternalMethod(method);
   }

   public static void check(MethodRefrence method, Function<Method, Boolean> checker) {
      checkLambda(method, checker);
   }

   public static <T> T check(MethodRefrence1<T> method, Function<Method, Boolean> checker) {
      checkLambda(method, checker);
      return null;
   }

   public static <T, Z> T check(MethodRefrence2<T, Z> method, Function<Method, Boolean> checker) {
      checkLambda(method, checker);
      return null;
   }

   private static HashSet<String> cache = new HashSet<>();

   private static void checkLambda(Object obj, Function<Method, Boolean> callable) {
      if (cache.contains(obj.toString().split("/")[0]))
         return;
      Method method = getInternalMethod(obj);
      if (method != null) {
         Boolean noRechecking = callable.apply(method);
         if (noRechecking.booleanValue() == true)
            cache.add(obj.toString().split("/")[0]);
      }
      cache.add(obj.toString().split("/")[0]);
   }

   private static Method getInternalMethod(Object obj) {
      for (Class<?> cl = obj.getClass(); cl != null; cl = cl.getSuperclass()) {
         try {
            Method m = cl.getDeclaredMethod("writeReplace", new Class[0]);
            m.setAccessible(true);
            Object replacement = m.invoke(obj, new Object[0]);
            if (!(replacement instanceof SerializedLambda))
               break;
            SerializedLambda l = (SerializedLambda)replacement;
            for (Method method : Class.forName(l.getImplClass().replace('/', '.')).getDeclaredMethods()) {
               if (method.getName().equals(l.getImplMethodName()))
                  return method;
            }
         } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
            break;
         } catch (Exception exception) {}
      }
      return null;
   }
}

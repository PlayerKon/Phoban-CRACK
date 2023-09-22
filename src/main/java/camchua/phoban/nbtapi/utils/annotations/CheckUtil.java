package camchua.phoban.nbtapi.utils.annotations;

import camchua.phoban.nbtapi.NbtApiException;
import camchua.phoban.nbtapi.utils.MinecraftVersion;
import java.lang.reflect.Method;

public class CheckUtil {
   public static boolean isAvaliable(Method method) {
      if (MinecraftVersion.getVersion().getVersionId() < ((AvailableSince)method.getAnnotation(AvailableSince.class)).version().getVersionId()) {
         throw new NbtApiException("The Method '" + method.getName() + "' is only avaliable for the Versions " + ((AvailableSince)method.getAnnotation(AvailableSince.class)).version() + "+, but still got called!");
      } else {
         return true;
      }
   }
}

package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.MinecraftVersion;

public class NbtApiException extends RuntimeException {
   private static final long serialVersionUID = -993309714559452334L;
   public static Boolean confirmedBroken = null;

   public NbtApiException() {
   }

   public NbtApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(generateMessage(message), cause, enableSuppression, writableStackTrace);
   }

   public NbtApiException(String message, Throwable cause) {
      super(generateMessage(message), cause);
   }

   public NbtApiException(String message) {
      super(generateMessage(message));
   }

   public NbtApiException(Throwable cause) {
      super(generateMessage(cause == null ? null : cause.toString()), cause);
   }

   private static String generateMessage(String message) {
      if (message == null) {
         return null;
      } else if (confirmedBroken == null) {
         return "[?]" + message;
      } else {
         return !confirmedBroken ? "[Selfchecked]" + message : "[" + MinecraftVersion.getVersion() + "]There were errors detected during the server self-check! Please, make sure that NBT-API is up to date. Error message: " + message;
      }
   }
}

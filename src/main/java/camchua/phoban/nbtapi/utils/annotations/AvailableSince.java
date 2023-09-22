package camchua.phoban.nbtapi.utils.annotations;

import camchua.phoban.nbtapi.utils.MinecraftVersion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AvailableSince {
   MinecraftVersion version();
}

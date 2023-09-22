package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.nmsmappings.ObjectCreator;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;
import com.mojang.authlib.GameProfile;

public class NBTGameProfile {
   public static NBTCompound toNBT(GameProfile profile) {
      return new NBTContainer(ReflectionMethod.GAMEPROFILE_SERIALIZE.run((Object)null, ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(), profile));
   }

   public static GameProfile fromNBT(NBTCompound compound) {
      return (GameProfile)ReflectionMethod.GAMEPROFILE_DESERIALIZE.run((Object)null, NBTReflectionUtil.gettoCompount(compound.getCompound(), compound));
   }
}

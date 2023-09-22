package camchua.phoban.nbtapi.utils.nmsmappings;

import camchua.phoban.nbtapi.NbtApiException;
import camchua.phoban.nbtapi.utils.MinecraftVersion;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

public enum ReflectionMethod {
   COMPOUND_SET_FLOAT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Float.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setFloat"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putFloat(java.lang.String,float)")}),
   COMPOUND_SET_STRING(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setString"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putString(java.lang.String,java.lang.String)")}),
   COMPOUND_SET_INT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Integer.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setInt"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putInt(java.lang.String,int)")}),
   COMPOUND_SET_BYTEARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, byte[].class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setByteArray"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putByteArray(java.lang.String,byte[])")}),
   COMPOUND_SET_INTARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, int[].class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setIntArray"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putIntArray(java.lang.String,int[])")}),
   COMPOUND_SET_LONG(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Long.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setLong"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putLong(java.lang.String,long)")}),
   COMPOUND_SET_SHORT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Short.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setShort"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putShort(java.lang.String,short)")}),
   COMPOUND_SET_BYTE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Byte.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setByte"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putByte(java.lang.String,byte)")}),
   COMPOUND_SET_DOUBLE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Double.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setDouble"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putDouble(java.lang.String,double)")}),
   COMPOUND_SET_BOOLEAN(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Boolean.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setBoolean"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putBoolean(java.lang.String,boolean)")}),
   COMPOUND_SET_UUID(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, UUID.class}, MinecraftVersion.MC1_16_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "putUUID(java.lang.String,java.util.UUID)")}),
   COMPOUND_MERGE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_8_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "put(java.lang.String,net.minecraft.nbt.Tag)")}),
   COMPOUND_SET(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, ClassWrapper.NMS_NBTBASE.getClazz()}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "set"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "put(java.lang.String,net.minecraft.nbt.Tag)")}),
   COMPOUND_GET(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "get"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "get(java.lang.String)")}),
   COMPOUND_GET_LIST(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class, Integer.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getList"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getList(java.lang.String,int)")}),
   COMPOUND_OWN_TYPE(ClassWrapper.NMS_NBTBASE, new Class[0], MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getTypeId")}),
   COMPOUND_GET_FLOAT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getFloat"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getFloat(java.lang.String)")}),
   COMPOUND_GET_STRING(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getString"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getString(java.lang.String)")}),
   COMPOUND_GET_INT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getInt"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getInt(java.lang.String)")}),
   COMPOUND_GET_BYTEARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getByteArray"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getByteArray(java.lang.String)")}),
   COMPOUND_GET_INTARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getIntArray"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getIntArray(java.lang.String)")}),
   COMPOUND_GET_LONG(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getLong"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getLong(java.lang.String)")}),
   COMPOUND_GET_SHORT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getShort"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getShort(java.lang.String)")}),
   COMPOUND_GET_BYTE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getByte"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getByte(java.lang.String)")}),
   COMPOUND_GET_DOUBLE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getDouble"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getDouble(java.lang.String)")}),
   COMPOUND_GET_BOOLEAN(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getBoolean"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getBoolean(java.lang.String)")}),
   COMPOUND_GET_UUID(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_16_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getUUID(java.lang.String)")}),
   COMPOUND_GET_COMPOUND(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getCompound"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getCompound(java.lang.String)")}),
   NMSITEM_GETTAG(ClassWrapper.NMS_ITEMSTACK, new Class[0], MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getTag"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getTag()")}),
   NMSITEM_SAVE(ClassWrapper.NMS_ITEMSTACK, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "save"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "save(net.minecraft.nbt.CompoundTag)")}),
   NMSITEM_CREATESTACK(ClassWrapper.NMS_ITEMSTACK, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_10_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "createStack")}),
   COMPOUND_REMOVE_KEY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "remove"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "remove(java.lang.String)")}),
   COMPOUND_HAS_KEY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "hasKey"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "contains(java.lang.String)")}),
   COMPOUND_GET_TYPE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[]{String.class}, MinecraftVersion.MC1_8_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "b"), new ReflectionMethod.Since(MinecraftVersion.MC1_9_R1, "d"), new ReflectionMethod.Since(MinecraftVersion.MC1_15_R1, "e"), new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "d"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getTagType(java.lang.String)")}),
   COMPOUND_GET_KEYS(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[0], MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "c"), new ReflectionMethod.Since(MinecraftVersion.MC1_13_R1, "getKeys"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getAllKeys()")}),
   LISTCOMPOUND_GET_KEYS(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[0], MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "c"), new ReflectionMethod.Since(MinecraftVersion.MC1_13_R1, "getKeys"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getAllKeys()")}),
   LIST_REMOVE_KEY(ClassWrapper.NMS_NBTTAGLIST, new Class[]{Integer.TYPE}, MinecraftVersion.MC1_8_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_9_R1, "remove"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "remove(int)")}),
   LIST_SIZE(ClassWrapper.NMS_NBTTAGLIST, new Class[0], MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "size"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "size()")}),
   LIST_SET(ClassWrapper.NMS_NBTTAGLIST, new Class[]{Integer.TYPE, ClassWrapper.NMS_NBTBASE.getClazz()}, MinecraftVersion.MC1_8_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_13_R1, "set"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "setTag(int,net.minecraft.nbt.Tag)")}),
   LEGACY_LIST_ADD(ClassWrapper.NMS_NBTTAGLIST, new Class[]{ClassWrapper.NMS_NBTBASE.getClazz()}, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_13_R2, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "add")}),
   LIST_ADD(ClassWrapper.NMS_NBTTAGLIST, new Class[]{Integer.TYPE, ClassWrapper.NMS_NBTBASE.getClazz()}, MinecraftVersion.MC1_14_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_14_R1, "add"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "addTag(int,net.minecraft.nbt.Tag)")}),
   LIST_GET_STRING(ClassWrapper.NMS_NBTTAGLIST, new Class[]{Integer.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getString"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getString(int)")}),
   LIST_GET_COMPOUND(ClassWrapper.NMS_NBTTAGLIST, new Class[]{Integer.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "get"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getCompound(int)")}),
   LIST_GET(ClassWrapper.NMS_NBTTAGLIST, new Class[]{Integer.TYPE}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "get"), new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "g"), new ReflectionMethod.Since(MinecraftVersion.MC1_9_R1, "h"), new ReflectionMethod.Since(MinecraftVersion.MC1_12_R1, "i"), new ReflectionMethod.Since(MinecraftVersion.MC1_13_R1, "get"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "get(int)")}),
   ITEMSTACK_SET_TAG(ClassWrapper.NMS_ITEMSTACK, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "setTag"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "setTag(net.minecraft.nbt.CompoundTag)")}),
   ITEMSTACK_NMSCOPY(ClassWrapper.CRAFT_ITEMSTACK, new Class[]{ItemStack.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "asNMSCopy")}),
   ITEMSTACK_BUKKITMIRROR(ClassWrapper.CRAFT_ITEMSTACK, new Class[]{ClassWrapper.NMS_ITEMSTACK.getClazz()}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "asCraftMirror")}),
   CRAFT_WORLD_GET_HANDLE(ClassWrapper.CRAFT_WORLD, new Class[0], MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getHandle")}),
   NMS_WORLD_GET_TILEENTITY(ClassWrapper.NMS_WORLDSERVER, new Class[]{ClassWrapper.NMS_BLOCKPOSITION.getClazz()}, MinecraftVersion.MC1_8_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "getTileEntity"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getBlockEntity(net.minecraft.core.BlockPos)")}),
   NMS_WORLD_SET_TILEENTITY(ClassWrapper.NMS_WORLDSERVER, new Class[]{ClassWrapper.NMS_BLOCKPOSITION.getClazz(), ClassWrapper.NMS_TILEENTITY.getClazz()}, MinecraftVersion.MC1_8_R3, MinecraftVersion.MC1_16_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "setTileEntity")}),
   NMS_WORLD_REMOVE_TILEENTITY(ClassWrapper.NMS_WORLDSERVER, new Class[]{ClassWrapper.NMS_BLOCKPOSITION.getClazz()}, MinecraftVersion.MC1_8_R3, MinecraftVersion.MC1_17_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "t"), new ReflectionMethod.Since(MinecraftVersion.MC1_9_R1, "s"), new ReflectionMethod.Since(MinecraftVersion.MC1_13_R1, "n"), new ReflectionMethod.Since(MinecraftVersion.MC1_14_R1, "removeTileEntity")}),
   NMS_WORLD_GET_TILEENTITY_1_7_10(ClassWrapper.NMS_WORLDSERVER, new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE}, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getTileEntity")}),
   TILEENTITY_LOAD_LEGACY191(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_MINECRAFTSERVER.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_9_R1, MinecraftVersion.MC1_9_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_9_R1, "a")}),
   TILEENTITY_LOAD_LEGACY183(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_8_R3, MinecraftVersion.MC1_9_R2, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "c"), new ReflectionMethod.Since(MinecraftVersion.MC1_9_R1, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_9_R2, "c")}),
   TILEENTITY_LOAD_LEGACY1121(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_WORLD.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_10_R1, MinecraftVersion.MC1_12_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_10_R1, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_12_R1, "create")}),
   TILEENTITY_LOAD_LEGACY1151(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_13_R1, MinecraftVersion.MC1_15_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_12_R1, "create")}),
   TILEENTITY_LOAD(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_IBLOCKDATA.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_16_R1, MinecraftVersion.MC1_16_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "create")}),
   TILEENTITY_GET_NBT(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_17_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "b"), new ReflectionMethod.Since(MinecraftVersion.MC1_9_R1, "save")}),
   TILEENTITY_GET_NBT_1181(ClassWrapper.NMS_TILEENTITY, new Class[0], MinecraftVersion.MC1_18_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "saveWithId()")}),
   TILEENTITY_SET_NBT_LEGACY1151(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_15_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_12_R1, "load")}),
   TILEENTITY_SET_NBT_LEGACY1161(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_IBLOCKDATA.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_16_R1, MinecraftVersion.MC1_16_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "load")}),
   TILEENTITY_SET_NBT(ClassWrapper.NMS_TILEENTITY, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_17_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "load"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "load(net.minecraft.nbt.CompoundTag)")}),
   TILEENTITY_GET_BLOCKDATA(ClassWrapper.NMS_TILEENTITY, new Class[0], MinecraftVersion.MC1_16_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "getBlock"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getBlockState()")}),
   CRAFT_ENTITY_GET_HANDLE(ClassWrapper.CRAFT_ENTITY, new Class[0], MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "getHandle")}),
   NMS_ENTITY_SET_NBT(ClassWrapper.NMS_ENTITY, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "f"), new ReflectionMethod.Since(MinecraftVersion.MC1_16_R1, "load"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "load(net.minecraft.nbt.CompoundTag)")}),
   NMS_ENTITY_GET_NBT(ClassWrapper.NMS_ENTITY, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "e"), new ReflectionMethod.Since(MinecraftVersion.MC1_12_R1, "save"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "saveWithoutId(net.minecraft.nbt.CompoundTag)")}),
   NMS_ENTITY_GETSAVEID(ClassWrapper.NMS_ENTITY, new Class[0], MinecraftVersion.MC1_14_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_14_R1, "getSaveID"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "getEncodeId()")}),
   NBTFILE_READ(ClassWrapper.NMS_NBTCOMPRESSEDSTREAMTOOLS, new Class[]{InputStream.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "readCompressed(java.io.InputStream)")}),
   NBTFILE_WRITE(ClassWrapper.NMS_NBTCOMPRESSEDSTREAMTOOLS, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), OutputStream.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "a"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "writeCompressed(net.minecraft.nbt.CompoundTag,java.io.OutputStream)")}),
   PARSE_NBT(ClassWrapper.NMS_MOJANGSONPARSER, new Class[]{String.class}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "parse"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "parseTag(java.lang.String)")}),
   REGISTRY_KEYSET(ClassWrapper.NMS_REGISTRYSIMPLE, new Class[0], MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_11_R1, "keySet")}),
   REGISTRY_GET(ClassWrapper.NMS_REGISTRYSIMPLE, new Class[]{Object.class}, MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_11_R1, "get")}),
   REGISTRY_SET(ClassWrapper.NMS_REGISTRYSIMPLE, new Class[]{Object.class, Object.class}, MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_11_R1, "a")}),
   REGISTRY_GET_INVERSE(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[]{Object.class}, MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_11_R1, "b")}),
   REGISTRYMATERIALS_KEYSET(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[0], MinecraftVersion.MC1_13_R1, MinecraftVersion.MC1_17_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_13_R1, "keySet")}),
   REGISTRYMATERIALS_GET(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[]{ClassWrapper.NMS_MINECRAFTKEY.getClazz()}, MinecraftVersion.MC1_13_R1, MinecraftVersion.MC1_17_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_13_R1, "get")}),
   REGISTRYMATERIALS_GETKEY(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[]{Object.class}, MinecraftVersion.MC1_13_R2, MinecraftVersion.MC1_17_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_13_R2, "getKey")}),
   GAMEPROFILE_DESERIALIZE(ClassWrapper.NMS_GAMEPROFILESERIALIZER, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_7_R4, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_7_R4, "deserialize"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "readGameProfile(net.minecraft.nbt.CompoundTag)")}),
   GAMEPROFILE_SERIALIZE(ClassWrapper.NMS_GAMEPROFILESERIALIZER, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), ClassWrapper.GAMEPROFILE.getClazz()}, MinecraftVersion.MC1_8_R3, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_8_R3, "serialize"), new ReflectionMethod.Since(MinecraftVersion.MC1_18_R1, "writeGameProfile(net.minecraft.nbt.CompoundTag,com.mojang.authlib.GameProfile)")}),
   CRAFT_PERSISTENT_DATA_CONTAINER_TO_TAG(ClassWrapper.CRAFT_PERSISTENTDATACONTAINER, new Class[0], MinecraftVersion.MC1_14_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_14_R1, "toTagCompound")}),
   CRAFT_PERSISTENT_DATA_CONTAINER_GET_MAP(ClassWrapper.CRAFT_PERSISTENTDATACONTAINER, new Class[0], MinecraftVersion.MC1_14_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_14_R1, "getRaw")}),
   CRAFT_PERSISTENT_DATA_CONTAINER_PUT_ALL(ClassWrapper.CRAFT_PERSISTENTDATACONTAINER, new Class[]{ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()}, MinecraftVersion.MC1_14_R1, new ReflectionMethod.Since[]{new ReflectionMethod.Since(MinecraftVersion.MC1_14_R1, "putAll")});

   private MinecraftVersion removedAfter;
   private ReflectionMethod.Since targetVersion;
   private Method method;
   private boolean loaded;
   private boolean compatible;
   private String methodName;
   private ClassWrapper parentClassWrapper;

   private ReflectionMethod(ClassWrapper targetClass, Class<?>[] args, MinecraftVersion addedSince, MinecraftVersion removedAfter, ReflectionMethod.Since... methodnames) {
      this.loaded = false;
      this.compatible = false;
      this.methodName = null;
      this.removedAfter = removedAfter;
      this.parentClassWrapper = targetClass;
      boolean specialCase = MinecraftVersion.isForgePresent() && this.name().equals("COMPOUND_MERGE") && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4;
      if (specialCase || MinecraftVersion.isAtLeastVersion(addedSince) && (this.removedAfter == null || !MinecraftVersion.isNewerThan(removedAfter))) {
         this.compatible = true;
         MinecraftVersion server = MinecraftVersion.getVersion();
         ReflectionMethod.Since target = methodnames[0];
         ReflectionMethod.Since[] var11 = methodnames;
         int var12 = methodnames.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            ReflectionMethod.Since s = var11[var13];
            if (s.version.getVersionId() <= server.getVersionId() && target.version.getVersionId() < s.version.getVersionId()) {
               target = s;
            }
         }

         this.targetVersion = target;
         String targetMethodName = this.targetVersion.name;

         try {
            if (MinecraftVersion.isForgePresent() && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
               targetMethodName = (String)Forge1710Mappings.getMethodMapping().getOrDefault(this.name(), targetMethodName);
            } else if (this.targetVersion.version.isMojangMapping()) {
               targetMethodName = (String)MojangToMapping.getMapping().getOrDefault(targetClass.getMojangName() + "#" + this.targetVersion.name, "Unmapped" + this.targetVersion.name);
            }

            this.method = targetClass.getClazz().getDeclaredMethod(targetMethodName, args);
            this.method.setAccessible(true);
            this.loaded = true;
            this.methodName = this.targetVersion.name;
         } catch (NoSuchMethodException | SecurityException | NullPointerException var16) {
            try {
               if (this.targetVersion.version.isMojangMapping()) {
                  targetMethodName = (String)MojangToMapping.getMapping().getOrDefault(targetClass.getMojangName() + "#" + this.targetVersion.name, "Unmapped" + this.targetVersion.name);
               }

               this.method = targetClass.getClazz().getMethod(targetMethodName, args);
               this.method.setAccessible(true);
               this.loaded = true;
               this.methodName = this.targetVersion.name;
            } catch (NoSuchMethodException | SecurityException | NullPointerException var15) {
               MinecraftVersion.getLogger().warning("[NBTAPI] Unable to find the method '" + targetMethodName + "' in '" + (targetClass.getClazz() == null ? targetClass.getMojangName() : targetClass.getClazz().getSimpleName()) + "' Args: " + Arrays.toString(args) + " Enum: " + this);
            }
         }

      }
   }

   private ReflectionMethod(ClassWrapper targetClass, Class<?>[] args, MinecraftVersion addedSince, ReflectionMethod.Since... methodnames) {
      this(targetClass, args, addedSince, (MinecraftVersion)null, methodnames);
   }

   public Object run(Object target, Object... args) {
      if (this.method == null) {
         throw new NbtApiException("Method not loaded! '" + this + "'");
      } else {
         try {
            return this.method.invoke(target, args);
         } catch (Exception var4) {
            throw new NbtApiException("Error while calling the method '" + this.methodName + "', loaded: " + this.loaded + ", Enum: " + this + " Passed Class: " + target.getClass(), var4);
         }
      }
   }

   public String getMethodName() {
      return this.methodName;
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public boolean isCompatible() {
      return this.compatible;
   }

   public ReflectionMethod.Since getSelectedVersionInfo() {
      return this.targetVersion;
   }

   public ClassWrapper getParentClassWrapper() {
      return this.parentClassWrapper;
   }

   // $FF: synthetic method
   private static ReflectionMethod[] $values() {
      return new ReflectionMethod[]{COMPOUND_SET_FLOAT, COMPOUND_SET_STRING, COMPOUND_SET_INT, COMPOUND_SET_BYTEARRAY, COMPOUND_SET_INTARRAY, COMPOUND_SET_LONG, COMPOUND_SET_SHORT, COMPOUND_SET_BYTE, COMPOUND_SET_DOUBLE, COMPOUND_SET_BOOLEAN, COMPOUND_SET_UUID, COMPOUND_MERGE, COMPOUND_SET, COMPOUND_GET, COMPOUND_GET_LIST, COMPOUND_OWN_TYPE, COMPOUND_GET_FLOAT, COMPOUND_GET_STRING, COMPOUND_GET_INT, COMPOUND_GET_BYTEARRAY, COMPOUND_GET_INTARRAY, COMPOUND_GET_LONG, COMPOUND_GET_SHORT, COMPOUND_GET_BYTE, COMPOUND_GET_DOUBLE, COMPOUND_GET_BOOLEAN, COMPOUND_GET_UUID, COMPOUND_GET_COMPOUND, NMSITEM_GETTAG, NMSITEM_SAVE, NMSITEM_CREATESTACK, COMPOUND_REMOVE_KEY, COMPOUND_HAS_KEY, COMPOUND_GET_TYPE, COMPOUND_GET_KEYS, LISTCOMPOUND_GET_KEYS, LIST_REMOVE_KEY, LIST_SIZE, LIST_SET, LEGACY_LIST_ADD, LIST_ADD, LIST_GET_STRING, LIST_GET_COMPOUND, LIST_GET, ITEMSTACK_SET_TAG, ITEMSTACK_NMSCOPY, ITEMSTACK_BUKKITMIRROR, CRAFT_WORLD_GET_HANDLE, NMS_WORLD_GET_TILEENTITY, NMS_WORLD_SET_TILEENTITY, NMS_WORLD_REMOVE_TILEENTITY, NMS_WORLD_GET_TILEENTITY_1_7_10, TILEENTITY_LOAD_LEGACY191, TILEENTITY_LOAD_LEGACY183, TILEENTITY_LOAD_LEGACY1121, TILEENTITY_LOAD_LEGACY1151, TILEENTITY_LOAD, TILEENTITY_GET_NBT, TILEENTITY_GET_NBT_1181, TILEENTITY_SET_NBT_LEGACY1151, TILEENTITY_SET_NBT_LEGACY1161, TILEENTITY_SET_NBT, TILEENTITY_GET_BLOCKDATA, CRAFT_ENTITY_GET_HANDLE, NMS_ENTITY_SET_NBT, NMS_ENTITY_GET_NBT, NMS_ENTITY_GETSAVEID, NBTFILE_READ, NBTFILE_WRITE, PARSE_NBT, REGISTRY_KEYSET, REGISTRY_GET, REGISTRY_SET, REGISTRY_GET_INVERSE, REGISTRYMATERIALS_KEYSET, REGISTRYMATERIALS_GET, REGISTRYMATERIALS_GETKEY, GAMEPROFILE_DESERIALIZE, GAMEPROFILE_SERIALIZE, CRAFT_PERSISTENT_DATA_CONTAINER_TO_TAG, CRAFT_PERSISTENT_DATA_CONTAINER_GET_MAP, CRAFT_PERSISTENT_DATA_CONTAINER_PUT_ALL};
   }

   public static class Since {
      public final MinecraftVersion version;
      public final String name;

      public Since(MinecraftVersion version, String name) {
         this.version = version;
         this.name = name;
      }
   }
}

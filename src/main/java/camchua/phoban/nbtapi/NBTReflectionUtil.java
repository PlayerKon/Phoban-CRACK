package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.GsonWrapper;
import camchua.phoban.nbtapi.utils.MinecraftVersion;
import camchua.phoban.nbtapi.utils.nmsmappings.ClassWrapper;
import camchua.phoban.nbtapi.utils.nmsmappings.ObjectCreator;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.ItemMeta;

public class NBTReflectionUtil {
   private static Field field_unhandledTags = null;

   static {
      try {
         field_unhandledTags = ClassWrapper.CRAFT_METAITEM.getClazz().getDeclaredField("unhandledTags");
         field_unhandledTags.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
   }

   public static Object getNMSEntity(Entity entity) {
      try {
         return ReflectionMethod.CRAFT_ENTITY_GET_HANDLE.run(ClassWrapper.CRAFT_ENTITY.getClazz().cast(entity), new Object[0]);
      } catch (Exception e) {
         throw new NbtApiException("Exception while getting the NMS Entity from a Bukkit Entity!", e);
      }
   }

   public static Object readNBT(InputStream stream) {
      try {
         return ReflectionMethod.NBTFILE_READ.run(null, new Object[] { stream });
      } catch (Exception e) {
         try {
            stream.close();
         } catch (IOException iOException) {}
         throw new NbtApiException("Exception while reading a NBT File!", e);
      }
   }

   public static Object writeNBT(Object nbt, OutputStream stream) {
      try {
         return ReflectionMethod.NBTFILE_WRITE.run(null, new Object[] { nbt, stream });
      } catch (Exception e) {
         throw new NbtApiException("Exception while writing NBT!", e);
      }
   }

   public static void writeApiNBT(NBTCompound comp, OutputStream stream) {
      try {
         Object nbttag = comp.getCompound();
         if (nbttag == null)
            nbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
         if (!valideCompound(comp).booleanValue())
            return;
         Object workingtag = gettoCompount(nbttag, comp);
         ReflectionMethod.NBTFILE_WRITE.run(null, new Object[] { workingtag, stream });
      } catch (Exception e) {
         throw new NbtApiException("Exception while writing NBT!", e);
      }
   }

   public static Object getItemRootNBTTagCompound(Object nmsitem) {
      try {
         Object answer = ReflectionMethod.NMSITEM_GETTAG.run(nmsitem, new Object[0]);
         return (answer != null) ? answer : ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      } catch (Exception e) {
         throw new NbtApiException("Exception while getting an Itemstack's NBTCompound!", e);
      }
   }

   public static Object convertNBTCompoundtoNMSItem(NBTCompound nbtcompound) {
      try {
         Object nmsComp = gettoCompount(nbtcompound.getCompound(), nbtcompound);
         if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_11_R1.getVersionId())
            return ObjectCreator.NMS_COMPOUNDFROMITEM.getInstance(new Object[] { nmsComp });
         return ReflectionMethod.NMSITEM_CREATESTACK.run(null, new Object[] { nmsComp });
      } catch (Exception e) {
         throw new NbtApiException("Exception while converting NBTCompound to NMS ItemStack!", e);
      }
   }

   public static NBTContainer convertNMSItemtoNBTCompound(Object nmsitem) {
      try {
         Object answer = ReflectionMethod.NMSITEM_SAVE.run(nmsitem, new Object[] { ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]) });
         return new NBTContainer(answer);
      } catch (Exception e) {
         throw new NbtApiException("Exception while converting NMS ItemStack to NBTCompound!", e);
      }
   }

   public static Map<String, Object> getUnhandledNBTTags(ItemMeta meta) {
      try {
         return (Map<String, Object>)field_unhandledTags.get(meta);
      } catch (Exception e) {
         throw new NbtApiException("Exception while getting unhandled tags from ItemMeta!", e);
      }
   }

   public static Object getEntityNBTTagCompound(Object nmsEntity) {
      try {
         Object nbt = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
         Object answer = ReflectionMethod.NMS_ENTITY_GET_NBT.run(nmsEntity, new Object[] { nbt });
         if (answer == null)
            answer = nbt;
         return answer;
      } catch (Exception e) {
         throw new NbtApiException("Exception while getting NBTCompound from NMS Entity!", e);
      }
   }

   public static Object setEntityNBTTag(Object nbtTag, Object nmsEntity) {
      try {
         ReflectionMethod.NMS_ENTITY_SET_NBT.run(nmsEntity, new Object[] { nbtTag });
         return nmsEntity;
      } catch (Exception ex) {
         throw new NbtApiException("Exception while setting the NBTCompound of an Entity", ex);
      }
   }

   public static Object getTileEntityNBTTagCompound(BlockState tile) {
      try {
         Object cworld = ClassWrapper.CRAFT_WORLD.getClazz().cast(tile.getWorld());
         Object nmsworld = ReflectionMethod.CRAFT_WORLD_GET_HANDLE.run(cworld, new Object[0]);
         Object o = null;
         if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
            o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY_1_7_10.run(nmsworld, new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), Integer.valueOf(tile.getZ()) });
         } else {
            Object pos = ObjectCreator.NMS_BLOCKPOSITION.getInstance(new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), Integer.valueOf(tile.getZ()) });
            o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY.run(nmsworld, new Object[] { pos });
         }
         Object answer = null;
         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1)) {
            answer = ReflectionMethod.TILEENTITY_GET_NBT_1181.run(o, new Object[0]);
         } else {
            answer = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
            ReflectionMethod.TILEENTITY_GET_NBT.run(o, new Object[] { answer });
         }
         if (answer == null)
            throw new NbtApiException("Unable to get NBTCompound from TileEntity! " + tile + " " + o);
         return answer;
      } catch (Exception e) {
         throw new NbtApiException("Exception while getting NBTCompound from TileEntity!", e);
      }
   }

   public static void setTileEntityNBTTagCompound(BlockState tile, Object comp) {
      try {
         Object cworld = ClassWrapper.CRAFT_WORLD.getClazz().cast(tile.getWorld());
         Object nmsworld = ReflectionMethod.CRAFT_WORLD_GET_HANDLE.run(cworld, new Object[0]);
         Object o = null;
         if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
            o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY_1_7_10.run(nmsworld, new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), Integer.valueOf(tile.getZ()) });
         } else {
            Object pos = ObjectCreator.NMS_BLOCKPOSITION.getInstance(new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), Integer.valueOf(tile.getZ()) });
            o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY.run(nmsworld, new Object[] { pos });
         }
         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1)) {
            ReflectionMethod.TILEENTITY_SET_NBT.run(o, new Object[] { comp });
         } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1)) {
            Object blockData = ReflectionMethod.TILEENTITY_GET_BLOCKDATA.run(o, new Object[0]);
            ReflectionMethod.TILEENTITY_SET_NBT_LEGACY1161.run(o, new Object[] { blockData, comp });
         } else {
            ReflectionMethod.TILEENTITY_SET_NBT_LEGACY1151.run(o, new Object[] { comp });
         }
      } catch (Exception e) {
         throw new NbtApiException("Exception while setting NBTData for a TileEntity!", e);
      }
   }

   public static Object getSubNBTTagCompound(Object compound, String name) {
      try {
         if (((Boolean)ReflectionMethod.COMPOUND_HAS_KEY.run(compound, new Object[] { name })).booleanValue())
            return ReflectionMethod.COMPOUND_GET_COMPOUND.run(compound, new Object[] { name });
         throw new NbtApiException("Tried getting invalide compound '" + name + "' from '" + compound + "'!");
      } catch (Exception e) {
         throw new NbtApiException("Exception while getting NBT subcompounds!", e);
      }
   }

   public static void addNBTTagCompound(NBTCompound comp, String name) {
      if (name == null) {
         remove(comp, name);
         return;
      }
      Object nbttag = comp.getCompound();
      if (nbttag == null)
         nbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         return;
      Object workingtag = gettoCompount(nbttag, comp);
      try {
         ReflectionMethod.COMPOUND_SET.run(workingtag, new Object[] { name, ClassWrapper.NMS_NBTTAGCOMPOUND
                 .getClazz().newInstance() });
         comp.setCompound(nbttag);
      } catch (Exception e) {
         throw new NbtApiException("Exception while adding a Compound!", e);
      }
   }

   public static Boolean valideCompound(NBTCompound comp) {
      Object root = comp.getCompound();
      if (root == null)
         root = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      return Boolean.valueOf((gettoCompount(root, comp) != null));
   }

   protected static Object gettoCompount(Object nbttag, NBTCompound comp) {
      Deque<String> structure = new ArrayDeque<>();
      while (comp.getParent() != null) {
         structure.add(comp.getName());
         comp = comp.getParent();
      }
      while (!structure.isEmpty()) {
         String target = structure.pollLast();
         nbttag = getSubNBTTagCompound(nbttag, target);
         if (nbttag == null)
            throw new NbtApiException("Unable to find tag '" + target + "' in " + nbttag);
      }
      return nbttag;
   }

   public static void mergeOtherNBTCompound(NBTCompound comp, NBTCompound nbtcompoundSrc) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
      Object workingtag = gettoCompount(rootnbttag, comp);
      Object rootnbttagSrc = nbtcompoundSrc.getCompound();
      if (rootnbttagSrc == null)
         rootnbttagSrc = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(nbtcompoundSrc).booleanValue())
         throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
      Object workingtagSrc = gettoCompount(rootnbttagSrc, nbtcompoundSrc);
      try {
         ReflectionMethod.COMPOUND_MERGE.run(workingtag, new Object[] { workingtagSrc });
         comp.setCompound(rootnbttag);
      } catch (Exception e) {
         throw new NbtApiException("Exception while merging two NBTCompounds!", e);
      }
   }

   public static String getContent(NBTCompound comp, String key) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
      Object workingtag = gettoCompount(rootnbttag, comp);
      try {
         return ReflectionMethod.COMPOUND_GET.run(workingtag, new Object[] { key }).toString();
      } catch (Exception e) {
         throw new NbtApiException("Exception while getting the Content for key '" + key + "'!", e);
      }
   }

   public static void set(NBTCompound comp, String key, Object val) {
      if (val == null) {
         remove(comp, key);
         return;
      }
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
      Object workingtag = gettoCompount(rootnbttag, comp);
      try {
         ReflectionMethod.COMPOUND_SET.run(workingtag, new Object[] { key, val });
         comp.setCompound(rootnbttag);
      } catch (Exception e) {
         throw new NbtApiException("Exception while setting key '" + key + "' to '" + val + "'!", e);
      }
   }

   public static <T> NBTList<T> getList(NBTCompound comp, String key, NBTType type, Class<T> clazz) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         return null;
      Object workingtag = gettoCompount(rootnbttag, comp);
      try {
         Object nbt = ReflectionMethod.COMPOUND_GET_LIST.run(workingtag, new Object[] { key, Integer.valueOf(type.getId()) });
         if (clazz == String.class)
            return (NBTList<T>) new NBTStringList(comp, key, type, nbt);
         if (clazz == NBTListCompound.class)
            return (NBTList<T>) new NBTCompoundList(comp, key, type, nbt);
         if (clazz == Integer.class)
            return (NBTList<T>) new NBTIntegerList(comp, key, type, nbt);
         if (clazz == Float.class)
            return (NBTList<T>) new NBTFloatList(comp, key, type, nbt);
         if (clazz == Double.class)
            return (NBTList<T>) new NBTDoubleList(comp, key, type, nbt);
         if (clazz == Long.class)
            return (NBTList<T>) new NBTLongList(comp, key, type, nbt);
         if (clazz == int[].class)
            return (NBTList<T>) new NBTIntArrayList(comp, key, type, nbt);
         if (clazz == UUID.class)
            return (NBTList<T>) new NBTUUIDList(comp, key, type, nbt);
         return null;
      } catch (Exception ex) {
         throw new NbtApiException("Exception while getting a list with the type '" + type + "'!", ex);
      }
   }

   public static NBTType getListType(NBTCompound comp, String key) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         return null;
      Object workingtag = gettoCompount(rootnbttag, comp);
      try {
         Object nbt = ReflectionMethod.COMPOUND_GET.run(workingtag, new Object[] { key });
         String fieldname = "type";
         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1))
            fieldname = "w";
         Field f = nbt.getClass().getDeclaredField(fieldname);
         f.setAccessible(true);
         return NBTType.valueOf(f.getByte(nbt));
      } catch (Exception ex) {
         throw new NbtApiException("Exception while getting the list type!", ex);
      }
   }

   public static Object getEntry(NBTCompound comp, String key) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         return null;
      Object workingtag = gettoCompount(rootnbttag, comp);
      try {
         Object nbt = ReflectionMethod.COMPOUND_GET.run(workingtag, new Object[] { key });
         return nbt;
      } catch (Exception ex) {
         throw new NbtApiException("Exception while getting an Entry!", ex);
      }
   }

   public static void setObject(NBTCompound comp, String key, Object value) {
      if (!MinecraftVersion.hasGsonSupport())
         return;
      try {
         String json = GsonWrapper.getString(value);
         setData(comp, ReflectionMethod.COMPOUND_SET_STRING, key, json);
      } catch (Exception e) {
         throw new NbtApiException("Exception while setting the Object '" + value + "'!", e);
      }
   }

   public static <T> T getObject(NBTCompound comp, String key, Class<T> type) {
      if (!MinecraftVersion.hasGsonSupport())
         return null;
      String json = (String)getData(comp, ReflectionMethod.COMPOUND_GET_STRING, key);
      if (json == null)
         return null;
      return (T)GsonWrapper.deserializeJson(json, type);
   }

   public static void remove(NBTCompound comp, String key) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         return;
      Object workingtag = gettoCompount(rootnbttag, comp);
      ReflectionMethod.COMPOUND_REMOVE_KEY.run(workingtag, new Object[] { key });
      comp.setCompound(rootnbttag);
   }

   public static Set<String> getKeys(NBTCompound comp) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
      Object workingtag = gettoCompount(rootnbttag, comp);
      return (Set<String>)ReflectionMethod.COMPOUND_GET_KEYS.run(workingtag, new Object[0]);
   }

   public static void setData(NBTCompound comp, ReflectionMethod type, String key, Object data) {
      if (data == null) {
         remove(comp, key);
         return;
      }
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
      if (!valideCompound(comp).booleanValue())
         throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
      Object workingtag = gettoCompount(rootnbttag, comp);
      type.run(workingtag, new Object[] { key, data });
      comp.setCompound(rootnbttag);
   }

   public static Object getData(NBTCompound comp, ReflectionMethod type, String key) {
      Object rootnbttag = comp.getCompound();
      if (rootnbttag == null)
         return null;
      if (!valideCompound(comp).booleanValue())
         throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
      Object workingtag = gettoCompount(rootnbttag, comp);
      return type.run(workingtag, new Object[] { key });
   }
}

package camchua.phoban.nbtapi;

import camchua.phoban.nbtapi.utils.MinecraftVersion;
import camchua.phoban.nbtapi.utils.nmsmappings.Forge1710Mappings;
import camchua.phoban.nbtapi.utils.nmsmappings.ReflectionMethod;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.bukkit.inventory.ItemStack;

public class NBTCompound {
   private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
   private final Lock readLock;
   private final Lock writeLock;
   private String compundName;
   private NBTCompound parent;

   protected NBTCompound(NBTCompound owner, String name) {
      this.readLock = this.readWriteLock.readLock();
      this.writeLock = this.readWriteLock.writeLock();
      this.compundName = name;
      this.parent = owner;
   }

   protected Lock getReadLock() {
      return this.readLock;
   }

   protected Lock getWriteLock() {
      return this.writeLock;
   }

   protected void saveCompound() {
      if (this.parent != null) {
         this.parent.saveCompound();
      }

   }

   public String getName() {
      return this.compundName;
   }

   public Object getCompound() {
      return this.parent.getCompound();
   }

   protected void setCompound(Object compound) {
      this.parent.setCompound(compound);
   }

   public NBTCompound getParent() {
      return this.parent;
   }

   public void mergeCompound(NBTCompound comp) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.mergeOtherNBTCompound(this, comp);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public void setString(String key, String value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_STRING, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public String getString(String key) {
      String var2;
      try {
         this.readLock.lock();
         var2 = (String)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_STRING, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   protected String getContent(String key) {
      return NBTReflectionUtil.getContent(this, key);
   }

   public void setInteger(String key, Integer value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_INT, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public Integer getInteger(String key) {
      Integer var2;
      try {
         this.readLock.lock();
         var2 = (Integer)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_INT, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setDouble(String key, Double value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_DOUBLE, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public Double getDouble(String key) {
      Double var2;
      try {
         this.readLock.lock();
         var2 = (Double)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_DOUBLE, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setByte(String key, Byte value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BYTE, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public Byte getByte(String key) {
      Byte var2;
      try {
         this.readLock.lock();
         var2 = (Byte)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BYTE, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setShort(String key, Short value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_SHORT, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public Short getShort(String key) {
      Short var2;
      try {
         this.readLock.lock();
         var2 = (Short)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_SHORT, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setLong(String key, Long value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_LONG, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public Long getLong(String key) {
      Long var2;
      try {
         this.readLock.lock();
         var2 = (Long)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_LONG, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setFloat(String key, Float value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_FLOAT, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public Float getFloat(String key) {
      Float var2;
      try {
         this.readLock.lock();
         var2 = (Float)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_FLOAT, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setByteArray(String key, byte[] value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BYTEARRAY, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public byte[] getByteArray(String key) {
      byte[] var2;
      try {
         this.readLock.lock();
         var2 = (byte[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BYTEARRAY, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setIntArray(String key, int[] value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_INTARRAY, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public int[] getIntArray(String key) {
      int[] var2;
      try {
         this.readLock.lock();
         var2 = (int[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_INTARRAY, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setBoolean(String key, Boolean value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BOOLEAN, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   protected void set(String key, Object val) {
      NBTReflectionUtil.set(this, key, val);
      this.saveCompound();
   }

   public Boolean getBoolean(String key) {
      Boolean var2;
      try {
         this.readLock.lock();
         var2 = (Boolean)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BOOLEAN, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public void setObject(String key, Object value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setObject(this, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public <T> T getObject(String key, Class<T> type) {
      Object var3;
      try {
         this.readLock.lock();
         var3 = NBTReflectionUtil.getObject(this, key, type);
      } finally {
         this.readLock.unlock();
      }

      return (T) var3;
   }

   public void setItemStack(String key, ItemStack item) {
      try {
         this.writeLock.lock();
         this.removeKey(key);
         this.addCompound(key).mergeCompound(NBTItem.convertItemtoNBT(item));
      } finally {
         this.writeLock.unlock();
      }

   }

   public ItemStack getItemStack(String key) {
      ItemStack var3;
      try {
         this.readLock.lock();
         NBTCompound comp = this.getCompound(key);
         var3 = NBTItem.convertNBTtoItem(comp);
      } finally {
         this.readLock.unlock();
      }

      return var3;
   }

   public void setUUID(String key, UUID value) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_UUID, key, value);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public UUID getUUID(String key) {
      UUID var2;
      try {
         this.readLock.lock();
         var2 = (UUID)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_UUID, key);
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public Boolean hasKey(String key) {
      return this.hasTag(key);
   }

   public boolean hasTag(String key) {
      boolean var3;
      try {
         this.readLock.lock();
         Boolean b = (Boolean)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_HAS_KEY, key);
         if (b != null) {
            var3 = b;
            return var3;
         }

         var3 = false;
      } finally {
         this.readLock.unlock();
      }

      return var3;
   }

   public void removeKey(String key) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.remove(this, key);
         this.saveCompound();
      } finally {
         this.writeLock.unlock();
      }

   }

   public Set<String> getKeys() {
      Set var1;
      try {
         this.readLock.lock();
         var1 = NBTReflectionUtil.getKeys(this);
      } finally {
         this.readLock.unlock();
      }

      return var1;
   }

   public NBTCompound addCompound(String name) {
      NBTCompound comp;
      try {
         this.writeLock.lock();
         if (this.getType(name) != NBTType.NBTTagCompound) {
            NBTReflectionUtil.addNBTTagCompound(this, name);
            comp = this.getCompound(name);
            if (comp == null) {
               throw new NbtApiException("Error while adding Compound, got null!");
            }

            this.saveCompound();
            NBTCompound var3 = comp;
            return var3;
         }

         comp = this.getCompound(name);
      } finally {
         this.writeLock.unlock();
      }

      return comp;
   }

   public NBTCompound getCompound(String name) {
      NBTCompound var3;
      try {
         this.readLock.lock();
         NBTCompound next;
         if (this.getType(name) != NBTType.NBTTagCompound) {
            next = null;
            return next;
         }

         next = new NBTCompound(this, name);
         if (!NBTReflectionUtil.valideCompound(next)) {
            var3 = null;
            return var3;
         }

         var3 = next;
      } finally {
         this.readLock.unlock();
      }

      return var3;
   }

   public NBTCompound getOrCreateCompound(String name) {
      return this.addCompound(name);
   }

   public NBTList<String> getStringList(String name) {
      NBTList var3;
      try {
         this.writeLock.lock();
         NBTList<String> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagString, String.class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public NBTList<Integer> getIntegerList(String name) {
      NBTList var3;
      try {
         this.writeLock.lock();
         NBTList<Integer> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagInt, Integer.class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public NBTList<int[]> getIntArrayList(String name) {
      NBTList var3;
      try {
         this.writeLock.lock();
         NBTList<int[]> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagIntArray, int[].class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public NBTList<UUID> getUUIDList(String name) {
      NBTList var3;
      try {
         this.writeLock.lock();
         NBTList<UUID> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagIntArray, UUID.class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public NBTList<Float> getFloatList(String name) {
      NBTList var3;
      try {
         this.writeLock.lock();
         NBTList<Float> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagFloat, Float.class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public NBTList<Double> getDoubleList(String name) {
      NBTList var3;
      try {
         this.writeLock.lock();
         NBTList<Double> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagDouble, Double.class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public NBTList<Long> getLongList(String name) {
      NBTList var3;
      try {
         this.writeLock.lock();
         NBTList<Long> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagLong, Long.class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public NBTType getListType(String name) {
      NBTType var2;
      try {
         this.readLock.lock();
         if (this.getType(name) == NBTType.NBTTagList) {
            var2 = NBTReflectionUtil.getListType(this, name);
            return var2;
         }

         var2 = null;
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public NBTCompoundList getCompoundList(String name) {
      NBTCompoundList var3;
      try {
         this.writeLock.lock();
         NBTCompoundList list = (NBTCompoundList)NBTReflectionUtil.getList(this, name, NBTType.NBTTagCompound, NBTListCompound.class);
         this.saveCompound();
         var3 = list;
      } finally {
         this.writeLock.unlock();
      }

      return var3;
   }

   public <T> T getOrDefault(String key, T defaultValue) {
      if (defaultValue == null) {
         throw new NullPointerException("Default type in getOrDefault can't be null!");
      } else if (!this.hasTag(key)) {
         return defaultValue;
      } else {
         Class<?> clazz = defaultValue.getClass();
         if (clazz == Byte.class) {
            return (T) this.getByte(key);
         } else if (clazz == Short.class) {
            return (T) this.getShort(key);
         } else if (clazz == Integer.class) {
            return (T) this.getInteger(key);
         } else if (clazz == Long.class) {
            return (T) this.getLong(key);
         } else if (clazz == Float.class) {
            return (T) this.getFloat(key);
         } else if (clazz == Double.class) {
            return (T) this.getDouble(key);
         } else if (clazz == byte[].class) {
            return (T) this.getByteArray(key);
         } else if (clazz == int[].class) {
            return (T) this.getIntArray(key);
         } else if (clazz == String.class) {
            return (T) this.getString(key);
         } else if (clazz == UUID.class) {
            return (T) this.getUUID(key);
         } else {
            throw new NbtApiException("Unsupported type for getOrDefault: " + clazz.getName());
         }
      }
   }

   public NBTType getType(String name) {
      NBTType var3;
      try {
         this.readLock.lock();
         Object o;
         if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
            o = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET, name);
            if (o == null) {
               var3 = null;
               return var3;
            }

            var3 = NBTType.valueOf((Byte)ReflectionMethod.COMPOUND_OWN_TYPE.run(o));
            return var3;
         }

         o = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_TYPE, name);
         if (o != null) {
            var3 = NBTType.valueOf((Byte)o);
            return var3;
         }

         var3 = null;
      } finally {
         this.readLock.unlock();
      }

      return var3;
   }

   public void writeCompound(OutputStream stream) {
      try {
         this.writeLock.lock();
         NBTReflectionUtil.writeApiNBT(this, stream);
      } finally {
         this.writeLock.unlock();
      }

   }

   public String toString() {
      return this.asNBTString();
   }

   /** @deprecated */
   @Deprecated
   public String toString(String key) {
      return this.asNBTString();
   }

   public void clearNBT() {
      Iterator var1 = this.getKeys().iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         this.removeKey(key);
      }

   }

   /** @deprecated */
   @Deprecated
   public String asNBTString() {
      String var2;
      try {
         this.readLock.lock();
         Object comp = NBTReflectionUtil.gettoCompount(this.getCompound(), this);
         if (comp != null) {
            if (MinecraftVersion.isForgePresent() && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
               var2 = Forge1710Mappings.toString(comp);
               return var2;
            }

            var2 = comp.toString();
            return var2;
         }

         var2 = "{}";
      } finally {
         this.readLock.unlock();
      }

      return var2;
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else {
         if (obj instanceof NBTCompound) {
            NBTCompound other = (NBTCompound)obj;
            if (this.getKeys().equals(other.getKeys())) {
               Iterator var3 = this.getKeys().iterator();

               String key;
               do {
                  if (!var3.hasNext()) {
                     return true;
                  }

                  key = (String)var3.next();
               } while(isEqual(this, other, key));

               return false;
            }
         }

         return false;
      }
   }

   protected static boolean isEqual(NBTCompound compA, NBTCompound compB, String key) {
      if (compA.getType(key) != compB.getType(key)) {
         return false;
      } else {
         switch(compA.getType(key)) {
         case NBTTagByte:
            return compA.getByte(key).equals(compB.getByte(key));
         case NBTTagByteArray:
            return Arrays.equals(compA.getByteArray(key), compB.getByteArray(key));
         case NBTTagCompound:
            return compA.getCompound(key).equals(compB.getCompound(key));
         case NBTTagDouble:
            return compA.getDouble(key).equals(compB.getDouble(key));
         case NBTTagEnd:
            return true;
         case NBTTagFloat:
            return compA.getFloat(key).equals(compB.getFloat(key));
         case NBTTagInt:
            return compA.getInteger(key).equals(compB.getInteger(key));
         case NBTTagIntArray:
            return Arrays.equals(compA.getIntArray(key), compB.getIntArray(key));
         case NBTTagList:
            return NBTReflectionUtil.getEntry(compA, key).toString().equals(NBTReflectionUtil.getEntry(compB, key).toString());
         case NBTTagLong:
            return compA.getLong(key).equals(compB.getLong(key));
         case NBTTagShort:
            return compA.getShort(key).equals(compB.getShort(key));
         case NBTTagString:
            return compA.getString(key).equals(compB.getString(key));
         default:
            return false;
         }
      }
   }
}

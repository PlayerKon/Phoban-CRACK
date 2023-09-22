package camchua.phoban.game;

import java.util.HashMap;
import org.bukkit.entity.Entity;

public class EntityData {
   private static HashMap<Entity, EntityData> data = new HashMap();
   private Entity entity;
   private Game game;

   public static HashMap<Entity, EntityData> data() {
      return data;
   }

   public EntityData(Entity e, Game g) {
      this.entity = e;
      this.game = g;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public Game getGame() {
      return this.game;
   }
}

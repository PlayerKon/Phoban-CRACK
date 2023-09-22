package camchua.phoban.game;

public enum GameStatus {
   WAITING("WAITING"),
   STARTING("STARTING"),
   PLAYING("PLAYING");

   private String key;

   private GameStatus(String k) {
      this.key = k;
   }

   public String toString() {
      return this.key;
   }

   // $FF: synthetic method
   private static GameStatus[] $values() {
      return new GameStatus[]{WAITING, STARTING, PLAYING};
   }
}

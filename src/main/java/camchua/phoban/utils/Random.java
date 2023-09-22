package camchua.phoban.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
   private List<Random.Chance> chances;
   private double sum;
   private java.util.Random random;

   public Random() {
      this.random = new java.util.Random();
      this.chances = new ArrayList();
      this.sum = 0.0D;
   }

   public Random(long seed) {
      this.random = new java.util.Random(seed);
      this.chances = new ArrayList();
      this.sum = 0.0D;
   }

   public void addChance(Object element, double chance) {
      if (!this.chances.contains(element)) {
         this.chances.add(new Random.Chance(element, this.sum, this.sum + chance));
         this.sum += chance;
      }

   }

   public Object getRandomElement() {
      double index = ThreadLocalRandom.current().nextDouble(0.0D, this.sum);
      Object rw = null;

      while(rw == null) {
         Iterator var4 = this.chances.iterator();

         while(var4.hasNext()) {
            Random.Chance chance = (Random.Chance)var4.next();
            if (chance.getLowerLimit() <= index && chance.getUpperLimit() > index) {
               rw = chance.getElement();
               return rw;
            }
         }
      }

      return null;
   }

   public double getOptions() {
      return this.sum;
   }

   public int getChoices() {
      return this.chances.size();
   }

   private class Chance {
      private double upperLimit;
      private double lowerLimit;
      private Object element;

      public Chance(Object element, double lowerLimit, double upperLimit) {
         this.element = element;
         this.upperLimit = upperLimit;
         this.lowerLimit = lowerLimit;
      }

      public double getUpperLimit() {
         return this.upperLimit;
      }

      public double getLowerLimit() {
         return this.lowerLimit;
      }

      public Object getElement() {
         return this.element;
      }

      public String toString() {
         return "[" + Double.toString(this.lowerLimit) + "|" + Double.toString(this.upperLimit) + "]: " + this.element.toString();
      }
   }
}

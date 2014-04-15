//Matt Locklin
//Updated 4/10/2014 7:37pm

package creature.evolvingWilds.mattlock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import creature.evolvingWilds.vinny.Critter;
import creature.phenotype.Block;
import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.Joint;
import creature.phenotype.Vector3;

public class PopulationThread extends Thread
{

  private Critter critters[];

  private Critter alpha;
  private long numClimbs = 0;
  private long numCross = 0;

  private boolean paused = false;

  private boolean nextGeneration = false;

  /**
   * @param nextGeneration
   *          the nextGeneration to set
   */
  public void setNextGeneration(boolean nextGeneration)
  {
    if (!this.nextGeneration)
    {
      this.nextGeneration = nextGeneration;
    }
  }

  File CSV_Output;

  public PopulationThread(int size)
  {
    critters = new Critter[size];

  }

  /**
   * @return the critters
   */
  public Critter[] getCritters()
  {
    return critters;
  }

  BufferedWriter writer;

  public void run()
  {
    CSV_Output = new File("doc/CreatureHillclimbingOutput"
        + Thread.currentThread().getName());
    CSV_Output.delete();
    try
    {
      CSV_Output.createNewFile();
      FileWriter fw = new FileWriter(CSV_Output.getAbsoluteFile());
      writer = new BufferedWriter(fw);
      writer
          .write("Generations, Best Fitness, Avg Fitness, Worst Fitness, Largest body, Avg body\n");
    } catch (IOException e)
    {
      System.err.println("Error producing file for Thread: \n"
          + Thread.currentThread().getName());
      e.printStackTrace();
    }

    // Instantiates all fo the creatures
    for (int i = 0; i < critters.length; i++)
    {
      critters[i] = Birth();
      critters[i].setIndex(i);
      // System.out.println("KAISER: " + critters[i].toString());
    }
    // hillclimb each member
    while (true)
    {
      // pause function implemented
      float bestFitness = 0;
      float fitness = 0;
      float worstFitness = 0;
      float sumFitness = 0;
      int bodySize;
      int largestBody = 0;
      float bodyAverage = 0;
      if (!paused || nextGeneration)
      {
        for (Critter c : critters)
        {
          c.hillClimb();
          fitness = c.getFitness();
          if (fitness > bestFitness)
            bestFitness = fitness;
          else if (fitness < worstFitness)
            worstFitness = fitness;
          sumFitness += fitness;

          bodySize = c.getBody().length;
          if (bodySize > largestBody)
            largestBody = bodySize;
          bodyAverage += bodySize;
        }
        if (nextGeneration)
        {
          nextGeneration = false;
        }
        numClimbs++;

        bodyAverage /= critters.length;
        averageFitness = sumFitness / critters.length;
        try
        {
          writer.write(numClimbs + "," + bestFitness + "," + averageFitness
              + "," + worstFitness + "," + largestBody + "," + bodyAverage
              + "\n");
          if (numClimbs % 100 == 0)
          {
            writer.flush();
            System.out.println(Thread.currentThread().getName() +" Flushed");
          }

        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  
  float averageFitness;
  private Critter singlepointxover(Critter parent)
  {
    Random r = new Random();
    Genotype alphagenes = new Genotype(alpha.getBody(),
        alpha.getBlockForwardVector(0), alpha.getBlockUpVector(0));
    Genotype parentgenes = new Genotype(parent.getBody(),
        parent.getBlockForwardVector(0), parent.getBlockUpVector(0));
    // Genotype compl =
    return parent;
  }

  private Critter doubleparentxover(Critter parent)
  {
    return parent;
  }

  private Critter Birth()
  {

    Critter baby = null;

    while (baby == null)
    {
      Random randomgen = new Random();

      int numberoflegs = 1;// randomgen.nextInt(7);
      int seglength = /* randomgen.nextInt(2) + */1;

      float length = randomgen.nextFloat() * 30;
      float width = randomgen.nextFloat() * 30;
      float height = randomgen.nextFloat() * 30;

      length = checkdim(length, width, height, randomgen);
      width = checkdim(width, height, length, randomgen);
      height = checkdim(height, length, width, randomgen);

      EnumJointType[] jointtypes = EnumJointType.values();
      EnumJointSite[] sites = EnumJointSite.values();

      Vector3.setDisplayDecimalPlaces(3);
      Vector3.test();

      Vector3 rootForward = Vector3.FORWARD;
      Vector3 rootUp = Vector3.UP;

      // building up
      Block[] body = new Block[1 + numberoflegs * seglength];
      body[0] = new Block(Block.PARENT_INDEX_NONE, null, length, width, height);
      Block[] leg = new Block[seglength];

      for (int i = 0; i < seglength; i++)
      {
        EnumJointSite toparent = sites[randomgen.nextInt(26)];
        EnumJointSite tochild = getValidSite(toparent, randomgen);
        Joint joint = new Joint(jointtypes[randomgen.nextInt(4)],
            sites[randomgen.nextInt(26)], sites[randomgen.nextInt(26)],
            getRandomOrientation(randomgen));

        length = randomgen.nextFloat() * 10;
        width = randomgen.nextFloat() * 10;
        height = randomgen.nextFloat() * 10;

        length = checkdim(length, width, height, randomgen);
        width = checkdim(width, height, length, randomgen);
        height = checkdim(height, length, width, randomgen);

        leg[i] = new Block(i, joint, length, width, height);

      }
      int bodyindex = 1;
      for (int k = 1; k <= numberoflegs; k++)
      {
        for (int j = 0; j < leg.length; j++)
        {
          body[bodyindex] = new Block(leg[j]);
          bodyindex++;
        }

      }

      try
      {
        baby = new Critter(body, rootForward, rootUp, true);
      } catch (Exception e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    return baby;
  }

  /**
   * @param paused
   *          the paused to set
   */
  public void setPause(boolean paused)
  {
    this.paused = paused;
  }

  private float checkdim(float dim, float a, float b, Random r)
  {
    float dominate;
    if (a >= b)
      dominate = a;
    else
      dominate = b;
    if (dim * 10 > dominate)
      return checkdim(r.nextFloat() * dominate, a, b, r);
    else
    {
      if (dim < 1.0f)
        return dim + 1;
      else
        return dim;
    }

  }

  private float getRandomOrientation(Random r)
  {
    return Math.abs(r.nextFloat() * (float) Math.PI / 2);

  }

  // doesn't prevent site colisions
  private EnumJointSite getValidSite(EnumJointSite parent, Random r)
  {
    int randomint = r.nextInt(26);
    EnumJointSite[] sites = EnumJointSite.values();

    EnumJointSite out = sites[randomint];
    if (out.ordinal() == parent.ordinal())
      return getValidSite(parent, r);
    else
      return parent;
  }

  /**
   * @author Mario LoPrinzi Gets the fittest creature in the thread
   */
  public Critter getAlpha()
  {
    for (Critter c : critters)
    {
      if (alpha == null || c.getFitness() > alpha.getFitness())
      {
        alpha = c;
      }
    }
    return alpha;
  }

  Critter lowest;

  public Critter getLowest()
  {
    for (Critter c : critters)
    {
      if (lowest == null || c.getFitness() < lowest.getFitness())
      {
        lowest = c;
      }
    }
    return lowest;
  }

  /**
   * @author Mario LoPirnzi Calculates the average Fitness of the thread not
   *         100% accurate because it isn't synched so it is very instanced
   *         based.
   * 
   */
  public float getAverage()
  {
    float average = 0;
    for (Critter c : critters)
    {
      average += c.getFitness();
    }
    return (average / critters.length);
  }

  private Genotype getcomplexdna(Genotype a, Genotype b)
  {
    if (a.getDnaLength() >= b.getDnaLength())
      return a;
    else
      return b;
  }

  /*
   * private EnumJointSite getJointOtherEnd(EnumJointSite j,Random r){
   * EnumJointSite[] sites = EnumJointSite.values();
   * if(j==EnumJointSite.VERTEX_FRONT_NORTHWEST){
   * 
   * }
   * 
   * 
   * }
   */

  /**
   * @author Mario
   * 
   * @return the number of hill climbs completed on this thread
   */
  public long getNumClimbs()
  {
    return numClimbs;
  }

  /**
   * @author Mario
   * @return the number of crossovers completed on this thread.
   */
  public long getNumCross()
  {
    return numCross;
  }

  /*
   * public static void main(String[] args) throws InterruptedException {
   * PopulationThread threadA = new PopulationThread(5);
   * 
   * }
   */

}

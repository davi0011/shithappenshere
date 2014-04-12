package creature.evolvingWilds.vinny;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import creature.evolvingWilds.james.BlockBrain;
import creature.evolvingWilds.james.CompleteBrain;
import creature.phenotype.Block;
import creature.phenotype.Creature;
import creature.phenotype.Vector3;

public class Critter extends Creature
{
  Block blockList[];
  Random rand = new Random();
  public float bestFitness;
  public float lastFitness;
  Vector3 forward;
  Vector3 up;
  Critter creature;
  CompleteBrain decisions;

  public float getFitness()
  {
    return bestFitness;
  }

  public Critter(Block[] body, Vector3 rootForward, Vector3 rootUp,
      boolean isParent)
  {
    super(body, rootForward, rootUp);
    forward = rootForward;
    up = rootUp;
    blockList = body;

    if (isParent)//Critter acts as its own wrapper class,
      //so this removes initalizations
    {
      decisions = new CompleteBrain(body, rand);
      //this keeps track of values
    }
  }

  public Critter clone()
  {

    //Avoiding call by reference for cloning methods
    Block[] body = new Block[blockList.length];
    for (int i = 0; i < blockList.length; i++)
    {
      body[i] = new Block(blockList[i]);
      body[i].setIndexOfParent(blockList[i].getIndexOfParent());
      if (body[i].getIndexOfParent() == -1)
        body[i].setJointToParent(null);
    }
    Vector3 forward = new Vector3(this.forward);
    Vector3 up = new Vector3(this.up);
    Critter clone = new Critter(body, forward, up, false);
    clone.bestFitness = bestFitness; //Makes sure fitness is passed
    return clone;
  }

  public Block[] getBody()
  {
    return blockList;
  }

  boolean isIllegal;
  boolean notPrinted;

  public void hillClimb()
  {
    Block[] attemptBlockList = blockList;
    isIllegal = true;
    // System.out.print("================Hillclimbing");
    notPrinted = true;
    while (isIllegal) //Will keep hillclimbing from making an illegal creature
    {
      try
      {
        //Block creation to check sizes
        attemptBlockList = decisions.changeCreature();
        //Critter creation to check validity
        creature = new Critter(attemptBlockList, forward, up, false);
        isIllegal = false;
      } catch (IllegalArgumentException e)
      {
        // e.printStackTrace();
        decisions.resetChange();
      }
    }
    // System.out.println();
    // if(i == 100) return;
    float newFitness = creature.runSimulation();
    if (newFitness > bestFitness)
    {
      //assigns last changes to creature
      //System.err.println(bestFitness + "-->" + newFitness + ": Improved!");
      bestFitness = newFitness;
      decisions.confirmChange();
      blockList = attemptBlockList;
    } else
    {
      decisions.resetChange();
    }
  }

  private float runSimulation()
  {
    float curFitness = 0;
    float newFitness = 0;

    int steps = 0;
    boolean hasNotPrinted = true;
    //Loops for 500 steps after the last best fitness
    while (steps < 500)
    {
      try
      {
        curFitness = advanceSimulation();
      } catch (IllegalArgumentException e)
      {
        if (hasNotPrinted)
        { //Some error with the force Vectors inside run simulation
          //e.printStackTrace();
          System.err.println();
          hasNotPrinted = false;
        }

      }
      if (curFitness > newFitness)
      {
        //assures loop continues if creature is jumping
        newFitness = curFitness;
        steps = 0;
      }
      steps++;
    }
    return newFitness;
  }

  public int index;
  public void setIndex(int i)
  {
    //Index of the Creature on the little bar thing
    index = i;
    
  }
  public int getIndex()
  {
    return index;
  }
}

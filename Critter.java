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

    if (isParent)
    {
      decisions = new CompleteBrain(body, rand);
      // blockList = decisions.initializeRules();
    }
  }

  public Critter clone()
  {

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
    clone.bestFitness = bestFitness;
    return new Critter(body, forward, up, false);
  }

  public Block[] getBody()
  {
    return blockList;
  }

  boolean isIllegal;
  boolean notPrinted;

  public void hillClimb()
  {
    Block[] attemptBlockList = blockList;// = decisions.changeCreature();
    isIllegal = true;
    // System.out.print("================Hillclimbing");
    notPrinted = true;
    int i = 0;
    while (isIllegal)
    {
      try
      {
        attemptBlockList = decisions.changeCreature();
        creature = new Critter(attemptBlockList, forward, up, false);
        isIllegal = false;
      } catch (IllegalArgumentException e)
      {
        // e.printStackTrace();
        decisions.resetChange();
        // i++;
        // System.out.print(".");
      }
    }
    // System.out.println();
    // if(i == 100) return;
    float newFitness = creature.runSimulation();
    if (newFitness > bestFitness)
    {
      System.err.println(bestFitness + "-->" + newFitness + ": Improved!");
      bestFitness = newFitness;
      assert(bestFitness == getFitness());
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
    while (steps < 500)
    {
      try
      {
        curFitness = advanceSimulation();
      } catch (IllegalArgumentException e)
      {
        if (hasNotPrinted)
        {
          e.printStackTrace();
          hasNotPrinted = false;
        }

      }
      if (curFitness > newFitness)
      {
        newFitness = curFitness;
        steps = 0;
      }
      steps++;
    }
    return newFitness;
  }
}

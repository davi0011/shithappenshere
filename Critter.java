package evolvingWilds.vinny;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import creature.phenotype.Block;
import creature.phenotype.Creature;
import creature.phenotype.Vector3;
import evolvingWilds.james.BlockBrain;
import evolvingWilds.james.CompleteBrain;

public class Critter extends Creature
{
  Block blockList[];
  Random rand = new Random();
  float bestFitness;
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
    }
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
    //System.out.print("================Hillclimbing");
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
        //e.printStackTrace();
        decisions.resetChange();
        //i++;
        //System.out.print(".");
      }
    }
    //System.out.println();
    //if(i == 100) return;
    System.out.println("Changed ~ ");
    float newFitness = creature.runSimulation();
    if (newFitness > bestFitness)
    {
      System.err.println(bestFitness + "-->" + newFitness + ": Improved!");
      bestFitness = newFitness;
      decisions.confirmChange();
      blockList = attemptBlockList;
    } else
    {
      System.out.println(newFitness);
      decisions.resetChange();
    }
  }
  public Critter clone()
  {
    Block body[] = new Block[this.getBody().length];
    for (int i = 0; i < this.getBody().length; i++)
    {
      body[i] = new Block(this.getBody()[i]);
    }
    Vector3 forward = new Vector3(this.forward);
    Vector3 up = new Vector3(this.up);
    return new Critter(body, forward, up, true);
  }

  private float runSimulation()
  {
    float curFitness = 0;
    float newFitness = 0;

    int steps = 0;

    while (steps < 1000)
    {
      curFitness = advanceSimulation();
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

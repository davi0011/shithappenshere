package evolvingWilds.vinny;

import java.util.Random;

import creature.phenotype.Block;
import creature.phenotype.Creature;
import creature.phenotype.Vector3;
import evolvingWilds.james.BlockBrain;

public class Critter extends Creature
{
  Block blockList[];
  BlockBrain brainList[];
  Random rand = new Random();
  float bestFitness;
  Vector3 forward;
  Vector3 up;
  Critter creature;

  public float getFitness()
  {
    return bestFitness;
  }

  public Critter(Block[] body, Vector3 rootForward, Vector3 rootUp, boolean parent)
  {
    super(body, rootForward, rootUp);
    forward = rootForward;
    up = rootUp;
    blockList = body;
    if (parent)
    {
      brainList = new BlockBrain[blockList.length];

      for (int i = 0; i < blockList.length; i++)
      {
        brainList[i] = new BlockBrain(rand, i);
      }
    }
  }

  public Block[] getBody()
  {
    return blockList;
  }

  public void hillClimb()
  {
    int jointChoice = rand.nextInt(getNumberOfBodyBlocks());
    brainList[jointChoice].changeJoint();
    blockList[jointChoice].setJointToParent(brainList[jointChoice].getJoint());
    creature = new Critter(blockList, forward, up, false);
    float newFitness = creature.runSimulation();
    if (newFitness > bestFitness)
    {
      bestFitness = newFitness;
      brainList[jointChoice].confirmChange();
    } else
    {
      brainList[jointChoice].resetJoint();
    }
  }

  private float runSimulation()
  {
    float curFitness = 0;
    float newFitness = 0;

    int steps = 0;

    while (steps < 500)
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

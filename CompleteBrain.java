package creature.evolvingWilds.james;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import creature.phenotype.Block;

public class CompleteBrain
{

  ArrayList<BlockBrain> brainList = new ArrayList<BlockBrain>();
  ArrayList<Byte> adaptionChoices;
  ArrayList<Block> creatureList;
  byte adaptChoice;
  BlockBrain blockChoice;
  Random rand = new Random();

  public CompleteBrain(Block[] blockArray, Random val)
  {
    adaptionChoices = new ArrayList<Byte>(Arrays.asList(new Byte[]
    { 0, 1, 3, 4, 5}));
    // 0 = Joint Type; 1 = Rule - change 1; 2 = Rule - change all
    // 3 = changeLength; 4 = Joint Site
    // 5 = addChildToBlock
    // rand = val;

    ArrayList<Block> temp = new ArrayList<Block>();
    for (int i = 0; i < blockArray.length; i++)
    { //Initializing all brains
      brainList.add(new BlockBrain(rand, i, blockArray[i]));
      blockArray[i].setJointToParent(brainList.get(i).getJoint());
      blockArray[i] = new Block(blockArray[i]);
      temp.add(blockArray[i]);
    }
    creatureList = temp;
  }

  byte blockIndex;

  public Block[] changeCreature()
  {
    blockIndex = (byte) rand.nextInt(brainList.size());
    blockChoice = brainList.get(blockIndex);
    adaptChoice = adaptionChoices.get(rand.nextInt(adaptionChoices.size()));
    if (creatureList.get(blockIndex).getIndexOfParent() == Block.PARENT_INDEX_NONE)
      adaptChoice = 3;
    Block temp = creatureList.get(blockIndex);
    switch (adaptChoice)
    {
    case 0:
      blockChoice.changeJoint();
      temp.setJointToParent(blockChoice.getJoint());
      break;
    case 1:
      blockChoice.changeRules();
      temp.setJointToParent(blockChoice.getJoint());
      break;
    //case 2:
    //  break;
    case 3:
      blockChoice.changeLengths();
      temp.setSize(blockChoice.getLength(), blockChoice.getHeight(),
          blockChoice.getWidth());
      break;
    case 4:
      blockChoice.changeJointSite();
      temp.setJointToParent(blockChoice.getJoint());
      break;
    case 5:
      //Creating new blocks to add, special case
      BlockBrain childBrain = new BlockBrain(rand, creatureList.size());
      Block childBlock = new Block(blockIndex, childBrain.getJoint(), 1.0f,
          1.0f, 1.0f);
      brainList.add(childBrain);
      creatureList.add(childBlock);
      break;
    }
    try
    {
        temp = new Block(temp.getIndexOfParent(), temp.getJointToParent(),
            temp.getLength(), temp.getHeight(), temp.getWidth());
        creatureList.set(blockIndex, temp);

      return toArray();
    } catch (IllegalArgumentException e)
    {
      // System.out.println("Caught");
      throw e;
    }
  }

  public void confirmChange()
  {
    blockChoice.confirmChange();
  }

  public void resetChange()
  {
    if (adaptChoice == 5)
    {//Special remove bad added block case
      creatureList.remove(creatureList.size() - 1);
      brainList.remove(brainList.size() - 1);
    }

    blockChoice.resetJoint();
  }

  private Block[] toArray()
  { //Avoiding issues with Lists and Arrays
    Block[] blockArray = new Block[creatureList.size()];

    Iterator<Block> blockIt = creatureList.iterator();
    int i = 0;
    while (blockIt.hasNext())
    {
      blockArray[i] = blockIt.next();
      i++;
    }

    return blockArray;
  }
}

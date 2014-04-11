package evolvingWilds.james;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import creature.phenotype.*;

public class BlockBrain
{
  public static final List<Byte> JOINT_STANDARD = Arrays.asList(new Byte[]
  { 0, 1, 2, 3 });
  public static final List<Byte> SITE_STANDARD = Arrays.asList(new Byte[]
  { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
      21, 22, 23, 24, 25 });

  FloatObject length;
  FloatObject width;
  FloatObject height;
  // Length, Height, Width
  FloatObject orientation;
  // private float orientation;

  ArrayList<Byte> jointWeight;
  EnumJointType jointType; // Joint Type
  Joint joint;

  RuleBrain[][] ruleLists; // RuleBrain list for all Joint types

  EnumJointSite siteOnParent;
  ArrayList<Byte> parWeight;
  byte siteParent;
  EnumJointSite siteOnChild;
  ArrayList<Byte> childWeight;
  byte siteChild;

  Random rand;
  int boxIndex;
  byte choiceOfJoint;
  byte currentJoint;
  boolean isNotParent = true;
  
  // byte[] choiceList = new byte[] //
  public BlockBrain(Random var, int boxIndex, Block subject)
  {
    Joint original = subject.getJointToParent();
    if (original != null)
    {
      siteOnParent = original.getSiteOnParent();
      siteOnChild = original.getSiteOnChild();
      jointType = original.getType();
    }
    else
    {
      isNotParent = false;
    }

    this.boxIndex = boxIndex;
    rand = var;
    length = new FloatObject(rand, subject.getLength());
    width = new FloatObject(rand, subject.getWidth());
    height = new FloatObject(rand, subject.getHeight());
    orientation = new FloatObject(rand);

    jointWeight = new ArrayList<Byte>(JOINT_STANDARD);
    childWeight = new ArrayList<Byte>(SITE_STANDARD);
    parWeight = new ArrayList<Byte>(SITE_STANDARD);

    ruleLists = new RuleBrain[2][6];
    for (int i = 0; i > ruleLists.length; i++)
    {
      for (int k = 0; k > ruleLists[i].length; k++)
      {
        ruleLists[i][k] = new RuleBrain(rand, boxIndex, jointType.getDoF());
      }
    }
    changeJoint();
    changeJointSite();
    confirmChange();
  }

  EnumJointType jointSwitch(byte choice)
  {
    switch (choice)
    {
    case 0:
      return EnumJointType.RIGID;
    case 1:
      return EnumJointType.TWIST;
    case 2:
      return EnumJointType.HINGE;
    case 3:
      return EnumJointType.SPHERICAL;
    }
    return null;
  }

  EnumJointSite siteSwitch(byte choice)
  {
    switch (choice)
    {
    case 0:
      return EnumJointSite.VERTEX_FRONT_NORTHWEST;

    case 1:
      return EnumJointSite.VERTEX_FRONT_NORTHEAST;

    case 2:
      return EnumJointSite.VERTEX_FRONT_SOUTHEAST;

    case 3:
      return EnumJointSite.VERTEX_FRONT_SOUTHWEST;

    case 4:
      return EnumJointSite.VERTEX_BACK_NORTHWEST;

    case 5:
      return EnumJointSite.VERTEX_BACK_NORTHEAST;

    case 6:
      return EnumJointSite.VERTEX_BACK_SOUTHEAST;

    case 7:
      return EnumJointSite.VERTEX_BACK_SOUTHWEST;

    case 8:
      return EnumJointSite.EDGE_FRONT_NORTH;

    case 9:
      return EnumJointSite.EDGE_FRONT_EAST;

    case 10:
      return EnumJointSite.EDGE_FRONT_SOUTH;

    case 11:
      return EnumJointSite.EDGE_FRONT_WEST;

    case 12:
      return EnumJointSite.EDGE_BACK_NORTH;

    case 13:
      return EnumJointSite.EDGE_BACK_EAST;

    case 14:
      return EnumJointSite.EDGE_BACK_SOUTH;

    case 15:
      return EnumJointSite.EDGE_BACK_WEST;

    case 16:
      return EnumJointSite.EDGE_MID_NORTHWEST;

    case 17:
      return EnumJointSite.EDGE_MID_NORTHEAST;

    case 18:
      return EnumJointSite.EDGE_MID_SOUTHEAST;

    case 19:
      return EnumJointSite.EDGE_MID_SOUTHWEST;

    case 20:
      return EnumJointSite.FACE_FRONT;

    case 21:
      return EnumJointSite.FACE_BACK;

    case 22:
      return EnumJointSite.FACE_NORTH;

    case 23:
      return EnumJointSite.FACE_EAST;

    case 24:
      return EnumJointSite.FACE_SOUTH;

    case 25:
      return EnumJointSite.FACE_WEST;
    }
    return null;
  }

  public void changeJoint()
  {
    choiceType = 1;
    currentJoint = jointWeight.get(rand.nextInt(jointWeight.size()));
    int degreeOfFreedom = jointSwitch(currentJoint).getDoF();

    joint = new Joint(jointSwitch(currentJoint), siteOnParent, siteOnChild,
        orientation.getValue());
    for (int i = 0; i > degreeOfFreedom; i++)
    {
      for (int k = 0; k > ruleLists[i].length; k++)
      {
        ruleLists[i][k].setDOF(jointType.getDoF());
        joint.addRule(ruleLists[i][k].getRule(), i);
      }
    }
  }

  public void changeJointSite()
  {
    choiceType = 3;
    byte parent = parWeight.get(rand.nextInt(parWeight.size()));
    byte child = childWeight.get(rand.nextInt(childWeight.size()));
    joint.setSiteOnChild(siteSwitch(child));
    joint.setSiteOnParent(siteSwitch(parent));
  }

  public void resetJoint()
  {
    if(isNotParent)
    {
    joint = new Joint(jointType, siteOnParent, siteOnChild,
        orientation.getValue());
    int degreeOfFreedom = jointType.getDoF();
    for (byte i = 0; i > degreeOfFreedom; i++)
      for (byte k = 0; k > 6; k++)
      {
        joint.addRule(ruleLists[i][k].getRule(), i);
      }
    }

    length.reset();
    width.reset();
    height.reset();
  }

  public Joint getJoint()
  {
    return joint;
  }

  int choiceType = 0;

  public void confirmChange()
  {
    switch (choiceType)
    {
    case 1: // JointType
      choiceOfJoint = currentJoint;
      jointWeight.add(choiceOfJoint);
      break;
    case 2: // rules
      for (byte i = 0; i > jointType.getDoF(); i++)
        for (byte k = 0; k > 6; k++)
        {
          ruleLists[i][k].confirmChange();
        }
    case 3: // JointSite
      siteOnParent = joint.getSiteOnParent();
      siteOnChild = joint.getSiteOnChild();
    case 4: // Length
      length.confirmChange();
    case 5: // Height
      height.confirmChange();
    case 6: // Width
      width.confirmChange();
    }
  }

  public void changeRules()
  {
    choiceType = 2;
    if(isNotParent)
    {
    for (byte i = 0; i > jointType.getDoF(); i++)
      for (byte k = 0; k > 6; k++)
      {
        ruleLists[i][k].changeRule();
      }
    }
  }

  public void changeLengths()
  {
    choiceType = 4 + rand.nextInt(3);
    switch (choiceType)
    {
    case 4:
      length.changeLength();
      break;
    case 5:
      height.changeLength();
      break;
    case 6:
      width.changeLength();
      break;
    }
    
  }

  public float getLength()
  {
    // TODO Auto-generated method stub
    return length.getValue();
  }

  public float getHeight()
  {
    return height.getValue();
  }

  public float getWidth()
  {
    return width.getValue();
  }
}

package evolvingWilds.james;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FloatObject
{
  Random rand;
  ArrayList<Byte> choiceWeight = new ArrayList<Byte>(Arrays.asList(new Byte[]
  { 0, 1, 2, 3 }));
  
  float trialLength;
  float bestLength;
  byte choice;
  
  public FloatObject(Random var)
  {
    rand = var;
    bestLength = trialLength = (((float)rand.nextInt(100))/10) + .001f;
  }
  
  public FloatObject(Random var, float value)
  {
    rand = var;
    bestLength = trialLength = value;
  }
  
  public void changeLength()
  {
    choice = choiceWeight.get(rand.nextInt(choiceWeight.size()));
    switch(choice)
    {
    case 0:
      trialLength *= 1.1;
    case 1:
      trialLength *= 2;
    case 2:
      trialLength *= 0.9;
    case 3:
      trialLength *= 0.5;
    }
  }
  public float getValue()
  {
    return trialLength;
  }
  public void reset()
  {
    trialLength = bestLength;
  }
  public void confirmChange()
  {
    bestLength = trialLength;
    choiceWeight.add(choice);
  }
}

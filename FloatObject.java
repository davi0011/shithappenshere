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
    if(choice == -1)
      choice = choiceWeight.get(rand.nextInt(choiceWeight.size()));
    switch(choice)
    {
    case 0:
      trialLength /= (rand.nextInt(50)+49)/100; //Increase Large Random Amount
      break;
    case 1:
      trialLength /= (rand.nextInt(70)+29)/100; //Increase Small Random Ammount
      break;
    case 2:
      trialLength *= (rand.nextInt(50)+49)/100;
      break;
    case 3:
      trialLength *= (rand.nextInt(70)+29)/100;
      break;
    }
    if (trialLength < 1) trialLength = 1;
  }
  public float getValue()
  {
    return trialLength;
  }
  public void reset()
  {
    trialLength = bestLength;
    choice = -1;
  }
  public void confirmChange()
  {
    bestLength = trialLength;
    choiceWeight.add(choice);
  }
}

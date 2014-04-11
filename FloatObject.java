package evolvingWilds.james;

import java.util.Random;

public class FloatObject
{
  Random rand;
  Float length = (float) 1;
  boolean increasing = true;
  boolean cont = true;
  float stepsize = 0;

  public FloatObject(Random var)
  {
    rand = var;
    length = rand.nextFloat() * 10;
  }
  public FloatObject()
  {
    length = 1f;
  }
  public FloatObject(float value)
  {
    length = value;
  }

  public float getValue()
  {
    return length + stepsize;
  }
  
  public void changeValue()
  {
    if(increasing)
      increase();
    else
      decrease();
  }
  
  private void increase()
  {
    if(cont)
    {
      stepsize = length;
    }
    else
    {
      stepsize /= 2;
    }
    cont = false;
    increasing = false;
  }
  private void decrease()
  {
    stepsize *= -1;
    cont = true;
    if(length < 1)
    {
      length = 1f;
      cont = false;
    }
    increasing = true;
  }

  public void incrementChoice()
  {
    cont ^= cont;
    increasing ^= increasing;
    length += stepsize;
    
  }
}

package evolvingWilds.james;

import java.util.Random;

public class FloatObject
{
  Random rand;
  FloatObject(Random var)
  {
    rand = var;
  }
  public float getValue()
  {
    // TODO Auto-generated method stub
    return rand.nextFloat()*10;
  }

}

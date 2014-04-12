package evolvingWilds.james;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import creature.phenotype.*;

public class RuleBrain
{
  public static final List<Byte> INPUT_STANDARD = Arrays.asList(new Byte[]
  { 0, 1, 2, 3, 4 });
  public static final List<Byte> RULE_STANDARD = Arrays.asList(new Byte[]
  { 0, 1, 2, 3, 4, 5, 6, 7 });
  Random rand;

  private ArrayList<Byte> inputA;
  private ArrayList<Byte> inputB;
  private ArrayList<Byte> inputC;
  private ArrayList<Byte> inputD;
  private ArrayList<Byte> inputE;

  private ArrayList<Byte> ruleAB;
  private ArrayList<Byte> ruleF;
  private ArrayList<Byte> ruleC;
  private ArrayList<Byte> ruleDE;

  private FloatObject constant;

  byte[] choiceList = new byte[9];
  Rule refer = new Rule();

  private int boxIndex;
  private int degreeOfFreedom;

  RuleBrain(Random var, int boxIndex, int degreeOfFreedom)
  {
    rand = var;
    this.boxIndex = boxIndex;
    this.degreeOfFreedom = degreeOfFreedom;

    inputA = new ArrayList<Byte>(INPUT_STANDARD);
    inputB = new ArrayList<Byte>(INPUT_STANDARD);
    inputC = new ArrayList<Byte>(INPUT_STANDARD);
    inputD = new ArrayList<Byte>(INPUT_STANDARD);
    inputE = new ArrayList<Byte>(INPUT_STANDARD);

    ruleAB = new ArrayList<Byte>(RULE_STANDARD);
    ruleF = new ArrayList<Byte>(RULE_STANDARD);
    ruleC = new ArrayList<Byte>(RULE_STANDARD);
    ruleDE = new ArrayList<Byte>(RULE_STANDARD);

    constant = new FloatObject(rand);
    changeRule();
  }

  void changeRule()
  {
    choiceList[0] = inputA.get(rand.nextInt(inputA.size()));
    choiceList[1] = inputB.get(rand.nextInt(inputB.size()));
    choiceList[2] = inputC.get(rand.nextInt(inputC.size()));
    choiceList[3] = inputD.get(rand.nextInt(inputD.size()));
    choiceList[4] = inputE.get(rand.nextInt(inputE.size()));

    choiceList[5] = ruleAB.get(rand.nextInt(ruleAB.size()));
    choiceList[6] = ruleC.get(rand.nextInt(ruleC.size()));
    choiceList[7] = ruleDE.get(rand.nextInt(ruleDE.size()));
    choiceList[8] = ruleF.get(rand.nextInt(ruleF.size()));

    resetRule();
  }

  private int option;
  private byte choice;

  void changeValue()
  {
    option = rand.nextInt(choiceList.length);
    ArrayList<Byte> temp = switchArrays((byte) option);
    choice = (byte) temp.get(rand.nextInt(temp.size()));

    switch (option)
    {
    case 0:
      refer.setInput(inputSwitch(choice), NeuronInput.A);
      break;
    case 1:
      refer.setInput(inputSwitch(choice), NeuronInput.B);
      break;
    case 2:
      refer.setInput(inputSwitch(choice), NeuronInput.C);
      break;
    case 3:
      refer.setInput(inputSwitch(choice), NeuronInput.D);
      break;
    case 4:
      refer.setInput(inputSwitch(choice), NeuronInput.E);
      break;
    case 5:
      refer.setOp1(binarySwitch(choice));
      break;
    case 6:
      refer.setOp2(unarySwitch(choice));
      break;
    case 7:
      refer.setOp3(binarySwitch(choice));
      break;
    case 8:
      refer.setOp4(unarySwitch(choice));
      break;
    }
  }

  void confirmChange()
  {
    choiceList[option] = choice;
    switchArrays((byte)option).add(choice);
  }

  private void resetRule()
  {
    refer.setInput(inputSwitch(choiceList[0]), NeuronInput.A);
    refer.setInput(inputSwitch(choiceList[1]), NeuronInput.B);
    refer.setInput(inputSwitch(choiceList[2]), NeuronInput.C);
    refer.setInput(inputSwitch(choiceList[3]), NeuronInput.D);
    refer.setInput(inputSwitch(choiceList[4]), NeuronInput.E);

    refer.setOp1(binarySwitch(choiceList[6]));
    refer.setOp2(unarySwitch(choiceList[5]));
    refer.setOp3(binarySwitch(choiceList[8]));
    refer.setOp4(unarySwitch(choiceList[7]));

    if ((option < 6) && (choice == 4))
    {
      constant = new FloatObject(rand);
    }
  }

  public Rule getRule()
  {
    return refer;
  }

  private EnumOperatorBinary binarySwitch(byte choice)
  {
    switch (choice)
    {
    case 0:
      return EnumOperatorBinary.ADD;

    case 1:
      return EnumOperatorBinary.SUBTRACT;

    case 2:
      return EnumOperatorBinary.MULTIPLY;

    case 3:
      return EnumOperatorBinary.POWER;

    case 4:
      return EnumOperatorBinary.MAX;

    case 5:
      return EnumOperatorBinary.MIN;

    case 6:
      return EnumOperatorBinary.ARCTAN2;
    }
    return null;
  }

  private EnumOperatorUnary unarySwitch(byte choice)
  {
    switch (choice)
    {
    case 0:
      return EnumOperatorUnary.ABS;

    case 1:
      return EnumOperatorUnary.IDENTITY;

    case 2:
      return EnumOperatorUnary.SIN;

    case 3:
      return EnumOperatorUnary.SIGN;

    case 4:
      return EnumOperatorUnary.NEGATIVE;

    case 5:
      return EnumOperatorUnary.LOG;

    case 6:
      return EnumOperatorUnary.EXP;
    }
    return null;
  }

  private NeuronInput inputSwitch(byte choice)
  {
    switch (choice)
    {
    case 0: // Height
      return new NeuronInput(EnumNeuronInputType.HEIGHT, boxIndex);
    case 1: // Touch
      return new NeuronInput(EnumNeuronInputType.TOUCH, boxIndex);
    case 2: // Time
      return new NeuronInput(EnumNeuronInputType.TIME);
    case 3: // Joint
      return new NeuronInput(EnumNeuronInputType.JOINT, boxIndex,
          degreeOfFreedom);
    case 4: // Constant
      return new NeuronInput(EnumNeuronInputType.CONSTANT, constant.getValue());
    }
    return null;
  }

  void setDOF(int degreeOfFreedom)
  {
    this.degreeOfFreedom = degreeOfFreedom;
  }

  private ArrayList<Byte> switchArrays(byte choice)
  {
    switch (choice)
    {
    case 0:
      return inputA;
    case 1:
      return inputB;
    case 2:
      return inputC;
    case 3:
      return inputD;
    case 4:
      return inputE;
    case 5:
      return ruleAB;
    case 6:
      return ruleF;
    case 7:
      return ruleC;
    case 8:
      return ruleDE;
    }
    return null;
  }
}

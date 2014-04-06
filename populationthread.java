//Matt Locklin
//Updated 4/5/2014 6:46pm

package creature.group;

import java.util.Random;

import creature.phenotype.Block;
import creature.phenotype.Creature;
import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.EnumNeuronInputType;
import creature.phenotype.EnumOperatorBinary;
import creature.phenotype.EnumOperatorUnary;
import creature.phenotype.Joint;
import creature.phenotype.NeuronInput;
import creature.phenotype.Rule;
import creature.phenotype.Vector3;

public class populationthread extends Thread {

	private Creature creatures[];

	private Creature alpha;

	public populationthread(int size) {

		for (int i = 0; i < size; i++) {
			creatures[i] = Birth();
			System.out.println("KAISER: " + creatures[i].toString());
		}

	}

	public void run() {
		// hillclimb each memember

	}

	private Creature singleparentxover(Creature parent) {
		return parent;
	}

	private Creature doubleparentxover(Creature parent) {
		return parent;
	}

	private Creature Birth() {

		Random randomgen = new Random();

		int numberoflegs = randomgen.nextInt(7);
		int seglength = randomgen.nextInt(2) + 1;

		float length = randomgen.nextFloat() * 30;
		float width = randomgen.nextFloat() * 30;
		float height = randomgen.nextFloat() * 30;

		length = checkdim(length, width, height, randomgen);
		width = checkdim(width, height, length, randomgen);
		height = checkdim(height, length, width, randomgen);

		EnumJointType[] jointtypes = EnumJointType.values();
		EnumJointSite[] sites = EnumJointSite.values();
		EnumNeuronInputType[] neurontypes = EnumNeuronInputType.values();
		EnumOperatorBinary[] binaries = EnumOperatorBinary.values();
		EnumOperatorUnary[] unaries = EnumOperatorUnary.values();

		Vector3.setDisplayDecimalPlaces(3);
		Vector3.test();

		Vector3 rootForward = Vector3.FORWARD;
		Vector3 rootUp = Vector3.UP;

		Block[] body = new Block[1 + seglength * numberoflegs];
		body[0] = new Block(Block.PARENT_INDEX_NONE, null, length, width,
				height);
		Block[] leg = new Block[seglength];

		for (int i = 0; i < seglength; i++) {
			EnumJointSite toparent = sites[randomgen.nextInt(26)];
			EnumJointSite tochild = getValidSite(toparent, randomgen);
			Joint joint = new Joint(jointtypes[randomgen.nextInt(4)],
					sites[randomgen.nextInt(26)], sites[randomgen.nextInt(26)],
					getRandomOrientation(randomgen));

			length = randomgen.nextFloat() * 10;
			width = randomgen.nextFloat() * 10;
			height = randomgen.nextFloat() * 10;

			length = checkdim(length, width, height, randomgen);
			width = checkdim(width, height, length, randomgen);
			height = checkdim(height, length, width, randomgen);

			leg[i] = new Block(i, joint, length, width, height);

			Rule rule = new Rule();
			NeuronInput neuron1A = new NeuronInput(
					EnumNeuronInputType.CONSTANT, 1f);
			NeuronInput neuron1B = new NeuronInput(
					EnumNeuronInputType.CONSTANT, 0f);
			NeuronInput neuron1C = new NeuronInput(
					EnumNeuronInputType.CONSTANT, 0f);
			NeuronInput neuron1D = new NeuronInput(
					EnumNeuronInputType.CONSTANT, 0f);
			NeuronInput neuron1E = new NeuronInput(
					EnumNeuronInputType.CONSTANT, Float.MAX_VALUE);

			rule.setInput(neuron1A, NeuronInput.A);
			rule.setInput(neuron1B, NeuronInput.B);
			rule.setInput(neuron1C, NeuronInput.C);
			rule.setInput(neuron1D, NeuronInput.D);
			rule.setInput(neuron1E, NeuronInput.E);

			rule.setOp1(EnumOperatorBinary.ADD);
			rule.setOp2(EnumOperatorUnary.IDENTITY);
			rule.setOp3(EnumOperatorBinary.ADD);
			rule.setOp4(EnumOperatorUnary.IDENTITY);
			// changes these
			/*
			 * NeuronInput neuron1A = new
			 * NeuronInput(EnumNeuronInputType.HEIGHT, i); NeuronInput neuron1B
			 * = new NeuronInput(EnumNeuronInputType.TOUCH, 0); NeuronInput
			 * neuron1C = new NeuronInput(EnumNeuronInputType.JOINT, i,
			 * joint.getType().getDoF()); NeuronInput neuron1D = new
			 * NeuronInput( EnumNeuronInputType.CONSTANT,
			 * randomgen.nextFloat());// test // for // range NeuronInput
			 * neuron1E = new NeuronInput( EnumNeuronInputType.CONSTANT,
			 * Float.MAX_VALUE);// Fix later
			 * 
			 * rule.setInput(neuron1A, NeuronInput.A); rule.setInput(neuron1B,
			 * NeuronInput.B); rule.setInput(neuron1C, NeuronInput.C);
			 * rule.setInput(neuron1D, NeuronInput.D); rule.setInput(neuron1E,
			 * NeuronInput.E);
			 * 
			 * rule.setOp1(binaries[randomgen.nextInt(7)]);
			 * rule.setOp2(unaries[randomgen.nextInt(7)]);
			 * rule.setOp3(binaries[randomgen.nextInt(7)]);
			 * rule.setOp4(unaries[randomgen.nextInt(7)]);
			 */
			joint.addRule(rule, 0);
		}
		int bodyindex = 1;
		for (int k = 1; k <= numberoflegs; k++) {
			for (int j = 0; j < leg.length; j++) {
				body[bodyindex] = new Block(leg[j]);
				bodyindex++;
			}

		}

		return new Creature(body, rootForward, rootUp);
	}

	private float checkdim(float dim, float a, float b, Random r) {
		float dominate;
		if (a >= b)
			dominate = a;
		else
			dominate = b;
		if (dim * 10 > dominate)
			return checkdim(r.nextFloat() * dominate, a, b, r);
		else {
			if (dim < 1.0f)
				return dim + 1;
			else
				return dim;
		}

	}

	private float getRandomOrientation(Random r) {
		return Math.abs(r.nextFloat() * 2 * (float) Math.PI);

	}

	//doesn't prevent site colisions
	private EnumJointSite getValidSite(EnumJointSite parent, Random r) {
		int randomint = r.nextInt(26);
		EnumJointSite[] sites = EnumJointSite.values();

		EnumJointSite out = sites[randomint];
		if (out.ordinal() == parent.ordinal())
			return getValidSite(parent, r);
		else
			return parent;
	}

	public static void main(String[] args) throws InterruptedException {
		populationthread threadA = new populationthread(5);

	}

}

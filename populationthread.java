//Matt Locklin
//Updated 4/10/2014 7:37pm

package creature.group;

import java.util.Random;

import creature.phenotype.Block;
import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.Joint;
import creature.phenotype.Vector3;

public class populationthread extends Thread {

	private Critter critters[];

	private Critter alpha;

	public populationthread(int size) {

		critters= new Critter[size];
		
		for (int i = 0; i < size; i++) {
			critters[i] = Birth();
			System.out.println("KAISER: " + critters[i].toString());
		}

	}

	public void run() {
		// hillclimb each memember

	}

	private Critter singlepointxover(Critter parent) {
		Random r = new Random();
		Genotype alphagenes = new Genotype(alpha.getBody(),
				alpha.getBlockForwardVector(0), alpha.getBlockUpVector(0));
		Genotype parentgenes = new Genotype(parent.getBody(),
				parent.getBlockForwardVector(0), parent.getBlockUpVector(0));
		// Genotype compl =
		return parent;
	}

	private Critter doubleparentxover(Critter parent) {
		return parent;
	}

	private Critter Birth() {

		Critter baby = null;

		while (baby == null) {
			Random randomgen = new Random();

			int numberoflegs = 1;//randomgen.nextInt(7);
			int seglength = /*randomgen.nextInt(2) +*/ 1;

			float length = randomgen.nextFloat() * 30;
			float width = randomgen.nextFloat() * 30;
			float height = randomgen.nextFloat() * 30;

			length = checkdim(length, width, height, randomgen);
			width = checkdim(width, height, length, randomgen);
			height = checkdim(height, length, width, randomgen);

			EnumJointType[] jointtypes = EnumJointType.values();
			EnumJointSite[] sites = EnumJointSite.values();

			Vector3.setDisplayDecimalPlaces(3);
			Vector3.test();

			Vector3 rootForward = Vector3.FORWARD;
			Vector3 rootUp = Vector3.UP;

			// building up
			Block[] body = new Block[1+numberoflegs*seglength];
			body[0] = new Block(Block.PARENT_INDEX_NONE, null, length, width,
					height);
			Block[] leg = new Block[seglength];

			for (int i = 0; i < seglength; i++) {
				EnumJointSite toparent = sites[randomgen.nextInt(26)];
				EnumJointSite tochild = getValidSite(toparent, randomgen);
				Joint joint = new Joint(jointtypes[randomgen.nextInt(4)],
						sites[randomgen.nextInt(26)],
						sites[randomgen.nextInt(26)],
						getRandomOrientation(randomgen));

				length = randomgen.nextFloat() * 10;
				width = randomgen.nextFloat() * 10;
				height = randomgen.nextFloat() * 10;

				length = checkdim(length, width, height, randomgen);
				width = checkdim(width, height, length, randomgen);
				height = checkdim(height, length, width, randomgen);

				leg[i] = new Block(i, joint, length, width, height);

			}
			int bodyindex = 1;
			for (int k = 1; k <= numberoflegs; k++) {
				for (int j = 0; j < leg.length; j++) {
					body[bodyindex] = new Block(leg[j]);
					bodyindex++;
				}

			}

			try {
				baby = new Critter(body, rootForward, rootUp, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return baby;
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
		return Math.abs(r.nextFloat() * (float) Math.PI/2);

	}

	// doesn't prevent site colisions
	private EnumJointSite getValidSite(EnumJointSite parent, Random r) {
		int randomint = r.nextInt(26);
		EnumJointSite[] sites = EnumJointSite.values();

		EnumJointSite out = sites[randomint];
		if (out.ordinal() == parent.ordinal())
			return getValidSite(parent, r);
		else
			return parent;
	}

	public Critter getAlpha() {
		return alpha;
	}

	private Genotype getcomplexdna(Genotype a, Genotype b) {
		if (a.getDnaLength() >= b.getDnaLength())
			return a;
		else
			return b;
	}
	/*private EnumJointSite getJointOtherEnd(EnumJointSite j,Random r){
		EnumJointSite[] sites = EnumJointSite.values();
		if(j==EnumJointSite.VERTEX_FRONT_NORTHWEST){
			
		}
			
		
	}*/

	public static void main(String[] args) throws InterruptedException {
		populationthread threadA = new populationthread(5);

	}

}

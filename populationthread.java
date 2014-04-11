//Matt Locklin
//Updated 4/11/2014 12:40pm

package creature.group;

import java.util.Random;

import creature.phenotype.Block;
import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.Joint;
import creature.phenotype.Vector3;

public class Populationthread extends Thread {

	private Critter critters[];

	private Critter alpha;

	public Populationthread(int size) {

		critters = new Critter[size];

	}

	public void run() {
		while (true) {
			synchronized (this) {
				if(critters[critters.length-1]==null){
					for (int i = 0; i < critters.length; i++) {
						critters[i] = Birth();
						System.out.println("KAISER: " + i + ": " + critters[i].toString());
					}
				}
				for (Critter c : critters) {
					c.hillClimb();
					if (alpha == null || c.bestFitness > alpha.bestFitness)
						c = alpha;
				}
			}
		}

	}

	private Critter singlepointxover(Critter parent) {
		if (isAlpha(parent))
			return parent;
		Critter child = parent;
		while (parent.bestFitness >= child.bestFitness) {
			Random r = new Random();
			GenoType alphagenes = new GenoType(alpha.getBody(),
					alpha.getBlockForwardVector(0), alpha.getBlockUpVector(0));
			GenoType parentgenes = new GenoType(parent.getBody(),
					parent.getBlockForwardVector(0), parent.getBlockUpVector(0));
			int crossindex = r.nextInt(getcomplexdna(alphagenes, parentgenes)
					.getDnaLength());
			GenoType childgenes = new GenoType(parent.getBody(),
					parent.getBlockForwardVector(0), parent.getBlockUpVector(0));

			for (int i = 0; i < getcomplexdna(alphagenes, parentgenes)
					.getDnaLength(); i++) {
				if (i >= crossindex) {
					childgenes.setDna(i, getcomplexdna(alphagenes, parentgenes)
							.get_Allele(i).getValue(),
							getcomplexdna(alphagenes, parentgenes)
									.get_Allele(i).getAlleletype());
				} else
					childgenes.setDna(i, getsimpledna(alphagenes, parentgenes)
							.get_Allele(i).getValue(),
							getsimpledna(alphagenes, parentgenes).get_Allele(i)
									.getAlleletype());
			}

			child = childgenes.toCritter();
		}
		return child;
	}

	private Critter doubleparentxover(Critter parent) {
		if (isAlpha(parent))
			return parent;
		Critter child = parent;
		while (parent.bestFitness >= child.bestFitness) {
			Random r = new Random();
			GenoType alphagenes = new GenoType(alpha.getBody(),
					alpha.getBlockForwardVector(0), alpha.getBlockUpVector(0));
			GenoType parentgenes = new GenoType(parent.getBody(),
					parent.getBlockForwardVector(0), parent.getBlockUpVector(0));
			int crossindexa = r.nextInt(getsimpledna(alphagenes, parentgenes)
					.getDnaLength());
			int crossindexb = crossindexa+r.nextInt(getcomplexdna(alphagenes, parentgenes)
					.getDnaLength()-crossindexa);
			if(crossindexb>getsimpledna(alphagenes, parentgenes)
					.getDnaLength()){
				crossindexb=getsimpledna(alphagenes, parentgenes)
						.getDnaLength()+1;
			}
			GenoType childgenes = new GenoType(parent.getBody(),
					parent.getBlockForwardVector(0), parent.getBlockUpVector(0));

			for (int i = 0; i < getcomplexdna(alphagenes, parentgenes)
					.getDnaLength(); i++) {
				if (i <= crossindexb||1>crossindexa) {
					childgenes.setDna(i, getcomplexdna(alphagenes, parentgenes)
							.get_Allele(i).getValue(),
							getcomplexdna(alphagenes, parentgenes)
									.get_Allele(i).getAlleletype());
				} else
					childgenes.setDna(i, getsimpledna(alphagenes, parentgenes)
							.get_Allele(i).getValue(),
							getsimpledna(alphagenes, parentgenes).get_Allele(i)
									.getAlleletype());
			}

			child = childgenes.toCritter();
		}
		return child;
	}

	private Critter Birth() {

		Critter baby = null;

		while (baby == null) {
			Random randomgen = new Random();

			int numberoflegs = 1;// randomgen.nextInt(7);
			int seglength = randomgen.nextInt(2) + 1;

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
			Block[] body = new Block[1 + numberoflegs * seglength];
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
		return Math.abs(r.nextFloat() * (float) Math.PI / 2);

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

	private GenoType getcomplexdna(GenoType a, GenoType b) {
		if (a.getDnaLength() >= b.getDnaLength())
			return a;
		else
			return b;
	}

	private GenoType getsimpledna(GenoType a, GenoType b) {
		if (a.getDnaLength() < b.getDnaLength())
			return a;
		else
			return b;
	}

	private boolean isAlpha(Critter c) {
		return (c == alpha);
	}

	public static void main(String[] args) throws InterruptedException {
		Populationthread threadA = new Populationthread(5);

	}

}

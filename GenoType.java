//Matt Locklin
//Updated 4/11/14 12:39pm

package creature.group;

import creature.phenotype.Block;
import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.EnumOperatorBinary;
import creature.phenotype.EnumOperatorUnary;
import creature.phenotype.Joint;
import creature.phenotype.NeuronInput;
import creature.phenotype.Rule;
import creature.phenotype.Vector3;

public class GenoType extends Critter {

	private Allele[] dna = new Allele[100];
	private int index = 0;
	private EnumAlleleType next[] = { EnumAlleleType.LENGTH, null };

	public GenoType(Block[] body, Vector3 rootForward, Vector3 rootUp) {
		super(body, rootForward, rootUp, true);

		for (Block b : body) {
			float_insert(b.getLength(), EnumAlleleType.LENGTH);
			float_insert(b.getWidth(), EnumAlleleType.WIDTH);
			float_insert(b.getHeight(), EnumAlleleType.HEIGHT);
			int_insert(b.getIndexOfParent(), EnumAlleleType.INDEX_OF_PARENT);
			joint_type_insert(b.getJointToParent().getType(),
					EnumAlleleType.JOINT_TYPE);
			float_insert(b.getJointToParent().getOrientation(),
					EnumAlleleType.ORIENTATION);
			site_insert(b.getJointToParent().getSiteOnParent(),
					EnumAlleleType.SITE_ON_PARENT);
			site_insert(b.getJointToParent().getSiteOnChild(),
					EnumAlleleType.SITE_ON_CHILD);
			for (Rule r : b.getJointToParent().getRuleList(
					b.getJointToParent().getType().getDoF())) {
				Neuron_insert(r.getInput(0), EnumAlleleType.RULE_A);
				Neuron_insert(r.getInput(1), EnumAlleleType.RULE_B);
				Neuron_insert(r.getInput(2), EnumAlleleType.RULE_C);
				Neuron_insert(r.getInput(3), EnumAlleleType.RULE_D);
				Neuron_insert(r.getInput(4), EnumAlleleType.RULE_E);
				Binary_insert(r.getOp1(), EnumAlleleType.OP_1);
				Unary_insert(r.getOp2(), EnumAlleleType.OP_2);
				Binary_insert(r.getOp3(), EnumAlleleType.OP_3);
				Unary_insert(r.getOp4(), EnumAlleleType.OP_4);
			}
		}
	}

	private void int_insert(int o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new Int_Allele(o, at);
				incrementIndex();
				next = nxt.checknext();
			} else
				throw new IllegalArgumentException("[" + at
						+ "] was not expected allele in the genotype sequence.");
		}

	}

	private void float_insert(float o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new Float_Allele(o, at);
				incrementIndex();
				next = nxt.checknext();
			} else
				throw new IllegalArgumentException("[" + at
						+ "] was not expected allele in the genotype sequence.");
		}

	}

	private void site_insert(EnumJointSite o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new Joint_Site_Allele(o, at);
				incrementIndex();
				next = nxt.checknext();
			} else
				throw new IllegalArgumentException("[" + at
						+ "] was not expected allele in the genotype sequence.");
		}

	}

	private void joint_type_insert(EnumJointType o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new Joint_Type_Allele(o, at);
				incrementIndex();
				next = nxt.checknext();
			} else
				throw new IllegalArgumentException("[" + at
						+ "] was not expected allele in the genotype sequence.");
		}

	}

	private void Binary_insert(EnumOperatorBinary o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new OpBinary_Allele(o, at);
				incrementIndex();
				next = nxt.checknext();
			} else
				throw new IllegalArgumentException("[" + at
						+ "] was not expected allele in the genotype sequence.");
		}

	}

	private void Unary_insert(EnumOperatorUnary o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new OpUnary_Allele(o, at);
				incrementIndex();
				next = nxt.checknext();
			} else
				throw new IllegalArgumentException("[" + at
						+ "] was not expected allele in the genotype sequence.");
		}

	}

	private void Neuron_insert(NeuronInput o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new Neuron_Allele(o, at);
				incrementIndex();
				next = nxt.checknext();
			} else
				throw new IllegalArgumentException("[" + at
						+ "] was not expected allele in the genotype sequence.");
		}

	}

	private void incrementIndex() {
		index++;
		if (index >= dna.length) {
			Allele[] temp_dna = new Allele[dna.length * 2];
			for (int i = 0; i < dna.length; i++) {
				temp_dna[i] = dna[i];
			}
			dna = temp_dna;
		}
	}

	public Allele[] getGenotype() {
		return dna;
	}

	public int getDnaLength() {
		return dna.length;
	}

	public Allele get_Allele(int i) {
		if (i > dna.length)
			throw new IndexOutOfBoundsException("[" + i
					+ "] Out of bounds exception size: " + dna.length + "\n");
		else
			return dna[i];
	}

	public void setDna(int index, Object o, EnumAlleleType at) {
		dna[index] = new Allele(o, at);
	}

	private int getNumberofBlocks() {
		int count = 0;
		for (Allele a : dna) {
			if (a.getAlleletype() == EnumAlleleType.LENGTH)
				count++;
		}
		return count;
	}

	public Critter toCritter() {
		Block[] body = new Block[getNumberofBlocks()];
		int dnaindex = 0;
		Allele nextAllele = null;
		int blockcount = 0;
		while (dnaindex < this.getDnaLength()) {
			if (nextAllele == null
					|| nextAllele.getAlleletype() == EnumAlleleType.LENGTH) {
				body[blockcount] = dnaBlock(
						(Int_Allele) this.get_Allele(dnaindex + 3),
						(Float_Allele) this.get_Allele(dnaindex),
						(Float_Allele) this.get_Allele(dnaindex + 1),
						(Float_Allele) this.get_Allele(dnaindex + 2));
				blockcount++;
				dnaindex += 4;
				nextAllele = this.get_Allele(dnaindex);
			}
			if (nextAllele.getAlleletype() == EnumAlleleType.JOINT_TYPE) {
				Joint tempjoint = dnaJoint(
						(Joint_Type_Allele) this.get_Allele(dnaindex),
						(Float_Allele) this.get_Allele(dnaindex + 1),
						(Joint_Site_Allele) this.get_Allele(dnaindex + 2),
						(Joint_Site_Allele) this.get_Allele(dnaindex + 3));
				dnaindex += 4;
				nextAllele = this.get_Allele(dnaindex);

				while (nextAllele.getAlleletype() == EnumAlleleType.RULE_A) {
					Rule temprule = dnaRule(
							(Neuron_Allele) this.get_Allele(dnaindex),
							(Neuron_Allele) this.get_Allele(dnaindex + 1),
							(Neuron_Allele) this.get_Allele(dnaindex + 2),
							(Neuron_Allele) this.get_Allele(dnaindex + 3),
							(Neuron_Allele) this.get_Allele(dnaindex + 4),
							(OpBinary_Allele) this.get_Allele(dnaindex + 5),
							(OpUnary_Allele) this.get_Allele(dnaindex + 6),
							(OpBinary_Allele) this.get_Allele(dnaindex + 7),
							(OpUnary_Allele) this.get_Allele(dnaindex + 8));
					dnaindex += 9;
					nextAllele = this.get_Allele(dnaindex);
					tempjoint.addRule(temprule, tempjoint.getType().getDoF());
				}
				body[blockcount].setJointToParent(tempjoint);
			}

		}
		return new Critter(body,this.getBlockForwardVector(0),this.getBlockUpVector(0),true);

	}

	private Block dnaBlock(Int_Allele index, Float_Allele length,
			Float_Allele height, Float_Allele width) {
		return new Block(index.getInt_value(), null, length.getFloat_Value(),
				height.getFloat_Value(), width.getFloat_Value());
	}

	private Joint dnaJoint(Joint_Type_Allele type, Float_Allele orient,
			Joint_Site_Allele parent, Joint_Site_Allele child) {
		return new Joint(type.getEJT_value(), parent.getEJS_value(),
				child.getEJS_value(), orient.getFloat_Value());
	}

	private Rule dnaRule(Neuron_Allele a, Neuron_Allele b, Neuron_Allele c,
			Neuron_Allele d, Neuron_Allele e, OpBinary_Allele biop1,
			OpUnary_Allele uop1, OpBinary_Allele biop2, OpUnary_Allele uop2) {
		Rule temp = null;
		temp.setInput(a.getNi_value(), 0);
		temp.setInput(b.getNi_value(), 1);
		temp.setInput(c.getNi_value(), 2);
		temp.setInput(d.getNi_value(), 3);
		temp.setInput(e.getNi_value(), 4);
		temp.setOp1(biop1.getEOB_value());
		temp.setOp2(uop1.getEou_value());
		temp.setOp3(biop2.getEOB_value());
		temp.setOp4(uop2.getEou_value());
		return temp;
	}

}

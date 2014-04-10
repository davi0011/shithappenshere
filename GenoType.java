//Matt Locklin
//Updated 4/10/14 4:39pm

package creature.group;

import creature.phenotype.Block;
import creature.phenotype.Rule;
import creature.phenotype.Vector3;

public class Genotype extends Critter {

	private Allele[] dna = new Allele[100];
	private int index = 0;
	private EnumAlleleType next[] = { EnumAlleleType.LENGTH, null };

	public Genotype(Block[] body, Vector3 rootForward, Vector3 rootUp) {
		super(body, rootForward, rootUp);

		for (Block b : body) {
			insert(b.getLength(), EnumAlleleType.LENGTH);
			insert(b.getWidth(), EnumAlleleType.WIDTH);
			insert(b.getHeight(), EnumAlleleType.HEIGHT);
			insert(b.getIndexOfParent(), EnumAlleleType.INDEX_OF_PARENT);
			insert(b.getJointToParent().getType(), EnumAlleleType.JOINT_TYPE);
			insert(b.getJointToParent().getOrientation(),
					EnumAlleleType.ORIENTATION);
			insert(b.getJointToParent().getSiteOnParent(),
					EnumAlleleType.SITE_ON_PARENT);
			insert(b.getJointToParent().getSiteOnChild(),
					EnumAlleleType.SITE_ON_CHILD);
			for (Rule r : b.getJointToParent().getRuleList(
					b.getJointToParent().getType().getDoF())) {
				insert(r.getInput(0), EnumAlleleType.RULE_A);
				insert(r.getInput(1), EnumAlleleType.RULE_B);
				insert(r.getInput(2), EnumAlleleType.RULE_C);
				insert(r.getInput(3), EnumAlleleType.RULE_D);
				insert(r.getInput(4), EnumAlleleType.RULE_E);
				insert(r.getOp1(), EnumAlleleType.OP_1);
				insert(r.getOp2(), EnumAlleleType.OP_2);
				insert(r.getOp3(), EnumAlleleType.OP_3);
				insert(r.getOp4(), EnumAlleleType.OP_4);
			}
		}
	}

	private void insert(Object o, EnumAlleleType at) {
		for (EnumAlleleType nxt : at.checknext()) {
			if (at == nxt) {
				dna[index] = new Allele(o, at);
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
	public int getDnaLength(){
		return dna.length;
	}
	public Allele get_Allele(int i){
		if(i>dna.length)
			throw new IndexOutOfBoundsException("[" + i
					+ "] Out of bounds exception size: "+dna.length+"\n");
		else
			return dna[i];
	}
	public void setDna(int index, Object o, EnumAlleleType at){
		dna[index] = new Allele(o,at);
	}
}

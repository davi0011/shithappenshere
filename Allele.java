//Matt Locklin
//Updated 4/11/14 12:10pm

package creature.group;

import creature.phenotype.EnumJointSite;
import creature.phenotype.EnumJointType;
import creature.phenotype.EnumOperatorBinary;
import creature.phenotype.EnumOperatorUnary;

public class Allele {

	private EnumAlleleType alleletype;
	private Object value;

	private EnumOperatorUnary EOU_value;

	public Allele(Object v, EnumAlleleType a) {
		value = v;
		alleletype = a;
	}

	public EnumAlleleType getAlleletype() {
		return alleletype;
	}
	public Object getValue(){
		return value;
	}

}

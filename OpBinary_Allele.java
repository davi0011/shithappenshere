//Matt Locklin
//Updated 4/11/14 12:27pm
package creature.group;

import creature.phenotype.EnumOperatorBinary;

public class OpBinary_Allele extends Allele {

	private EnumOperatorBinary eob_value;

	public OpBinary_Allele(EnumOperatorBinary v, EnumAlleleType a) {
		super(v, a);
		eob_value = v;
	}

	public EnumOperatorBinary getEOB_value() {
		return eob_value;
	}

	public void setEOB_value(EnumOperatorBinary eOB_value) {
		eob_value = eob_value;
	}

}

//Matt Locklin
//Updated 4/11/14 12:27pm
package creature.group;

import creature.phenotype.EnumOperatorUnary;

public class OpUnary_Allele extends Allele {

	private EnumOperatorUnary eou_value;

	public OpUnary_Allele(EnumOperatorUnary v, EnumAlleleType a) {
		super(v, a);
		eou_value = v;
	}

	public EnumOperatorUnary getEou_value() {
		return eou_value;
	}

	public void setEou_value(EnumOperatorUnary eou_value) {
		this.eou_value = eou_value;
	}

}

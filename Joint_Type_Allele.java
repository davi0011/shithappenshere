//Matt Locklin
//Updated 4/11/14 12:27pm
package creature.group;

import creature.phenotype.EnumJointType;

public class Joint_Type_Allele extends Allele{
	
	private EnumJointType ejt_value;

	public Joint_Type_Allele(EnumJointType v, EnumAlleleType a) {
		super(v, a);
		ejt_value=v;
	}

	public EnumJointType getEJT_value() {
		return ejt_value;
	}

	public void setEJT_value(EnumJointType ejt_value) {
		ejt_value = ejt_value;
	}

}

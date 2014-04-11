//Matt Locklin
//Updated 4/11/14 12:27pm

package creature.group;

import creature.phenotype.EnumJointSite;

public class Joint_Site_Allele extends Allele {

	private EnumJointSite ejs_value;

	public Joint_Site_Allele(EnumJointSite v, EnumAlleleType a) {
		super(v, a);
		ejs_value = v;

	}

	public EnumJointSite getEJS_value() {
		return ejs_value;
	}

	public void setEJS_value(EnumJointSite ejs_value) {
		ejs_value = ejs_value;
	}

}

//Matt Locklin
//Updated 4/11/14 12:27pm
package creature.group;

public class Float_Allele extends Allele{
	
	private float float_value;
	
	public Float_Allele(float v,EnumAlleleType a){
		super(v,a);
		float_value=v;
	}

	public float getFloat_Value() {
		return float_value;
	}

	public void setFloat_value(float float_value) {
		this.float_value = float_value;
	}

}

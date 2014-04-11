//Matt Locklin
//Updated 4/11/14 12:27pm
package creature.group;

public class Int_Allele extends Allele {

	private int int_value;

	public Int_Allele(int v, EnumAlleleType a) {
		super(v, a);
		int_value = v;
	}

	public int getInt_value() {
		return int_value;
	}

	public void setInt_value(int int_value) {
		this.int_value = int_value;
	}

}

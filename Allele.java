//Matt Locklin
//Updated 4/8/14 10:15pm

package creature.group;


public class Allele {
	
	private Object value;
	
	private EnumAlleleType alleletype;
	
	
	public Allele(Object v,EnumAlleleType a){
		value=v;
		alleletype=a;
	}
		
	public Object getValue() {
		return value;
	}

	public EnumAlleleType getAlleletype() {
		return alleletype;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	

}

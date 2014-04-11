//Matt Locklin
//Updated 4/8/14 10:15pm

package creature.group;

import creature.group.EnumAlleleReturn;

public enum EnumAlleleType {

	LENGTH {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { WIDTH };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.FLOAT;}
	},

	WIDTH {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { HEIGHT };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.FLOAT;}
	},

	HEIGHT {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { INDEX_OF_PARENT };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.FLOAT;}
	},

	INDEX_OF_PARENT {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { JOINT_TYPE, null };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.INT;}
	},

	JOINT_TYPE {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { ORIENTATION };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMJOINTTYPE;}
	},

	ORIENTATION {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { SITE_ON_PARENT };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.FLOAT;}
	},

	SITE_ON_PARENT {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { SITE_ON_CHILD };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMJOINTSITE;}
	},

	SITE_ON_CHILD {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_A };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMNEURONINPUTTYPE;}
	},

	RULE_A {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_B };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMNEURONINPUTTYPE;}
	},

	RULE_B {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_C };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMNEURONINPUTTYPE;}
	},

	RULE_C {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_D };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMNEURONINPUTTYPE;}
	},

	RULE_D {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_E };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMNEURONINPUTTYPE;}
	},

	RULE_E {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_1 };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMNEURONINPUTTYPE;}
	},

	OP_1 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_2 };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMBIN;}
	},

	OP_2 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_3 };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMUN;}
	},

	OP_3 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_4 };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMBIN;}
	},

	OP_4 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { LENGTH, RULE_A, null };
			return next;
		}
		public EnumAlleleReturn getReturnType(){return EnumAlleleReturn.ENUMUN;}
	};
	public abstract EnumAlleleType[] checknext();
	public abstract EnumAlleleReturn getReturnType();
}

//Matt Locklin
//Updated 4/8/14 10:15pm

package creature.group;

public enum EnumAlleleType {

	LENGTH {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { WIDTH };
			return next;
		}
	},

	WIDTH {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { HEIGHT };
			return next;
		}
	},

	HEIGHT {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { INDEX_OF_PARENT };
			return next;
		}
	},

	INDEX_OF_PARENT {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { JOINT_TYPE, null };
			return next;
		}
	},

	JOINT_TYPE {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { ORIENTATION };
			return next;
		}
	},

	ORIENTATION {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { SITE_ON_PARENT };
			return next;
		}
	},

	SITE_ON_PARENT {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { SITE_ON_CHILD };
			return next;
		}
	},

	SITE_ON_CHILD {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_A };
			return next;
		}
	},

	RULE_A {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_B };
			return next;
		}
	},

	RULE_B {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_C };
			return next;
		}
	},

	RULE_C {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_D };
			return next;
		}
	},

	RULE_D {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { RULE_E };
			return next;
		}
	},

	RULE_E {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_1 };
			return next;
		}
	},

	OP_1 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_2 };
			return next;
		}
	},

	OP_2 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_3 };
			return next;
		}
	},

	OP_3 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { OP_4 };
			return next;
		}
	},

	OP_4 {
		public EnumAlleleType[] checknext() {
			EnumAlleleType[] next = { LENGTH, RULE_A, null };
			return next;
		}
	};
	public abstract EnumAlleleType[] checknext();
}

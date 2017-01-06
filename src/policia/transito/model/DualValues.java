package policia.transito.model;

public class DualValues <LeftValue, RightVal>
{
	private LeftValue leftValue;
	private RightVal rigthValue;

	public DualValues(LeftValue leftValue, RightVal rigthValue)
	{
		this.leftValue = leftValue;
		this.rigthValue = rigthValue;
	}

	public DualValues() 
	{
	}

	public LeftValue getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(LeftValue leftValue) {
		this.leftValue = leftValue;
	}

	public RightVal getRigthValue() {
		return rigthValue;
	}

	public void setRigthValue(RightVal rigthValue) {
		this.rigthValue = rigthValue;
	}
	
	
}

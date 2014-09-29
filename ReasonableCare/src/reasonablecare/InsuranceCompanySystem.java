package reasonablecare;

/**
 * Utility class containing functionality allowing our system to contact external
 * Insurance company providers.
 *
 */
public class InsuranceCompanySystem {
	
	//default constructor
	public InsuranceCompanySystem()
	{
		
	}
	
	/**
	 * Returns whether the deductible has been paid for a given insurance policy
	 * 
	 * @param insuranceProvider
	 * @param policyNumber
	 * @return
	 */
	public boolean getDeductiblePaid(String insuranceProvider, String policyNumber)
	{
		/*
		 * Assume deductible has not been paid for the purposes of demonstrating additional
		 * billing steps
		 */
		return false;
	}
	
	/**
	 * Returns the amount of the copay for a given appointment
	 * 
	 * @param apptType
	 * @param doctorID
	 * @param insCompany
	 * @param insNumber
	 * @return
	 */
	public int getCopay(String apptType, int doctorID, String insCompany, String insNumber)
	{
		/*
		 * Accepts all of the required parameters, but for the purposes of the
		 * demo, the copayment is always 20
		 */
		return 20;
	}

}

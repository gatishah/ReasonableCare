package reasonablecare;

import java.util.Calendar;

/**
 * Utility class for interacting with external credit card processing systems
 *
 */
public class CreditCardSystem {

	public CreditCardSystem()
	{
		
	}
	
	/**
	 * Contacts the credit card company to get preapproval for a copay charge
	 * 
	 * @param credit card number
	 * @return true if preapproved false if declined
	 */
	public boolean getPreapproval(String creditCardNumber)
	{
		/*
		 * This is a stub.  We cannot possibly contact all credit card systems, so we assume
		 * true for the purposes of this demonstration
		 */
		return true;
	}
	
	/**
	 * Validates that the expiration date of a credit card is after today's date.
	 * 
	 * Does not validate the number
	 */
	public boolean validateCreditCard(String creditCardNumber, int ccMonth, int ccYear)
	{
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);

		if (ccYear>year)
			{
				return true;
			}
		else if(ccYear==year)
		{
			if (ccMonth >= month)
				return true;
			}
			return false;
	}
	
	
	
}

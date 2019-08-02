package io.epopeia.hsm.command;

import io.epopeia.hsm.HSMResponse;

class CvvValidate extends Command 
{
	
	private String pan;
	private String cvkKey;
	private String cardExpirationCavv;
	private String serviceCode;
	private String cvv;
	
	// CVV1 service code vem da trilha
	// CVV2 service code é 000
	// iCVV service code é 999
	public CvvValidate(String pan, String cvv, int expirationYear, int expirationMonth, String serviceCode, String cvkKey)
	{
		this.pan = pan;
		this.cvkKey = cvkKey;
		this.cvv = cvv;
		this.cardExpirationCavv = String.valueOf(expirationYear) + String.valueOf(expirationMonth);
		this.serviceCode = serviceCode;
	}

	public HSMResponse execute(String[] args) throws Exception {
		
		String header = "0000";
		StringBuffer sb = new StringBuffer();
		sb.append(header); //0000 header
		sb.append("CY"); // command to generate
		sb.append(this.cvkKey);
		sb.append(this.cvv);
		sb.append(this.pan); 
		sb.append(";");
		sb.append(this.cardExpirationCavv);
		sb.append(serviceCode);
		
		
		return new HSMResponse( super.runCmd(sb.toString()), header, "CZ");
	}

}

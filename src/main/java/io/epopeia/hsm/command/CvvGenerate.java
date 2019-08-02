package io.epopeia.hsm.command;

import io.epopeia.hsm.HSMResponse;

class CvvGenerate extends Command 
{
	
	private String pan;
	private String cvkKey;
	private String cardExpirationCavv;
	private String serviceCode;
	
	// servicecode for magstripe
	// 000 cvv2
	// 999 icvv
	public CvvGenerate(String pan, int expirationYear, int expirationMonth, String serviceCode, String cvkKey)
	{
		this.pan = pan;
		this.cvkKey = cvkKey;
		this.cardExpirationCavv = String.valueOf(expirationYear) + String.valueOf(expirationMonth);
		this.serviceCode = serviceCode;
	}

	public HSMResponse execute(String[] args) throws Exception {
		
		String header = "0000";
		
		StringBuffer sb = new StringBuffer();
		sb.append(header); //0000 header
		sb.append("CW"); // command to generate
		sb.append(this.cvkKey);
		sb.append(this.pan); 
		sb.append(";");
		sb.append(this.cardExpirationCavv);
		sb.append(serviceCode);
		
		
		return new HSMResponse( super.runCmd(sb.toString()), header, "CX");
		
	}

}

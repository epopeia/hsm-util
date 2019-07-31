package io.epopeia.hsm.command;

class CvkGenerate extends Command 
{
	
	private String pan;
	private String cvkKey;
	private String cardExpirationCavv;
	private String serviceCode;
	
	public CvkGenerate(String pan, int expirationYear, int expirationMonth, String serviceCode, String cvkKey)
	{
		this.pan = pan;
		this.cvkKey = cvkKey;
		this.cardExpirationCavv = String.valueOf(expirationYear) + String.valueOf(expirationMonth);
		this.serviceCode = serviceCode;
	}

	public String execute(String[] args) throws Exception {
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("0000"); //0000 header
		sb.append("CK"); // command to generate
		sb.append(this.cvkKey);
		sb.append(this.pan); 
		sb.append(";");
		sb.append(this.cardExpirationCavv);
		sb.append(serviceCode);
		
		
		String cmd = super.runCmd(sb.toString());
		
		
		
		
		return cmd;
	}

}

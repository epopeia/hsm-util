package io.epopeia.hsm.command;

class PinValidate extends Command 
{
	
	private String pan;
	
	public PinValidate(String pan)
	{
		// 12 digits from the right to the left
		this.pan = pan.substring( pan.length() - 12, pan.length() -1);	
	}

	public String execute(String[] args) throws Exception {
		
		String cmd = super.runCmd("0000JA" + this.pan);
		return cmd;
	}

}

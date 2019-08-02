package io.epopeia.hsm.command;

import io.epopeia.hsm.HSMResponse;

class PinGenerate extends Command 
{
	
	private String pan;
	
	public PinGenerate(String pan)
	{
		// 12 digits from the right to the left
		this.pan = pan.substring( pan.length() - 13, pan.length() -1);	
	}

	public HSMResponse execute(String[] args) throws Exception {
		
		String header = "0000";
		return new HSMResponse( super.runCmd(super.runCmd(header+"JA" + this.pan)), header, "JB");
		
		// COMANDO JE para geerar o pin e traduzir pra pinhost (lmk)
		
		// ver comandos para geracaoo do pin
		// qc (reference number e pin)
		// qa
		
	}

}

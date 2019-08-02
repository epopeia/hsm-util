package io.epopeia.hsm.command;

import io.epopeia.hsm.HSMResponse;

class PinValidate extends Command 
{
	
	private String pan;
	private String pinblock;
	private String pinhost;
	private String tpk;
	
	public PinValidate(String pan, String pinblock, String pinhost, String tpk)
	{
		// 12 digits from the right to the left
		this.pan = pan.substring( pan.length() - 13, pan.length() -1);
		this.pinblock = pinblock;
		this.pinhost = pinhost;
		this.tpk = tpk;
	}

	public HSMResponse execute(String[] args) throws Exception {
		
		String header = "0000"; //Numero random
		
		StringBuffer sb = new StringBuffer();
		sb.append(header); //header
		sb.append("BC"); //command
		sb.append(this.tpk); //tpk
		sb.append(this.pinblock); //pinblock da rede
		sb.append("01"); //iso0 - pinblock format
		sb.append(this.pan); //12 digitos do pan
		sb.append(this.pinhost); //pinhost
		
		return new HSMResponse(super.runCmd(sb.toString()), header, "BD");
	}

}

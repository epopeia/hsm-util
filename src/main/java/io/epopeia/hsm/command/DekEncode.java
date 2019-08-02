package io.epopeia.hsm.command;

import io.epopeia.hsm.HSMResponse;

class DekEncode extends Command 
{
	
	private String data;
	private String dekKey;
	
	public DekEncode(String data, String dekKey)
	{
		this.data = data;
		this.dekKey = dekKey;
	}

	public HSMResponse execute(String[] args) throws Exception {
		
		
		//00 ECB crypto
		//2 text encoded
		//1 output hex encoded
		//00B key type
		//
		
		String header = "0000"; 
		
		StringBuffer sb = new StringBuffer();
		sb.append(header); //0000 header
		sb.append("M0"); // command to encrypt
		sb.append("00"); // 00 ECB crypto
		sb.append("2"); //2 text encoded
		sb.append("1"); //1 output hex encoded
		sb.append("00B"); //00B key type
		sb.append("U" + this.dekKey); // dek key
		sb.append( String.format("%4d", Integer.toHexString( data.length() ))  ); //max 32000 bytes, length in hex
		sb.append( data ); // multiples of 16
		
		
		return new HSMResponse( super.runCmd(sb.toString()), header, "M1");
		
	}

}

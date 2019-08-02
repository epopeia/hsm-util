package io.epopeia.hsm;

import java.util.Hashtable;

public class HSMResponse {

	private Hashtable<String, String> errors;
	protected String cmd;
	protected String header;
	protected String commandReturn;
	protected String errorCode;
	protected String data;
	
	private String expectedHeader;
	private String expectedCommandReturn;
	
	
	public HSMResponse(String cmd, String expectedHeader, String expectedCommand)
	{
		errors = new Hashtable<String, String>();
		
		this.cmd = cmd;
		this.header = cmd.substring(0, 4); //0000
		this.commandReturn = cmd.substring(4, 6); //JA -> JB
		this.errorCode = cmd.substring(6, 8);
		this.data = cmd.substring(8);
		
		this.expectedCommandReturn = expectedCommand;
		this.expectedHeader = expectedHeader; // header enviado 
		
		
		errors.put("00","No error");
		errors.put("01","Verification failure or warning of imported key parity error");
		errors.put("02","Key inappropriate length for algorithm");
		errors.put("04","Invalid key type code");
		errors.put("05","Invalid key length flag");
		errors.put("10","Source key parity error");
		errors.put("11","Destination key parity error or key all zeros");
		errors.put("12","Contents of user storage not available. Reset, power-down or overwrite");
		errors.put("13","Invalid LMK Identifier");
		errors.put("14","PIN encrypted under LMK pair 02-03 is invalid");
		errors.put("15","Invalid input data (invalid format, invalid characters, or not enough data provided)");
		errors.put("16","Console or printer not ready or not connected");
		errors.put("17","HSM not authorized, or operation prohibited by security settings");
		errors.put("18","Document format definition not loaded");
		errors.put("19","Specified Diebold Table is invalid");
		errors.put("20","PIN block does not contain valid values");
		errors.put("21","Invalid index value, or index/block count would cause an overflow condition");
		errors.put("22","Invalid account number");
		errors.put("23","Invalid PIN block format code. (Use includes where the security setting to implement PCI HSM limitations on PIN Block format usage is applied, and a Host command attempts to convert a PIN Block to a disallowed format.)");
		errors.put("24","PIN is fewer than 4 or more than 12 digits in length");
		errors.put("25","Decimalization Table error");
		errors.put("26","Invalid key scheme");
		errors.put("27","Incompatible key length");
		errors.put("28","Invalid key type");
		errors.put("29","Key function not permitted");
		errors.put("30","Invalid reference number");
		errors.put("31","Insufficient solicitation entries for batch");
		errors.put("32","LIC007 (AES) not installed");
		errors.put("33","LMK key change storage is corrupted");
		errors.put("39","Fraud detection");
		errors.put("40","Invalid checksum");
		errors.put("41","Internal hardware/software error; bad RAM, invalid error codes, etc.");
		errors.put("42","DES failure");
		errors.put("43","RSA Key Generation Failure");
		errors.put("46","Invalid tag for encrypted PIN");
		errors.put("47","Algorithm not licensed");
		errors.put("49","Private key error, report to supervisor");
		errors.put("51","Invalid message header");
		errors.put("65","Transaction Key Scheme set to None");
		errors.put("67","Command not licensed");
		errors.put("68","Command has been disabled");
		errors.put("69","PIN block format has been disabled");
		errors.put("74","Invalid digest info syntax (no hash mode only)");
		errors.put("75","Single length key masquerading as double or triple length key");
		errors.put("76","RSA public key length error or RSA encrypted data length error");
		errors.put("77","Clear data block error");
		errors.put("78","Private key length error");
		errors.put("79","Hash algorithm object identifier error");
		errors.put("80","Data length error. The amount of MAC data (or other data) is greater than or less than the expected amount");
		errors.put("81","Invalid certificate header");
		errors.put("82","Invalid check value length");
		errors.put("83","Key block format error");
		errors.put("84","Key block check value error");
		errors.put("85","Invalid OAEP Mask Generation Function");
		errors.put("86","Invalid OAEP MGF Hash Function");
		errors.put("87","OAEP Parameter Error");
		errors.put("90","Data parity error in the request message received by the HSM");
		errors.put("91","Longitudinal Redundancy Check (LRC) character does not match the value computed over the input data (when the HSM has received a transparent async packet)");
		errors.put("92","The Count value (for the Command/Data field) is not between limits, or is not correct (when the HSM has received a transparent async packet)");
		errors.put("A1","Incompatible LMK schemes");
		errors.put("A2","Incompatible LMK identifiers");
		errors.put("A3","Incompatible key block LMK identifiers");
		errors.put("A4","Key block authentication failure");
		errors.put("A5","Incompatible key length");
		errors.put("A6","Invalid key usage");
		errors.put("A7","Invalid algorithm");
		errors.put("A8","Invalid mode of use");
		errors.put("A9","Invalid key version number");
		errors.put("AA","Invalid export field");
		errors.put("AB","Invalid number of optional blocks");
		errors.put("AC","Optional header block error");
		errors.put("AD","Key status optional block error");
		errors.put("AE","Invalid start date/time");
		errors.put("AF","Invalid end date/time");
		errors.put("B0","Invalid encryption mode");
		errors.put("B1","Invalid authentication mode");
		errors.put("B2","Miscellaneous key block error");
		errors.put("B3","Invalid number of optional blocks");
		errors.put("B4","Optional block data error");
		errors.put("B5","Incompatible components");
		errors.put("B6","Incompatible key status optional blocks");
		errors.put("B7","Invalid change field");
		errors.put("B8","Invalid old value");
		errors.put("B9","Invalid new value");
		errors.put("BA","No key status block in the key block");
		errors.put("BB","Invalid wrapping key");
		
		parse();
		
	}
	

	public Boolean isSuccessful()
	{
		
		return this.errorCode.equals( "00" ) && 
		       this.expectedHeader.equals( this.header ) &&
		       this.expectedCommandReturn.equals( this.commandReturn);
		       
	}
	
	public void parse()
	{
		
	}
	
	public String getData()
	{
		return this.data;
	}
	
	
	
}

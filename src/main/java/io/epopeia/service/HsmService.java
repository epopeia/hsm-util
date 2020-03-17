package io.epopeia.service;

public interface HsmService {

	boolean cvvGenerate(String pan, int expYear, int expMonth, String srvCode, String cvkKey);

	boolean cvvValidate(String pan, int expYear, int expMonth, String srvCode, String cvkKey);

	boolean dekEncode(String data, String dekKey);

	boolean dekDecode(String encriptedData, String dekKey);

	String pinGenerate(String pan);

    String encryptClearPin(String pan, String clearPin);

    boolean pinValidate(String pan, String pinblock, String pinhost, String tpk);

	boolean performDiagnostic();

	String sendBufferCommand(String buffer);
}

package io.epopeia.service;

public interface HsmService {

	void cvvGenerate(String pan, int expYear, int expMonth, String srvCode, String cvkKey);

	void cvvValidate(String pan, int expYear, int expMonth, String srvCode, String cvkKey);

	void dekEncode(String data, String dekKey);

	void pinGenerate(String pan);

	void pinValidate(String pan, String pinblock, String pinhost, String tpk);
}

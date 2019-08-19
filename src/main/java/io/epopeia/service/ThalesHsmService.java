package io.epopeia.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import io.epopeia.hsm.HSMResponse;
import io.epopeia.integration.HSMClient.HsmGateway;

@Profile("hsm")
@Service
public class ThalesHsmService implements HsmService {

	private static final Logger LOGGER = LogManager.getLogger(ThalesHsmService.class);

	@Autowired
	public HsmGateway hsmGateway;

	@Override
	public boolean cvvGenerate(String pan, int expYear, int expMonth, String srvCode, String cvkKey) {
		final String header = "0000";

		final StringBuffer sb = new StringBuffer();
		sb.append(header); // 0000 header
		sb.append("CW"); // command to generate
		sb.append(cvkKey);
		sb.append(pan);
		sb.append(";");
		sb.append(String.valueOf(expYear) + String.valueOf(expMonth));
		sb.append(srvCode);

		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "CX");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		LOGGER.info("Response for cvvGenerate: " + isSuccessful);
		return isSuccessful;
	}

	@Override
	public boolean cvvValidate(String pan, int expYear, int expMonth, String srvCode, String cvkKey) {
		final String header = "0000";

		final StringBuffer sb = new StringBuffer();
		sb.append(header); // 0000 header
		sb.append("CY"); // command to generate
		sb.append(cvkKey);
		sb.append(pan);
		sb.append(";");
		sb.append(String.valueOf(expYear) + String.valueOf(expMonth));
		sb.append(srvCode);

		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "CZ");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		LOGGER.info("Response for cvvValidate: " + isSuccessful);
		return isSuccessful;
	}

	@Override
	public boolean dekEncode(String data, String dekKey) {
		final String header = "0000";

		final StringBuffer sb = new StringBuffer();
		sb.append(header); // 0000 header
		sb.append("M0"); // command to encrypt
		sb.append("00"); // 00 ECB crypto
		sb.append("2"); // 2 text encoded
		sb.append("1"); // 1 output hex encoded
		sb.append("00B"); // 00B key type
		sb.append("U" + dekKey); // dek key
		sb.append(String.format("%4d", Integer.toHexString(data.length()))); // max 32000 bytes, length in hex
		sb.append(data); // multiples of 16

		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "M1");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		LOGGER.info("Response for dekEncode: " + isSuccessful);
		return isSuccessful;
	}

	@Override
	public String pinGenerate(String pan) {
		final String header = "0000";
		final String s = header + "JA" + pan.substring(pan.length() - 13, pan.length() - 1);

		final String ret = hsmGateway.sendAndReceive(s);
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "JB");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		LOGGER.info("Response for pinGenerate: " + isSuccessful);
		return ret.substring(ret.length()-4);
	}

	@Override
	public String encryptClearPin(String pan, String clearPin) {
		final String header = "0000";
		final String s = header 
				+ "BA" 
				//+ clearPin.length()
				+ "F"
				+ clearPin 
				+ pan.substring(pan.length() - 13, pan.length() - 1);

		final String ret = hsmGateway.sendAndReceive(s);
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "BB");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		LOGGER.info("Response for encript clear pin: " + isSuccessful);
		return ret.substring(8);
	}


	@Override
	public boolean pinValidate(String pan, String pinblock, String pinhost, String tpk) {
		final String header = "0000"; // Numero random

		final StringBuffer sb = new StringBuffer();
		sb.append(header); // header
		LOGGER.info(header);
		sb.append("BC"); // command
		LOGGER.info("BC");
		sb.append("U");
		LOGGER.info("U");
		sb.append(tpk); // tpk
		LOGGER.info(tpk);
		sb.append(pinblock); // pinblock da rede
		LOGGER.info(pinblock);
		sb.append("01"); // iso0 - pinblock format
		LOGGER.info("01");
		sb.append(pan.substring(pan.length() - 13, pan.length() - 1)); // 12 digitos do pan
		LOGGER.info(pan.substring(pan.length() - 13, pan.length() - 1));
		sb.append(pinhost); // pinhost
		LOGGER.info(pinhost);


		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "BD");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		LOGGER.info("Response for pinValidate: " + isSuccessful);
		return isSuccessful;
	}

	@Override
	public boolean performDiagnostic() {
		final String header = "0000";
		final StringBuffer sb = new StringBuffer();
		sb.append(header);
		sb.append("NC");
		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "ND");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		LOGGER.info("Response for performDiagnostics: " + isSuccessful);
		return isSuccessful;
	}
}
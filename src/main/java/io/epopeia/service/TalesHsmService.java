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
public class TalesHsmService implements HsmService {

	private static final Logger LOGGER = LogManager.getLogger(TalesHsmService.class);

	@Autowired
	private HsmGateway hsmGateway;

	@Override
	public void cvvGenerate(String pan, int expYear, int expMonth, String srvCode, String cvkKey) {
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
		LOGGER.info("Response for cvvGenerate: " + hsmResponse.isSuccessful());
	}

	@Override
	public void cvvValidate(String pan, int expYear, int expMonth, String srvCode, String cvkKey) {
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
		LOGGER.info("Response for cvvValidate: " + hsmResponse.isSuccessful());
	}

	@Override
	public void dekEncode(String data, String dekKey) {
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
		LOGGER.info("Response for dekEncode: " + hsmResponse.isSuccessful());
	}

	@Override
	public void pinGenerate(String pan) {
		final String header = "0000";
		final String s = header + "JA" + pan.substring(pan.length() - 13, pan.length() - 1);

		final String ret = hsmGateway.sendAndReceive(s);
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "JB");
		LOGGER.info("Response for pinGenerate: " + hsmResponse.isSuccessful());
	}

	@Override
	public void pinValidate(String pan, String pinblock, String pinhost, String tpk) {
		final String header = "0000"; // Numero random

		final StringBuffer sb = new StringBuffer();
		sb.append(header); // header
		sb.append("BC"); // command
		sb.append(tpk); // tpk
		sb.append(pinblock); // pinblock da rede
		sb.append("01"); // iso0 - pinblock format
		sb.append(pan.substring(pan.length() - 13, pan.length() - 1)); // 12 digitos do pan
		sb.append(pinhost); // pinhost

		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "BD");
		LOGGER.info("Response for pinValidate: " + hsmResponse.isSuccessful());
	}
	
	@Override
	public void performDiagnostic(){
		final String header = "0000";
		final StringBuffer sb = new StringBuffer();
		sb.append(header);
		sb.append("NC");
		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "ND");
		LOGGER.info("Response for performDiagnostics	: " + hsmResponse.isSuccessful());	
		
	}
}
package io.epopeia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.epopeia.hsm.HSMResponse;
import io.epopeia.integration.HSMClient.HsmGateway;

@Service
public class ThalesHsmService implements HsmService {

	@Autowired
	public HsmGateway hsmGateway;

	@Value("${hsm.lmk:%01}")
	private String lmk;

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
		System.out.println("Response for cvvGenerate: " + isSuccessful);
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
		System.out.println("Response for cvvValidate: " + isSuccessful);
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
		final String dataLength = Integer.toHexString(data.length()).toUpperCase();
		final String dataLengthPadZeros = String.format("%1$" + 4 + "s", dataLength).replace(' ', '0');
		sb.append(dataLengthPadZeros);
		sb.append(data); // multiples of 16
		sb.append(lmk);

		final String cmd = sb.toString();
		System.out.println("sending to HSM: " + cmd);
		final String ret = hsmGateway.sendAndReceive(cmd);
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "M1");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		final String retData = hsmResponse.getData();
		System.out.println("Response for dekEncode: " + isSuccessful + " data: " + retData);
		return isSuccessful;
	}

	@Override
	public String pinGenerate(String pan) {
		final String header = "0000";
		final String s = header + "JA" + pan.substring(pan.length() - 13, pan.length() - 1);

		final String ret = hsmGateway.sendAndReceive(s);
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "JB");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		System.out.println("Response for pinGenerate: " + isSuccessful);
		return ret.substring(ret.length() - 4);
	}

	@Override
	public String encryptClearPin(String pan, String clearPin) {
		final String header = "0000";
		final String s = header + "BA"
		// + clearPin.length()
				+ "F" + clearPin + pan.substring(pan.length() - 13, pan.length() - 1);

		final String ret = hsmGateway.sendAndReceive(s);
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "BB");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		System.out.println("Response for encript clear pin: " + isSuccessful);
		return ret.substring(8);
	}

	@Override
	public boolean pinValidate(String pan, String pinblock, String pinhost, String tpk) {
		final String header = "0000"; // Numero random

		final StringBuffer sb = new StringBuffer();
		sb.append(header); // header
		System.out.println(header);
		sb.append("BC"); // command
		System.out.println("BC");
		sb.append("U");
		System.out.println("U");
		sb.append(tpk); // tpk
		System.out.println(tpk);
		sb.append(pinblock); // pinblock da rede
		System.out.println(pinblock);
		sb.append("01"); // iso0 - pinblock format
		System.out.println("01");
		sb.append(pan.substring(pan.length() - 13, pan.length() - 1)); // 12 digitos do pan
		System.out.println(pan.substring(pan.length() - 13, pan.length() - 1));
		sb.append(pinhost); // pinhost
		System.out.println(pinhost);

		final String ret = hsmGateway.sendAndReceive(sb.toString());
		final HSMResponse hsmResponse = new HSMResponse(ret, header, "BD");
		final boolean isSuccessful = hsmResponse.isSuccessful();
		System.out.println("Response for pinValidate: " + isSuccessful);
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
		System.out.println("Response for performDiagnostics: " + isSuccessful);
		return isSuccessful;
	}

	@Override
	public String sendBufferCommand(String buffer) {
		final String ret = hsmGateway.sendAndReceive(buffer);
		System.out.println("Response buffer from HSM: " + ret);
		final HSMResponse hsmResponse = new HSMResponse(ret, buffer.substring(0, 4), buffer.substring(4, 6));
		System.out.println(hsmResponse.getError());
		return ret;
	}
}

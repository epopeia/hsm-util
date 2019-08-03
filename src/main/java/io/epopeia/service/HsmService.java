package io.epopeia.service;

public interface HsmService {

	void sendCommandToHsm(byte[] requestCommand);
	void handleResponseFromHsm(byte[] hsmResponse);
}

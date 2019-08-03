package io.epopeia.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TalesHsmService implements HsmService {

	private static final Logger LOGGER = LogManager.getLogger(TalesHsmService.class);

	@Override
	public void sendCommandToHsm(byte[] requestCommand) {
		LOGGER.info("TODO: send command to HSM");
	}

	@Override
	public void handleResponseFromHsm(byte[] hsmResponse) {
		LOGGER.info("TODO: handle response from HSM");
	}
}

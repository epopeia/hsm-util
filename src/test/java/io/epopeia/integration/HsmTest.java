package io.epopeia.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.epopeia.Iso8583ClientServer;
import io.epopeia.hsm.HSMResponse;
import io.epopeia.integration.HSMClient.HsmGateway;
import io.epopeia.service.ThalesHsmService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Iso8583ClientServer.class})
@SpringIntegrationTest
@ActiveProfiles("hsm")
public class HsmTest {

	@Autowired
	HSMClient client;
	
	@Autowired
	ThalesHsmService service;

	private static final Logger LOGGER = LogManager.getLogger(HsmTest.class);
	
	
	@Test
	public void pinGenerateTest() {
		
		String pan = "4761340000000019";
		String pin = service.pinGenerate(pan);
		LOGGER.info(pin);
		assertTrue(pin.length() == 4);
	}
	
	@Test
	public void pinValidateTest(){
		String pan = "4761340000000019";
		String pinblock = "BF6572C824CE3604";
		String pinhost = "01234";
		String tpk = "23A080EB030EF7A298D1B349B306C615";
		boolean validated = service.pinValidate(pan, pinblock, pinhost, tpk);
		assertTrue(validated);
	}
	
	@Test
	public void encryptClearPinTest(){
		String encryptedPin = service.encryptClearPin("4761340000000019", "1234");
		assertTrue(encryptedPin.equals("01234"));
	}
	
	@Test
	public void NCTest(){
		boolean isOk = service.performDiagnostic();
		assertTrue(isOk);
	}

}

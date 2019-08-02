package io.epopeia.integration;

import java.util.HashMap;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.header.BASE1Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.epopeia.integration.Server.CustomBase1Packager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Server.class)
@SpringIntegrationTest(noAutoStartup = "*")
public class ServerTest {
	
	@Autowired
	Server server;

	@Test
	public void handleMessageFromClientTest() throws ISOException {
		ISOMsg msg = new ISOMsg();
		msg.setPackager(new CustomBase1Packager());
		msg.set(0, "0200");
		msg.set(2, "4761340000000019");
		msg.set(3, "004000");
		msg.set(4, "000000007722");
		msg.set(7, "0730134627");
		msg.set(11, "000025");
		msg.set(12, "104628");
		msg.set(13, "0730");
		msg.set(14, "2212");
		msg.set(18, "5411");
		msg.set(19, "840");
		msg.set(22, "9010");
		msg.set(25, "00");
		msg.set(28, "D00000000");
		msg.set(32, "12345678901");
		msg.set(35, "4761340000000019=221210112345129");
		msg.set(37, "921113000025");
		msg.set(41, "TERMID01");
		msg.set(42, "CARD ACCEPTOR  ");
		msg.set(43, "ACQUIRER NAME            CITY NAME    US");
		msg.set(49, "840");
		msg.set(52, "DAE84D88984C5A96");
		msg.set(53, "2001010100000000");
		msg.set(60, "42");
		msg.set(63, "8070000002F1F1F1F1F2F3F4F5F6F7F8F9F0E9C7C1C2C3C640404040404040404040404040404040404040404040404040");
		
		
		final byte[] mb = msg.pack();

		// get the header buffer
		final BASE1Header h = new BASE1Header();
		h.setLen(mb.length);
		final byte[] hb = h.pack();

		// concatenate the header and message in a buffer
		final byte[] b = new byte[hb.length + mb.length];
		System.arraycopy(hb, 0, b, 0, hb.length);
		System.arraycopy(mb, 0, b, hb.length, mb.length);
		
		
		Message<byte[]> message = new Message<byte[]>() {

			@Override
			public byte[] getPayload() {
				return b;
			}

			@Override
			public MessageHeaders getHeaders() {
				// TODO Auto-generated method stub
				return new MessageHeaders(new HashMap<>());
			}
		};
		server.handleMessageFromClient(message);
	}

}

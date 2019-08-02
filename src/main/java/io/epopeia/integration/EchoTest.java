package io.epopeia.integration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.header.BASE1Header;
import org.jpos.iso.packager.Base1Packager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Profile("echotest")
@Service
public class EchoTest {

	private static int stan = 0;

	@Lazy
	@Autowired
	@Qualifier("clientOut")
	private TcpSendingMessageHandler sender;

	@Scheduled(fixedRate = 5000)
	public void sendEchoTest() throws ISOException {

		// reset stan
		if (++stan > 999999) {
			stan = 1;
		}

		// create a message container
		final ISOMsg m = new ISOMsg();
		m.setPackager(new Base1Packager());

		// set the message fields
		m.setMTI("0800");
		m.set(7, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMHHmmss")));
		m.set(11, String.valueOf(stan));
		m.set(70, "301");

		// get the message buffer
		final byte[] mb = m.pack();

		// get the header buffer
		final BASE1Header h = new BASE1Header();
		h.setLen(mb.length);
		final byte[] hb = h.pack();

		// concatenate the header and message in a buffer
		final byte[] b = new byte[hb.length + mb.length];
		System.arraycopy(hb, 0, b, 0, hb.length);
		System.arraycopy(mb, 0, b, hb.length, mb.length);

		// send the full buffer
		sender.handleMessageInternal(new GenericMessage<byte[]>(b));
	}
}

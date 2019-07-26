package io.epopeia.integration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.Base1Packager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

@Service
@Profile("client")
public class EchoTest {

	private static int stan = 0;

	@Lazy
	@Autowired
	private TcpSendingMessageHandler sender;

	@Scheduled(fixedRate = 5000)
	public void sendEchoTest() throws ISOException, DecoderException {

		// reset stan
		if (++ stan > 999999) {
			stan = 1;
		}

		ISOMsg m = new ISOMsg();
		m.setMTI("0800");
		m.set(7, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMHHmmss")));
		m.set(11, String.valueOf(stan));
		m.set(70, "301");
		m.setPackager(new Base1Packager());

		final byte[] header = Hex.decodeHex("16010200440000000000000000000000000000000000".toCharArray());
		byte[] isomsg = m.pack();
		byte[] fullmsg = new byte[header.length + isomsg.length];
		System.arraycopy(header, 0, fullmsg, 0, header.length);
		System.arraycopy(isomsg, 0, fullmsg, header.length, isomsg.length);

		sender.handleMessageInternal(new GenericMessage<byte[]>(fullmsg));
	}
}

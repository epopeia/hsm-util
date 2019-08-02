package io.epopeia.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.IFE_AMOUNT;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.header.BASE1Header;
import org.jpos.iso.packager.Base1Packager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@Configuration
public class Server {

	private static final Logger LOGGER = LogManager.getLogger(Server.class);

	@Value("${iso8583.server.port:9090}")
	private Integer port;

	@Value("${iso8583.server.header.length:4}")
	private int tcpHeaderLength;

	private static final String serverInChannel = "serverInChannel";
	private static final String serverOutChannel = "serverOutChannel";

	@Bean
	public ByteArrayLengthHeaderSerializer serverSerializer() {
		return new ByteArrayLengthHeaderSerializer(tcpHeaderLength);
	}

	@Bean
	public ByteArrayLengthHeaderSerializer serverDeserializer() {
		return new ByteArrayLengthHeaderSerializer(tcpHeaderLength);
	}

	@Bean
	public AbstractServerConnectionFactory myServer() {
		final TcpNetServerConnectionFactory server = new TcpNetServerConnectionFactory(this.port);
		server.setSerializer(serverSerializer());
		server.setDeserializer(serverDeserializer());
		return server;
	}

	@Bean
	@ServiceActivator(inputChannel = serverOutChannel)
	public TcpSendingMessageHandler serverOut() {
		final TcpSendingMessageHandler sender = new TcpSendingMessageHandler();
		sender.setConnectionFactory(myServer()); // share the same connections
		return sender;
	}

	@Bean
	public TcpReceivingChannelAdapter serverIn() {
		final TcpReceivingChannelAdapter receiver = new TcpReceivingChannelAdapter();
		receiver.setConnectionFactory(myServer()); // share the same connections
		receiver.setOutputChannelName(serverInChannel);
		return receiver;
	}

	@ServiceActivator(inputChannel = serverInChannel, outputChannel = serverOutChannel)
	public Message<byte[]> handleMessageFromClient(Message<byte[]> message) throws ISOException {
		LOGGER.info("-------------------------------------------------------------------");
		message.getHeaders().forEach((k, v) -> LOGGER.info(String.format("%s: %s", k, v)));
		final byte[] payloadRaw = message.getPayload();
		LOGGER.info("Received from client: " + ISOUtil.hexString(payloadRaw));

		final byte[] header = Arrays.copyOfRange(payloadRaw, 0, BASE1Header.LENGTH);
		final byte[] iso8583 = Arrays.copyOfRange(payloadRaw, BASE1Header.LENGTH, payloadRaw.length);

		// create a header container and read the header
		final BASE1Header h = new BASE1Header(header);
		LOGGER.info(h.formatHeader());

		// create a message container and read message
		ISOMsg m = new ISOMsg();
		m.setDirection(ISOMsg.INCOMING);
		m.setPackager(new CustomBase1Packager());
		m.unpack(iso8583);
		jposDumpToLog4j(m);

		// set response fields in the message
		final int MTI = Integer.parseInt(m.getMTI());
		if (MTI == 200) {
			final ISOMsg mr = ISOMsg.class.cast(m.clone(new int[] { 2, 3, 4, 6, 7, 11, 19, 23, 25, 32, 37, 41, 42, 48,
					49, 51, 54, 55, 63, 102, 103, 104, 117, 121 }));
			mr.setMTI("0210");

			mr.set(38, "123456");
			mr.set(39, "00");

			m = mr;

		} else {
			m.setResponseMTI();
			m.set(39, "00");
		}

		m.setDirection(ISOMsg.OUTGOING);
		// logs the response message before pack
		jposDumpToLog4j(m);

		// get the message buffer
		final byte[] mb = m.pack();

		// get the header buffer
		h.setLen(mb.length);
		final byte[] hb = h.pack();

		// concatenate the header and message in a buffer
		final byte[] b = new byte[hb.length + mb.length];
		System.arraycopy(hb, 0, b, 0, hb.length);
		System.arraycopy(mb, 0, b, hb.length, mb.length);

		// send the full buffer
		return new GenericMessage<byte[]>(b);
	}

	private static void jposDumpToLog4j(ISOMsg m) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		ps.println();
		m.dump(ps, "");
		LOGGER.info(baos.toString());
	}

	private static class CustomBase1Packager extends Base1Packager {
		public CustomBase1Packager() {
			super();
			base1Fld[28] = new IFE_AMOUNT(9, "AMOUNT, TRANSACTION FEE");
		}
	}
}

package io.epopeia.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.header.BASE1Header;
import org.jpos.iso.packager.Base1Packager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.Message;

@Profile("client")
@Configuration
public class Iso8583Client {

	private static final Logger LOGGER = LogManager.getLogger(Iso8583Client.class);

	@Value("${iso8583.server.host:localhost}")
	private String host;

	@Value("${iso8583.server.port:9090}")
	private Integer port;

	@Value("${iso8583.client.header.length:4}")
	private int headerLength;

	private static final String clientOutChannel = "clientOutChannel";
	private static final String clientInChannel = "clientInChannel";

	@Bean
	public ByteArrayLengthHeaderSerializer clientSerializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public ByteArrayLengthHeaderSerializer clientDeserializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public AbstractClientConnectionFactory myClient() {
		final TcpNetClientConnectionFactory client = new TcpNetClientConnectionFactory(host, port);
		client.setSerializer(clientSerializer());
		client.setDeserializer(clientDeserializer());
		return client;
	}

	@Bean
	@ServiceActivator(inputChannel = clientOutChannel)
	public TcpSendingMessageHandler clientOut() {
		final TcpSendingMessageHandler sender = new TcpSendingMessageHandler();
		sender.setConnectionFactory(myClient()); // share the same connections
		return sender;
	}

	@Bean
	public TcpReceivingChannelAdapter clientIn() {
		final TcpReceivingChannelAdapter receiver = new TcpReceivingChannelAdapter();
		receiver.setConnectionFactory(myClient()); // share the same connections
		receiver.setOutputChannelName(clientInChannel);
		return receiver;
	}

	@ServiceActivator(inputChannel = clientInChannel)
	public void handleMessageFromServer(Message<byte[]> message) throws ISOException {
		LOGGER.info("-------------------------------------------------------------------");
		message.getHeaders().forEach((k, v) -> LOGGER.info(String.format("%s: %s", k, v)));
		final byte[] payloadRaw = message.getPayload();
		LOGGER.info("Received from server: " + Hex.encodeHexString(payloadRaw));

		final byte[] header = Arrays.copyOfRange(payloadRaw, 0, BASE1Header.LENGTH);
		final byte[] iso8583 = Arrays.copyOfRange(payloadRaw, BASE1Header.LENGTH, payloadRaw.length);

		// create a message container and read message
		final ISOMsg m = new ISOMsg();
		m.setDirection(ISOMsg.INCOMING);
		m.setPackager(new Base1Packager());
		m.unpack(iso8583);

		// create a header container and read the header
		final BASE1Header h = new BASE1Header(header);

		// print header and message
		LOGGER.info(h.formatHeader());
		jposDumpToLog4j(m);
	}

	private static void jposDumpToLog4j(ISOMsg m) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		ps.println();
		m.dump(ps, "");
		LOGGER.info(baos.toString());
	}
}

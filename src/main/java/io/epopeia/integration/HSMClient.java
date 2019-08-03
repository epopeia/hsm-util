package io.epopeia.integration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.Message;

@Configuration
public class HSMClient {

	private static final Logger LOGGER = LogManager.getLogger(HSMClient.class);

	@Value("${iso8583.hsm.host}")
	private String host;

	@Value("${iso8583.hsm.port}")
	private Integer port;

	@Value("${iso8583.hsm.header.length:2}")
	private int headerLength;

	private static final String hsmOutChannel = "hsmOutChannel";
	private static final String hsmInChannel = "hsmInChannel";

	@Bean
	public ByteArrayLengthHeaderSerializer hsmSerializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public ByteArrayLengthHeaderSerializer hsmDeserializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public AbstractClientConnectionFactory myHsm() {
		final TcpNetClientConnectionFactory client = new TcpNetClientConnectionFactory(host, port);
		client.setSerializer(hsmSerializer());
		client.setDeserializer(hsmDeserializer());
		return client;
	}

	@Bean
	@ServiceActivator(inputChannel = hsmOutChannel)
	public TcpSendingMessageHandler hsmOut() {
		final TcpSendingMessageHandler sender = new TcpSendingMessageHandler();
		sender.setConnectionFactory(myHsm()); // share the same connections
		return sender;
	}

	@Bean
	public TcpReceivingChannelAdapter hsmIn() {
		final TcpReceivingChannelAdapter receiver = new TcpReceivingChannelAdapter();
		receiver.setConnectionFactory(myHsm()); // share the same connections
		receiver.setOutputChannelName(hsmInChannel);
		return receiver;
	}

	@ServiceActivator(inputChannel = hsmInChannel)
	public void handleMessageFromHsm(Message<byte[]> message) throws ISOException {
		LOGGER.info("-------------------------------------------------------------------");
		message.getHeaders().forEach((k, v) -> LOGGER.info(String.format("%s: %s", k, v)));
		final byte[] payloadRaw = message.getPayload();
		LOGGER.info("Received from hsm: " + ISOUtil.hexString(payloadRaw));
	}
}

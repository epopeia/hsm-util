package io.epopeia.integration;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import io.epopeia.service.NetworkService;

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

	@Autowired
	private NetworkService networkService;

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
	public void handleMessageFromServer(Message<byte[]> message) {
		LOGGER.info("-------------------------------------------------------------------");
		message.getHeaders().forEach((k, v) -> LOGGER.info(String.format("%s: %s", k, v)));
		final byte[] payloadRaw = message.getPayload();
		LOGGER.info("Received from server: " + Hex.encodeHexString(payloadRaw));

		networkService.handleMessageFromAuthorizator(payloadRaw);
	}
}

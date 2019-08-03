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
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import io.epopeia.service.AuthorizationService;

@Profile("server")
@Configuration
public class Iso8583Server {

	private static final Logger LOGGER = LogManager.getLogger(Iso8583Server.class);

	@Value("${iso8583.server.port:9090}")
	private Integer port;

	@Value("${iso8583.server.header.length:4}")
	private int tcpHeaderLength;

	@Autowired
	private AuthorizationService networkService;

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
	public Message<byte[]> handleMessageFromClient(Message<byte[]> message) {
		LOGGER.info("-------------------------------------------------------------------");
		message.getHeaders().forEach((k, v) -> LOGGER.info(String.format("%s: %s", k, v)));
		final byte[] payloadRaw = message.getPayload();
		LOGGER.info("Received from client: " + Hex.encodeHexString(payloadRaw));

		final byte[] b = networkService.handleMessageFromNetwork(payloadRaw);

		return new GenericMessage<byte[]>(b);
	}
}

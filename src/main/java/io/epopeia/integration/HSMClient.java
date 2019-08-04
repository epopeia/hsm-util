package io.epopeia.integration;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

@Profile("hsm")
@Configuration
public class HSMClient {

	private static final Logger LOGGER = LogManager.getLogger(HSMClient.class);

	@Value("${iso8583.hsm.host:localhost}")
	private String host;

	@Value("${iso8583.hsm.port:9998}")
	private Integer port;

	@Value("${iso8583.hsm.header.length:2}")
	private int headerLength;

	@MessagingGateway(defaultRequestChannel = "hsmOutChannel")
	public interface HsmGateway {

		String sendAndReceive(String command);
	}

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
		client.setSingleUse(true);
		return client;
	}

	@Bean
	@ServiceActivator(inputChannel = hsmOutChannel)
	public MessageHandler hsmOut() {
		final TcpOutboundGateway gate = new TcpOutboundGateway();
		gate.setConnectionFactory(myHsm());
		gate.setOutputChannelName(hsmInChannel);
		return gate;
	}

	@ServiceActivator(inputChannel = hsmInChannel)
	public String handleMessageFromHsm(Message<byte[]> message) {
		LOGGER.info("-------------------------------------------------------------------");
		message.getHeaders().forEach((k, v) -> LOGGER.info(String.format("%s: %s", k, v)));
		final byte[] payloadRaw = message.getPayload();
		final String payloadString = new String(payloadRaw, StandardCharsets.US_ASCII);
		LOGGER.info("Received from HSM: " + payloadString);
		return payloadString;
	}
}

package io.epopeia.integration;

import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
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
public class Client {

	@Value("${iso8583.server.host:localhost}")
	private String host;

	@Value("${iso8583.server.port:8080}")
	private Integer port;

	@Value("${iso8583.client.header.length:4}")
	private int headerLength;

	private static final String toServer = "toServer";
	private static final String fromServer = "fromServer";

	@Bean
	public ByteArrayLengthHeaderSerializer javaSerializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public ByteArrayLengthHeaderSerializer javaDeserializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public AbstractClientConnectionFactory myClient() {
		final TcpNetClientConnectionFactory client = new TcpNetClientConnectionFactory(host, port);
		client.setSerializer(javaSerializer());
		client.setDeserializer(javaDeserializer());
		return client;
	}

	@Bean
	@ServiceActivator(inputChannel = toServer)
	public TcpSendingMessageHandler mySender() {
		final TcpSendingMessageHandler sender = new TcpSendingMessageHandler();
		sender.setConnectionFactory(myClient()); // share the same connections
		return sender;
	}

	@Bean
	public TcpReceivingChannelAdapter myReceiver() {
		final TcpReceivingChannelAdapter receiver = new TcpReceivingChannelAdapter();
		receiver.setConnectionFactory(myClient()); // share the same connections
		receiver.setOutputChannelName(fromServer);
		return receiver;
	}

	@ServiceActivator(inputChannel = fromServer)
	public void handleMessageFromServer(Message<byte[]> message) throws ISOException {
		message.getHeaders().forEach((k, v) -> System.out.printf("%s: %s\n", k, v));
		System.out.println("---------------------------------------");
		final byte[] payloadRaw = message.getPayload();
		System.out.println("Received from server: " + Hex.encodeHexString(payloadRaw));
		final int headerLength = payloadRaw[0];
		final byte[] header = Arrays.copyOfRange(payloadRaw, 0, headerLength);
		System.out.println("Header of message: " + Hex.encodeHexString(header));
		final byte[] iso8583msg = Arrays.copyOfRange(payloadRaw, headerLength, payloadRaw.length);
		System.out.println("Iso8583 message: " + Hex.encodeHexString(iso8583msg));

		ISOMsg m = new ISOMsg();
		m.setPackager(new Base1Packager());
		m.unpack(iso8583msg);

		m.dump(System.out, "\t");
	}
}

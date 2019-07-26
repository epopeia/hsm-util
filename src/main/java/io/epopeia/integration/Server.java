package io.epopeia.integration;

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
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@Profile("server")
@Configuration
public class Server {

	@Value("${iso8583.server.port:8080}")
	private Integer port;

	@Value("${iso8583.server.header.length:4}")
	private int headerLength;

	private static final String fromClient = "fromClient";
	private static final String toClient = "toClient";

	@Bean
	public ByteArrayLengthHeaderSerializer javaSerializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public ByteArrayLengthHeaderSerializer javaDeserializer() {
		return new ByteArrayLengthHeaderSerializer(headerLength);
	}

	@Bean
	public AbstractServerConnectionFactory myServer() {
		final TcpNetServerConnectionFactory server = new TcpNetServerConnectionFactory(this.port);
		server.setSerializer(javaSerializer());
		server.setDeserializer(javaDeserializer());
		return server;
	}

	@Bean
	@ServiceActivator(inputChannel = toClient)
	public TcpSendingMessageHandler mySender() {
		final TcpSendingMessageHandler sender = new TcpSendingMessageHandler();
		sender.setConnectionFactory(myServer()); // share the same connections
		return sender;
	}

	@Bean
	public TcpReceivingChannelAdapter myReceiver() {
		final TcpReceivingChannelAdapter receiver = new TcpReceivingChannelAdapter();
		receiver.setConnectionFactory(myServer()); // share the same connections
		receiver.setOutputChannelName(fromClient);
		return receiver;
	}

	@ServiceActivator(inputChannel = fromClient, outputChannel = toClient)
	public Message<byte[]> handleMessageFromClient(Message<byte[]> message) throws ISOException {
		message.getHeaders().forEach((k, v) -> System.out.printf("%s: %s\n", k, v));
		System.out.println("---------------------------------------");
		System.out.println("Received from client: " + new String(message.getPayload()));

		ISOMsg m = new ISOMsg();
		m.setPackager(new Base1Packager());
		m.unpack(message.getPayload());

		m.dump(System.out, "\t");

		m.setResponseMTI();
		m.set(39, "00");

		return new GenericMessage<byte[]>(m.pack());
	}
}

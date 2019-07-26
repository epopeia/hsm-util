package io.epopeia.integration;

import java.util.Arrays;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.Base1Packager;
import org.apache.commons.codec.binary.Hex;
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
	private int tcpHeaderLength;

	private static final String fromClient = "fromClient";
	private static final String toClient = "toClient";

	@Bean
	public ByteArrayLengthHeaderSerializer javaSerializer() {
		return new ByteArrayLengthHeaderSerializer(tcpHeaderLength);
	}

	@Bean
	public ByteArrayLengthHeaderSerializer javaDeserializer() {
		return new ByteArrayLengthHeaderSerializer(tcpHeaderLength);
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
	public Message<byte[]> handleMessageFromClient(Message<byte[]> message) throws ISOException, DecoderException {
		message.getHeaders().forEach((k, v) -> System.out.printf("%s: %s\n", k, v));
		System.out.println("---------------------------------------");
		final byte[] payloadRaw = message.getPayload();
		System.out.println("Received from client: " + Hex.encodeHexString(payloadRaw));
		final int headerLength = payloadRaw[0];
		final byte[] header = Arrays.copyOfRange(payloadRaw, 0, headerLength);
		System.out.println("Header of message: " + Hex.encodeHexString(header));
		final byte[] iso8583msg = Arrays.copyOfRange(payloadRaw, headerLength, payloadRaw.length);
		System.out.println("Iso8583 message: " + Hex.encodeHexString(iso8583msg));

		ISOMsg m = new ISOMsg();
		m.setPackager(new Base1Packager());
		m.unpack(iso8583msg);

		m.dump(System.out, "\t");

		m.setResponseMTI();
		m.set(39, "00");

		final byte[] headerResp = Hex.decodeHex("16010200440000000000000000000000000000000000".toCharArray());
		byte[] isomsg = m.pack();
		byte[] fullmsg = new byte[headerResp.length + isomsg.length];
		System.arraycopy(headerResp, 0, fullmsg, 0, headerResp.length);
		System.arraycopy(isomsg, 0, fullmsg, headerResp.length, isomsg.length);

		return new GenericMessage<byte[]>(fullmsg);
	}
}

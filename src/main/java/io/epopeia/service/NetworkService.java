package io.epopeia.service;

public interface NetworkService {

	void sendMessageToAuthorizator(byte[] message);
	void handleMessageFromAuthorizator(byte[] message);
}

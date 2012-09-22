package com.client.network;

public interface NetworkListener 
{
	void listenToServerMessage(String message);
	void sendMessageToServer(String message);
	public boolean wantSending();
	public void setSentOk();
	public String messageToSend();
}

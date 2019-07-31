package io.epopeia.hsm.command;

import io.epopeia.hsm.HSMIO;

abstract class Command
{
  public abstract String execute(String[] paramArrayOfString)
    throws Exception;
  
  protected String runCmd(String cmd) throws Exception {
		String respMsg = HSMIO.sendHSMCommand(cmd);

		//Log.info("Request: " + reqMsg + " Response: " + respMsg);
		//Log.debug("Request: " + reqMsg + " Response: " + respMsg);

		System.out.println("HSM Response: " + respMsg);
		return respMsg;
		
	}
}

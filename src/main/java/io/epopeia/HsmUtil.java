package io.epopeia;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import io.epopeia.service.HsmService;

@SpringBootApplication
public class HsmUtil implements CommandLineRunner {

	@Autowired
	private HsmService hsm;

	@Autowired
	private ConfigurableApplicationContext ctx;

	public static void main(String... args) {
		SpringApplication.run(HsmUtil.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (hsm == null) {
			ctx.close();
			return;
		}

		if (args.length > 0) {
			hsm.sendBufferCommand(args[0]);
			ctx.close();
			return;
		}

		int iCmd = Integer.MAX_VALUE;
		while (iCmd > 0) {
			System.out.println("########################");
			System.out.println("### HSM Command test ###");
			System.out.println("########################");
			System.out.println("1) cvvGenerate");
			System.out.println("2) cvvValidate");
			System.out.println("3) dekEncode");
			System.out.println("4) pinGenerate");
			System.out.println("5) pinValidate");
			System.out.println("6) performDiagnostics");
			System.out.println("0) ZERO to quit");

			final Console c = System.console();
			final String ret = c.readLine("Enter the command number: ");

			try {
				iCmd = Integer.valueOf(ret);

				switch (iCmd) {
				case 0:
					System.out.println("Exiting.........");
					ctx.close();
					return;
				case 1:
					final String pan1 = c.readLine("Enter the pan: ");
					final int expYear1 = Integer.valueOf(c.readLine("Enter the expYear: "));
					final int expMonth1 = Integer.valueOf(c.readLine("Enter the expMonth: "));
					final String srvCode1 = c.readLine("Enter the srvCode: ");
					final String cvkKey1 = c.readLine("Enter the cvkKey: ");
					hsm.cvvGenerate(pan1, expYear1, expMonth1, srvCode1, cvkKey1);
					break;
				case 2:
					final String pan2 = c.readLine("Enter the pan: ");
					final int expYear2 = Integer.valueOf(c.readLine("Enter the expYear: "));
					final int expMonth2 = Integer.valueOf(c.readLine("Enter the expMonth: "));
					final String srvCode2 = c.readLine("Enter the srvCode: ");
					final String cvkKey2 = c.readLine("Enter the cvkKey: ");
					hsm.cvvValidate(pan2, expYear2, expMonth2, srvCode2, cvkKey2);
					break;
				case 3:
					final String data = c.readLine("Enter the data: ");
					final String dekKey = c.readLine("Enter the dekKey: ");
					hsm.dekEncode(data, dekKey);
					break;
				case 4:
					final String pan4 = c.readLine("Enter the pan: ");
					hsm.pinGenerate(pan4);
					break;
				case 5:
					final String pan5 = c.readLine("Enter the pan: ");
					final String pinblock5 = c.readLine("Enter the srvCode: ");
					final String pinhost5 = c.readLine("Enter the cvkKey: ");
					final String tpk5 = c.readLine("Enter the tpk: ");
					hsm.pinValidate(pan5, pinblock5, pinhost5, tpk5);
					break;
				case 6:
					hsm.performDiagnostic();
					break;

				default:
					System.out.println("Invalid Command");
					break;
				}
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
	}
}

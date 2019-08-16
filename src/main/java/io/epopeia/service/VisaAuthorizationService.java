package io.epopeia.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.IFE_AMOUNT;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.header.BASE1Header;
import org.jpos.iso.packager.Base1Packager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("server")
@Service
public class VisaAuthorizationService implements AuthorizationService {

	private static final Logger LOGGER = LogManager.getLogger(VisaAuthorizationService.class);

	@Autowired
	ThalesHsmService hsmService;

	@Override
	public byte[] handleMessageFromNetwork(byte[] message) {
		try {
			final byte[] header = Arrays.copyOfRange(message, 0, BASE1Header.LENGTH);
			final byte[] iso8583 = Arrays.copyOfRange(message, BASE1Header.LENGTH, message.length);

			// create a header container and read the header
			final BASE1Header h = new BASE1Header(header);
			LOGGER.info(h.formatHeader());

			// create a message container and read message
			ISOMsg m = new ISOMsg();
			m.setDirection(ISOMsg.INCOMING);
			m.setPackager(new CustomBase1Packager());
			m.unpack(iso8583);
			jposDumpToLog4j(m);

			// set response fields in the message
			final int MTI = Integer.parseInt(m.getMTI());
			if (MTI == 200) {
				final ISOMsg mr = ISOMsg.class.cast(m.clone(new int[] { 2, 3, 4, 6, 7, 11, 19, 23, 25, 32, 37, 41, 42,
						48, 49, 51, 54, 55, 63, 102, 103, 104, 117, 121 }));
				mr.setMTI("0210");

				mr.set(38, "123456");
				mr.set(39, "00");

				String pinhost = hsmService.encryptClearPin(m.getString(2), "1234");

				if(hsmService.pinValidate(
						m.getString(2),
						m.getString(52),
						pinhost,
						"")){

					mr.set(39,"55");
				}

				m = mr;

			} else {
				m.setResponseMTI();
				m.set(39, "00");
			}

			m.setDirection(ISOMsg.OUTGOING);
			// logs the response message before pack
			jposDumpToLog4j(m);

			// get the message buffer
			final byte[] mb = m.pack();

			// get the header buffer
			h.setLen(mb.length);
			final byte[] hb = h.pack();

			// concatenate the header and message in a buffer
			final byte[] b = new byte[hb.length + mb.length];
			System.arraycopy(hb, 0, b, 0, hb.length);
			System.arraycopy(mb, 0, b, hb.length, mb.length);

			// send the full buffer
			return b;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static void jposDumpToLog4j(ISOMsg m) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos);
		ps.println();
		m.dump(ps, "");
		LOGGER.info(baos.toString());
	}

	public static class CustomBase1Packager extends Base1Packager {
		public CustomBase1Packager() {
			super();
			base1Fld[28] = new IFE_AMOUNT(9, "AMOUNT, TRANSACTION FEE");
		}
	}
}

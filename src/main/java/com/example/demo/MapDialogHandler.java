package com.example.demo;


import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapDialogHandler implements MAPDialogListener  {
	private static Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
                String methodName = new Object().getClass().getEnclosingMethod().getName();
                LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			AddressString eriMsisdn, AddressString eriVlrNo) {
                String methodName = new Object().getClass().getEnclosingMethod().getName();
                LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
                String methodName = new Object().getClass().getEnclosingMethod().getName();
                LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
                String methodName = new Object().getClass().getEnclosingMethod().getName();
                LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
                String methodName = new Object().getClass().getEnclosingMethod().getName();
                LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, mapDialog);

	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, mapDialog);
	}

	@Override
	public void onDialogRelease(MAPDialog mapDialog) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, mapDialog);
	}

	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, mapDialog);
	}
}

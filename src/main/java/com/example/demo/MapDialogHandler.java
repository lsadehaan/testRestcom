package com.cerebrosphere.smsgw.redis2smpp;

import org.mobicents.protocols.ss7.map.api.*;
import org.mobicents.protocols.ss7.map.api.dialog.*;
import org.mobicents.protocols.ss7.map.api.primitives.*;
import org.mobicents.protocols.ss7.map.api.errors.*;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.*;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
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

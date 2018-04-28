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


public class MapSMSHandler implements MAPServiceSmsListener  {
    private static Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);


	@Override
	public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, forwSmInd);		
		
	}

	@Override
	public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, forwSmRespInd);		
		
	}

	@Override
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, moForwSmInd);		
		
	}

	@Override
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, moForwSmRespInd);		
		
	}

	@Override
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, mtForwSmInd);		
		
	}

	@Override
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, mtForwSmRespInd);		
		
	}

	@Override
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, sendRoutingInfoForSMInd);		
		
	}

	@Override
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, sendRoutingInfoForSMRespInd);		
		
	}

	@Override
	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, reportSMDeliveryStatusInd);		
		
	}

	@Override
	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, reportSMDeliveryStatusRespInd);		
		
	}

	@Override
	public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, informServiceCentreInd);		
		
	}

	@Override
	public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, alertServiceCentreInd);		
		
	}

	@Override
	public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, alertServiceCentreInd);		
		
	}

	@Override
	public void onReadyForSMRequest(ReadyForSMRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, request);		
		
	}

	@Override
	public void onReadyForSMResponse(ReadyForSMResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, response);		
		
	}

	@Override
	public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, request);		
		
	}

	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, mapDialog);
	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, mapDialog);
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, mapDialog);
	}

	@Override
	public void onMAPMessage(MAPMessage mapMessage) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();        
        LOGGER.info("{}: {}", methodName, mapMessage);
	}

}

package com.example.demo;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapSupplementaryHandler implements MAPServiceSupplementaryListener  {
    private static Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

    // Implemet all MAPDialogListener methods and MAPServiceSupplementaryListener methods here
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

        @Override
        public void onRegisterSSRequest(RegisterSSRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, request);
        }

        @Override
        public void onRegisterSSResponse(RegisterSSResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, response);
        }

        @Override
        public void onEraseSSRequest(EraseSSRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, request);

        }

        @Override
        public void onEraseSSResponse(EraseSSResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, response);
        }

        @Override
        public void onActivateSSRequest(ActivateSSRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, request);
    }


        @Override
        public void onActivateSSResponse(ActivateSSResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, response);
    }

        @Override
        public void onDeactivateSSRequest(DeactivateSSRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, request);
        }

        @Override
        public void onDeactivateSSResponse(DeactivateSSResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, response);
        }

        @Override
        public void onInterrogateSSRequest(InterrogateSSRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, request);

        }

        @Override
        public void onInterrogateSSResponse(InterrogateSSResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, response);

        }

        @Override
        public void onGetPasswordRequest(GetPasswordRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, request);

        }

        @Override
        public void onGetPasswordResponse(GetPasswordResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, response);

        }

        @Override
        public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, request);

        }

        @Override
        public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, response);

        }

        @Override
        public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, procUnstrReqInd);

        }

        @Override
        public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, procUnstrResInd);

        }

        @Override
        public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, unstrReqInd);

        }

        @Override
        public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, unstrResInd);

        }

        @Override
        public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, unstrNotifyInd);

        }

        @Override
        public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        String methodName = new Object().getClass().getEnclosingMethod().getName();
        LOGGER.info("{}: {}", methodName, unstrNotifyInd);

        }

}

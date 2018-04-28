package com.example.demo;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.m3ua.impl.AsImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AspImpl;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Ss7Stack {
	private Logger LOGGER = LoggerFactory.getLogger(Ss7Stack.class);

	@Value("${ss7.persistFolder}")
	private String persistFolder;

	public Management sctpManagement;

	public M3UAManagementImpl clientM3UAManagement;

	public SccpStackImpl sccpStack;

	public TCAPStackImpl tcapStack;

	public MAPStackImpl mapStack;

    @PostConstruct
    public void init() {
        LOGGER.info("Init Ss7Stack");
        try {
            sctpManagement = new org.mobicents.protocols.sctp.netty.NettySctpManagementImpl("Client");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sctpManagement.setPersistDir(persistFolder);

        int DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = 5;

		clientM3UAManagement = new M3UAManagementImpl("M3UA_Client", null);
		clientM3UAManagement.setPersistDir(persistFolder);
		clientM3UAManagement.setTransportManagement(sctpManagement);
		try {
			clientM3UAManagement.setDeliveryMessageThreadCount(DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sccpStack = new SccpStackImpl("SccpStack");
		sccpStack.setPersistDir(persistFolder);
		sccpStack.setMtp3UserPart(1, clientM3UAManagement);

        int SSN = 0;
		tcapStack = new TCAPStackImpl("TCAP", sccpStack.getSccpProvider(), SSN);
		tcapStack.setPersistDir(persistFolder);

        mapStack = new MAPStackImpl("MapClient", tcapStack.getProvider());
        MAPProvider mapProvider = mapStack.getMAPProvider();
		mapProvider.addMAPDialogListener(new MapDialogHandler());
        mapProvider.getMAPServiceSupplementary().addMAPServiceListener(new MapSupplementaryHandler());
        mapProvider.getMAPServiceSms().addMAPServiceListener(new MapSMSHandler());
        mapProvider.getMAPServiceSupplementary().acivate();
        LOGGER.info("Init Ss7Stack done");
    }

    public String getState() {

        StringBuilder sb = new StringBuilder();
        sb.append("SCTP: ");
        sctpManagement.getAssociations().forEach(
            (name,assoc) -> {
                sb.append("[");
                sb.append(name);
                sb.append(" : ");
                sb.append(assoc.isConnected() ? "Connected" : "Disconnected");
                sb.append("] ");
            } );
        sb.append("  M3UA:");

        clientM3UAManagement.getAppServers().forEach(
            as -> {
                sb.append(" AS: ");
                sb.append(as.getName());
                AsImpl asImpl = (AsImpl) as;
                FSM lFsm = asImpl.getLocalFSM();
                if (lFsm != null) {
                    sb.append(" localFsm:");
                    sb.append(lFsm.getState().toString());
                }
                FSM pFsm = asImpl.getPeerFSM();
                if (pFsm != null) {
                    sb.append(" peerFsm:");
                    sb.append(pFsm.getState().toString());
                }
                as.getAspList().forEach(
                    asp -> {
                        sb.append(" ASP: ");
                        sb.append(asp.getName());
                        AspImpl aspImpl = (AspImpl) asp;
                        FSM lFsmP = aspImpl.getLocalFSM();
                        FSM pFsmP = aspImpl.getPeerFSM();
                        if (lFsmP != null) {
                            sb.append(" lFsmP:");
                            sb.append(lFsmP.getState().toString());
                        }
                        if (pFsmP != null) {
                            sb.append(" pFsmP:");
                            sb.append(pFsmP.getState().toString());
                        }
                    }
                );
            }
        );

        return sb.toString();
    }

    public void aspStart() {
        LOGGER.info("Ss7Stack aspStart");

        clientM3UAManagement.getAppServers().forEach(
            as -> {
                as.getAspList().forEach(
                    asp -> {
                        String name = asp.getName();
                        LOGGER.info("Ss7Stack starting ASP: {}", name);

                        try {
							clientM3UAManagement.startAsp(name);
						} catch (Exception e) {
                            LOGGER.info("Error starting Asp: {}", name);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                );
            }
        );
        LOGGER.info("Ss7Stack aspStart done");

    }


    public void stop()
    {
        mapStack.stop();
        tcapStack.stop();
        sccpStack.stop();
        try {
			clientM3UAManagement.stop();
            sctpManagement.stop();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void start()
    {
        LOGGER.info("Ss7Stack start");
        try {
			sctpManagement.start();
            sctpManagement.setConnectDelay(10000);
            clientM3UAManagement.start();
            sccpStack.start();
            tcapStack.start();
            mapStack.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LOGGER.info("Ss7Stack start done");


        /* ------------
        String CLIENT_IP = "localhost";
        int CLIENT_PORT = 8080;
        String SERVER_IP = "localhost";
        int SERVER_PORT = 8080;
        String SERVER_NAME = "test";
        String CLIENT_ASSOCIATION_NAME = "CLIENT_ASS";
        String SERVER_ASSOCIATION_NAME = "SERVER_ASS";

        IpChannelType ipChannelType = IpChannelType.SCTP;

        // client
        sctpManagement.addAssociation(CLIENT_IP, CLIENT_PORT, SERVER_IP, SERVER_PORT, CLIENT_ASSOCIATION_NAME, ipChannelType, null);
        // server
        sctpManagement.addServerAssociation(CLIENT_IP, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);

        sctpManagement.addServer(SERVER_NAME, SERVER_IP, SERVER_PORT, ipChannelType, null);
        sctpManagement.addServerAssociation(CLIENT_IP, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);
        sctpManagement.startServer(SERVER_NAME);
        // ------------ */

        // m3ua



        /*------------
        ParameterFactoryImpl factory = new ParameterFactoryImpl();
		RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
		TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
		clientM3UAMgmt.createAs("AS1", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, null);

		// Step 2 : Create ASP
		clientM3UAMgmt.createAspFactory("ASP1", CLIENT_ASSOCIATION_NAME);

		// Step3 : Assign ASP to AS
		Asp asp = clientM3UAMgmt.assignAspToAs("AS1", "ASP1");

        // Step 4: Add Route. Remote point code is 2
        int SERVER_SPC = 0;
		clientM3UAMgmt.addRoute(SERVER_SPC, -1, -1, "AS1");

        //-----------*/


        /* manual config
        //** SCTP SERVER **
        sctpManagement.removeAllResourses();
        boolean acceptAnonymousConnections = false;
        int maxConcurrentConnectionsCount = 0; // for AnonymousConnections, 0 means unlimited
        String[] extraHostAddresses = null; // for multi-homing
        IpChannelType ipChannelType = IpChannelType.SCTP;

        sctpManagement.addServer(SERVER_NAME, SERVER_IP, SERVER_PORT, ipChannelType, acceptAnonymousConnections, maxConcurrentConnectionsCount, extraHostAddresses);
        sctpManagement.addServerAssociation(PEER_IP, PEER_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);
        sctpManagement.startServer(SERVER_NAME);

        //** SCTP CLIENT **
        String[] extraHostAddresses = null; // for multi-homing
        IpChannelType ipChannelType = IpChannelType.SCTP;

        sctpManagement.addAssociation(HOST_IP, HOST_PORT, PEER_IP, PEER_PORT, CLIENT_ASSOCIATION_NAME, ipChannelType, extraHostAddresses);

        //** M3AU
        clientM3UAMgmt.removeAllResourses();
        ParameterFactoryImpl factory = new ParameterFactoryImpl();

        // Step 1 : Create App Server
        RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        As as = clientM3UAMgmt.createAs("AS1", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, null);

        // Step 2 : Create ASP
        AspFactory aspFactor = clientM3UAMgmt.createAspFactory("ASP1", CLIENT_ASSOCIATION_NAME);

        // Step3 : Assign ASP to AS
        Asp asp = clientM3UAMgmt.assignAspToAs("AS1", "ASP1");

        // Step 4: Add Route. Remote point code is 2
        clientM3UAMgmt.addRoute(SERVET_SPC, -1, -1, "AS1");

        =========
        sccpStack.getSccpResource().addRemoteSpc(0, SERVET_SPC, 0, 0);
        sccpStack.getSccpResource().addRemoteSsn(0, SERVET_SPC, SSN, 0, false);

        sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR, 0);
        sccpStack.getRouter().addMtp3Destination(1, 1, SERVET_SPC, SERVET_SPC, 0, 255, 255);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, CLIENT_SPC, 0);
        sccpStack.getRouter().addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, SERVET_SPC, 0);
        sccpStack.getRouter().addRoutingAddress(2, remoteAddress);

        GlobalTitle gt = fact.createGlobalTitle("*", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        sccpStack.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern, "K", 1, -1, null, 0);
        sccpStack.getRouter().addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 2, -1, null, 0);

        */
    }




    public String sendSri(String destIsdnNumber, String serviceCentreAddr) {

        MAPProvider mapProvider = this.mapStack.getMAPProvider();

        MAPApplicationContextVersion vers = MAPApplicationContextVersion.version3;

        MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext, vers);

        MAPParameterFactory paramFactory = mapProvider.getMAPParameterFactory();
        ISDNAddressString msisdn = paramFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, destIsdnNumber);
        AddressString serviceCentreAddress = paramFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, serviceCentreAddr);

        try {
            SccpAddress origAddress = sccpStack.getRouter().getRoutingAddress(1);
            SccpAddress destAddress = createSccpAddress(destIsdnNumber, 6);
            MAPDialogSms curDialog = mapProvider.getMAPServiceSms().createNewDialog(mapAppContext,
                origAddress, null,
                destAddress, null);

            curDialog.setNetworkId(0);
            curDialog.addSendRoutingInfoForSMRequest(msisdn, true, serviceCentreAddress, null, false, null, null, null,
                    false, null, false, false, null);
            // this cap helps us give SCCP error if any
            curDialog.setReturnMessageOnError(true);
            curDialog.send();

            return "SendRoutingInfoForSMRequest has been sent";
        } catch (MAPException ex) {
            return "Exception when sending SendRoutingInfoForSMRequest: " + ex.toString();
        }
    }


    private SccpAddress createSccpAddress(String address, int SSN) {
        ParameterFactory parameterFactory = this.sccpStack.getSccpProvider().getParameterFactory();
        GlobalTitle gt = parameterFactory.createGlobalTitle(address,
                                        0,
                                        org.mobicents.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                                        null,
                                        NatureOfAddress.INTERNATIONAL);
        return parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, SSN);
    }

}

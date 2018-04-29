package com.example.demo;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.AsImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AspImpl;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
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
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
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

    public M3UAManagementImpl m3uaMgmt;

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    public Association assoc;
    public Association assoc2;
    private As localAs;
    private As localAs2;
    private AspFactory localAspFactory;
    private AspFactory localAspFactory2;
    private Asp localAsp;
    private Asp localAsp2;



	public SccpStackImpl sccpStack;

	public TCAPStackImpl tcapStack;

	public MAPStackImpl mapStack;

    @PostConstruct
    public void init() {
        LOGGER.info("Init Ss7Stack");

        int DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = 5;

        try {
			initM3ua("10.0.0.6", 2064, "192.168.212.7", 2064, "10.0.0.6", 2066, "192.168.213.7", 2066, IpChannelType.SCTP, null, persistFolder, 2, RoutingLabelFormat.ITU);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sccpStack = new SccpStackImpl("SccpStack");
		sccpStack.setPersistDir(persistFolder);
		sccpStack.setMtp3UserPart(1, m3uaMgmt);

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

        m3uaMgmt.getAppServers().forEach(
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

        m3uaMgmt.getAppServers().forEach(
            as -> {
                as.getAspList().forEach(
                    asp -> {
                        String name = asp.getName();
                        LOGGER.info("Ss7Stack starting ASP: {}", name);

                        try {
							m3uaMgmt.startAsp(name);
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
			m3uaMgmt.stop();
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
            m3uaMgmt.start();
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

    private void initM3ua(String localHost, int localPort, String remoteHost, int remotePort, String localHost2,
            int localPort2, String remoteHost2, int remotePort2, IpChannelType ipChannelType, String[] extraHostAddresses, String persistDir,
            int trafficModeTypeInt, RoutingLabelFormat routingLabelFormat) throws Exception {

        this.stopM3ua();

        String name = "main";

        // init SCTP stack
        this.sctpManagement = new NettySctpManagementImpl("SimSCTPServer_" + name);
        // set 8 threads for delivering messages
        this.sctpManagement.setPersistDir(persistDir);
        this.sctpManagement.setWorkerThreads(8);
        this.sctpManagement.setSingleThread(false);

        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.removeAllResourses();
        Thread.sleep(500); // waiting for freeing ip ports

        // init M3UA stack+
        this.m3uaMgmt = new M3UAManagementImpl("SimM3uaServer_" + name, "pname");
        this.m3uaMgmt.setPersistDir(persistDir);
        this.m3uaMgmt.setTransportManagement(this.sctpManagement);
        this.m3uaMgmt.setRoutingLabelFormat(routingLabelFormat);

        this.m3uaMgmt.start();
        this.m3uaMgmt.removeAllResourses();

        // configure SCTP stack
        String SERVER_NAME = "Server_" + name;
        String SERVER_NAME_2 = "Server_" + name + "_2";
        String SERVER_ASSOCIATION_NAME = "ServerAss_" + name;
        String SERVER_ASSOCIATION_NAME_2 = "ServerAss_" + name + "_2";
        String ASSOCIATION_NAME = "Ass_" + name;
        String ASSOCIATION_NAME_2 = "Ass_" + name + "_2";
        String assName;
        String assName2 = null;

        // String localHost2, int localPort2, String remoteHost2, int remotePort2

        // 1. Create SCTP Server
        sctpManagement.addServer(SERVER_NAME, localHost, localPort, ipChannelType, extraHostAddresses);

        // 2. Create SCTP Server Association
        sctpManagement.addServerAssociation(remoteHost, remotePort, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);
        this.assoc = sctpManagement.getAssociation(SERVER_ASSOCIATION_NAME);
        assName = SERVER_ASSOCIATION_NAME;

        // 3. Start Server
        sctpManagement.startServer(SERVER_NAME);

        if (localHost2 != null && !localHost2.equals("") && remoteHost2 != null && !remoteHost2.equals("")) {
            // a second link is defined

            // 1. Create SCTP Server
            sctpManagement.addServer(SERVER_NAME_2, localHost2, localPort2, ipChannelType, null);

            // 2. Create SCTP Server Association
            sctpManagement.addServerAssociation(remoteHost2, remotePort2, SERVER_NAME_2, SERVER_ASSOCIATION_NAME_2, ipChannelType);
            this.assoc2 = sctpManagement.getAssociation(SERVER_ASSOCIATION_NAME_2);
            assName2 = SERVER_ASSOCIATION_NAME_2;

            // 3. Start Server
            sctpManagement.startServer(SERVER_NAME_2);
        }

        // configure M3UA stack
        // 1. Create AS
        RoutingContext rc = factory.createRoutingContext(new long[] { 0 });
        TrafficModeType trafficModeType = factory.createTrafficModeType(trafficModeTypeInt);
        NetworkAppearance na = null;

        localAs = m3uaMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);
        // 2. Create ASP
        localAspFactory = m3uaMgmt.createAspFactory("testasp", assName);
        // 3. Assign ASP to AS
        localAsp = m3uaMgmt.assignAspToAs("testas", "testasp");

        // 4. Define Route
        // Define Route
        m3uaMgmt.addRoute(3600, 3232, -1, "testas");

        // 2. Start ASP
        m3uaMgmt.startAsp("testasp");

        // 1. Create AS
        localAs2 = m3uaMgmt.createAs("testas2", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);
        // 2. Create ASP
        localAspFactory = m3uaMgmt.createAspFactory("testasp2", assName2);
        // 3. Assign ASP to AS
        localAsp = m3uaMgmt.assignAspToAs("testas2", "testasp2");

        // 4. Define Route
        // Define Route
        m3uaMgmt.addRoute(3700, 3232, -1, "testas2");

        // 2. Start ASP
        m3uaMgmt.startAsp("testasp2");

    }

    private void stopM3ua() throws Exception {
        if (this.m3uaMgmt != null) {
            this.m3uaMgmt.stop();
            this.m3uaMgmt = null;
        }
        if (this.sctpManagement != null) {
            this.sctpManagement.stop();
            this.sctpManagement = null;
        }
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

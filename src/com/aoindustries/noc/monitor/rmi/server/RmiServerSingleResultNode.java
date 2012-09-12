/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.rmi.server;

import com.aoindustries.noc.monitor.common.SingleResultNode;
import com.aoindustries.noc.monitor.wrapper.WrappedSingleResultNode;
import java.rmi.RemoteException;

/**
 * @author  AO Industries, Inc.
 */
public class RmiServerSingleResultNode extends WrappedSingleResultNode {

    RmiServerSingleResultNode(RmiServerMonitor monitor, SingleResultNode wrapped) throws RemoteException {
        super(monitor, wrapped);
        monitor.exportObject(this);
    }
}

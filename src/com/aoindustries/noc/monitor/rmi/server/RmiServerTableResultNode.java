/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.rmi.server;

import com.aoindustries.noc.monitor.common.TableResultNode;
import com.aoindustries.noc.monitor.wrapper.WrappedTableResultNode;
import java.rmi.RemoteException;

/**
 * @author  AO Industries, Inc.
 */
public class RmiServerTableResultNode extends WrappedTableResultNode {

    RmiServerTableResultNode(RmiServerMonitor monitor, TableResultNode wrapped) throws RemoteException {
        super(monitor, wrapped);
        monitor.exportObject(this);
    }
}

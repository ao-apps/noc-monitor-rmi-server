/*
 * Copyright 2012, 2020 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.rmi.server;

import com.aoindustries.noc.monitor.common.Node;
import com.aoindustries.noc.monitor.wrapper.WrappedNode;
import java.rmi.RemoteException;

/**
 * @author  AO Industries, Inc.
 */
public class RmiServerNode extends WrappedNode {

	RmiServerNode(RmiServerMonitor monitor, Node wrapped) throws RemoteException {
		super(monitor, wrapped);
		monitor.exportObject(this);
	}
}

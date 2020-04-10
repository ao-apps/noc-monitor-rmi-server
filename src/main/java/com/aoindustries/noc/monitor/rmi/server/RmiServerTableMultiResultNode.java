/*
 * Copyright 2012, 2020 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.rmi.server;

import com.aoindustries.noc.monitor.common.TableMultiResult;
import com.aoindustries.noc.monitor.common.TableMultiResultNode;
import com.aoindustries.noc.monitor.wrapper.WrappedTableMultiResultNode;
import java.rmi.RemoteException;

/**
 * @author  AO Industries, Inc.
 */
public class RmiServerTableMultiResultNode<R extends TableMultiResult> extends WrappedTableMultiResultNode<R> {

	RmiServerTableMultiResultNode(RmiServerMonitor monitor, TableMultiResultNode<R> wrapped) throws RemoteException {
		super(monitor, wrapped);
		monitor.exportObject(this);
	}
}

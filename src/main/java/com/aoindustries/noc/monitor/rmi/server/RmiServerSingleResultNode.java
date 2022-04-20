/*
 * noc-monitor-rmi-server - RMI Server for Network Operations Center Monitoring.
 * Copyright (C) 2012, 2020, 2022  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of noc-monitor-rmi-server.
 *
 * noc-monitor-rmi-server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * noc-monitor-rmi-server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with noc-monitor-rmi-server.  If not, see <https://www.gnu.org/licenses/>.
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

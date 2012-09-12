/*
 * Copyright 2012 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.noc.monitor.rmi.server;

import com.aoindustries.lang.ObjectUtils;
import com.aoindustries.noc.monitor.common.Monitor;
import com.aoindustries.noc.monitor.common.Node;
import com.aoindustries.noc.monitor.common.RootNode;
import com.aoindustries.noc.monitor.common.SingleResultNode;
import com.aoindustries.noc.monitor.common.TableMultiResult;
import com.aoindustries.noc.monitor.common.TableMultiResultNode;
import com.aoindustries.noc.monitor.common.TableResultNode;
import com.aoindustries.noc.monitor.wrapper.WrappedMonitor;
import com.aoindustries.rmi.RMIClientSocketFactorySSL;
import com.aoindustries.rmi.RMIServerSocketFactorySSL;
import com.aoindustries.rmi.RegistryManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * The RMI server for wrapping and exposing monitors to the network.
 *
 * Exports the monitor and all nodes.  The wrapped monitor is not exported directly,
 * but rather this wrapper of it is exported.
 *
 * @author  AO Industries, Inc.
 */
public class RmiServerMonitor extends WrappedMonitor {

    private static class CacheKey {

        private final Monitor wrapped;
        private final String publicAddress;
        private final String listenAddress;
        private final int port;

        private CacheKey(Monitor wrapped, String publicAddress, String listenAddress, int port) {
            this.wrapped = wrapped;
            this.publicAddress = publicAddress;
            this.listenAddress = listenAddress;
            this.port = port;
        }

        @Override
        public boolean equals(Object O) {
            if(O==null) return false;
            if(!(O instanceof CacheKey)) return false;
            CacheKey other = (CacheKey)O;
            return
                port==other.port
                && wrapped==other.wrapped
                && ObjectUtils.equals(publicAddress, other.publicAddress)
                && ObjectUtils.equals(listenAddress, other.listenAddress)
            ;
        }

        @Override
        public int hashCode() {
            return
                System.identityHashCode(wrapped)
                ^ (ObjectUtils.hashCode(publicAddress)*7)
                ^ (ObjectUtils.hashCode(listenAddress)*11)
                ^ (port*13)
            ;
        }
    }

    private static final Map<CacheKey,RmiServerMonitor> cache = new HashMap<CacheKey,RmiServerMonitor>();

    /**
     * One unique RmiServerMonitor is created for each set addresses, port, and monitor (by identity equals).
     */
    public static RmiServerMonitor getInstance(Monitor wrapped, String publicAddress, String listenAddress, int port) throws RemoteException {
        // Don't double-wrap server with same values
        if(wrapped instanceof RmiServerMonitor) {
            RmiServerMonitor wrapper = (RmiServerMonitor)wrapped;
            if(
                ObjectUtils.equals(publicAddress, wrapper.publicAddress)
                && ObjectUtils.equals(listenAddress, wrapper.listenAddress)
                && port==wrapper.port
            ) return wrapper;
        }
        CacheKey key = new CacheKey(wrapped, publicAddress, listenAddress, port);
        synchronized(cache) {
            RmiServerMonitor server = cache.get(key);
            if(server==null) {
                server = new RmiServerMonitor(wrapped, publicAddress, listenAddress, port);
                cache.put(key, server);
            }
            return server;
        }
    }

    private final String publicAddress;
    private final String listenAddress;
    final int port;
    final RMIClientSocketFactory csf;
    final RMIServerSocketFactory ssf;

    private RmiServerMonitor(Monitor wrapped, String publicAddress, String listenAddress, int port) throws RemoteException {
        super(wrapped);
        // Setup the RMI system properties
        if(publicAddress!=null && publicAddress.length()>0) {
            System.setProperty("java.rmi.server.hostname", publicAddress);
        } else if(listenAddress!=null && listenAddress.length()>0) {
            System.setProperty("java.rmi.server.hostname", listenAddress);
        } else {
            System.clearProperty("java.rmi.server.hostname");
        }
        System.setProperty("java.rmi.server.randomIDs", "true");
        System.setProperty("java.rmi.server.useCodebaseOnly", "true");
        System.clearProperty("java.rmi.server.codebase");
        System.setProperty("java.rmi.server.disableHttp", "true");
        // System.setProperty("sun.rmi.server.suppressStackTraces", "true");

        // RMI socket factories
        if(listenAddress!=null && listenAddress.length()>0) {
            csf = new RMIClientSocketFactorySSL(listenAddress); // csf = new RMIClientSocketFactorySSL();
            ssf = new RMIServerSocketFactorySSL(listenAddress);
        } else {
            csf = new RMIClientSocketFactorySSL();
            ssf = new RMIServerSocketFactorySSL();
        }

        this.publicAddress = publicAddress;
        this.listenAddress = listenAddress;
        this.port = port;

        exportObject(this, Monitor.class.getName()+"_Stub");
    }

    /**
     * Creates the local registry if not yet created, then exports the object.
     */
    final Remote exportObject(Remote obj) throws RemoteException {
        return exportObject(obj, null);
    }

    /**
     * Creates the local registry if not yet created, then exports the object.
     */
    final Remote exportObject(Remote obj, String name) throws RemoteException {
        Registry registry = RegistryManager.createRegistry(port, csf, ssf);
        Remote stub = UnicastRemoteObject.exportObject(obj, port, csf, ssf);
        if(name!=null) registry.rebind(name, stub);
        return stub;
    }

    @Override
    protected RmiServerNode newWrappedNode(Node node) throws RemoteException {
        return new RmiServerNode(this, node);
    }

    @Override
    protected RmiServerRootNode newWrappedRootNode(RootNode node) throws RemoteException {
        return new RmiServerRootNode(this, node);
    }

    @Override
    protected RmiServerSingleResultNode newWrappedSingleResultNode(SingleResultNode node) throws RemoteException {
        return new RmiServerSingleResultNode(this, node);
    }

    @Override
    protected <R extends TableMultiResult> RmiServerTableMultiResultNode<R> newWrappedTableMultiResultNode(TableMultiResultNode<R> node) throws RemoteException {
        return new RmiServerTableMultiResultNode<R>(this, node);
    }

    @Override
    protected RmiServerTableResultNode newWrappedTableResultNode(TableResultNode node) throws RemoteException {
        return new RmiServerTableResultNode(this, node);
    }
}

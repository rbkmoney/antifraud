package com.rbkmoney.antifraud.service;

import com.rbkmoney.damsel.proxy_inspector.InspectorProxySrv;
import com.rbkmoney.woody.thrift.impl.http.THServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * Created by vpankrashkin on 24.07.17.
 */

@WebServlet("/inspector")
public class AntifraudServlet extends GenericServlet {

    private Servlet thriftServlet;

    @Autowired
    private InspectorProxySrv.Iface requestHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        thriftServlet = new THServiceBuilder()
                .build(InspectorProxySrv.Iface.class, requestHandler);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        thriftServlet.service(req, res);
    }
}

/*
 * Copyright 2014 Y12STUDIO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bkbc.tool.jetty.workshop;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.Resource;

public class JettyCrosServer {

	Server server;
	
	public JettyCrosServer() throws Exception {
		server = new Server(8080);
		HandlerList handlers = new HandlerList();
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[] { "index.html" });
        resource_handler.setBaseResource(Resource.newClassPathResource("web"));
		
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setInitParameter("maxInterval", "1000");
        context.setInitParameter("logLevel", "2");
                
        handlers.setHandlers(new Handler[] { resource_handler, context});
        
        server.setHandler(handlers);
        
        
        FilterHolder filterHolder = new FilterHolder(new CrossOriginFilter());
        filterHolder.getInitParameters().put(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        context.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));
        
        context.addServlet(new ServletHolder(new JsonServlet()), "/t.json");
        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        
	}

	public static void main(String[] args) {
		
		try {
			new JettyCrosServer().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() throws Exception {
		server.start();
		server.join();
		
	}

}

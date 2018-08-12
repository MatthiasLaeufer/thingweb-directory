/********************************************************************************
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 * 
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the W3C Software Notice and
 * Document License (2015-05-13) which is available at
 * https://www.w3.org/Consortium/Legal/2015/copyright-software-and-document.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR W3C-20150513
 ********************************************************************************/
package org.eclipse.thingweb.directory.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.thingweb.directory.ResourceManager;
import org.eclipse.thingweb.directory.ResourceManagerFactory;

/**
 * .
 *
 * @author Victor Charpenay
 * @creation 08.08.2018
 *
 */
@WebServlet(
	name="RegistrationHandle",
	urlPatterns={"/td/*", "/vocab/*"},
	description=".",
	loadOnStartup=1
)
public class RegistrationHandleServlet extends HttpServlet {
	
	private static final long serialVersionUID = -6067916068657744771L;

	public final static String DEFAULT_MEDIA_TYPE = "application/ld+json";
	
	private final ResourceManager manager = ResourceManagerFactory.get("td");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = getItemID(req);
		
		String contentType = req.getHeader("Accept");
		if (contentType == null) contentType = DEFAULT_MEDIA_TYPE;
		
		Map<String, String> parameters = new HashMap<>();
		// TODO
		
		resp.setContentType(contentType);
		manager.get(id, resp.getOutputStream(), contentType, parameters);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = getItemID(req);
		
		String contentType = req.getHeader("Content-Type");
		if (contentType == null) contentType = DEFAULT_MEDIA_TYPE;
		
		Map<String, String> parameters = new HashMap<>();
		// TODO
		
		manager.replace(id, req.getInputStream(), contentType, parameters);
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		manager.delete(getItemID(req));
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	
	/**
	 * Assumes resource name is a URL-encoded URI
	 */
	private String getItemID(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String id = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
		try {
			return URLDecoder.decode(id, "UTF-8");
		} catch (UnsupportedEncodingException e) {
//			log.warn("Cannot URL-decode resource identifier: " + id, e);
			return null;
		}
	}
	
}
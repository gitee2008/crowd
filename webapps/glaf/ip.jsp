<%@page import="java.util.Enumeration"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.glaf.core.util.RequestUtils"%>
<%
	String ipAddr = RequestUtils.getIPAddress(request);
	out.println(ipAddr);
%>
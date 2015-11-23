<%@page import="tdt.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!private tdt.Main main = new tdt.Main();%>
<%!public void jspInit() {
		System.out.println("jspInit() is called.");
	}

	public void jspDestroy() {
		System.out.println("jspDestroy() is called. ");
	}%>

<%
	main.initialize();
	if (request.getMethod().equalsIgnoreCase("POST")) {
		main.doPost(request, response);
	} else if (request.getMethod().equalsIgnoreCase("GET")) {
		main.doGet(request, response);
	}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>tmp1</title>
</head>
<body>
</body>
</html>
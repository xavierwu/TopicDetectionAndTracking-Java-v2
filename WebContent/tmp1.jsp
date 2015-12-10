<%@page import="tdt.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!private static String dataFilesDir = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/stemData_4076/";
	private static String glossaryFile = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/4076_glossary.dat";
	private static String tfidfFile = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/4076_tfidf.dat";
	private static String matrixFile = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/4076_matrix.dat";
	private static String ansFile = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/answer4076.txt";
	private tdt.Main main = new tdt.Main(dataFilesDir, glossaryFile, tfidfFile, matrixFile, ansFile);%>

<%!public void jspInit() {
		System.out.println("jspInit() is called."   );
	}

	public void jspDestroy() {
		System.out.println("jspDestroy() is called. ");
	}%>

<%
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
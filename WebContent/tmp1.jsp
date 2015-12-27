<%@page import="tdt.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%!
	private String DATASET_DIR = "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/";

	private String STEMMED_DATASET_DIR_4076 = DATASET_DIR + "stemData_4076/";
	private String GLOSSARY_FILE_4076 = DATASET_DIR + "4076_glossary.dat";
	private String TFIDF_FILE_4076 = DATASET_DIR + "4076_tfidf.dat";
	private String MATRIX_FILE_4076 = DATASET_DIR + "4076_matrix.dat";
	private String ANSWER_FILE_4076 = DATASET_DIR + "answer4076.txt";

	private String STEMMED_DATASET_DIR_1403 = DATASET_DIR + "stemData_1403/";
	private String GLOSSARY_FILE_1403 = DATASET_DIR + "1403_glossary.dat";
	private String TFIDF_FILE_1403 = DATASET_DIR + "1403_tfidf.dat";
	private String MATRIX_FILE_1403 = DATASET_DIR + "1403_matrix.dat";
	private String ANSWER_FILE_1403 = DATASET_DIR + "answer1403.txt";

	private tdt.Main main = new tdt.Main(STEMMED_DATASET_DIR_1403, 
			GLOSSARY_FILE_1403, TFIDF_FILE_1403,
			MATRIX_FILE_1403, ANSWER_FILE_1403);
%>

<%!
	public void jspInit() {
		System.out.println("jspInit() is called.\n");
	}
	
	public void jspDestroy() {
		System.out.println("jspDestroy() is called. ");
	}
%>

<%
	if (request.getMethod().equalsIgnoreCase("POST")) {
		main.doPost(request, response);
	} else if (request.getMethod().equalsIgnoreCase("GET")) {
		main.doGet(request, response);
	}
%>


<!-- 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>tmp1</title>
</head>
<body>
</body>
</html> 
-->

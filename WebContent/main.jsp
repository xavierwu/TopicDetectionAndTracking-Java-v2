<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="tdt.*"%>
<%@page import="servlet.*"%>

<%!Vector<tdt.Story> corpus = tdt.Main.getCorpus("D:/Jee_workspace/TopicDetectionAndTracking/Dataset/");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TDT</title>
</head>
<body>
	<script type="text/javascript">
		function something() {
			document.getElementById("input").submit();
		}
	</script>

	<!-- 	<form name="input" action="./test.do" method="post">
		Something: <input type="submit" name="something" value="Something" />
	</form> -->

	<center>
		<h1>Topic Detection And Tracking</h1>
	</center>

	<div>select start time</div>

	<div class="input-wrapper">
		<input type="date" name="start-date" required> <input
			type="time" name="start-time" required>
	</div>

	<div>select end time</div>

	<div class="input-wrapper">
		<input type="date" name="end-date" required> <input
			type="time" name="end-time" required>
	</div>


	<form id="form1" name="form1" method="post" action="./test.do">
		<table width="100%" border="0">
			<tbody>
				<tr>
					<td width="50%">

						<table width="100%" border="1">
							<thead>
								<tr>
									<th class="header-time">
										<div id="time-btn">
											<a href="time" onclick="">Time</a>
										</div>
									</th>
									<th class="header-source">
										<div id="source-btn">
											<a href="source" onclick="">Source</a>
										</div>
									</th>
									<th class="header-content">
										<div id="content-btn">
											<a href="content" onclick="">Content</a>
										</div>
									</th>
								</tr>
							</thead>

							<tbody>
								<!-- 							<tr>
								<td>241</td>
								<td>26.9%</td>
								<td>What does the fox say?</td>
							</tr> -->

								<%
									if (null == corpus) {
								%>
								<tr>
									<td colspan="3">No info</td>
								</tr>
								<%
									} else {
										for (Story story : corpus) {
								%>
								<tr>
									<td><%=story.getTimeStamp()%></td>
									<td><%=story.getTopicID()%></td>
									<td><%=story.getWord(0)%></td>
								</tr>
								<%
									}
									}
								%>
							</tbody>
						</table>
					</td>

					<td width="50%">
						<table width="100%" border="1">
							<thead>
								<tr>
									<th class="header-time">
										<div id="time-btn">
											<a href="time" onclick="">Time</a>
										</div>
									</th>
									<th class="header-source">
										<div id="source-btn">
											<a href="source" onclick="">Source</a>
										</div>
									</th>
									<th class="header-content">
										<div id="content-btn">
											<a href="content" onclick="">Content</a>
										</div>
									</th>
								</tr>
							</thead>

							<tbody>
								<tr>
									<td>241</td>
									<td>26.9%</td>
									<td>What does the fox say?</td>
								</tr>
							</tbody>


						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Part 2</title>
</head>

<body>
	<h1>Pick Keyword(s)</h1>
	<!-- 	<form>
		<input type="checkbox" name="keyword1" /> keyword 1 <input
			type="checkbox" name="keyword2" /> keyword 2 <input type="checkbox"
			name="keyword3" /> keyword 3 <input type="checkbox" name="keyword4" />
		keyword 4 <br /> <input type="button" name="submit" value="submit" />
	</form>
	<ul>
		<li><a href="http://www.baidu.com" target="_blank">story 1</a></li>
		<li><a href="http://www.baidu.com" target="_blank">story 2</a></li>
		<li><a href="http://www.baidu.com" target="_blank">story 3</a></li>
		<li><a href="http://www.baidu.com" target="_blank">story 4</a></li>
		<li><a href="http://www.baidu.com" target="_blank">story 5</a></li>
	</ul>
	<script type="text/javascript">
		document.write("Hello World!")
	</script> -->


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
							<tr>
								<td>241</td>
								<td>26.9%</td>
								<td>What does the fox say?</td>
							</tr>
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

</body>

</html>
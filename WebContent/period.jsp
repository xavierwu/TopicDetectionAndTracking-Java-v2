<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>TODO supply a title</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<style>
body {
	overflow-y: scroll;
}

a {
	color: #0088cc;
}

a:hover {
	color: #005580;
}

.form-signin {
	max-width: 300px;
	padding: 19px 29px 29px;
	margin: 0 auto 20px;
	background-color: #fff;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
}

.block-center {
	display: block;
	margin-left: auto;
	margin-right: auto;
}

.message {
	border: 1px solid #e6db55;
	background-color: #ffffe0;
	padding: 15px 30px 15px 15px;
	margin: 20px 0;
}

.navbar-extra {
	border-radius: 0px;
	margin-bottom: 30px;
}

#chat-link {
	position: fixed;
	right: 5px;
	bottom: 10px;
	background: #fff;
	border: 1px solid #e0e0e0;
	padding: 5px 15px 10px 15px;
	-webkit-border-radius: 5px 0 0 5px;
	-moz-border-radius: 5px 0 0 5px;
	-ms-border-radius: 5px 0 0 5px;
	-o-border-radius: 5px 0 0 5px;
	border-radius: 5px 0 0 5px;
	-webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	color: #979faf;
}

.glyphicon-envelope {
	text-align: center;
	line-height: 26px;
	margin-right: 5px;
	position: relative;
}

[ng-cloak] {
	display: none;
}

.editor {
	font-size: 14px !important;
	border: 1px solid #e0e0e0;
	height: 475px;
}

.ac {
	display: inline-block;
	height: 16px;
	width: 16px;
	background-image: url('/static/images/correct.png');
	background-repeat: no-repeat;
}

.notac {
	display: inline-block;
	height: 16px;
	width: 16px;
	background-image: url('/static/images/question.png');
	background-repeat: no-repeat;
}

ul.nav li.dropdown:hover>ul.dropdown-menu {
	display: block;
}
</style>

<style>
.header-time {
	width: 5%;
}

.header-source {
	width: 5%;
}

.header-content {
	width: 60%;
}

#goTop {
	padding: 10px 15px 10px 15px;
	position: fixed;
	right: 10px;
	bottom: 40%;
	background: #fff;
	border: 1px solid #e0e0e0;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	-ms-border-radius: 5px;
	-o-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
	color: #979faf;
}
</style>


</head>

<body>
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
					<table id="all-story-list" width="100%" border="1">
						<thead>
							<tr>
								<th class="header-time">
									<div id="time-btn">
										<a href="time" onclick="">time</a>
									</div>
								</th>
								<th class="header-source">
									<div id="source-btn">
										<a href="source" onclick="">source</a>
									</div>
								</th>
								<th class="header-content">
									<div id="content-btn">
										<a href="content" onclick="">content</a>
									</div>
								</th>
							</tr>
						</thead>

						<tbody>
							<tr>
								<td>241</td>
								<td>26.9%</td>
								<td><a href="/problems/different-ways-to-add-parentheses/">Different
										Ways to Add Parentheses</a></td>
							</tr>
						</tbody>

					</table>
				</td>

				<td width="50%">
					<table id="new-story-list" width="100%" border="1">
						<thead>
							<tr>
								<th class="header-time">
									<div id="time-btn">
										<a href="time" onclick="">time</a>
									</div>
								</th>
								<th class="header-source">
									<div id="source-btn">
										<a href="source" onclick="">source</a>
									</div>
								</th>
								<th class="header-content">
									<div id="content-btn">
										<a href="content" onclick="">content</a>
									</div>
								</th>
							</tr>
						</thead>

						<tbody>
							<tr>
								<td>241</td>
								<td>26.9%</td>
								<td><a href="/problems/different-ways-to-add-parentheses/">Different
										Ways to Add Parentheses</a></td>
							</tr>
						</tbody>

					</table>
				</td>
			</tr>
		</tbody>
	</table>



</body>
</html>
<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>Signin</title>

<!-- Bootstrap core CSS -->
<link href="/wc/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="/wc/cms/css/signin.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<c:import url="../header.jsp" />

	<div class="container">

		<c:url value="/login" var="loginUrl" />
		<form class="form-signin" action="${loginUrl}" method="post">
			<c:if test="${error != null}">
				<div class="alert alert-danger" role="alert">${error}</div>
			</c:if>
			<h2 class="form-signin-heading">Please sign in</h2>

			<label for="inputEmail" class="sr-only">Email address</label> <input
				type="text" id="username" name="username" class="form-control"
				placeholder="User name or Email address" required autofocus>

			<label for="inputPassword" class="sr-only">Password</label> <input
				type="password" id="inputPassword" name="password"
				class="form-control" placeholder="Password" required>

			<div class="checkbox">
				<label> <input type="checkbox" value="remember-me"
					name="remember-me"> Remember me
				</label>
			</div>

			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
				in</button>
		</form>

	</div>
	<!-- /container -->

	<c:import url="../bottom.jsp" />
</body>
</html>
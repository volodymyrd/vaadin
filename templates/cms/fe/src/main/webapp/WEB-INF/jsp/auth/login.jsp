<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<head>
</head>
<body>
	<c:url value="/login" var="loginUrl" />
	<form action="${loginUrl}" method="post">
		<c:if test="${error != null}">
			<p>${error}</p>
		</c:if>
		<p>
			<label for="username">Username</label> <input type="text"
				id="username" name="username" />
		</p>
		<p>
			<label for="password">Password</label> <input type="password"
				id="password" name="password" />
		</p>
		<p>
			Remember Me: <input type="checkbox" name="remember-me" />
		</p>
<%-- 		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
		<button type="submit" class="btn">Log in</button>
	</form>
</body>
</html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
	<h1>Hello ${pageContext['request'].userPrincipal.name}, you
		entered to the administrative area</h1>
</body>
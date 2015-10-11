<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
	<%-- 	<c:url value="/resources/text.txt" var="url" /> --%>
	<%-- 	<spring:url value="/resources/text.txt" htmlEscape="true" --%>
	<%-- 		var="springUrl" /> --%>
	<%-- 	Spring URL: ${springUrl} at ${time} --%>
	<%-- 	<br> JSTL URL: ${url} --%>
	<%-- 	<br> Message: ${message} --%>
	<h1>Hello, this is an unauthorized area</h1>
	<a href="/admin">The entrance to the administrative area</a>
	</br>
	<a href="/cms">The entrance to the cms area</a>
</body>
</html>
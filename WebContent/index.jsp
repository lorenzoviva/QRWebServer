<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1" />
	<title>New Product</title>
</head>
<body>
	<form action='<c:url value="/signUp" />' method="post">
	<div>First name: <input type="text" name="firstName" value="${param.firstName}"/> </div>
	<div>Last name: <input type="text" name="lastName" value="${param.lastName}"/></div>
	<div><input type="submit" name="sumbit" value="invia" /></div>
	</form>
</body>
</html>

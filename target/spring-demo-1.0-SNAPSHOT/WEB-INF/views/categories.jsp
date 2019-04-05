<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fanzo
  Date: 23.03.2019
  Time: 9:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>categories page</title>
</head>
<body>

<h1>Categories</h1>

<c:forEach items="${categories}" var="c">
    <h3>Category name: <a href="<c:url value="/category?c_id=${c.id}"/>"><c:out value="${c.categoryName}"/></a></h3>
</c:forEach>



</body>
</html>

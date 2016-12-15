<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@ page language="java" import="com.zgmao.vo.*,com.zgmao.utils.Lg"
	session="true" isThreadSafe="false" isErrorPage="false"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>page指令</title>
</head>
<body>
	<%@include file="foot.jsp"%>
	<%
		request.getSession();
	%>
</body>
</html>
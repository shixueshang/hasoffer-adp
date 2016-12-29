<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<jsp:include page="../include/header.jsp"/>
<body class="fixed-top">
<jsp:include page="../include/nav.jsp"/>

<div class="page-container row-fluid">
    <jsp:include page="../include/left.jsp"/>
    <div class="page-content">
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span12">
                    <h4 class="page-title"></h4>
                    <ul class="breadcrumb">
                        <li>
                            <i class="icon-home"></i><a href="javascript:void(0);">系统管理</a><i
                                class="icon-angle-right"></i>
                        </li>
                        <li class="active"><a href="javascript:void(0);">服务器列表</a></li>
                    </ul>
                </div>
            </div>

            <div class="row-fluid">
                <div class="span12">
                    <div class="portlet box blue">
                        <div class="portlet-title">
                            <h4><i class="icon-reorder"></i></h4>
                        </div>
                        <div class="portlet-body">
                            <table class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <td>MAC地址</td>
                                    <td>是否全投</td>
                                    <td width="80px">操作</td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${servers}" var="server">
                                    <tr>
                                        <td>${server.key}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${server.value == 'false'}">
                                                    <span class="label label-default">否</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="label label-warning">是</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${server.value == 'false'}">
                                                <a href="<%=request.getContextPath()%>/switch/status/${server.key}"
                                                   class="btn mini purple"> 全投</a>
                                            </c:if>
                                            <c:if test="${server.value == 'true'}">
                                                <a href="<%=request.getContextPath()%>/switch/status/${server.key}"
                                                   class="btn mini purple"> 取消全投</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="../include/footer.jsp"/>

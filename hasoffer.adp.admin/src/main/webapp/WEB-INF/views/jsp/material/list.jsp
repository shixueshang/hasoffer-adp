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
                            <i class="icon-home"></i><a href="javascript:void(0);">素材管理</a><i class="icon-angle-right"></i>
                        </li>
                        <li class="active"><a href="javascript:void(0);">素材列表</a></li>
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
                                    <td>标题</td>
                                    <td>按钮文字</td>
                                    <td>结算方式</td>
                                    <td>打开方式</td>
                                    <td>价格</td>
                                    <td>链接</td>
                                    <td>投放国家</td>
                                    <td>投放平台</td>
                                    <td>投放标签</td>
                                    <td>是否投放</td>
                                    <td width="80px">操作</td>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${materials}" var="material">
                                    <tr>
                                        <td>${material.title}</td>
                                        <td>${material.btnText}</td>
                                        <td width="60px">${material.settlementWay}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${material.openWay == 'inner'}">
                                                    内部
                                                </c:when>
                                                <c:otherwise>
                                                    外部
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${material.price}</td>
                                        <td>${material.url}</td>
                                        <td>${material.putCountry}</td>
                                        <td>${material.putPlatform}</td>
                                        <td>${material.tags}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${material.isDelivery == '1'}">
                                                    <span class="label label-success">是</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="label label-warning">否</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="<%=request.getContextPath()%>/material/detail/${material.id}" class="btn mini purple"><i class="icon-edit"></i> 编辑</a>
                                            <c:if test="${material.isDelivery == '1'}">
                                                <a href="<%=request.getContextPath()%>/material/changeDelivery/${material.id}"
                                                   class="btn mini purple"> 取消投放</a>
                                            </c:if>
                                            <c:if test="${material.isDelivery == '0'}">
                                                <a href="<%=request.getContextPath()%>/material/changeDelivery/${material.id}"
                                                   class="btn mini purple"> 投放</a>
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
        <jsp:include page="../include/page.jsp"/>
    </div>
</div>

<jsp:include page="../include/footer.jsp"/>

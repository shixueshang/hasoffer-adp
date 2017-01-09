<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<jsp:include page="../include/header.jsp"/>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/bootstrap-datepicker/css/datepicker.css">
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
                        <li class="active"><a href="javascript:void(0);">数据统计</a></li>
                    </ul>
                </div>
            </div>

            <div class="row-fluid">
                <form class="form-inline" action="<%=request.getContextPath()%>/data/find" method="get">
                    <div class="form-group">
                        <label class="control-label">起始日期</label>
                        <input type="text" id="dateTimeStart" name="dateTimeStart" class="form-control datepicker"/>

                        <label class="control-label">截止日期</label>
                        <input type="text" id="dateTimeEnd" name="dateTimeEnd" class="form-control datepicker"/>
                        <button type="submit" class="btn blue searchBtn"><i class="icon-search"></i> 查询</button>
                    </div>
                </form>

            </div>
            <div class="row-fluid">
                <div class="span12">
                    <div class="portlet box blue">
                        <div class="portlet-title">
                            <h4><i class="icon-reorder"></i></h4>
                        </div>
                        <div class="portlet-body">
                            <table class="table table-bordered table-hover" id="dataCount">
                                <thead>
                                <tr>
                                    <td>日期</td>
                                    <td>请求数</td>
                                    <td>pv回调数</td>
                                    <td>click回调数</td>
                                    <td>图片请求数</td>
                                    <td>点击数</td>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${logs}" var="log">
                                    <tr>
                                        <td><fmt:formatDate value="${log.date}" pattern="yyyy-MM-dd"/></td>
                                        <td>${log.requests}</td>
                                        <td>${log.pvCallback}</td>
                                        <td>${log.pvClicks}</td>
                                        <td>${log.imgRequests}</td>
                                        <td>${log.clicks}</td>
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

<script type="text/javascript"
        src="<%=request.getContextPath()%>/assets/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>

<script>

    $(function () {
        $("#dateTimeStart").datepicker({
            format: 'yyyy-mm-dd',
            startView: 'day',
            autoclose: true,
            defaultDate: new Date()
        });
        $("#dateTimeEnd").datepicker({
            format: 'yyyy-mm-dd',
            startView: 'day',
            autoclose: true,
            defaultDate: new Date()
        });
    })

</script>

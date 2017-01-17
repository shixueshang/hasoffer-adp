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
                        <input type="text" id="dateTimeStart" name="dateTimeStart"
                               value="<fmt:formatDate value="${dateTimeStart}" pattern="yyyy-MM-dd"/>"
                               class="form-control datepicker"/>

                        <label class="control-label" style="margin-left: 20px">截止日期</label>
                        <input type="text" id="dateTimeEnd" name="dateTimeEnd"
                               value="<fmt:formatDate value="${dateTimeEnd}" pattern="yyyy-MM-dd"/>"
                               class="form-control datepicker"/>
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
                                    <td>pv点击数</td>
                                    <td>pv点击率</td>
                                    <td>图片请求数</td>
                                    <td>点击数</td>
                                    <td>点击率</td>
                                    <td>操作</td>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${logs}" var="log">
                                    <tr>
                                        <td><fmt:formatDate value="${log.date}" pattern="yyyy-MM-dd"/></td>
                                        <td>${log.requests}</td>
                                        <td>${log.pvCallback}</td>
                                        <td>${log.pvClicks}</td>
                                        <td><fmt:formatNumber value="${(log.pvClicks / log.pvCallback) * 100 }"
                                                              pattern="#.###" minFractionDigits="3"/> %
                                        </td>
                                        <td>${log.imgRequests}</td>
                                        <td>${log.clicks}</td>
                                        <td><fmt:formatNumber value="${(log.clicks / log.imgRequests) * 100 }"
                                                              pattern="#.###" minFractionDigits="3"/> %
                                        </td>
                                        <td>
                                            <a href="#data_detail" role="button" data-toggle="modal"
                                               data-date="<fmt:formatDate value="${log.date}" pattern="yyyy-MM-dd"/>"
                                               class="btn mini purple data_detail"> 详情</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!--页面操作详细内容 开始-->
            <div id="data_detail" class="modal hide fade" style="width: 1020px;height: 500px;margin-left:-400px;"
                 tabindex="-1" role="dialog" aria-labelledby="my_modal_edit" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h3>统计数据详情</h3>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <table class="table table-striped table-bordered table-hover" id="data_details">
                        <thead>
                        <tr>
                            <td style='white-space:nowrap; '>日期</td>
                            <td style='white-space:nowrap; '>素材</td>
                            <td style='white-space:nowrap; '>pv回调</td>
                            <td style='white-space:nowrap; '>pv点击</td>
                            <td style='white-space:nowrap; '>图片请求</td>
                            <td style='white-space:nowrap; '>点击数</td>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                </div>
            </div>


        </div>
        <jsp:include page="../include/page.jsp"/>
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


        $(".data_detail").click(function () {
            var date = $(this).data('date');
            $.ajax({
                url: "<%=request.getContextPath()%>/data/detail",
                type: "get",
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                data: {"date": date},
                success: function (data) {
                    var tBody = '';
                    $.each(data.data, function (i, val) {
                        var date = val.date;
                        var title = val.title;
                        var pvCallback = val.pvCallback;
                        var pvClicks = val.pvClicks;
                        var imgRequests = val.imgRequests;
                        var clicks = val.clicks;

                        tBody += "<tr> <td style='white-space:nowrap; '>" + getFormatDate(new Date(date))
                        + "</td> <td style='white-space:nowrap; '>" + title
                        + "</td> <td style='white-space:nowrap; '>" + pvCallback
                        + "</td> <td style='white-space:nowrap; '>" + pvClicks
                        + "</td> <td style='white-space:nowrap; '>" + imgRequests
                        + "</td> <td style='white-space:nowrap; '>" + clicks + "</td></tr>";
                    });

                    $("#data_details tbody").html(tBody);
                },
                error: function (err) {

                }
            });
        });
    })

    function getFormatDate(date) {
        var seperator = "-";
        var month = date.getMonth() + 1;
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }

        var day = date.getDate();
        if (day >= 1 && day <= 9) {
            day = "0" + day;
        }
        return date.getFullYear() + seperator + month + seperator + day;
    }

</script>

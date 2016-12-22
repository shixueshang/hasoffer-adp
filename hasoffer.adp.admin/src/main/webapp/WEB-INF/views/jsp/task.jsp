<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="include/header.jsp"/>
<body class="fixed-top">
<jsp:include page="include/nav.jsp"/>

<div class="page-container row-fluid">
    <jsp:include page="include/left.jsp"/>
    <div class="page-content">

        <div class="container-fluid">
            <div class="row-fluid">
                <div class="span12">
                    <button type="button" id="loadRedisData" class="btn blue">加载数据</button>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="include/footer.jsp"/>

<script>

    $('#loadRedisData').click(function () {

        $.ajax({
            type: 'GET',
            url: '<%=request.getContextPath()%>/task/execute',
            success: function () {
                BootstrapDialog.show({
                    title: 'success',
                    message: '执行成功'
                })
            },
            fail: function () {
                BootstrapDialog.show({
                    title: 'failure',
                    message: '执行失败'
                })
            }

        })
    })

</script>
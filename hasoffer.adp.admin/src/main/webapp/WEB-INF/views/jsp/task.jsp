<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="include/header.jsp"/>

<link href="<%=request.getContextPath()%>/assets/bootstrap-loading/dist/ladda-themeless.min.css" rel="stylesheet"/>
<body class="fixed-top">
<jsp:include page="include/nav.jsp"/>

<div class="page-container row-fluid">
    <jsp:include page="include/left.jsp"/>
    <div class="page-content">

        <div class="container-fluid">
            <div class="row-fluid">

                <div class="span12" style="height: 30px;"></div>
                <div class="span12">
                    <div class="alert alert-block alert-info">
                        <span style="font-size: 16px;">加载设备到Redis,请点击下面的按钮。。</span><br/>
                        <button type="button" class="btn blue ladda-button task-eq"
                                data-style="expand-left"><span class="ladda-label">加载数据</span></button>

                    </div>
                </div>

                <div class="span12" style="height: 30px;"></div>
                <div class="span12">
                    <div class="alert alert-block alert-info">
                        <span style="font-size: 16px;">加载素材标签到Redis,请点击下面的按钮。。</span><br/>
                        <button type="button" class="btn blue ladda-button task-mtag"
                                data-style="expand-left"><span class="ladda-label">加载数据</span></button>

                    </div>
                </div>

                <div class="span12" style="height: 30px;"></div>
                <div class="span12">
                    <div class="alert alert-block alert-info">
                        <span style="font-size: 16px;">加载返回结果到Redis,请点击下面的按钮。。</span><br/>
                        <button type="button" class="btn blue ladda-button task-result"
                                data-style="expand-left"><span class="ladda-label">加载数据</span></button>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="include/footer.jsp"/>
<script src="<%=request.getContextPath()%>/assets/bootstrap-loading/dist/spin.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/bootstrap-loading/dist/ladda.min.js"></script>

<script>

    $('.task-eq').click(function () {
        var la = Ladda.create(document.querySelector('.task-eq'));
        la.start();
        $.ajax({
            type: 'GET',
            url: '<%=request.getContextPath()%>/task/loadEqData',
            success: function (res) {
                la.stop();
                if (res.code == 200) {
                    BootstrapDialog.show({title: 'success', message: '执行成功'})
                } else {
                    BootstrapDialog.show({title: 'failure', message: '执行失败'})
                }
            },
            error: function () {
                la.stop();
                BootstrapDialog.show({title: 'failure', message: '执行失败'})
            }
        })
    })

    $('.task-mtag').click(function () {
        var la = Ladda.create(document.querySelector('.task-mtag'));
        la.start();
        $.ajax({
            type: 'GET',
            url: '<%=request.getContextPath()%>/task/loadMtagData',
            success: function (res) {
                la.stop();
                if (res.code == 200) {
                    BootstrapDialog.show({title: 'success', message: '执行成功'})
                } else {
                    BootstrapDialog.show({title: 'failure', message: '执行失败'})
                }
            },
            error: function () {
                la.stop();
                BootstrapDialog.show({title: 'failure', message: '执行失败'})
            }
        })
    })

    $('.task-result').click(function () {
        var la = Ladda.create(document.querySelector('.task-result'));
        la.start();
        $.ajax({
            type: 'GET',
            url: '<%=request.getContextPath()%>/task/loadResultData',
            success: function (res) {
                la.stop();
                if (res.code == 200) {
                    BootstrapDialog.show({title: 'success', message: '执行成功'})
                } else {
                    BootstrapDialog.show({title: 'failure', message: '执行失败'})
                }
            },
            error: function () {
                la.stop();
                BootstrapDialog.show({title: 'failure', message: '执行失败'})
            }
        })
    })

</script>
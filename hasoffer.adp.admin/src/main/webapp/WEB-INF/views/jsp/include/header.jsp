<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->

<head>
    <meta charset="utf-8" />
    <title>Hasoffer-Adp</title>
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/static/image/favicon.ico" type="image/x-icon">

    <link href="<%=request.getContextPath()%>/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet" /> <!-- bootstrap默认css -->
    <link href="<%=request.getContextPath()%>/assets/css/metro.css" rel="stylesheet" /> <!-- 控制button的显示样式，如果不引入的话，则为bootstrap默认样式 -->
    <link href="<%=request.getContextPath()%>/assets/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" /> <!-- bootstrap导航菜单样式 -->
    <link href="<%=request.getContextPath()%>/assets/font-awesome/css/admin-font-awesome.css" rel="stylesheet" />  <!-- 导航菜单图标 -->
    <link href="<%=request.getContextPath()%>/assets/css/admin-style.css" rel="stylesheet" /> <!-- 整体样式控制，网页字体 -->
    <link href="<%=request.getContextPath()%>/assets/css/style_responsive.css" rel="stylesheet" /> <!-- 本框架导航菜单样式 -->
    <link href="<%=request.getContextPath()%>/assets/css/style_blue.css" rel="stylesheet" id="style_color" /> <!-- 本框架皮肤样式 -->

    <script src="<%=request.getContextPath()%>/assets/js/jquery-1.8.3.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/excanvas.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/respond.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/jquery-ui.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/breakpoints/breakpoints.js"></script>
    <script src="<%=request.getContextPath()%>/assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/jquery.blockui.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/jquery.cookie.js"></script>

    <script src="<%=request.getContextPath()%>/assets/flot/jquery.flot.js"></script>
    <script src="<%=request.getContextPath()%>/assets/flot/jquery.flot.resize.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.pulsate.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/data-tables/jquery.dataTables.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/data-tables/DT_bootstrap.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/bootstrap-toggle-buttons/static/js/jquery.toggle.buttons.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/fancybox/source/jquery.fancybox.pack.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/app.js"></script>
    <script>
        jQuery(document).ready(function () {
            App.init();
        });
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/jquery.pulsate.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/jquery-validation/dist/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/bootstrap/js/bootstrap-dialog.js"></script>
</head>



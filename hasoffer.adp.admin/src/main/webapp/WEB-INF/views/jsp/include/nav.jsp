<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <div class="header navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <a href="javascript:;" class="btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
                        <img src="<%=request.getContextPath()%>/assets/images/menu-toggler.png" alt="" />
                    </a>
                    <ul class="nav pull-right">

                        <li class="dropdown user">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <img alt="" width="29" height="29" src="<%=request.getContextPath()%>/assets/images/avatar.png" />
                                <i class="icon-angle-down"></i>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="/logout"><i class="icon-signout"></i> 退出</a></li>
                            </ul>
                        </li>

                    </ul>

                </div>
            </div>
        </div>
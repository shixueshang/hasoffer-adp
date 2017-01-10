<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <div class="page-sidebar nav-collapse collapse">
        <ul>

            <li class="has-sub">
                <a href="javascript:;"><i class="icon-reorder"></i><span class="title">素材管理</span><span class="selected"></span><span class="arrow"></span></a>
                <ul class="sub">
                    <li class="active">
                        <a href="<%=request.getContextPath()%>/material/list">素材列表</a>
                    </li>
                    <li class="active">
                        <a href="<%=request.getContextPath()%>/material/create">素材添加</a>
                    </li>
                </ul>
            </li>


            <li class="has-sub">
                <a href="javascript:;"><i class="icon-wrench"></i><span class="title">系统管理</span><span
                        class="selected"></span><span class="arrow"></span></a>
                <ul class="sub">
                    <li class="active">
                        <a href="<%=request.getContextPath()%>/task/loadRedisData">手动任务</a>
                    </li>
                    <li class="active">
                        <a href="<%=request.getContextPath()%>/switch/listServers">服务器列表</a>
                    </li>
                    <li class="active">
                        <a href="<%=request.getContextPath()%>/data/list">数据统计</a>
                    </li>
                </ul>
            </li>

        </ul>
    </div>

<script>

    var url = '${url}';
    var domain = '';
    var obj = $('a[href*="'+url+'"]');
    $.each(obj, function(i, v){
        var _this = $(v), _parent = _this.closest('ul'), _p_parent = _parent.closest('li');
        _p_parent.addClass('active').addClass('open');
        _parent.show();

        return false;
    });

</script>

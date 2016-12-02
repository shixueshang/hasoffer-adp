<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="include/header.jsp"/>
<body class="login">
<div class="content" style="margin-top: 200px">

    <form role="form" action="/login" method="post">
    <h3 class="form-title"></h3>
    <div class="alert alert-error hide">
        <button class="close" data-dismiss="alert"></button>
        <span>请填写用户名和密码</span>
    </div>
    <div class="control-group">
        <label class="control-label visible-ie8 visible-ie9">用户名</label>
        <div class="controls">
            <div class="input-icon left">
                <i class="icon-user"></i>
                <input class="m-wrap placeholder-no-fix" type="text" name="username" placeholder="用户名" name="username"/>
            </div>
            <label  class="help-inline help-small no-left-padding" style="display:none;">请填写用户名</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label visible-ie8 visible-ie9">密码</label>
        <div class="controls">
            <div class="input-icon left">
                <i class="icon-lock"></i>
                <input class="m-wrap placeholder-no-fix" type="password" name="password" placeholder="密码" name="password"/>
            </div>
            <label class="help-inline help-small no-left-padding" style="display:none;">请填写密码</label>
        </div>
    </div>
    

    <div class="form-actions">
        <input type="submit" class="btn blue btn-block" value="登录"/>


    </div>
    </form>
    <div style="color: red">
        ${error}
    </div>
</div>

</body>

</html>


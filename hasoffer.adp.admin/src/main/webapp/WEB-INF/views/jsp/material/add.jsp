<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    String basePath  = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>

<jsp:include page="../include/header.jsp"/>
<body class="fixed-top">
<jsp:include page="../include/nav.jsp"/>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/assets/bootstrap-fileupload/bootstrap-fileupload.css" />

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
                        <li class="active"><a href="javascript:void(0);" id="admin_title">素材新增</a></li>
                    </ul>
                </div>
            </div>

            <div class="row-fluid">
                <div class="span12">
                    <div class="portlet box blue tabbable">
                        <div class="portlet-title">
                            <h4><i class="icon-plus"></i><span class="hidden-480"></span>&nbsp;</h4>
                        </div>
                        <div class="portlet-body form">
                            <div class="tabbable portlet-tabs">
                                <div class="tab-content">
                                    <div class="tab-pane active" id="portlet_tab1">
                                        <form action="<%=request.getContextPath()%>/material/add" id="material_form" enctype="multipart/form-data"  method="POST" class="form-horizontal">
                                            <div class="alert alert-error hide">
                                                <button class="close" data-dismiss="alert"></button>
                                                您的表单有未完成的必填项,请检查.
                                            </div>
                                            <div class="alert alert-success hide">
                                                <button class="close" data-dismiss="alert"></button>
                                                表单内容验证成功!
                                            </div>
                                            <div class="control-group"></div>

                                            <div class="control-group">
                                                <label class="control-label">标题<span class="required">*</span></label>
                                                <div class="controls">
                                                    <input type="text" name="title" class="m-wrap large" value="${material.title}" />
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">副标题<span class="required">*</span></label>
                                                <div class="controls">
                                                    <input type="text" name="subTitle" class="large m-wrap" value="${material.subTitle}">
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">描述</label>
                                                <div class="controls">
                                                    <textarea name="description" maxlength="200" rows="5" class="m-wrap large">${material.description}</textarea>
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">按钮文字<span class="required">*</span></label>
                                                <div class="controls">
                                                    <input type="text" name="btnText" class="large m-wrap" value="${material.btnText}">
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">是否CPI</label>
                                                <div class="controls">
                                                    <select name="isCPI" id="isCPI" class="large m-wrap">
                                                        <option value="1">是</option>
                                                        <option value="0">否</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">结算方式</label>
                                                <div class="controls">
                                                    <select name="settlementWay" id="settlementWay" class="large m-wrap">
                                                        <option value="CPI">CPI</option>
                                                        <option value="CPC">CPC</option>
                                                        <option value="CPM">CPM</option>
                                                        <option value="CPA">CPA</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">打开方式</label>
                                                <div class="controls">
                                                    <select name="openWay" class="large m-wrap">
                                                        <option value="inner">内部打开</option>
                                                        <option value="outer">外部打开</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">价格<span class="required">*</span></label>
                                                <div class="controls">
                                                    <input type="number" name="price" class="large m-wrap" value="${material.price}"/>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">链接</label>
                                                <div class="controls">
                                                    <input type="text" name="url" class="large m-wrap" value="${material.url}"/>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">投放国家</label>
                                                <div class="controls">
                                                    <select name="putCountry" class="large m-wrap">
                                                        <option value="IN">IN</option>
                                                    </select>
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">投放icon</label>
                                                <div class="controls">
                                                    <div class="fileupload fileupload-new" data-provides="fileupload">
                                                        <div class="fileupload-new thumbnail" style="width: 200px; height: 150px;">
                                                            <c:choose>
                                                                <c:when test="${material.icon == null}" >
                                                                    <img src="<%=request.getContextPath()%>/assets/images/no-image.png" alt="" />
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img src="<%=basePath%>/${material.icon}" alt="" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                        <div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
                                                        <div>
                                                            <span class="btn btn-file"><span class="fileupload-new">选择图片</span>
                                                            <span class="fileupload-exists">更换</span>
                                                            <input type="file" name="iconFile" class="default" accept="image/gif,image/jpeg,image/jpg,image/png,"/></span>
                                                            <input type="hidden" name="icon" id="icon" value="${material.icon}" />
                                                            <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">移除</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">其他icon</label>
                                                <div class="controls">
                                                    <div class="fileupload fileupload-new" data-provides="fileupload">
                                                        <div class="fileupload-new thumbnail" style="width: 200px; height: 150px;">
                                                            <c:choose>
                                                                <c:when test="${material.otherIcon == null}" >
                                                                    <img src="<%=request.getContextPath()%>/assets/images/no-image.png" alt="" />
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <img src="<%=basePath%>${material.otherIcon}" alt="" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                        <div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
                                                        <div>
                                                            <span class="btn btn-file"><span class="fileupload-new">选择图片</span>
                                                            <span class="fileupload-exists">更换</span>
                                                            <input type="file" name="otherIconFile" class="default" accept="image/gif,image/jpeg,image/jpg,image/png,"/></span>
                                                            <input type="hidden" name="otherIcon" id="otherIcon" value="${material.otherIcon}" />
                                                            <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">移除</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">投放平台<span class="required">*</span></label>
                                                <div class="controls">
                                                    <select name="putPlatform" id="putPlatform" class="large m-wrap">
                                                        <option value="">--请选择投放平台--</option>
                                                        <option value="Android">Android</option>
                                                        <option value="IOS">IOS</option>
                                                    </select>
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">平台版本</label>
                                                <div class="controls">
                                                    <select name="platformVersion" id="platformVersion" class="large m-wrap"></select>
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">投放app类型</label>
                                                <div class="controls">
                                                    <c:forEach items="${appTypes}" var="appType">
                                                        <span style="font-size: 14px;width: 100px !important;">
                                                            <input type="checkbox" name="appType" value="${appType.name}"  >${appType.name}&nbsp;&nbsp;&nbsp;
                                                        </span>
                                                    </c:forEach>
                                                </div>
                                            </div>

                                            <div class="control-group">
                                                <label class="control-label">每日跑量</label>
                                                <div class="controls">
                                                    <input type="text" name="dailyRunning" class="large m-wrap" value="${material.dailyRunning}">
                                                    <span class="help-inline"></span>
                                                </div>
                                            </div>


                                            <div class="form-actions">
                                                <input name="id" type="hidden" id="materialId" value="${material.id}"/>
                                                <button type="submit" class="btn blue"><i class="icon-ok"></i> 保存</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/assets/bootstrap-fileupload/bootstrap-fileupload.js"></script>
<jsp:include page="../include/footer.jsp"/>
<script>
    $(function(){

        var isCPI = $('#isCPI');
        if(isCPI.val() == '1'){
            $('#settlementWay').empty();
            $('#settlementWay').append("<option value='CPI'>CPI</option>")
        }else{
            $('#settlementWay option:first').remove();
        }
        isCPI.change(function(){
            if(isCPI.val() == '1'){
                $('#settlementWay').empty();
                $('#settlementWay').append("<option value='CPI'>CPI</option>");
            }else{
                $('#settlementWay').append("<option value='CPI'>CPI</option><option value='CPC'>CPC</option><option value='CPM'>CPM</option><option value='CPA'>CPA</option>")
                $('#settlementWay option[value="CPI"]').remove();
            }
        });

        var putPlatform = $('#putPlatform');
        var androidVersions = $.parseJSON('${androidVersions}');
        var IOSVersions = $.parseJSON('${IOSVersions}');
        putPlatform.change(function(){
            var platformVersion = $('#platformVersion');
            platformVersion.empty();
            if(putPlatform.val() == ''){
                return false;
            }else if(putPlatform.val() == 'Android'){
                $.each(androidVersions, function(i, val){
                    platformVersion.append("<option value='"+ val.index +"'>"+ val.name +"</option>");
                });
            }else{
                $.each(IOSVersions, function(i, val){
                    platformVersion.append("<option value='"+ val.index +"'>"+ val.name +"</option>");
                });
            }
        });

        var form = $('#material_form');
        var error = $('.alert-error', form);
        var success = $('.alert-success', form);

        form.validate({
            errorElement: 'span',
            errorClass: 'help-inline',
            focusInvalid: false,
            ignore: "",
            rules: {
                title: {
                    required: true
                },
                subTitle: {
                    required: true
                },
                price : {
                    required: true
                },
                url: {
                    required: true,
                    url: true
                },
                putPlatform: {
                    required: true
                }
            },

            invalidHandler: function (event, validator) {
                success.hide();
                error.show();
                App.scrollTo(error, -200);
                return false;
            },

            highlight: function (element) {
                $(element).closest('.help-inline').removeClass('ok');
                $(element).closest('.control-group').removeClass('success').addClass('error');
            },

            unhighlight: function (element) {
                $(element).closest('.control-group').removeClass('error');
            },

            success: function (label) {
                label.addClass('valid').addClass('help-inline ok').closest('.control-group').removeClass('error').addClass('success');
            },

            submitHandler: function (form) {
                success.show();
                error.hide();
                form.submit();
            }
        });

    })
</script>

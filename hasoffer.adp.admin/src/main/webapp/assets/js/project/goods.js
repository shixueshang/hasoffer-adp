
/**
 * 商品添加页面处理js
 * @param {type} msg
 * @returns {undefined}
 */

function lls(msg) {
    console.log(msg);
}

$(function () {
    
    //分类展示
    $('table[name="custom_table"] tr input').click(function () {
        var obj = $(this),
                tbl = $('table tr'), // 整个分类列表表格
                lvl = parseInt(obj.data("level")), // 当前分类级别
                fnd = false, // 是否找到当前元素
                checked = $(this).is(':checked'),
                selfId = $(this).attr('id'),
                pa = $(this).data('pa');

        //判断子分类是否已经全部选中
        var upLvl = pa ? lvl - 1 : '';
        var upId = upLvl + "_" + pa;
        if (pa) {
            var $subs = $("input[data-pa='" + pa + "']");
            $("#" + upId).prop("checked", $subs.filter(":checked").length ? true : false);
            if (!$subs.filter(":checked").length) {
                $("#" + upId).closest('span').removeClass('checked');
            }
        }

        // 遍历所有的分类
        for (i = 0; i < tbl.length; i++) {
            var row = tbl.eq(i).find('input');
            if (row.attr('id') == obj.attr('id')) {
                fnd = true;  //找到当前行
            } else {
                if (fnd == true) {
                    var cur = parseInt(row.data('level')); //当前循环行的级别
                    var cur_span = row.closest('span');

                    if (cur > lvl) {
                        if (checked) {
                            cur_span.addClass('checked');
                            row.prop("checked", true);
                        } else {
                            cur_span.removeClass("checked");
                            row.prop("checked", false);
                        }

                    } else {
                        fnd = false;
                        break;
                    }

                }
            }
        }
    });

    //全部全选
    $('#custom_all').click(function () {
        $('table[name="custom_table"] tr input').prop('checked', $(this).is(":checked"));
        if ($(this).is(":checked")) {
            $('table[name="custom_table"] tr input').closest('span').addClass('checked');
        } else {
            $('table[name="custom_table"] tr input').closest('span').removeClass('checked');
        }
    });
    
    
    //编辑时商品属性初始化
    var goods_type = $('select[name="goods_type"]').val(),
        url = $('select[name="goods_type"]').data('url'),
        goods_id = $('select[name="goods_type"]').data('gid');
    if(parseInt(goods_type)){
        $.post(url, {goods_type: goods_type, goods_id: goods_id}, function(data){
            if (!data.ok) {
                alert(data.msg);
                return false;
            }
            $('#goods_type_span').html('');
            if(!data.msg){
                $('#goods_type_span').append('<span class="span12">无数据</span>');
            }
            $('#goods_type_span').append(data.msg);
       }, 'json');
    }

    //商品属性
    $('select[name="goods_type"]').change(function(){
       var typeId = $(this).val();
       $('#goods_type_span').html('');
       var url = $(this).data('url');
       $.post(url, {goods_type: typeId}, function(data){
            if (!data.ok) {
                alert(data.msg);
                return false;
            }
            
            if(!data.msg){
                $('#goods_type_span').append('<span class="span12">无数据</span>');
            }
            $('#goods_type_span').append(data.msg);
       }, 'json');
    });


    //测量数据全选
    $('#measure_all').click(function () {
        $('table[name="measure_table"] tr input').prop('checked', $(this).is(":checked"));
        if ($(this).is(":checked")) {
            $('table[name="measure_table"] tr input').closest('span').addClass('checked');
        } else {
            $('table[name="measure_table"] tr input').closest('span').removeClass('checked');
        }
    });
    $('table[name="measure_table"] tr input').click(function () {
        $("#measure_all").prop("checked", $(this).length == $(this).filter(":checked").length ? true : false);
        if ($(this).length == $(this).filter(":checked").length) {
            $("#measure_all").closest('span').addClass('checked');
        } else {
            $("#measure_all").closest('span').removeClass('checked');
        }
    });
    
    
    // 身材数据全选()
    $('table[name="figure_table"] tr input').click(function () {
        var obj = $(this),
                tbl = $('table[name="figure_table"] tr'), // 整个分类列表表格
                lvl = parseInt(obj.data("flevel")), // 当前分类级别
                fnd = false, // 是否找到当前元素
                checked = $(this).is(':checked'),
                pa = $(this).data('fpa');

        //判断子分类是否已经全部选中
        var upLvl = pa ? lvl - 1 : '';
        var upId = upLvl + "_" + pa + 'f';
        if (pa) {
            var $subs = $("input[data-fpa='" + pa + "']");
            console.log($subs.filter(":checked").length);
            $("#" + upId).prop("checked", (($subs.filter(":checked").length) ? true : false) );
            if (!$subs.filter(":checked").length) {
                $("#" + upId).closest('span').removeClass('checked');
            }
        }

        // 遍历所有的分类
        for (i = 0; i < tbl.length; i++) {
            var row = tbl.eq(i).find('input');
            if (row.attr('id') == obj.attr('id')) {
                fnd = true;  //找到当前行
            } else {
                if (fnd == true) {
                    var cur = parseInt(row.data('flevel')); //当前循环行的级别
                    var cur_span = row.closest('span');

                    if (cur > lvl) {
                        if (checked) {
                            cur_span.addClass('checked');
                            row.prop("checked", true);
                        } else {
                            cur_span.removeClass("checked");
                            row.prop("checked", false);
                        }

                    } else {
                        fnd = false;
                        break;
                    }

                }
            }
        }
    });
    $('#figure_all').click(function () {
        $('table[name="figure_table"] tr input').prop('checked', $(this).is(":checked"));
        if ($(this).is(":checked")) {
            $('table[name="figure_table"] tr input').closest('span').addClass('checked');
        } else {
            $('table[name="figure_table"] tr input').closest('span').removeClass('checked');
        }
    });
    
    
    //关联用途全选
    $('#use_all').click(function () {
        $('table[name="use_table"] tr input').prop('checked', $(this).is(":checked"));
        if ($(this).is(":checked")) {
            $('table[name="use_table"] tr input').closest('span').addClass('checked');
        } else {
            $('table[name="use_table"] tr input').closest('span').removeClass('checked');
        }
    });
    $('table[name="use_table"] tr input').click(function () {
        $("#use_all").prop("checked", $(this).length == $(this).filter(":checked").length ? true : false);
        if ($(this).length == $(this).filter(":checked").length) {
            $("#use_all").closest('span').addClass('checked');
        } else {
            $("#use_all").closest('span').removeClass('checked');
        }
    });

    //关联文章
    $('a[name="article_query"]').click(function () {
        var url = $(this).data('url');
        var title = $('input[name="title_input"]').val();
        if (!title) {
            return false;
        }
        $.post(url, {title: title}, function (data) {
            if (!data.ok) {
                alert(data.msg);
                return false;
            }
            var html = '';
            $.each(data.msg, function (i, v) {
                html += "<tr><td>";
                html += v.title;
                html += '<input type="hidden" name="unlink[]" value="' + v.article_id + '">';
                html += '<a href="javascript:void(0);" class="btn mini green-stripe" style="float:right;" onclick="moveTr(this)">添加</a>';
                html += '</td></tr>';
            });
            $('table[name="article_unlink"] tbody').html('');
            $('table[name="article_unlink"] tbody').append(html);
        });
    });
    
    
    //关联图案
    $('a[name="pattern_query"]').click(function () {
        var url = $(this).data('url');
        var pcat_id = $('select[name="pcat_id"]').val();
        if(!parseInt(pcat_id)){
            return false;
        }
        $.post(url, {cat_id: pcat_id}, function (data) {
            if (!data.ok) {
                alert(data.msg);
                return false;
            }
            $('#pattern_div').html('');
            $('#pattern_div').append(data.msg);
        });
    });
    
    //点击选中
    $(document).on('click', '.zoom', function(){
        var html = $(this).html();
        var paid = $(this).data('paid');
        var inputHidden = '<input type="hidden" name="pattern_id" value="'+paid+'" />';
        html += inputHidden;
        $('#pattern_linked').html(html);
    })
    
    
    //检查提交
    $('button[type="submit"]').click(function(){
        var post = {};
        post.img_upload = false;
        post.img_exists = parseInt($('span[name="goods_exists_img"]').find('.row-fluid').length) ? true : false;
        post.goods_name = $('input[name="goods_name"]').val();
        post.goods_sn = $('input[name="goods_sn"]').val();
        post.cat_id = $('select[name="cat_id"]').val();
        post.goods_number_reg = /^-?\d+$/;
        post.goods_number = $('input[name="goods_number"]').val();
        post.warn_number = $('input[name="warn_number"]').val();
        post.shop_price_reg = /\d+/;
        post.shop_price = $('input[name="shop_price"]').val();
        post.pattern_id = $('input[name="pattern_id"]').val();
        
        if(!post.goods_name){
            alert("请填写商品名称");
            $('input[name="goods_name"]').focus()
            return false;
        }
        if(!post.goods_sn){
            alert("请填写商品货号");
            $('input[name="goods_sn"]').focus()
            return false;
        }
        if(!post.cat_id){
            alert("请选择商品分类");
            $('select[name="cat_id"]').focus()
            return false;
        }
        if(!post.goods_number || !post.goods_number_reg.test(post.goods_number)){
            alert("请填写正确的库存数量");
            $('input[name="goods_number"]').focus()
            return false;
        }
        if(post.warn_number && !post.shop_price_reg.test(post.warn_number)){
            alert("请填写正确的商品报警数量");
            $('input[name="warn_number"]').focus()
            return false;
        }
        if(!post.shop_price || !post.shop_price_reg.test(post.shop_price)){
            alert("请填写正确的本店售价");
            $('input[name="shop_price"]').focus()
            return false;
        }
        
        // 判断图片
        $('div[name="goods_img_upload"] input[name="goods_img[]"]').each(function(){
            if($(this).val()){
                post.img_upload = true;
            }
        });
        if(!post.img_exists && !post.img_upload){
            alert("请至少上传一张商品图片");
            $('.nav-tabs li').each(function(){
               var li_html = $(this).find('a').html(),
                   li_id = $(this).find("a").attr('href');
                   li_id = li_id.substring(1, li_id.length);
               
               // 跳转
               if(li_html == "商品图片上传"){
                   $(this).addClass("active").siblings().removeClass("active");
                   $('.tab-pane').each(function(){
                       if($(this).attr("id") == li_id){
                           $(this).addClass("active").siblings().removeClass("active");
                       }
                   });
               }
            });
            return false;
        }
        
    });
    
    
    /**
     * 编辑时删除图片
     * @param {type} obj
     * @returns {Boolean}
     */
    $('span[name="delete_img"]').click(function(){
        var imgid = $(this).data('imgid');
        var url = $(this).data('url');
        var or = confirm("确定要删除吗？");
        if(!or){
            return false;
        }
        
        $.post(url, {img_id: imgid}, function(data){
            alert(data.msg);
            if(data.ok){
                $('a[name="hover_'+imgid+'"]').remove();
            } 
        });
    })
    

});

//文章搜索后添加
function moveTr(obj) {
    var clone_tr = $(obj).closest('tr'),
            article_id = clone_tr.find('input').val(),
            linkedObj = $('table[name="article_linked"]');

    if (linkedObj.find('input[value="' + article_id + '"]').val()) {
        return false;
    }
    var add_html = '<tr>';
    add_html += clone_tr.html().replace('unlink[]', 'linked[]')
            .replace('moveTr(this)', 'delTr(this)')
            .replace('green-stripe', 'red-stripe')
            .replace('添加', '删除');
    add_html += "</tr>";
    $('table[name="article_linked"]').append(add_html);
    clone_tr.remove();
}

//文章添加后删除
function delTr(obj) {
    $(obj).closest('tr').remove();
}

//图片上传添加
function addImgDiv(obj){
    var hideDiv = $('.upload_div').html();
    $('span[name="goods_img_div"]').append(hideDiv);
}

function delImgDiv(obj){
    $(obj).closest('.control-group').remove();
}

//商品属性添加
function addAttrDiv(obj){
    var html = '<div class="control-group">';
        html += $(obj).closest('.control-group').html();
        html += '</div>';
    var newHtml = html.replace('addAttrDiv(this)','delAttrDiv(this)').replace('icon-plus', 'icon-minus');
    $(newHtml).insertAfter($(obj).closest('.control-group'));
}

function delAttrDiv(obj){
    $(obj).closest('.control-group').remove();
}
    
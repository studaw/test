<#include "../import/adminTop.ftl">
<div class="panel col-xs-12">
    <div class="panel-body">
        <div class="form-inline">
            <div class="form-group">
                <label for="articleTagAdd">标签名称: </label>
                <input type="text"  class="form-control " id="articleTagAdd">
            </div>
            <button class="btn btn-success" type="button" onclick="articleTagAdd()">添加</button>
        </div>

        <hr/>
        <#if articleTagList??&& articleTagList? size gt 0>
            <#list articleTagList as articleTag>
                <div class="col-sm-3" style="padding: 2px">
                    <div id="${(articleTag.articleTagId)!}" class="img-thumbnail" style="width: 100%;height: 100% ">
                        <input type="text"  class="form-control " value="${(articleTag.articleTagName)!}" >

                        <div class="pull-reght" style="text-align: right">
                          <i class="icon icon-cog" data-toggle="tooltip" data-placement="top" title="修改" style="margin-right: 10px"
                           onclick="addOrUpdataArticleTag('${(articleTag.articleTagId)!}','${articleTag.articleTagName}')"></i>
                          <i class="icon icon-remove" data-toggle="tooltip" data-placement="top" title="删除" style="margin-right: 10px"
                            onclick="delArticleTag('${(articleTag.articleTagId)}')"></i>
                        </div>
                    </div>
                </div>
            </#list>

            <#else >
                <div class="panel">
                    <div class="panel-body" style="padding: 0;">
                        <div style="text-align: center">
                            <h3><i class="icon icon-coffee"></i></h3>
                            <h3>暂无数据</h3>
                        </div>
                    </div>
                </div>
        </#if>
    </div>
</div>

<script>
    function articleTagAdd() {
        let articleTagAddName = $("#articleTagAdd").val();
        if(!checkNotNull(articleTagAddName)){
            zuiMsg("添加出问题了,请刷新重试");
            return;
        }

        $.post("/hzh/article/tag/addOrUpdata",{
            articleTagName: articleTagAddName
        }, function (data) {
            if (data.code === 200) {
                alert(data.message);
                location.reload();
                return;
            }else if(data.code===1000){
                alert(data.message);
                location.reload();
                return;
            }else if(data.code===1000){
                alert(data.message);
                location.reload();
                return;
            }

            new $.zui.Messager('程序出错，请刷新页面', {
                type: 'center' // 定义颜色主题
            }).show();
        });


    }

    function delArticleTag(articleTagId) {
        if(confirm("确定删除吗？")) {
            if(!checkNotNull(articleTagId)){
                zuiMsg("删除参数不正确");
                return;
            }
            $.post("/hzh/article/tag/del", {
                articleTagId: articleTagId,
            }, function (data) {
                if (data.code === 200) {
                    alert(data.message);
                    location.reload();
                    return;
                }else if(data.code===1000){
                    alert(data.message);
                    location.reload();
                    return;
                }

                new $.zui.Messager('程序出错，请刷新页面', {
                    type: 'center' // 定义颜色主题
                }).show();
            });
        }
    }

    $('[data-toggle="tooltip"]').tooltip({
        placement: 'bottom'
    });
    function addOrUpdataArticleTag(articleTagId,articleTagName) {

        if(confirm("确定修改吗？")) {
            if(!checkNotNull(articleTagId)||!checkNotNull(articleTagName)){
                zuiMsg("修改参数不正确");
                return;
            }

            $.post("/hzh/article/tag/addOrUpdata", {
                articleTagId: articleTagId,
                articleTagName:articleTagName
            }, function (data) {
                if (data.code === 200) {
                    alert(data.message);
                    location.reload();
                    return;
                }else if(data.code===1000){
                    alert(data.message);
                    location.reload();
                    return;
                }

                new $.zui.Messager('程序出错，请刷新页面', {
                    type: 'center' // 定义颜色主题
                }).show();
            });
        }
    }


</script>
<#include "../import/bottom.ftl">
<#include "../import/adminTop.ftl">
    <#if articleIPage?? && articleIPage.list?size gt 0>
        <form class="form-horizontal" aria-atomic="/hzh/article/list" method="get">
        <div class="form-group">
            <label for="exampleInputAccount4" class="col-sm-1">账号</label>
            <div class="col-sm-3">
                <input type="text" class="form-control" value="${articleTitle!}" name="articleTitle" id="articleTitle" placeholder="文章标题">
            </div>
            <div class="cpl-sm-2">
                <button type="submit" class="btn btn-success" ><i class="icon icon-search"></i>搜索</button>
                <a type="submit" class="btn btn-success" href="/hzh/article/list"><i class="icon icon-search"></i>全部信息</a>
            </div>
        </div>
        </form>
        <div class="panel">
            <div class="panel-body">
                <h4>当前: ${(articleIPage.total)!0}篇文章</h4>

                <hr/>
                <table class="table table-striped ">
                    <thead>
                    <tr>
                        <th>发布时间</th>
                        <th>文章类型</th>
                        <th>发布者</th>
                        <th>文章标题</th>
                        <th>浏览数</th>
                        <th>点赞数</th>
                        <th>收藏数</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list articleIPage.list as articleVo>
                    <tr>
                        <td>
                            ${(articleVo.articleAddTime)?string("yyyy-MM-dd HH:mm:ss")}
                        </td>
                        <td>${(articleVo.articleTypeName)!}</td>
                        <td>${(articleVo.userName)!}</td>
                        <td>
                            ${(articleVo.articleTitle)!}
                        </td>
                        <td>${(articleVo.articleLookNumber)!}</td>
                        <td>${(articleVo.articleGoodNumber)!}</td>
                        <td>${(articleVo.articleCollectionNumber)!}</td>
                        <td >
                            <div style="text-align: right">

<#--                                <button onclick="hotArticle('${(articleVo.articleId)!}')" type="button"-->
<#--                                        class="btn btn-mini">🔥 设为热门-->
<#--                                </button>-->
                                <button onclick="delArticle('${(articleVo.articleId)!}')" type="button"
                                        class="btn btn-mini"><i class="icon-remove"></i> 删除
                                </button>
                                <a target="_blank" class="btn btn-mini" href="/view/article/${articleVo.articleId}"><i class="icon icon-eye-open"></i> 查看</a>
                            </div>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="panel">
            <div class="panel-body" style="padding: 0;">
                <div class="col-sm-12" style="padding: 0;text-align: center;">
                    <ul class="pager" style="margin-top: 10px;margin-bottom: 10px;">
                        <li class="previous" onclick="getNewData(1)">
                            <a href="javascript:void(0)"><i class="icon-step-backward"></i></a>
                        </li>

                        <#if articleIPage.pageNumber lte 1>
                            <li class="previous disabled">
                                <a href="javascript:void(0)"><i class="icon-chevron-left"></i></a>
                            </li>
                        <#else>
                            <li class="previous" onclick="getNewData('${articleIPage.pageNumber-1}')">
                                <a href="javascript:void(0)"><i class="icon-chevron-left"></i></a>
                            </li>
                        </#if>
                        <li>
                            <a href="javascript:void(0)" class="btn">
                                ${articleIPage.pageNumber}页/共${articleIPage.totalPage}</a>
                        </li>
                        <#if articleIPage.pageNumber gte articleIPage.totalPage>
                            <li class="next disabled">
                                <a href="javascript:void(0)"><i class="icon-chevron-right"></i></a>
                            </li>
                        <#else>
                            <li class="next" onclick="getNewData('${articleIPage.pageNumber+1}')">
                                <a href="javascript:void(0)"><i class="icon-chevron-right"></i></a>
                            </li>
                        </#if>
                        <li class="previous" onclick="getNewData('${articleIPage.totalPage}')">
                            <a href="javascript:void(0)"><i class="icon-step-forward"></i></a>
                        </li>


                        <li class="next">
                            <a href="javascript:void(0)">
                                <input type="number" id="renderPageNumber" maxlength="5"
                                       style="width:50px;height: 20px;" oninput="value=value.replace(/[^\d]/g,'')">
                            </a>
                        </li>
                        <li class="next">
                            <a href="javascript:void(0)" onclick="renderPage()"
                               style="padding-left: 2px;padding-right: 2px;">
                                跳转
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

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
<div class="modal fade" id="articleUpDateModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/hzh/article/update" method="post">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                    <h4 class="modal-title" id="articleTypeTitle">修改文章类型</h4>
                </div>
                 <div class="modal-body">

                    <input type="hidden" name="articleTypeId" id="articleTypeId">
                    <div class="form-group">
                        <label for="articleTypeName">类型名称:</label>
                        <input type="text" class="form-control" id="articleTypeName" disabled="disabled" placeholder="类型名称">
                    </div>
                    <div class="form-group">
                        <label for="articleTypeSort">排序类型:</label>
                        <input type="number" class="form-control" id="articleTypeSort" name="articleTypeSort" placeholder="排序类型">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="articleUpdateActive()" class="btn btn-primary">保存</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">

    function addOrUpdataArticleType(articleTypeSort, articleTypeName, articleTypeId) {
        console.log(articleTypeId);

        $('#articleUpDateModal').modal('toggle','center');
        $("#articleTypeSort").val(articleTypeSort);
        $("#articleTypeName").val(articleTypeName);
        $("#articleTypeId").val(articleTypeId);

        if(!checkNotNull(articleTypeId)){
            $("#articleTypeName").removeAttr("disabled");
            $("articleTypeTitle").text("添加文章类型");
        }else {
            $("articleTypeTitle").text("修改文章类型");
        }
    }


    function articleUpdateActive(){

        let articleTypeSort = $("#articleTypeSort").val();
        let articleTypeName = $("#articleTypeName").val();
        let articleTypeId = $("#articleTypeId").val();


        if(!checkNotNull(articleTypeId)){
            zuiMsg("程序出错，请刷新重试")
        }

        $.post("/hzh/article/addOrUpdata" , {
            articleTypeId: articleTypeId,
            articleTypeName:articleTypeName,
            articleTypeSort:articleTypeSort

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
            zuiMsg(data.message);
        });
    }



    function delArticle(articleId) {

        if (confirm("是否删除")) {
            if (!checkNotNull(articleId)) {
                new $.zui.Messager('程序出错，请刷新页面', {
                    type: 'warning', // 定义颜色主题
                    placement: 'center'
                }).show();
                return;
            }

            $.post("/hzh/article/del", {
                articleId: articleId
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

    function getNewData(pageNumber) {
        if (!checkNotNull(pageNumber)) {
            pageNumber = 1;
        }
        window.location.href = "/hzh/article/list?pageNumber=" + pageNumber + "<#if (articleName?? && articleName?length>0)>&articleName=${articleName!}</#if>";
    }

    function renderPage() {
        let renderPageNumber = $("#renderPageNumber").val();
        if (!checkNotNull(renderPageNumber)) {
            zuiMsg("请输入跳转的页码！");
            return;
        }
        let totalPage = '${articleIPage.totalPage}';
        if (parseInt(renderPageNumber) > parseInt(totalPage)) {
            renderPageNumber = totalPage;
        }
        getNewData(renderPageNumber);
    }


</script>
<#include "../import/bottom.ftl">
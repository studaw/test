<#include "../import/adminTop.ftl">
<div class="panel">
    <div class="panel-body">
    <form class="form-horizontal" action="/hzh/user/list" method="get">
        <div class="input-control search-box search-box-circle has-icon-left has-icon-right " id="searchboxExample">
            <div class="col-sm-8">
                <input id="userName" type="search" class="form-control search-input" placeholder="      用户名" name="userName">
                <label for="inputSearchExample1" class="input-control-icon-left search-icon"> <i class="icon icon-search"></i></label>
            </div>
            <div class="col-sm-1">
                <button type="submit" class="btn btn-success">查询</button>
            </div>
            <div class="col-sm-1">
                <a href="/hzh/user/list" class="btn btn-success">查询全部</a>
            </div>
        </div>
    </form>
    </div>
</div>
    <#if userPage??&&userPage.list?size gt 0>
        <h4><i class="icon icon-info-sign"></i> 提示：被冻结的用户无法登录</h4>
        <div class="panel">
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>用户名</th>
                        <th>注册时间</th>
                        <th>是否可以发布文章</th>
                        <th>是否冻结</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list userPage.list as user>
                        <tr>
                            <td>${(user.username)!}</td>
                            <td>${(user.userRegisterTime)?string("yyyy-MM-dd HH:mm:ss")}</td>
                            <td>${(user.userPublishArticle)!}
                                <#if (user.userPublishArticle)??&&(user.userPublishArticle)==1>
                                    <span class="label label-success">可以发布</span>
                                <#else>
                                    <span class="label label-danger">不能发布</span>
                                </#if>
                            </td>
                            <td>${(user.userFrozen)!}
                                <#if (user.userFrozen)??&&(user.userFrozen)==1>
                                    <span class="label label-danger">冻结</span>
                                    <#else>
                                        <span class="label label-success">正常</span>
                                </#if>
                            </td>
                            <td>
                                <button type="button"
                                        onclick="userUpdate('${(user.username)!}','${(user.userFrozen)!}','${(user.userId)!}','${(user.userPublishArticle)!}')"
                                        class="btn btn-mini"><i class="icon icon-cog"></i>修改</button>
                                <button type="button" class="btn btn-mini" onclick="delUser('${user.userId}')">
                                    <i class="icon icon-remove"></i>删除</button>
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

                        <#if userPage.pageNumber lte 1>
                            <li class="previous disabled">
                                <a href="javascript:void(0)"><i class="icon-chevron-left"></i></a>
                            </li>
                        <#else>
                            <li class="previous" onclick="getNewData('${userPage.pageNumber-1}')">
                                <a href="javascript:void(0)"><i class="icon-chevron-left"></i></a>
                            </li>
                        </#if>
                        <li>
                            <a href="javascript:void(0)" class="btn">
                                ${userPage.pageNumber}页/共${userPage.totalPage}</a>
                        </li>
                        <#if userPage.pageNumber gte userPage.totalPage>
                            <li class="next disabled">
                                <a href="javascript:void(0)"><i class="icon-chevron-right"></i></a>
                            </li>
                        <#else>
                            <li class="next" onclick="getNewData('${userPage.pageNumber+1}')">
                                <a href="javascript:void(0)"><i class="icon-chevron-right"></i></a>
                            </li>
                        </#if>
                        <li class="previous" onclick="getNewData('${userPage.totalPage}')">
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
<div class="modal fade" id="userUpDateModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/hzh/user/update" method="post">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                    <h4 class="modal-title">修改用户</h4>
                </div>
                 <div class="modal-body">

                    <input type="hidden" name="userId" id="userId">
                    <div class="form-group">
                        <label for="exampleInputAccount1">用户名:</label>
                        <input type="text" class="form-control" id="usernameUpdate" placeholder="用户名">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">用户密码:</label>
                        <input type="password" class="form-control" id="passwordUpdate" name="password" placeholder="用户密码">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputMoney1">是否冻结</label>
                        <div class="form-group">
                            <label class="radio-inline">
                                <input type="radio" name="userFrozen" value="0" checked="checked"> 正常
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="userFrozen"  value="1"> 冻结
                            </label>
                        </div>
                    </div>
                     <div class="form-group">
                         <label for="exampleInputMoney1">是否冻结</label>
                         <div class="form-group">
                             <label class="radio-inline">
                                 <input type="radio" name="userPublishArticle" value="0" checked="checked"> 不能发布文章
                             </label>
                             <label class="radio-inline">
                                 <input type="radio" name="userPublishArticle"  value="1"> 可以发布文章
                             </label>
                         </div>
                     </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" onclick="userUpdateActive()" class="btn btn-primary">保存</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">

    function userUpdateActive(){

        let userId = $("#userId").val();

        let password = $("#passwordUpdate").val();
        let userFrozen = $("input[name='userFrozen']:checked").val();
        let userPublishArticle = $("input[name='userPublishArticle']:checked").val();

        if(!checkNotNull(userId)){
            zuiMsg("程序出错，请刷新重试")
        }
        if(!checkNotNull(userFrozen)){
            zuiMsg("请选择是否冻结用户")
        }
        $.post("/hzh/user/update", {
            userId: userId,
            password:password,
            userFrozen:userFrozen,
            userPublishArticle:userPublishArticle
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

    function userUpdate(username, userFrozen, userId,userPublishArticle) {
        $('#userUpDateModal').modal('toggle','center');

        $("#userId").val(userId);
        $("#usernameUpdate").val(username);

        $(":radio[name='userFrozen'][value='"+ userFrozen +"']").prop("checked","checked")
        $(":radio[name='userPublishArticle'][value='" + userPublishArticle + "']").prop("checked", "checked");
    }


    function delUser(userId) {
        if (confirm("是否删除")) {
            if (!checkNotNull(userId)) {
                new $.zui.Messager('程序出错，请刷新页面', {
                    type: 'warning' // 定义颜色主题
                }).show();
                return;
            }

            $.post("/hzh/user/del", {
                userId: userId
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
        window.location.href = "/hzh/user/list?pageNumber=" + pageNumber + "<#if (userName?? && userName?length>0)>&userName=${userName!}</#if>";
    }

    function renderPage() {
        let renderPageNumber = $("#renderPageNumber").val();
        if (!checkNotNull(renderPageNumber)) {
            zuiMsg("请输入跳转的页码！");
            return;
        }
        let totalPage = '${userPage.totalPage}';
        if (parseInt(renderPageNumber) > parseInt(totalPage)) {
            renderPageNumber = totalPage;
        }
        getNewData(renderPageNumber);
    }

</script>
<#include "../import/bottom.ftl">
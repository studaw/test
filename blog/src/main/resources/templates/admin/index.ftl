<#include "../import/adminTop.ftl">
<div class="panel col-xs-12">
    <div class="panel-body col-xs-6">
        <div class="panel" >
            <div class="panel-body">
                <h5><i class="icon icon-desktop"></i> 系统类型:${osName!}</h5>
                <h5><i class="icon icon-server"></i> 服务器ip:${hostAddress!}</h5>
            </div>
        </div>
    </div>
    <div class="panel-body col-xs-6">
        <div class="panel">
            <div class="panel-body">
                <h5><i class="icon icon-th"></i> 文章类型数:${articleTypeCount!}</h5>
                <h5><i class="icon icon-th-list"></i> 文章标签数:${articleTagCount!}</h5>
                <h5><i class="icon icon-stack"></i> 文章数:${articleCount!}</h5>
                <h5><i class="icon icon-user"></i> 用户数:${userCount!}</h5>
            </div>
        </div>
    </div>
</div>
<#include "../import/bottom.ftl">
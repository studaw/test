<#include "./top.ftl">
<div class="container">
    <nav class="navbar nav-default" role="navigation" >
        <div class="container-fluid">
            <div class="navbar-header">
                <!-- 移动设备上的导航切换按钮 -->
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-example">
                    <span class="sr-only">切换导航</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <!-- 品牌名称或logo -->
            </div>
            <!-- 导航项目 -->
            <div class="collapse navbar-collapse navbar-collapse-example">
                <!-- 一般导航项目 -->
                <ul class="nav navbar-nav">
                    <li><a href="/hzh/"><i class="icon icon-list-ol"></i> 基础数据</a></li>

                    <!-- 导航中的下拉菜单 -->
                    <li class="dropdown">
                        <a href="your/nice/url" class="dropdown-toggle" data-toggle="dropdown">
                            <i class="icon icon-group"></i>用户管理<b class="caret"></b></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/hzh/user/list">用户列表</a></li>

                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="your/nice/url" class="dropdown-toggle" data-toggle="dropdown">
                            <i class="icon icon-th-list"></i>文章管理<b class="caret"></b></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="/hzh/article/type/list">文章类型 </a></li>
                            <li><a href="/hzh/article/tag/list">文章标签 </a></li>
                            <li><a href="/hzh/article/list">文章列表 </a></li>
                        </ul>
                    </li>
                    <li><a href="/hzh/link/list"><i class="icon icon-link"></i>友情链接</a></li>
                    <li><a href="/hzh/ad/list"><i class="icon icon-dollar"></i>广告管理</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="/hzh/logout"><i class="icon icon-off"></i>退出登录</a></li>
                </ul>
            </div>
        </div>
    </nav>
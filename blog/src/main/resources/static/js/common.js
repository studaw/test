function checkNotNull(str){
    if (str == null || str == "" || str.length < 1 || str == undefined) {
        return false;
    }
    return true;
}

function zuiMsg(msg){
    new $.zui.Messager(msg, {
        type: 'warning' // 定义颜色主题
    }).show();
    return;
}
//mod1.js
layui.define('layer', function(exports){
    //…
    exports(mod1, {});
});

//mod2.js，假设依赖 mod1 和 form
layui.define(['mod1', 'form'], function(exports){
    //…
    exports(mod2, {});
});

//mod3.js
//…

//main.js 主入口模块
layui.define('mod2', function(exports){
    //…
    exports('main', {});
});
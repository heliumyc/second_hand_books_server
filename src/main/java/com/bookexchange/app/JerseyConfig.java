package com.bookexchange.app;

import com.bookexchange.app.service.BookService;
import com.bookexchange.app.service.TestSearchService;
import com.bookexchange.app.service.UserService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(RequestContextFilter.class);
        //配置restful package.
        // 这是个open and well-known的bug, spring boot 1.4+中使用jersey不能用packages
        // 从2014年有人抱怨这个问题, 当时有人出了pull, 但是现在仍然没有merge进去
        // 可能整个项目也没人管了吧, sigh
        // 要么用spring mvc, 要么魔改, 各种hack packages方法, 要么就像下面这样, 分别register
//        packages("com.bookexchange.app.service");
        register(UserService.class);
        register(BookService.class);
        register(TestSearchService.class);
    }
}

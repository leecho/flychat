package com.honvay.flyai.document.infra.interceptor;

import com.pgvector.PGvector;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

@Intercepts({
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class PgVectorInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if(args != null){
            Connection connections = (Connection) args[0];
            PGvector.addVectorType(connections);
        }
        return invocation.proceed();
    }
}

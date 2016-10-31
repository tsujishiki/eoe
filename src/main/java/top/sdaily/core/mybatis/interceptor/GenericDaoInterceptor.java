package top.sdaily.core.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import top.sdaily.core.mybatis.MappedStatmentHelper;
import top.sdaily.core.mybatis.MapperSqlHelper;

import java.util.Properties;

/**
 * Created by soya on 2016/10/29.
 */

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        , @Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class})
})
public class GenericDaoInterceptor implements Interceptor {
    private final static String _sql_regex = ".*MapperGD.*";

    @SuppressWarnings("unchecked")
    private void processIntercept(final Object[] queryArgs) {
        final MappedStatement ms = (MappedStatement) queryArgs[0];
        final Object parameter = queryArgs[1];
        String mapperSQL = ms.getBoundSql(parameter).getSql();
        BoundSql boundSQL = ms.getBoundSql(parameter);

        Class<?> entityclazz = MappedStatmentHelper.getEntityClazz(ms.getResource());
//        queryArgs[0] = MappedStatmentHelper.setMSReturnSetMap(ms, entityclazz);

        boolean interceptor = mapperSQL.matches(_sql_regex);
        if (!interceptor) {
            return;
        }
        if (entityclazz == null) {
            throw new RuntimeException("使用公共dao必須給mapper接口的@MyBatisRepository(User.class) 注解設置值.");
        }

        String new_sql = MapperSqlHelper.getExecuSQL(entityclazz, mapperSQL, parameter);

        BoundSql newBoundSql = MappedStatmentHelper.copyFromBoundSql(ms,
                boundSQL, new_sql);
        MappedStatement newMs = MappedStatmentHelper
                .copyFromMappedStatement(ms, newBoundSql);
        queryArgs[0] = newMs;

    }

    public Object intercept(Invocation invocation) throws Throwable {

        processIntercept(invocation.getArgs());

        return invocation.proceed();
    }

    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    public void setProperties(Properties arg0) {
    }
}

package top.sdaily.core.mybatis;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import top.sdaily.core.mybatis.annotation.MyBatisRepository;

/**
 * Created by soya on 2016/10/30.
 */
public class MappedStatmentHelper {

    public static Class<?> getEntityClazz(String resource){

        String classPath = resource.substring(0,resource.lastIndexOf(".java")).replaceAll("/",".");

        try {
            Class mapperClazz = Class.forName(classPath);
            MyBatisRepository annotation = (MyBatisRepository)mapperClazz.getAnnotation(MyBatisRepository.class);
            if(annotation == null){
                return null;
            }
            return annotation.value();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("不存在类" + classPath);
        }
    }

    public static MappedStatement setMSReturnSetMap(MappedStatement ms,Class<?> entityclazz){
        ms.getResultSetType();
        return null;
    }

    public static BoundSql copyFromBoundSql(MappedStatement ms,BoundSql boundSQL,String new_sql){
        ReflectUtil.setFieldValue(boundSQL, "sql", new_sql);
        return boundSQL;
    }

    public static MappedStatement copyFromMappedStatement(MappedStatement ms,BoundSql newBoundSql){
//        ReflectUtil.setFieldValue(ms, "boundSql", newBoundSql);
        return ms;
    }

}

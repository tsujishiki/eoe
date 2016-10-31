package top.sdaily.core.mybatis;

import top.sdaily.core.mybatis.annotation.MyBatisColumn;
import top.sdaily.core.mybatis.annotation.MyBatisRepository;
import top.sdaily.core.mybatis.annotation.MybatisID;
import top.sdaily.core.mybatis.annotation.MybatisTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by soya on 2016/10/29.
 */
public class MapperSqlHelper {

    public static final Class<? extends Annotation> MYBATISREPOSITORY = MyBatisRepository.class;
    public static final Class<? extends Annotation> MYBATISTABLE = MybatisTable.class;
    public static final Class<? extends Annotation> MYBATISCOLUMN = MyBatisColumn.class;
    public static final Class<? extends Annotation> MYBATISID = MybatisID.class;


    public  String getUpdateSQL(){
        return null;
    }
    /**
     * 传入mapper接口class
     * @param clazz
     * @return
     */
    private String insertEntity(Class<?> clazz){
        StringBuilder sql = new StringBuilder();
        StringBuilder intosql = new StringBuilder();
        StringBuilder valuessql = new StringBuilder();
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("INSERT INTO "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("INSERT INTO "+clazz.getName());
            }else{
                sql.append("INSERT INTO "+antable.value());
            }
        }
        Field[] files = clazz.getDeclaredFields();
        intosql.append("(");
        for(Field file : files){
            file.setAccessible(true);
            if(file.isAnnotationPresent(MYBATISCOLUMN)){
                MyBatisColumn anColumn = (MyBatisColumn)file.getAnnotation(MYBATISCOLUMN);
                if(!anColumn.isAutoKey()){
                    if(anColumn.value().equals("")){
                        intosql.append(file.getName()+",");
                    }else{
                        intosql.append(anColumn.value()+",");
                    }
                }
            }else{
                intosql.append(file.getName()+",");
            }
        }
        valuessql.append(" values (");
        for(Field file : files){
            file.setAccessible(true);
            if(file.isAnnotationPresent(MYBATISCOLUMN)){
                MyBatisColumn anColumn = (MyBatisColumn)file.getAnnotation(MYBATISCOLUMN);
                if(!anColumn.isAutoKey()){
                    if(anColumn.value().equals("")){
                        valuessql.append("#{"+file.getName()+"},");
                    }else{
                        valuessql.append("#{"+file.getName()+"},");
                    }
                }
            }else{
                valuessql.append("#{"+file.getName()+"},");
            }

        }
        return sql.append(intosql.substring(0, intosql.length()-1)).append(") ").append(valuessql.substring(0, valuessql.length()-1)).append(")").toString();
    }

    /**
     * 传入mapper接口class
     * @param clazz
     * @return
     */
    private  String updateEntityById(Class<?> clazz){
        StringBuilder sql = new StringBuilder();
        StringBuilder set = new StringBuilder();
        StringBuilder wheresql = new StringBuilder();
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("UPDATE  "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("UPDATE  "+clazz.getName());
            }else{
                sql.append("UPDATE  "+antable.value());
            }
        }
        Field[] files = clazz.getDeclaredFields();
        set.append(" set ");
        wheresql.append(" where 1=1 ");
        for(Field file : files){
            file.setAccessible(true);
            if(file.isAnnotationPresent(MYBATISCOLUMN)){
                MyBatisColumn anColumn = (MyBatisColumn)file.getAnnotation(MYBATISCOLUMN);
                if(!anColumn.isID()){//判断字段不为主键
                    if(anColumn.value().equals("")){
                        set.append(file.getName()+" = #{"+file.getName()+"} ,");
                    }else{
                        set.append(anColumn.value()+" = #{"+file.getName()+"} ,");
                    }
                }else{
                    if(anColumn.value().equals("")){
                        wheresql.append(" and "+file.getName()+" = #{"+file.getName()+"},");
                    }else{
                        wheresql.append(" and "+anColumn.value()+" = #{"+anColumn.value()+"},");
                    }
                }
            }else{
                set.append(file.getName()+" = #{"+file.getName()+"} ,");
            }
        }
        if(wheresql.equals(" where 1=1 ")){
            throw new RuntimeException("实体变量没有设置ID字段值");
        }
        sql.append(set.substring(0, set.length()-1)).append(wheresql.substring(0, wheresql.length()-1));
        return sql.toString();
    }
    private String findEntityAll(Class<?> clazz,Object args,boolean islike){
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSQL = new StringBuilder(" where 1=1 ");
        StringBuffer orderby = new StringBuffer();
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("select * from  "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("select * from  "+clazz.getSimpleName());
            }else{
                sql.append("select * from  "+antable.value());
            }
        }

        if(args != null){
            Conditions<String,Object> conditions = (Conditions<String,Object>) args;

            for (Object entry : conditions.entrySet()) {
                String key = ((Map.Entry<String, Object>) entry).getKey();
                Object value = ((Map.Entry<String, Object>) entry).getValue();
                if(key.startsWith("_eq_")){
                    if(value instanceof String) {
                        whereSQL.append(MessageFormat.format(" and {0} = #{{1}}''", key.substring(4), key));
                    }else {
                        whereSQL.append(MessageFormat.format(" and {0} = #{{1}}", key.substring(4), key));
                    }
                }else if(key.startsWith("_gt_")){
                    whereSQL.append(MessageFormat.format(" and {0} > #{{1}}", key.substring(4), key));
                }else if(key.startsWith("_gte_")){
                    whereSQL.append(MessageFormat.format(" and {0} >= #{{1}}", key.substring(5), key));
                }else if(key.startsWith("_lt_")){
                    whereSQL.append(MessageFormat.format(" and {0} < #{{1}}", key.substring(4), key));
                }else if(key.startsWith("_lte_")){
                    whereSQL.append(MessageFormat.format(" and {0} <= #{{1}}", key.substring(4), key));
                }else if(key.startsWith("_like_")){
                    whereSQL.append(MessageFormat.format(" and {0} like ''%{1}%''", key.substring(6), value));
                }else if(key.startsWith("_in_")){
                    if(value instanceof String) {
                        whereSQL.append(MessageFormat.format(" and {0} in ({1})", key.substring(6), value));
                    }else{
                        Object[] ins = (Object[])value;
                        StringBuilder values = new StringBuilder();
                        if(ins[0] instanceof String) {
                            for(Object oval : ins){
                                values.append(MessageFormat.format("''{0}'',",oval.toString()));
                            }
                        }else{
                            for(Object oval : ins){
                                values.append(MessageFormat.format("{0},",oval.toString()));
                            }
                        }

                        if(values.length() > 0){
                            values.substring(0,values.length() - 1);
                        }
                        whereSQL.append(MessageFormat.format(" and {0} in ({1})'", key.substring(6), values));
                    }
                }else if(key.startsWith("_order_by")){
                    orderby.append(MessageFormat.format(" order by {0}",value));
                    if(conditions.containsKey("_sort_rule")){
                        orderby.append(" desc");
                    }
                }
                System.out.println("key= " + key + " and value= " + value);
            }

            sql.append(whereSQL.append(orderby));
        }

        /*Object[] paramObjs = (Object[]) ((Map)args).get("array");
        if(paramObjs != null && paramObjs.length>0){
            Object param = paramObjs[0];
            if(param != null ){
                if(param instanceof Map){
                    Map<String,Object> map = (Map)param;
                    if(map.containsKey("orderby")){
                        orderby.append(" order by "+map.get("orderby"));
                    }
                    if(map.containsKey("sortby")){
                        orderby.append(" "+map.get("sortby")+" ");
                    }
                    for(String key : map.keySet())
                    {
                        if(islike)
                            whereSQL.append(" and "+key + " like '%" + map.get(key)+"%',");
                        else whereSQL.append(" and "+key + " = '" + map.get(key)+"',");
                    }
                    sql.append(whereSQL.subSequence(0,whereSQL.length()-1)).append(orderby);
                }else if(param instanceof Conditions){
                    Conditions<String,Object> map = (Conditions)param;
                    if(map.containsKey("orderby")){
                        orderby.append(" order by "+map.get("orderby"));
                    }
                    if(map.containsKey("sortby")){
                        orderby.append(" "+map.get("sortby")+" ");
                    }
                    for(Object key : map.keySet())
                    {
                        if(key.equals("orderby") || key.equals("sortby")){
                            continue;
                        }
                        if(islike)
                            whereSQL.append(" and "+key + " like '%" + map.get(key)+"%',");
                        else whereSQL.append(" and "+key + " = '" + map.get(key)+"',");
                    }
                    sql.append(whereSQL.subSequence(0,whereSQL.length()-1)).append(orderby);
                }
            }
        }*/
        return sql.toString();
    }
    private String findEntityById(Class<?> clazz){
        StringBuilder sql = new StringBuilder();
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("select * from  "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("select * from  "+clazz.getSimpleName());
            }else{
                sql.append("select * from  "+antable.value());
            }
        }
        sql.append("  where 1=1 ");
        Field[] files = clazz.getDeclaredFields();
        boolean falg = false;
        for(Field file : files){
            file.setAccessible(true);
            if(file.isAnnotationPresent(MyBatisColumn.class)){
                MyBatisColumn anColumn = (MyBatisColumn)file.getAnnotation(MYBATISCOLUMN);
                if(anColumn.isID()){//判断字段不为主键
                    falg = true;
                    if(anColumn.value().equals("")){
                        sql.append(" and "+file.getName()+" = #{"+file.getName()+"},");
                    }else{
                        sql.append(" and "+anColumn.value()+" = #{"+file.getName()+"},");
                    }
                }
            }
        }
        if(!falg){
            throw new RuntimeException("不能通過id查詢實體,實體中沒有定義@mybatisID");
        }

        return sql.subSequence(0, sql.length()-1).toString();
    }
    public String deleteById(Class<?> clazz){
        StringBuilder sql = new StringBuilder();
        StringBuilder wheresql = new StringBuilder(" where 1=1 ");
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("delete  "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("delete  "+clazz.getName());
            }else{
                sql.append("delete  "+antable.value());
            }
        }
        Field[] files = clazz.getDeclaredFields();
        for(Field file : files){
            file.setAccessible(true);
            if(file.isAnnotationPresent(MYBATISCOLUMN)){
                MyBatisColumn anColumn = (MyBatisColumn)file.getAnnotation(MYBATISCOLUMN);
                if(anColumn.isID()){//判断字段不为主键
                    if(anColumn.value().equals("")){
                        wheresql.append(" and "+file.getName()+" = #{"+file.getName()+"},");
                    }else{
                        wheresql.append(" and "+anColumn.value()+" = #{"+anColumn.value()+"},");
                    }
                }
            }
        }
        if(wheresql.equals(" where 1=1 ")){
            throw new RuntimeException("实体变量没有设置ID字段值");
        }
        sql.append(wheresql.substring(0,wheresql.length()-1));
        return sql.toString();
    }
    public String queryByVo(Class<?> clazz,Object args,boolean islike){
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSQL = new StringBuilder(" where 1=1 ");
        StringBuilder orderbySQL = new StringBuilder("");
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("select *  from "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("select * from "+clazz.getName());
            }else{
                sql.append("select * from "+antable.value());
            }
        }
        if(args instanceof Map){
            Map map = (Map)args;
            Object[] arr = (Object[])map.get("param3");
            if(arr.length>0){
                Map<String,Object> params = (Map)arr[0];
                if(params.containsKey("orderby")){
                    orderbySQL.append(" order by "+params.get("orderby"));
                }
                if(params.containsKey("sortby")){
                    orderbySQL.append(" "+params.get("sortby")+" ");
                }
                for(String key:params.keySet()){
                    if(key.equals("orderby") || key.equals("sortby")){
                        continue;
                    }
                    if(islike)
                        whereSQL.append(" and "+key + " like '%" + params.get(key)+"%',");
                    else whereSQL.append(" and "+key + " = '" + params.get(key)+"',");
                }
            }
        }

        return sql.append(whereSQL.substring(0, whereSQL.length()-1)).append(orderbySQL).toString();
    }
    public String count(Class<?> clazz,Object args){
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSQL = new StringBuilder(" where 1=1 ");
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("select count(*)  from "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("select count(*) from "+clazz.getName());
            }else{
                sql.append("select count(*) from "+antable.value());
            }
        }
        Object[] paramObjs = (Object[]) ((Map)args).get("array");
        if(paramObjs != null && paramObjs.length>0){
            Object param = paramObjs[0];
            if(param != null ){
                if(param instanceof Map){
                    Map<String,Object> map = (Map)param;
                    for(String key : map.keySet())
                    {
                        whereSQL.append(" and "+key + " = '" + map.get(key)+"',");
                    }
                    sql.append(whereSQL.subSequence(0,whereSQL.length()-1));
                }else if(param instanceof Conditions){
                    Conditions<String,Object> map = (Conditions)param;
                    for(Object key : map.keySet())
                    {
                        whereSQL.append(" and "+key + " = '" + map.get(key)+"',");
                    }
                    sql.append(whereSQL.subSequence(0,whereSQL.length()-1));
                }
            }
        }
        return sql.toString();
    }
    public String deleteByCondition(Class<?> clazz,Object param){
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSQL = new StringBuilder(" where 1=1 ");
        if(!clazz.isAnnotationPresent(MYBATISTABLE)){
            sql.append("delete  "+clazz.getName());
        }else{
            MybatisTable antable = (MybatisTable)clazz.getAnnotation(MYBATISTABLE);
            if(antable.value() == ""){
                sql.append("delete  "+clazz.getName());
            }else{
                sql.append("delete  "+antable.value());
            }
        }
        if(param != null ){
            if(param instanceof Map){
                Map<String,Object> map = (Map)param;
                for(String key : map.keySet())
                {
                    whereSQL.append(" and "+key + " = '" + map.get(key)+"',");
                }
                sql.append(whereSQL.subSequence(0,whereSQL.length()-1));
            }else if(param instanceof Conditions){
                Conditions<String,Object> map = (Conditions)param;
                for(Object key : map.keySet())
                {
                    whereSQL.append(" and "+key + " = '" + map.get(key)+"',");
                }
                sql.append(whereSQL.subSequence(0,whereSQL.length()-1));
            }
        }
        if(whereSQL.equals(" where 1=1 ")){
            throw new RuntimeException("实体变量没有设置ID字段值");
        }
        return sql.toString();
    }
    private static MapperSqlHelper App(){
        return new MapperSqlHelper();
    }
    public static String getExecuSQL(Class<?> clazz, String mapperDBsql,Object param) {
        if(mapperDBsql.equals("MapperGD.find.entitys")){
            return MapperSqlHelper.App().findEntityAll(clazz,param,false);//条件查询实体列表
        }else if(mapperDBsql.equals("MapperGD.find.entityById")){
            return MapperSqlHelper.App().findEntityById(clazz);//id查询实体
        }else if(mapperDBsql.equals("MapperGD.find.ListByLike")){
            return MapperSqlHelper.App().findEntityAll(clazz,param,true);//条件查询实体列表
        }else if(mapperDBsql.equals("MapperGD.insert.entity")){
            return MapperSqlHelper.App().insertEntity(clazz);//保存单一实体
        }else if(mapperDBsql.equals("MapperGD.update.entity")){
            return MapperSqlHelper.App().updateEntityById(clazz);//保存单一实体
        }else if(mapperDBsql.equals("MapperGD.update.entity.condistion")){
            return mapperDBsql;
        }else if(mapperDBsql.equals("MapperGD.delete.id")){
            return MapperSqlHelper.App().deleteById(clazz);
        }else if(mapperDBsql.equals("MapperGD.delete.condition")){
            return MapperSqlHelper.App().deleteByCondition(clazz,param);
        }else if(mapperDBsql.equals("MapperGD.count.condition")){
            return MapperSqlHelper.App().count(clazz,param);
        }else if(mapperDBsql.equals("MapperGD.find.entity.queryByVo")){
            return MapperSqlHelper.App().queryByVo(clazz,param,false);
        }else if(mapperDBsql.equals("MapperGD.find.entity.queryByVoLike")){
            return MapperSqlHelper.App().queryByVo(clazz,param,true);
        }
        return null;
    }
    //预留接口
    private  String updateEntityByConditions(Class<?> clazz,Object param){

        return null;
    }
    public void getParam(Object param){
        StringBuffer bf = new StringBuffer();
        if (isPrimitiveType(param.getClass())) {
            bf.append(param);
        } else if (param instanceof Map) {
            Map<String,Object> map = (Map)param;
        }
    }

    public static boolean isPrimitiveType(Class clazz) {
        return clazz != null && (clazz.isPrimitive() || clazz.equals(Long.class) || clazz.equals(Integer.class)
                || clazz.equals(Short.class) || clazz.equals(Byte.class) || clazz.equals(Double.class)
                || clazz.equals(Float.class) || clazz.equals(Boolean.class) || clazz.equals(Character.class) || clazz.equals(String.class));
    }
    public static void main(String[] args) {

//		Object object = null;
//		if (isPrimitiveType(object.getClass())) {
//        } else if (object instanceof Map) {
//        }
//        Field[] files = User.class.getDeclaredFields();
//        User u = new User();
//        try {
//            System.out.println(FieldUtils.readDeclaredField(u, "id",true));
//        } catch (IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

}

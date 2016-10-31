package top.sdaily.core.mybatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by soya on 2016/10/29.
 */
public interface IGenericDao<T,PK> {


    @Select("MapperGD.find.entityById")
    T findById(PK id);

    @Select("MapperGD.find.entitys.all")
    List<T> findAll();

    @Select("MapperGD.find.entitys")
    List<T> find(Conditions conditions);

    /*@Select("MapperGD.find.ListByLike")
    List<T> findLikeEntity(Object... obj);*/

    @Select("MapperGD.find.count")
    void count(Conditions conditions);

    @Insert("MapperGD.insert.entity")
    void insert(T t);

    @Insert("MapperGD.insert.entitys")
    void insertByBatch(List<T> t);

    @Update("MapperGD.update.entity")
    void update(T entity);

    @Update("MapperGD.update.entitys")
    void updateByBatch(List<T> entity);

    @Delete("MapperGD.delete.id")
    void deleteById(PK id);

    @Delete("MapperGD.delete.condition")
    void deleteByCondition(Conditions conditions);

    /*@Select("MapperGD.find.entity.queryByVo")
    public PageMyBatis<T> queryByVo(int i,int c,Object... obj);

    @Select("MapperGD.find.entity.queryByVoLike")
    public PageMyBatis<T> LikequeryByVo(int i,int c,Object... obj);*/


}

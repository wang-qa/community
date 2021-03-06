package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag) " +
            "values (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size); // Integer 实际页码 size 分页数
//    List<Question> list( Integer offset,  Integer size); // Integer 实际页码 size 分页数

    @Select("select count (1) from question")
    Integer count(); // 查询总问题数

    @Select("select * from question where creator = #{userId} limit #{offset}, #{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("select count (1) from question where creator = #{userId}")
    Integer countByUserId(@Param("userId") Integer userId); // 查询当前用户问题总数

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id") Integer id);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{id} where id = #{id}")
    void update(Question question); // 更新问题
}

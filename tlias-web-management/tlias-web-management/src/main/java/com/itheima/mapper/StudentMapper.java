package com.itheima.mapper;

import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("""
            <script>
            select
                s.id,
                s.name,
                s.no,
                s.gender,
                s.phone,
                s.degree,
                s.id_card as idCard,
                s.is_college as isCollege,
                s.address,
                s.graduation_date as graduationDate,
                s.violation_count as violationCount,
                s.violation_score as violationScore,
                s.clazz_id as clazzId,
                s.create_time as createTime,
                s.update_time as updateTime,
                c.name as clazzName
            from student s
            left join clazz c on s.clazz_id = c.id
            <where>
                <if test="name != null and name != ''">
                    and s.name like concat('%', #{name}, '%')
                </if>
                <if test="degree != null">
                    and s.degree = #{degree}
                </if>
                <if test="clazzId != null">
                    and s.clazz_id = #{clazzId}
                </if>
            </where>
            order by s.update_time desc, s.id desc
            </script>
            """)
    List<Student> pageList(StudentQueryParam studentQueryParam);

    @Select("select count(*) from student where clazz_id = #{clazzId}")
    Integer countByClazzId(Integer clazzId);

    @Select("""
            select
                s.id,
                s.name,
                s.no,
                s.gender,
                s.phone,
                s.degree,
                s.id_card as idCard,
                s.is_college as isCollege,
                s.address,
                s.graduation_date as graduationDate,
                s.violation_count as violationCount,
                s.violation_score as violationScore,
                s.clazz_id as clazzId,
                s.create_time as createTime,
                s.update_time as updateTime,
                c.name as clazzName
            from student s
            left join clazz c on s.clazz_id = c.id
            where s.id = #{id}
            """)
    Student getById(Integer id);

    @Select("""
            select degree, count(*) as total
            from student
            group by degree
            order by degree
            """)
    List<java.util.Map<String, Object>> countStudentDegreeData();

    @Select("""
            select c.name as clazzName, count(s.id) as total
            from clazz c
            left join student s on c.id = s.clazz_id
            group by c.id, c.name
            order by c.id
            """)
    List<java.util.Map<String, Object>> countStudentCountData();

    @Insert("""
            insert into student(
                name, no, gender, phone, degree, id_card, is_college, address,
                graduation_date, clazz_id, violation_count, violation_score, create_time, update_time
            ) values (
                #{name}, #{no}, #{gender}, #{phone}, #{degree}, #{idCard}, #{isCollege}, #{address},
                #{graduationDate}, #{clazzId}, #{violationCount}, #{violationScore}, #{createTime}, #{updateTime}
            )
            """)
    void insert(Student student);

    @Update("""
            update student
            set violation_count = violation_count + 1,
                violation_score = violation_score + #{score},
                update_time = now()
            where id = #{id}
            """)
    int increaseViolation(@Param("id") Integer id, @Param("score") Integer score);

    @Delete("""
            <script>
            delete from student
            where id in
            <foreach collection='ids' item='id' open='(' separator=',' close=')'>
                #{id}
            </foreach>
            </script>
            """)
    void deleteByIds(@Param("ids") List<Integer> ids);
}

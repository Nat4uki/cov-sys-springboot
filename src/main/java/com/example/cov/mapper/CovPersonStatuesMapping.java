package com.example.cov.mapper;

import com.example.cov.entity.CovPerson;
import com.example.cov.entity.CovPersonStatues;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CovPersonStatuesMapping {
    /**
     * 更新状态
     * @param
     */
    @Update("UPDATE cov_person_statues set refreshTime=#{refreshTime},personStatues=#{personStatues} where personKey=#{personKey}")
    void updatePersonStatues(CovPersonStatues covPersonStatues);

    /**
     * 通过id删除一个人员信息
     * @param personKey
     */
    @Delete("DELETE from cov_person_statues where personKey = #{personKey}")
    void delete(String personKey);

    /**
     * 根据personKey判断是否存在成员状态
     * @param personKey
     * @return
     */
    @Select("SELECT COUNT(*) FROM cov_person_statues WHERE personKey=#{personKey};")
    int countOfKey(String personKey);

    /**
     * 如若不存则添加状态
     * @param
     */
    @Insert("INSERT into cov_person_statues(personKey,refreshTime,personStatues) values (#{personKey},#{refreshTime},#{personStatues})")
    void addPersonStatues(CovPersonStatues covPersonStatues);

    /**
     * 查询所有人
     * @return 所有人
     */
    @Select("select * from cov_person_statues ORDER BY personKey")
    List<CovPersonStatues> findAll();


}

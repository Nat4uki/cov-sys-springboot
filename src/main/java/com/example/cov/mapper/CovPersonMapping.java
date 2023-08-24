package com.example.cov.mapper;

import com.example.cov.entity.CovPerson;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CovPersonMapping{
    /**
     * 增加一个人员信息
     * @param covPerson
     */
    @Insert("INSERT into cov_people_form(targetId,targetName,targetAge,targetSex, occupationType) values (#{targetId},#{targetName},#{targetAge},#{targetSex},#{occupationType})")
    void save(CovPerson covPerson);
    /**
     * 增加一个人员信息，不包括ID
     * @param covPerson
     */
    @Insert("INSERT into cov_people_form(targetName,targetAge,targetSex, occupationType) values (#{targetName},#{targetAge},#{targetSex},#{occupationType})")
    void noIdSave(CovPerson covPerson);
    /**
     * 通过id删除一个人员信息
     * @param targetId
     */
    @Delete("DELETE from cov_people_form where targetId = #{targetId}")
    void delete(int targetId);

    /**
     * 更新信息
     * @param covPerson
     */
    @Update("UPDATE cov_people_form set targetName=#{targetName},targetAge=#{targetAge},targetSex=#{targetSex},occupationType=#{occupationType}")
    void update(CovPerson covPerson);

    /**
     * 查询所有人
     * @return 所有人
     */
    @Select("select * from cov_people_form ORDER BY targetId")
    List<CovPerson> findAll();

    /**
     * 通过id查询对象
     * @param id
     */
    @Select("select * from cov_people_form where id=#{id}")
    CovPerson findById(int id);

    /**
     * 判断id是否存在
     * @param targetId
     * @return
     */
    @Select("SELECT COUNT(*) FROM cov_people_form WHERE targetId=#{targetId};")
    int countOfId(String targetId);

    /**
     * 查询所有人
     * @return 所有人
     */
    @Select("select targetId from cov_people_form ORDER BY targetId")
    List<String> findAllId();
}

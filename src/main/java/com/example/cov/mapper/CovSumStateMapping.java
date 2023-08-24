package com.example.cov.mapper;

import com.example.cov.entity.CovSumState;
import com.example.cov.entity.CovSumState;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CovSumStateMapping {
    /**
     * 更新状态
     * @param
     */
    @Update("UPDATE cov_sum_seir set sType=#{sType},eType=#{eType},iType=#{iType},rType=#{rType} where date=#{date}")
    void updateState(CovSumState covSumState);

    /**
     * 判断是否存在
     * @param date
     * @return
     */
    @Select("SELECT COUNT(*) FROM cov_sum_seir WHERE date=#{date};")
    int countOfKey(String date);

    /**
     * 如若不存则添加状态
     * @param
     */
    @Insert("INSERT into cov_sum_seir(date,sType,eType,iType,rType) values (#{date},#{sType},#{eType},#{iType},#{rType})")
    void addState(CovSumState covSumState);

    /**
     * 查询所有天的状态统计
     * @return 所有人
     */
    @Select("select * from cov_sum_seir ORDER BY date")
    List<CovSumState> getCovSumState();
}

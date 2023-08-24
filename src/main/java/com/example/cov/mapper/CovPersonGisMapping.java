package com.example.cov.mapper;

import com.example.cov.entity.CovPersonGis;
import com.example.cov.entity.FilterTime;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CovPersonGisMapping {
    /**
     * 表存在则删除（弃用-存储方式改变）
     * @param name
     */
    @Update("DROP TABLE IF EXISTS gis_${name};")
    void deleteExistTab(String name);

    /**
     * 通过id删除一个成员的gis数据
     * @param personKey
     */
    @Delete("DELETE from cov_person_gis where personKey = #{personKey}")
    void delete(String personKey);

    /**
     * 创建数据库表（弃用-存储方式改变）
     * @parmas name
     */
    @Update("CREATE TABLE gis_${name} (" +
            "  indexId int(6) NOT NULL DEFAULT '0'," +
            "  indexTime datetime DEFAULT NULL," +
            "  indexGps varchar(30) DEFAULT NULL," +
            "  personStatus int(2) DEFAULT NULL," +
            "  PRIMARY KEY (indexId)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;")
    void createTab(String name);

    /**
     * 添加数据
     * @param name
     * @param covPersonGis
     */
    @Insert("INSERT into ${name}(personKey,indexTime,lng,lat,personStatus) values (#{covPersonGis.personKey},#{covPersonGis.indexTime},#{covPersonGis.lng},#{covPersonGis.lat},#{covPersonGis.personStatus})")
    void addData(String name, CovPersonGis covPersonGis);

    /**
     * 分页查询
     */
    @Select("SELECT * from cov_person_gis limit #{curPage}, #{pageSize}")
    List<CovPersonGis> getCovGisData(int curPage, int pageSize);

    /**
     * 时间区间筛选
     */
    @Select("SELECT personKey,MIN(indexTime) as indexTime,lng,lat,personStatus from cov_person_gis WHERE indexTime>=#{startTime} and indexTime<#{endTime} GROUP BY personKey")
    List<CovPersonGis> timeFilterCovGisData(FilterTime filterTime);

    /**
     * 根据key选择个人信息
     */
    @Select("SELECT * from cov_person_gis WHERE personKey in (${personKey})")
    List<CovPersonGis> keyFilterCovGisData(String personKey);

    /**
     * 根据key获取最新的一条数据
     */
    @Select("SELECT * from cov_person_gis WHERE personKey = ${personKey} ORDER BY indexTime DESC LIMIT 1")
    List<CovPersonGis> getLeastDataByKey(String personKey);

    /**
     * 根据当前日期获取所有人最新的一条数据
     */
    @Select("SELECT personKey,MAX(indexTime) as indexTime,lng,lat,personStatus from cov_person_gis WHERE indexTime>=#{startTime} and indexTime<#{endTime} GROUP BY personKey")
    List<CovPersonGis> getLeastData(FilterTime filterTime);

    /**
     * 判断数据是否存在
     */
    @Select("SELECT COUNT(*) FROM cov_person_gis WHERE personKey=#{personKey} LIMIT 1;")
    int countOfKey(String personKey);
}

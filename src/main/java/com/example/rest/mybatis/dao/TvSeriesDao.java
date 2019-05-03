package com.example.rest.mybatis.dao;

import com.example.rest.mybatis.pojo.TvSeries;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvSeriesDao {

    @Select("select * from tv_series")
    List<TvSeries> getAll();

    int update(TvSeries tvSeries);
}

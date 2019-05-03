package com.example.rest.mybatis.service;

import com.example.rest.mybatis.pojo.TvSeries;
import com.example.rest.mybatis.dao.TvSeriesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvSeriesService {
    @Autowired
    TvSeriesDao tvSeriesDao;

    public List<TvSeries> getAllTvSeries() {
        return tvSeriesDao.getAll();
    }

    public int updateTvSeries(TvSeries tvSeries) {
        return tvSeriesDao.update(tvSeries);
    }
}

package com.example.rest.mybatis.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

public class TvSeries {
    @Null private Integer id;  // 表示客户端可以不传id
    @NotNull private String name;  // 表示客户端必须传名称
    @DecimalMin("1") private int seasonCount;  // 总共多少季
    @Valid @NotNull @Size(min = 2)  // @Valid表示要级联校验；@Size(min = 2)表示这个列表至少要有2项内容，否则不通过校验
    private List<TvCharacter> tvCharacters;
    // 设置日期格式化为Json格式
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    // @Past表示只接受过去的时间，比当前时间晚的被认为不合格
    @Past private Date originRelease;  // 发行日期

    public TvSeries(){
    }

    public TvSeries(int id, String name, int seasonCount, Date originRelease) {
        this.id = id;
        this.name = name;
        this.seasonCount = seasonCount;
        this.originRelease = originRelease;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public Date getOriginRelease() {
        return originRelease;
    }

    public void setOriginRelease(Date originRelease) {
        this.originRelease = originRelease;
    }

    public List<TvCharacter> getTvCharacters() {
        return tvCharacters;
    }

    public void setTvCharacters(List<TvCharacter> tvCharacters) {
        this.tvCharacters = tvCharacters;
    }

    @Override
    public String toString() {
        return "TvSeriesDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seasonCount=" + seasonCount +
                ", originRelease=" + originRelease +
                '}';
    }
}

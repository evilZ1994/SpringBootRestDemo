package com.example.rest;

import com.example.rest.mybatis.controller.TvSeriesController;
import com.example.rest.mybatis.dao.TvCharacterDao;
import com.example.rest.mybatis.dao.TvSeriesDao;
import com.example.rest.mybatis.pojo.TvSeries;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppTests {

    /**
     * @MockBean 可给当前的spring context装载一个假的bean上去替代原有的同名bean
     * mock了dao的所有bean后，数据访问层就别接管了，从而实现测试不受具体数据库内数据值影响的效果
     */
    @MockBean
    TvSeriesDao tvSeriesDao;
    @MockBean
    TvCharacterDao tvCharacterDao;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TvSeriesController tvSeriesController;

    @Test
    public void testGetAll() throws Exception {
        List<TvSeries> list = new ArrayList<>();
        TvSeries ts = new TvSeries();
        ts.setName("POI");
        list.add(ts);

        Mockito.when(tvSeriesDao.getAll()).thenReturn(list);

        // 下面这个是相当于在启动项目后，执行 GET /tvseries , 被测模块是web控制层，因为web控制层会调用业务逻辑层，所以业务逻辑层也会被测试
        // 业务逻辑层调用了被mock出来的数据访问层桩模块
        // 如果想仅仅测试web控制层，（例如业务逻辑层尚未编码完毕），可以mock一个业务逻辑层的桩模块
        mockMvc.perform(MockMvcRequestBuilders.get("/tvseries")).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("POI")));
        // 上面这几句和字面意思一致，期望状态是200，返回值包含POI三个字母，桩模块返回的一个电视剧名字是POI，如果测试正确是包含这三个字母的。
    }
}

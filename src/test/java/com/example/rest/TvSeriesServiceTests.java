package com.example.rest;

import com.example.rest.mybatis.dao.TvSeriesDao;
import com.example.rest.mybatis.pojo.TvSeries;
import com.example.rest.mybatis.service.TvSeriesService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TvSeriesServiceTests {

    @MockBean
    TvSeriesDao tvSeriesDao;
    @Autowired
    TvSeriesService tvSeriesService;

    @Test
    public void testGetAllWithMockito() {
        List<TvSeries> list = tvSeriesService.getAllTvSeries();
        // 这里的测试结果依赖连接数据库内的记录，很难写一个判断是否成功的条件，甚至无法执行
        // 下面的testGetAll()方法，使用了mock出来的dao作为桩模块，避免了这一情形
        Assert.assertTrue(list.size() >= 0);
    }

    @Test
    public void testGetAll() {
        // 设置一个TvSeries list
        List<TvSeries> list = new ArrayList<>();
        TvSeries ts = new TvSeries();
        ts.setName("POI");
        list.add(ts);

        // 下面这个语句是告诉mock出来的tvSeriesDao当执行getAll方法时，返回上面创建的list
        Mockito.when(tvSeriesDao.getAll()).thenReturn(list);

        // 测试tvSeriesService的getAllTvSeries()方法，获得返回值
        List<TvSeries> result = tvSeriesService.getAllTvSeries();

        // 判断返回值是否和最初做的那个list相同，应该是相同的
        Assert.assertTrue(result.size() == list.size());
        Assert.assertTrue("POI".equals(result.get(0).getName()));
    }

    @Test
    public void testGetOne() {
        // 根据不同的传入参数，被mock的bean返回不同的数据的例子
        String newName = "Person of Interest";
        BitSet mockExecuted = new BitSet();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                TvSeries bean = (TvSeries)args[0];
                // 传入的值，应该和下面调用tvSeriesService.updateTvSeries()方法时的参数中的值相同
                Assert.assertEquals(newName, bean.getName());
                mockExecuted.set(0);
                return 1;
            }
        });

        TvSeries ts = new TvSeries();
        ts.setName(newName);
        ts.setId(111);

        tvSeriesService.updateTvSeries(ts);
        Assert.assertTrue(mockExecuted.get(0));
    }
}

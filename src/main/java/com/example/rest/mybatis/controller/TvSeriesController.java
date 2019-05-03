package com.example.rest.mybatis.controller;

import com.example.rest.mybatis.pojo.TvSeries;
import com.example.rest.mybatis.service.TvSeriesService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/tvseries")
public class TvSeriesController {
    // 日志
    private static final Log log = LogFactory.getLog(TvSeriesController.class);

    @Autowired
    TvSeriesService tvSeriesService;

    @GetMapping
    public List<TvSeries> getAll() {
        // 示例：http://localhost:8080/tvseries
        if (log.isTraceEnabled()) {
            log.trace("getAll()被调用了");
        }
        List<TvSeries> list = tvSeriesService.getAllTvSeries();
        if (log.isTraceEnabled()) {
            log.trace("查询到" + list.size() + "条记录");
        }
        return list;
    }

    @GetMapping("/{id}")
    public TvSeries getOne(@PathVariable int id) {
        // 示例：http://localhost:8080/tvseries/101
        if (log.isTraceEnabled()) {
            log.trace("getOne " + id);
        }
        // TODO: 查找数据库
        if (id == 101) {
            return createWestWorld();
        } else if (id == 102) {
            return createPoi();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * @Valid 注解表示需要验证传入的参数TvSeries，需要验证的field在TvSeries内通过注解定义
     * @param tvSeries
     * @return
     */
    @PostMapping
    public TvSeries insertOne(@Valid @RequestBody TvSeries tvSeries) {
        // Post请求
        if (log.isTraceEnabled()) {
            log.trace("这里应该写新增到数据库的代码，传递进来的参数是：" + tvSeries);
        }
        tvSeries.setId(9999);
        return tvSeries;
    }

    @PutMapping("/{id}")
    public TvSeries updateOne(@PathVariable int id, @RequestBody TvSeries tvSeries) {
        if (log.isTraceEnabled()) {
            log.trace("updateOne " + tvSeries);
        }
        if (id == 101 || id == 102) {
            //TODO: 根据tvSeries的内容更新数据库，更新后返回新内容
            return createPoi();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteOne(@PathVariable int id, HttpServletRequest request, @RequestParam(value = "delete_reason", required = false)String deleteReason) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("deleteOne " + id);
        }
        Map<String, String> result = new HashMap<>();
        if (id == 101) {
            //TODO: 执行删除代码
            result.put("message", "#101被" + request.getRemoteAddr() + "删除（原因：" + deleteReason + ")");
        } else if (id == 102) {
            throw new RuntimeException("#102不能被删除");
        } else {
            throw new ResourceNotFoundException();
        }
        return result;
    }

    @PostMapping(value = "/{id}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addPhoto(@PathVariable int id, @RequestParam("photo")MultipartFile imgFile) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("接收到文件 " + id + ", 收到文件： " + imgFile.getOriginalFilename());
        }
        // 保存文件
        FileOutputStream fos = new FileOutputStream("target/" + imgFile.getOriginalFilename());
        IOUtils.copy(imgFile.getInputStream(), fos);
        fos.close();
    }

    @GetMapping(value = "/{id}/icon", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getIcon(@PathVariable int id) throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("getIcon(" + id + ")");
        }
        String iconFile = "target/test.jpg";
        InputStream is = new FileInputStream(iconFile);
        return IOUtils.toByteArray(is);
    }

    private TvSeries createPoi() {
        Calendar c = Calendar.getInstance();
        c.set(2011, Calendar.SEPTEMBER, 22, 0, 0);
        return new TvSeries(102, "Person of Interest", 5, c.getTime());
    }

    private TvSeries createWestWorld() {
        Calendar c = Calendar.getInstance();
        c.set(2016, Calendar.OCTOBER, 2, 0, 0);
        return new TvSeries(101, "West World", 1, c.getTime());
    }
}

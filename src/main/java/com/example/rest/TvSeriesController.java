package com.example.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/tvseries")
public class TvSeriesController {
    // 日志
    private static final Log log = LogFactory.getLog(TvSeriesController.class);

    @GetMapping
    public List<TvSeriesDto> getAll() {
        // 示例：http://localhost:8080/tvseries
        if (log.isTraceEnabled()) {
            log.trace("getAll()被调用了");
        }
        List<TvSeriesDto> list = new ArrayList<>();
        list.add(createWestWorld());
        list.add(createPoi());
        return list;
    }

    @GetMapping("/{id}")
    public TvSeriesDto getOne(@PathVariable int id) {
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
     * @Valid 注解表示需要验证传入的参数TvSeriesDto，需要验证的field在TvSeriesDto内通过注解定义
     * @param tvSeriesDto
     * @return
     */
    @PostMapping
    public TvSeriesDto insertOne(@Valid @RequestBody TvSeriesDto tvSeriesDto) {
        // Post请求
        if (log.isTraceEnabled()) {
            log.trace("这里应该写新增到数据库的代码，传递进来的参数是：" + tvSeriesDto);
        }
        tvSeriesDto.setId(9999);
        return tvSeriesDto;
    }

    @PutMapping("/{id}")
    public TvSeriesDto updateOne(@PathVariable int id, @RequestBody TvSeriesDto tvSeriesDto) {
        if (log.isTraceEnabled()) {
            log.trace("updateOne " + tvSeriesDto);
        }
        if (id == 101 || id == 102) {
            //TODO: 根据tvSeriesDto的内容更新数据库，更新后返回新内容
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

    private TvSeriesDto createPoi() {
        Calendar c = Calendar.getInstance();
        c.set(2011, Calendar.SEPTEMBER, 22, 0, 0);
        return new TvSeriesDto(102, "Person of Interest", 5, c.getTime());
    }

    private TvSeriesDto createWestWorld() {
        Calendar c = Calendar.getInstance();
        c.set(2016, Calendar.OCTOBER, 2, 0, 0);
        return new TvSeriesDto(101, "West World", 1, c.getTime());
    }
}

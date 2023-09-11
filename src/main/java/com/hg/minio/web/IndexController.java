package com.hg.minio.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hougen
 * @since 2023/08/24 23:16
 */
@RequestMapping
@Controller
public class IndexController {

    @GetMapping
    public String index() {
        return "/upload.html";
    }
}

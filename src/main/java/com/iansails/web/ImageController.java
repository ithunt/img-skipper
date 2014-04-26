package com.iansails.web;
import com.iansails.img.Image;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

@RooWebJson(jsonObject = Image.class)
@Controller
@RequestMapping("/i")
public class ImageController implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody byte[] testphoto() throws IOException {
        InputStream in = resourceLoader.getResource("file:/var/imgs/42.jpg").getInputStream();
        return IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/{file_name}", method = RequestMethod.GET)
    public @ResponseBody byte[] testfile() throws IOException {
        InputStream in = resourceLoader.getResource("file:/var/imgs/42.jpg").getInputStream();
        return IOUtils.toByteArray(in);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}

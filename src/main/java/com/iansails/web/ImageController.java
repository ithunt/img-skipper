package com.iansails.web;

import com.iansails.Responses;
import com.iansails.img.Image;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.util.StringUtils.isEmpty;

@RooWebJson(jsonObject = Image.class)
@Controller
@RequestMapping("/i")
public class ImageController implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> testphoto() throws IOException {
        InputStream in = resourceLoader.getResource("file:/var/imgs/index.html").getInputStream();
        return Responses.file(in);
    }

    @RequestMapping(value = "/{filename:.+}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> testfile(@PathVariable String filename) throws IOException {

        if(isEmpty(filename)) return null;

        InputStream in = resourceLoader.getResource("file:/var/imgs/" + filename).getInputStream();

        return Responses.file(in);

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}

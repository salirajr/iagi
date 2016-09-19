package com.rj.sysinvest.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.sysinvest.model.Tower;
import com.rj.sysinvest.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Resource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
@Data
public class LayoutTemplateInfoRepository {

    @Resource
    private ObjectMapper objectMapper;
    @Value("${layoutTemplateDirectory}")
    private String layoutTemplateDirectory;

    public LayoutTemplateInfo get(Tower tower, String floor) throws IOException {
        return get(tower.getSite().getName(), tower.getName(), floor);
    }

    public LayoutTemplateInfo get(String site, String tower, String floor) throws IOException {
        // site_tower_floor.json
        String fileName = StringUtil.buildString(null, "_", ".json", site, tower, floor);
        Path path = Paths.get(layoutTemplateDirectory, fileName);
        if (!Files.exists(path)) {
            // site_tower.json
            fileName = StringUtil.buildString(null, "_", ".json", site, tower);
            path = Paths.get(layoutTemplateDirectory, fileName);
        }
        return objectMapper.readValue(Files.newInputStream(path), LayoutTemplateInfo.class);
    }

    public Path getImagePath(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        return Paths.get(layoutTemplateDirectory, layoutTemplateInfo.getTemplatePath());
    }

    public InputStream getImageInputStream(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        return Files.newInputStream(getImagePath(layoutTemplateInfo));
    }

    public byte[] getImageBytes(LayoutTemplateInfo layoutTemplateInfo) throws IOException {
        return Files.readAllBytes(getImagePath(layoutTemplateInfo));
    }

}

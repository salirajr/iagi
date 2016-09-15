/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rj.sysinvest.akad.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author salirajr
 */
@Service
@Data
public class CopyNationalIdentityFileStore {

    @Value("${fileStoreageDirectory}")
    private String fileStoreageDirectory;

    public String store(byte[] bytes, String socialId) throws IOException {
        String tPath = fileStoreageDirectory + "/" + socialId;
        Path path = Paths.get(tPath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, bytes);
        return tPath;
    }

}

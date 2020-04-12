package com.gy.fileserver.service;

import com.gy.fileserver.bean.FileInfo;
import com.gy.fileserver.bean.ReturnFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件服务
 */
public interface FileService {
    FileInfo uploadFile(HttpServletRequest req);
    ReturnFile downloadFile(String uuid);
    FileInfo getFileInfo(String uuid);
    List<FileInfo> listFileInfo(Integer count);
}

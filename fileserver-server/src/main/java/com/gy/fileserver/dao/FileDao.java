package com.gy.fileserver.dao;

import com.gy.fileserver.bean.FileInfo;

import java.util.List;

public interface FileDao {
    void insert(FileInfo fileInfo);
    List<FileInfo> listCount(Integer count);
    FileInfo findFileByUuid(String uuid);
}

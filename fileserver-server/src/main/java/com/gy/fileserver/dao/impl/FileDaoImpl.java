package com.gy.fileserver.dao.impl;

import com.gy.fileserver.bean.FileInfo;
import com.gy.fileserver.dao.FileDao;
import com.gy.fileserver.utils.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDaoImpl implements FileDao {
    @Override
    public void insert(FileInfo fileInfo) {
        Connection conn = DBConnUtil.getConnection();
        String sql = "INSERT INTO t_file VALUES(?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, fileInfo.getUuid());
            ps.setInt(2,fileInfo.getSize());
            ps.setString(3,fileInfo.getType());
            ps.setString(4,fileInfo.getOriginName());
            ps.setString(5,fileInfo.getCreateTime());
            ps.setString(6,fileInfo.getSaveAddress());
            ps.setString(7,fileInfo.getDigitEnvelop());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnUtil.release(null, ps, conn);

    }

    @Override
    public List<FileInfo> listCount(Integer count) {
        Connection conn = DBConnUtil.getConnection();
        String sql = "SELECT uuid,size,type,originName,createTime,saveAddress,digitEnvelop FROM t_file ORDER BY createtime DESC";
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        ArrayList<FileInfo> fileInfoList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setMaxRows(count);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setUuid(resultSet.getString("uuid"));
                fileInfo.setSize(resultSet.getInt("size"));
                fileInfo.setType(resultSet.getString("type"));
                fileInfo.setOriginName(resultSet.getString("originName"));
                fileInfo.setCreateTime(resultSet.getTimestamp("createTime") + "");
                fileInfo.setSaveAddress(resultSet.getString("saveAddress"));
                fileInfo.setDigitEnvelop(resultSet.getString("digitEnvelop"));
                fileInfoList.add(fileInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnUtil.release(resultSet, ps, conn);
        return fileInfoList;
    }

    @Override
    public FileInfo findFileByUuid(String uuid) {
        Connection conn = DBConnUtil.getConnection();
        String sql = "SELECT uuid,size,type,originName,createTime,saveAddress ,digitEnvelop FROM t_file WHERE uuid=?";
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        FileInfo fileInfo = new FileInfo();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, uuid);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                fileInfo.setUuid(resultSet.getString("uuid"));
                fileInfo.setSize(resultSet.getInt("size"));
                fileInfo.setType(resultSet.getString("type"));
                fileInfo.setOriginName(resultSet.getString("originName"));
                fileInfo.setCreateTime(resultSet.getTimestamp("createTime") + "");
                fileInfo.setSaveAddress(resultSet.getString("saveAddress"));
                fileInfo.setDigitEnvelop(resultSet.getString("digitEnvelop"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnUtil.release(resultSet, ps, conn);
        return fileInfo;
    }
}

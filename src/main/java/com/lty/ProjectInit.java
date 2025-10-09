package com.lty;

import com.lty.constant.BaseConstant;

/**
 * 项目初始化(移除无关代码)
 */
public class ProjectInit {

    public static void main(String[] args) {
        String projectRootDirectory = BaseConstant.PROJECT_ROOT_DIRECTORY;
        System.out.println("项目根目录: " + projectRootDirectory);
        // 删除和book相关的文件
        String[] filesToDelete = {
            "src/main/java/com/lty/controller/BookController.java",
            "src/main/java/com/lty/service/BookService.java",
            "src/main/java/com/lty/service/impl/BookServiceImpl.java",
            "src/main/java/com/lty/model/entity/Book.java",
            "src/main/java/com/lty/mapper/BookMapper.java",
            "src/main/resources/mapper/BookMapper.xml",
        };
        for (String filePath : filesToDelete) {
            String fullPath = projectRootDirectory + "/" + filePath;
            java.io.File file = new java.io.File(fullPath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("已删除文件: " + fullPath);
                } else {
                    System.out.println("无法删除文件: " + fullPath);
                }
            } else {
                System.out.println("文件不存在: " + fullPath);
            }
        }
        System.out.println("项目初始化成功！请手动删除以下文件: src/main/java/com/lty/model/dto/book");
    }
}

package cn.com.tjise.onlineedu.entity.vo.classinfo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 课程文件上传传输实体类
 * @author admin
 */
@Data
public class ClassFileVO
{
    private MultipartFile file;
    private String name;
}

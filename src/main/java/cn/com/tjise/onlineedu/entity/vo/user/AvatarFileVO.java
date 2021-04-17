package cn.com.tjise.onlineedu.entity.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * 头像上传传输类
 *
 * @author Admin
 */
@Data
@ToString
public class AvatarFileVO
{
    /**
     * 头像文件
     */
    @ApiModelProperty(value = "头像文件")
    private MultipartFile file;
    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private String userType;
}

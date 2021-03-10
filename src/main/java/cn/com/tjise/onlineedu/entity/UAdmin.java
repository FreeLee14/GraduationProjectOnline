package cn.com.tjise.onlineedu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UAdmin对象", description="")
public class UAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "管理员id")
    private String adminId;

    @ApiModelProperty(value = "管理员名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "管理员年龄")
    private Integer age;

    @ApiModelProperty(value = "管理员邮箱")
    private String email;

    @ApiModelProperty(value = "头像（存储头像所在服务器地址链接，切勿传入base64）")
    private String avatar;


}

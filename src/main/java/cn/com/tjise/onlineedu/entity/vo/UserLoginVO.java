package cn.com.tjise.onlineedu.entity.vo;

import cn.com.tjise.onlineedu.constant.RoleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@ApiModel(value = "user登录实体", description = "用户登录的传输实体")
public class UserLoginVO implements Serializable
{
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "用户密码")
    private String password;
    @ApiModelProperty(value = "用户权限")
    private RoleEnum roleType;
    
    
}

package cn.com.tjise.onlineedu.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel(value="UTeacher对象", description="")
public class UTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "教师id")
    private String teacherId;
    
    @ApiModelProperty(value = "权限id")
    private String roleId;

    @ApiModelProperty(value = "教师姓名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "教师年龄")
    private Integer age;

    @ApiModelProperty(value = "教师邮箱")
    private String email;

    @ApiModelProperty(value = "教师等级（1：初级教师；2：中级教师；3：高级教师；4：特技教师）")
    private Integer level;

    @ApiModelProperty(value = "教师简介")
    private String description;

    @ApiModelProperty(value = "头像")
    private String avatar;
    
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}

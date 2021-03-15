package cn.com.tjise.onlineedu.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: onlineedu
 * @description: 购物车实体
 * @author: admin
 * @create: 2021-03-15 19:47
 **/

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Cart对象", description = "")
public class Cart implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @ApiModelProperty(value = "课程id")
    private String classId;
    
    @ApiModelProperty(value = "学上id")
    private String studentId;
    
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @ApiModelProperty(value = "逻辑删除 1（true）1已删除，0（false）未删除")
    @TableLogic
    private Integer deleted;
    
}

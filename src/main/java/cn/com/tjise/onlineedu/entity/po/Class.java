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

import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel(value = "Class对象", description = "")
public class Class implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @ApiModelProperty(value = "课程id")
    private String classId;
    
    @ApiModelProperty(value = "课程名")
    private String name;
    
    @ApiModelProperty(value = "课程简介")
    private String description;
    
    @ApiModelProperty(value = "教师id")
    private String teacherId;
    
    @ApiModelProperty(value = "课程价格")
    private BigDecimal price;
    
    @ApiModelProperty(value = "课程剩余名额")
    private Integer quota;
    
    @ApiModelProperty(value = "课程状态(1：未开课；2：已开课；3：已结课)")
    private Integer status;
    
    @ApiModelProperty(value = "逻辑删除 1（true）1已删除，0（false）未删除")
    @TableLogic
    private Integer deleted;
    
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
}

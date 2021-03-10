package cn.com.tjise.onlineedu.entity;

import java.math.BigDecimal;
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
@ApiModel(value="Class对象", description="")
public class Class implements Serializable {

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

    @ApiModelProperty(value = "课程状态(1：未开课；2：已开课；3：已结课)")
    private Integer status;


}

package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.po.Class;
import cn.com.tjise.onlineedu.entity.po.StudentClassRelation;
import cn.com.tjise.onlineedu.entity.po.UStudent;
import cn.com.tjise.onlineedu.entity.vo.classinfo.ClassQueryVO;
import cn.com.tjise.onlineedu.mapper.ClassMapper;
import cn.com.tjise.onlineedu.mapper.StudentClassRelationMapper;
import cn.com.tjise.onlineedu.mapper.UStudentMapper;
import cn.com.tjise.onlineedu.service.ClassService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Service
public class ClassServiceImpl
    extends ServiceImpl<ClassMapper, Class>
    implements ClassService
{
    @Autowired
    private StudentClassRelationMapper relationMapper;
    @Autowired
    private UStudentMapper studentMapper;
    
    @Override
    public boolean deleteById(String id)
    {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", id);
        return remove(queryWrapper);
    }
    
    @Override
    public boolean updateByClassId(Class classInfo)
    {
        String classId = classInfo.getClassId();
        UpdateWrapper<Class> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("class_id", classId);
        return update(classInfo, updateWrapper);
    }
    
    @Override
    public void pageSearchByCondition(Page<Class> page, ClassQueryVO classQueryVO)
    {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        // 如果查询对象为null则进行普通的分页查询
        if (classQueryVO == null)
        {
            page(page, queryWrapper);
            return;
        }
        /*
            动态拼接sql
         */
        Optional.ofNullable(classQueryVO.getPriceHighLimit()).ifPresent(
            high -> {
                // 小于等于价格高限位
                queryWrapper.le("price", high);
            }
        );
        Optional.ofNullable(classQueryVO.getPriceLowLimit()).ifPresent(
            low -> {
                // 大于等于价格高限位
                queryWrapper.ge("price", low);
            }
        );
        Optional.ofNullable(classQueryVO.getStatus()).ifPresent(
            status -> {
                // 小于等于高限位
                queryWrapper.eq("status", status);
            }
        );
        Optional.ofNullable(classQueryVO.getBeginTime()).ifPresent(
            beginTime -> {
                // 大于等于开始时间
                if (beginTime.length() != 0)
                {
                    queryWrapper.ge("create_time", beginTime);
                }
            }
        );
        Optional.ofNullable(classQueryVO.getEndTime()).ifPresent(
            endTime -> {
                // 小于等于结束时间
                if (endTime.length() != 0)
                {
                    queryWrapper.le("create_time", endTime);
                }
            }
        );
        page(page, queryWrapper);
    }
    
    /**
     * 根据课程id查询学生
     *
     * @param classId 课程id
     * @return
     */
    @Override
    public List<UStudent> queryStuByClassId(String classId)
    {
        QueryWrapper queryWrapper = new QueryWrapper<StudentClassRelation>();
        queryWrapper.eq("class_id", classId);
        queryWrapper.select("student_id");
        List<StudentClassRelation> relations = relationMapper.selectList(queryWrapper);
        // 使用流式操作将当前关系实体流流转换为学生id集合
        List<String> collect = relations.stream().map(
            StudentClassRelation::getStudentId
        ).collect(Collectors.toList());
        
        queryWrapper = new QueryWrapper<UStudent>();
        // 将获取到的所有当前课程的学生id，在学生表中进行范围查询
        queryWrapper.in("student_id", collect);
        return studentMapper.selectList(queryWrapper);
    }
    
    /**
     * 根据课程id查询剩余名额
     *
     * @param classId
     * @return
     */
    @Override
    public int queryQuotaOfPeople(String classId)
    {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        // 只查询剩余名额字段
        queryWrapper.select("quota");
        queryWrapper.eq("class_id", classId);
        Class classInfo = getOne(queryWrapper);
        return classInfo.getQuota();
    }
}

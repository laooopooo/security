

package cn.xiaoyh.modules.sys.dao;

import cn.xiaoyh.modules.sys.entity.SysUserRoleEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;


import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author 肖云惠
 * @email i@xiaoyh.cn
 * @date 2018/3/8 17:07
 */
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}

package cn.xiaoyh.modules.sys.service.impl;

import cn.xiaoyh.common.annotation.DataFilter;
import cn.xiaoyh.common.utils.Constant;
import cn.xiaoyh.common.utils.PageUtils;
import cn.xiaoyh.common.utils.Query;
import cn.xiaoyh.modules.sys.dao.SysRoleDao;
import cn.xiaoyh.modules.sys.entity.SysDeptEntity;
import cn.xiaoyh.modules.sys.entity.SysRoleEntity;
import cn.xiaoyh.modules.sys.service.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 角色
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:45:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");

		Page<SysRoleEntity> page = this.selectPage(
			new Query<SysRoleEntity>(params).getPage(),
			new EntityWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
		);

		for(SysRoleEntity sysRoleEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysRoleEntity.getDeptId());
			if(sysDeptEntity != null){
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
		}

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysRoleEntity role) {
		role.setCreateTime(new Date());
		this.insert(role);

		//保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
		this.updateById(role);
		
		//更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		//删除角色
		this.deleteBatchIds(Arrays.asList(roleIds));

		//删除角色与菜单关联
		sysRoleMenuService.deleteBatch(roleIds);

		//删除角色与部门关联
		sysRoleDeptService.deleteBatch(roleIds);

		//删除角色与用户关联
		sysUserRoleService.deleteBatch(roleIds);
	}


}
package com.em.service.imple;

import com.em.dao.MenuMapper;
import com.em.model.MenuNode;
import com.em.model.Menu;
import com.em.model.MenuExample;
import com.em.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Baikang Lu
 * @date 2018/1/22
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {


    @Autowired
    MenuMapper menuMapper;

    @Override
    public List<Menu> getMenu(Integer pId) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andPidEqualTo(pId);
        List<Menu> menus = menuMapper.selectByExample(example);
        return menus;
    }

    //递归获取菜单
    @Override
    public List<MenuNode> getAllMenus(Integer pId) {
        List<MenuNode> result = new ArrayList<>();

        List<Menu> menus = getMenu(pId);
        for (Menu menu : menus) {
            MenuNode menuNode = new MenuNode();
            menuNode.setName(menu.getName());
            menuNode.setMenuList( getAllMenus(menu.getId()));
            result.add(menuNode);
        }
        return result;
    }


}

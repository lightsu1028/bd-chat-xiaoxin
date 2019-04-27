package com.em.service;

import com.em.model.Menu;
import com.em.model.MenuNode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Baikang Lu
 * @date 2018/1/22
 */

public interface MenuService {
    List<Menu> getMenu(Integer pId);

     List<MenuNode> getAllMenus(Integer pId);
}

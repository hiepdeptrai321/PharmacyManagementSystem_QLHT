package com.example.pharmacymanagementsystem_qlht.dao;

import java.util.List;

public interface DaoInterface<Entity> {

        void insert(Entity e);
        void update(Entity e);
        void deleteById(Object... keys);
        Entity selectById(Object... keys);
        List<Entity> selectBySql(String sql, Object... args);
        List<Entity> selectAll();

}

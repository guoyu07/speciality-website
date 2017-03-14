package com.hiczp.web.speciality.service;

import com.alibaba.fastjson.JSON;
import com.hiczp.web.speciality.entity.ArticleEntity;
import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by czp on 17-2-14.
 */
@Service
public class SortService {
    private SortRepository sortRepository;
    private ConfigRepository configRepository;

    public SortService(SortRepository sortRepository, ConfigRepository configRepository) {
        this.sortRepository = sortRepository;
        this.configRepository = configRepository;
    }

    private Integer getTreeRoot(Map<Integer, List<SortEntity>> integerListMap) {
        List<SortEntity> tempList = new ArrayList<>();
        integerListMap.forEach((sortId, hashMapNode) -> tempList.addAll(hashMapNode));
        //其父分类是否存在
        boolean flag;
        for (SortEntity sortEntity : tempList) {
            flag = false;
            for (SortEntity sortEntity1 : tempList) {
                if (sortEntity1.getId() == sortEntity.getParent()) {
                    flag = true;
                    break;
                }
            }
            //当前遍历对象的父分类不存在
            if (!flag) {
                return sortEntity.getParent();
            }
        }
        return null;
    }

    public List<SortEntity> getRootSorts() {
        return sortRepository.findByParentOrderByTaxis(0);
    }

    public List<SortEntity> getNavbarRootSorts() {
        List<SortEntity> sortEntities = new ArrayList<>();
        List<Integer> ids = JSON.parseArray(configRepository.findByKey("navbarSorts").getValue(), Integer.class);
        if (ids != null) {
            ids.forEach(id -> sortEntities.add(sortRepository.findOne(id)));
        }
        return sortEntities;
    }

    public List<SortEntity> getChildSorts(Integer id) {
        return sortRepository.findByParentOrderByTaxis(id);
    }

    public List<SortEntity> getChildSorts(SortEntity sortEntity) {
        return getChildSorts(sortEntity.getId());
    }

    public SortEntity getParentSort(Integer id) {
        return getParentSort(sortRepository.findOne(id));
    }

    public SortEntity getParentSort(SortEntity sortEntity) {
        return sortRepository.findOne(sortEntity.getParent());
    }

    public List<SortEntity> getSiblingSorts(Integer id) {
        return getSiblingSorts(sortRepository.findOne(id));
    }

    public List<SortEntity> getSiblingSorts(SortEntity sortEntity) {
        return sortRepository.findByParentOrderByTaxis(sortEntity.getParent());
    }

    public List<SortEntity> getSidebarSorts(SortEntity sortEntity) {
        return getSiblingSorts(sortEntity);
    }

    public List<SortEntity> getSidebarSorts(ArticleEntity articleEntity) {
        return getSiblingSorts(articleEntity.getSort());
    }

    public List<SortEntity> getParentsChain(SortEntity sortEntity) {
        List<SortEntity> sortEntities = new ArrayList<>();
        SortEntity parentEntity = sortEntity;
        do {
            sortEntities.add(parentEntity);
            parentEntity = getParentSort(parentEntity);
        } while (parentEntity != null);

        Collections.reverse(sortEntities);
        return sortEntities;
    }

    public Map<Integer, List<SortEntity>> getTreeMap() {
        return sortRepository.findByOrderByTaxis().parallelStream().collect(Collectors.groupingBy(SortEntity::getParent));
    }

    public List<SortEntity> getTreeList() {
        Map<Integer, List<SortEntity>> integerListMap = getTreeMap();
        LinkedList<SortEntity> sortEntities = new LinkedList<>();
        while (integerListMap.size() > 0) {
            int i;
            for (i = 0; i < sortEntities.size(); i++) {
                int currentId = sortEntities.get(i).getId();
                if (integerListMap.containsKey(currentId)) {
                    sortEntities.addAll(i + 1, integerListMap.get(currentId));
                    integerListMap.remove(currentId);
                    break;
                }
            }
            //存在游离分类
            if (i >= sortEntities.size()) {
                Integer treeRoot = getTreeRoot(integerListMap);
                if (treeRoot != null) {   //剩余项中有至少一个根
                    sortEntities.addAll(integerListMap.get(treeRoot));
                    integerListMap.remove(treeRoot);
                } else {    //剩余项相互依赖
                    integerListMap.forEach((sortId, hashMapNode) -> sortEntities.addAll(hashMapNode));
                    break;
                }
            }
        }

        return sortEntities;
    }

    public List<SortEntity> getTreeListText(String prefix) {
        List<SortEntity> sortEntities = getTreeList();
        int[] level = new int[sortEntities.size()];
        for (int i = 1; i < sortEntities.size(); i++) {
            SortEntity pre = sortEntities.get(i - 1);
            SortEntity current = sortEntities.get(i);
            if (current.getParent() == pre.getId()) {   //first child
                level[i] = level[i - 1] + 1;
            } else if (current.getParent() == pre.getParent()) {  //Sibling
                level[i] = level[i - 1];
            } else {    //after last child
                for (int j = i; j > 0; j--) {
                    if (current.getParent() == sortEntities.get(j).getParent()) {
                        level[i] = level[j];
                    }
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int k = level[i]; k > 0; k--) {
                stringBuilder.append(prefix);
            }
            stringBuilder.append(" ").append(current.getName());
            current.setName(stringBuilder.toString());
        }

        return sortEntities;
    }

    public List<SortEntity> getTreeListText() {
        return getTreeListText("---");
    }

    public void saveTaxis(Integer[] ids, Integer[] taxis) {
        if (ids.length != taxis.length) {
            throw new InternalError("The length of ids[] and taxis[] does not match");
        }
        List<SortEntity> sortEntities = sortRepository.findByIdIn(ids);
        List<SortEntity> changed = new LinkedList<>();
        sortEntities.parallelStream().forEach(sortEntity -> {
            for (int i = 0; i < ids.length; i++) {
                if (sortEntity.getId() == ids[i]) {
                    if (sortEntity.getTaxis() != taxis[i]) {
                        sortEntity.setTaxis(taxis[i]);
                        changed.add(sortEntity);
                        break;
                    }
                }
            }
        });
        sortRepository.save(changed);
    }
}

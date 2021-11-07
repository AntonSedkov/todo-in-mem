package by.anthony.todo.repository;

import by.anthony.todo.domain.ToDo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ToDoRepository implements CommonRepository<ToDo> {
    private Map<String, ToDo> toDos = new HashMap<>();

    @Override
    public ToDo save(ToDo entity) {
        ToDo result = toDos.get(entity.getId());
        if (result != null) {
            result.setModified(LocalDateTime.now());
            result.setDescription(entity.getDescription());
            result.setCompleted(entity.isCompleted());
            entity = result;
        }
        toDos.put(entity.getId(), entity);
        return toDos.get(entity.getId());
    }

    @Override
    public Iterable<ToDo> save(Collection<ToDo> entities) {
        entities.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(ToDo entity) {
        toDos.remove(entity.getId());
    }

    @Override
    public ToDo findById(String id) {
        return toDos.get(id);
    }

    @Override
    public Iterable<ToDo> findAll() {
        return toDos.entrySet().stream().sorted(entryComparator).map
                (Map.Entry::getValue).collect(Collectors.toList());
    }

    private Comparator<Map.Entry<String, ToDo>> entryComparator =
            (Map.Entry<String, ToDo> o1, Map.Entry<String, ToDo> o2) -> {
                return o1.getValue().getCreated().compareTo
                        (o2.getValue().getCreated());
            };
}
